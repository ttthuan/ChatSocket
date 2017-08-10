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
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

enum Header {
    REGISTER, LOGIN, LOGOUT, SINGLECHAT, MULTIPECHAT
};

/**
 *
 * @author Totoro
 */
public class Server {
    private static final int PORT = 7777;
    private static boolean running = true;
    private static ServerSocket socketServer = null;
    private static Socket socket = null;
    private static ArrayList<Socket> listOfSocket = new ArrayList<Socket>();
    private static ArrayList<Account> listOfAccount = new ArrayList<Account>();
    private static Queue<String> queueOfMessenger = new LinkedList<String>();
    private static int numberOfSendMember = 0;
    
    public static void main(String[] args) throws IOException {
       
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Server.start();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    // lắng nghe các kết nối từ phía Client
    // khi gặp kết nối tạo ra một luồng phục vụ riêng cho mỗi Client.
    public static void start() throws IOException {
        socketServer = new ServerSocket(PORT);
        System.out.println("Listenning...");
        while (running) {
            socket = socketServer.accept();
            listOfSocket.add(socket);
            
            Service service = new Service((socket));
            service.start();
            System.out.println("Connected!");
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
        
        if(account != null)
            listOfAccount.add(account);
        return account;
    }
    
    public static void addMessengerIntoQueue(String mess){
        queueOfMessenger.add(mess);
    }
    
    public static Queue<String> getQueueMessenger(){
        return queueOfMessenger;
    }
    
    public static int getNumberOfMember(){
        return numberOfSendMember;
    }
    
    public static void setNumberOfMember(int num){
        numberOfSendMember = num;
    }
}
