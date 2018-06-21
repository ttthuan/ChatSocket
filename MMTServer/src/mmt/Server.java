/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static int PORT = 60601;
    public static ServerSocket socketServer = null;
    private static Socket socket = null;
    private static final int MAX_NUMBER_CLIENT = 20;
    private static final Service[] SERVICES = new Service[MAX_NUMBER_CLIENT];
    private static boolean running = true;
    private static List<Account> accountsInDatabase = new ArrayList<Account>();

    public List<Account> getAccounts() {
        return accountsInDatabase;
    }

    public void showAllAccount() {
        String[] ip = new String[accountsInDatabase.size()];
        Integer[] port = new Integer[accountsInDatabase.size()];

        synchronized (this) {
            int i;
            for (i = 0; i < accountsInDatabase.size(); i++) {
                int j;
                for (j = 0; j < MAX_NUMBER_CLIENT; j++) {
                    if (SERVICES[j] != null && SERVICES[j].getAccount().getUserName().equals(accountsInDatabase.get(i).getUserName())) {
                        port[i] = (SERVICES[j].getSocket().getPort());
                        ip[i] = (SERVICES[j].getSocket().getRemoteSocketAddress().toString());
                        accountsInDatabase.get(i).setIsOnline(true);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < accountsInDatabase.size(); i++) {
            if (accountsInDatabase.get(i).isIsOnline()) {
                System.out.println(accountsInDatabase.get(i).getUserName() + " online " + ip[i] + " " + port[i]);
            } else {
                System.out.println(accountsInDatabase.get(i).getUserName() + " offline");
            }
        }
    }

    public Server() {
        DataProvider dataProvider = new DataProvider();
        accountsInDatabase = dataProvider.getAllAccount();
    }

    // lắng nghe các kết nối từ phía Client
    // khi gặp kết nối tạo ra một luồng phục vụ riêng cho mỗi Client.
    public static void start() throws IOException {
        System.out.println("Listenning...");
        while (running) {
            socket = socketServer.accept();
            System.out.println("a client Connected!");

            int i;
            for (i = 0; i < MAX_NUMBER_CLIENT; i++) {
                if (SERVICES[i] == null) {
                    (SERVICES[i] = new Service(socket, SERVICES)).start();
                    break;
                }
            }

            if (i == MAX_NUMBER_CLIENT) {
                Package pck = new Package(Header.BUSY, "Server too busy. Try later.");
                Transport transport = new Transport(socket);
                transport.sendPackage(pck);
                transport.close();
                socket.close();
            }
        }
    }

    public void closeServer() {
        try {

            synchronized (this) {
                int i;
                for (i = 0; i < MAX_NUMBER_CLIENT; i++) {
                    if (SERVICES[i] != null) {
                        SERVICES[i].closeService();
                    }
                }
            }

            socketServer.close();
            running = false;

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // test signIn
    public synchronized static Account signIn(String username, String password) {
        Account account = null;
        
        int i;
        for (i = 0; i < accountsInDatabase.size(); i++) {
            if (accountsInDatabase.get(i).getUserName().equals(username) == true
                    && accountsInDatabase.get(i).getPassword().equals(password) == true) {
                if(accountsInDatabase.get(i).isIsOnline() == true){
                    break;
                }
                account = accountsInDatabase.get(i);
                accountsInDatabase.get(i).setIsOnline(true);
                break;
            }
        }

        if (account != null) {
            account.setIsOnline(true);
        }
        return account;
    }
    
    public synchronized static boolean signup(Account account){
        int i, n = accountsInDatabase.size();
        
        for(i = 0; i < n; i++){
            if(accountsInDatabase.get(i).getUserName().equals(account.getUserName())){
                return false;
            }
        }
        
        accountsInDatabase.add(account);
        // gọi data provider ghi xuống file
        DataProvider dataProvider = new DataProvider();
        dataProvider.addAccount(account);
        
        return true;
    }
    
    public synchronized static void removeUser(String user){
        for(int i = 0; i < MAX_NUMBER_CLIENT; i++){
            if(SERVICES[i] != null && SERVICES[i].getAccount() != null && 
                    SERVICES[i].getAccount().getUserName().equals(user)){
                SERVICES[i].closeService();
                try {
                    if(SERVICES[i].getTransport() != null)
                        SERVICES[i].getTransport().close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if(SERVICES[i].getSocket() != null)
                        SERVICES[i].getSocket().close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(SERVICES[i] != null)
                    SERVICES[i] = null;
            }
        }
        
        for(int i = 0; i < accountsInDatabase.size(); i++){
            if(accountsInDatabase.get(i).getUserName().equals(user)){
                accountsInDatabase.remove(i);
                break;
            }
        }
    }
    
    public static boolean changePass(String username, String passwordold, String passwordnew){
        boolean result = false;
        int i;
        for (i = 0; i < accountsInDatabase.size(); i++) {
            if (accountsInDatabase.get(i).getUserName().equals(username) == true
                    && accountsInDatabase.get(i).getPassword().equals(passwordold) == true) {
                accountsInDatabase.get(i).setPassword(passwordnew);
                result = true;
                // thay đổi xuống file
                DataProvider dataProvider = new DataProvider();
                dataProvider.modiferAccount(accountsInDatabase.get(i));
                break;
            }
        }
        
        return result;
    }
}
