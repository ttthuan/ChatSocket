/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Totoro
 */
public class Server {
    private static int PORT = 7777;
    private static ServerSocket socketServer = null;
    private static Socket socket = null;
    private static final int maxNumberClient = 20;
    private static final Service[] services = new Service[maxNumberClient];
    private static boolean running = true;
    private List<Account> accountsInDatabase = new ArrayList<Account>();
    

    public List<Account> getAccounts() {
        return accountsInDatabase;
    }
    
    public void showAllAccount(){
        String[] ip = new String[accountsInDatabase.size()];
        Integer[] port = new Integer[accountsInDatabase.size()];
        
        synchronized(this){
            int i = 0;
            for(; i < accountsInDatabase.size(); i++){
                int j = 0;
                for(; j < services.length; j++){
                    if(services[j] != null && services[i].getAccount().getUserName().equals(accountsInDatabase.get(i).getUserName())){
                        port[i] = (services[j].getSocket().getPort());
                        ip[i] = (services[j].getSocket().getRemoteSocketAddress().toString());
                        accountsInDatabase.get(i).setIsOnline(true);
                        break;
                    }
                }
            }
        }
        
        for(int i = 0; i < accountsInDatabase.size(); i++){
            if(accountsInDatabase.get(i).isIsOnline()){
                System.out.println(accountsInDatabase.get(i).getUserName() + " online " + ip[i] + " " + port[i]);
            }else{
                System.out.println(accountsInDatabase.get(i).getUserName() + " offline");
            }
        }
    }
    
    public Server() throws SQLException{
        DataProvider dataProvider = new DataProvider();
        accountsInDatabase = dataProvider.getAllAccount();
    }
    

    // lắng nghe các kết nối từ phía Client
    // khi gặp kết nối tạo ra một luồng phục vụ riêng cho mỗi Client.
    public static void start() throws IOException {
        socketServer = new ServerSocket(PORT);
        System.out.println("Listenning...");
        while (running) {
            socket = socketServer.accept();
            System.out.println("a client Connected!");
            
            int i;
            for(i = 0; i < maxNumberClient; i++){
                if(services[i] == null){
                    (services[i] = new Service(socket, services)).start();
                    break;
                }
            }
            
            if(i == maxNumberClient){
                Package pck = new Package(Header.BUSY, "Server too busy. Try later.");
                Transport transport = new Transport(socket);
                transport.sendPackage(pck);
                transport.close();
                socket.close();
            }
        }
    }

    public static void stop() {
        try {
            socketServer.close();
            running = false;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // test login
    public static Account login(String userName, String password) {
        Account account = null;
        try {
            DataProvider provider = null;
            provider = new DataProvider();
            account = provider.login(userName, password);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(account != null){
            account.setIsOnline(true);
        }
        return account;
    }
}
