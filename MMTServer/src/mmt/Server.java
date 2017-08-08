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
            
            ThreadService service = new ThreadService((socket));
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

/**
 *
 * @author Serivce
 */
class ThreadService extends Thread {

    private Socket socket = null;
    private ArrayList<Account> listOfAccount = null;
    Transport transport;

    public ThreadService(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        // nhận
        try {
            transport = new Transport(socket);
            while (true) {
                Package pagClient = (Package) transport.recivePackage();
                if (null != pagClient.getHeader()) {
                    switch (pagClient.getHeader()) {
                        // create account for label REGISTER
                        case REGISTER:
                            Account act = ((Account) pagClient.getData());
                            boolean resultRegister = createAccount(act);

                            Package pagRegister = new Package(Header.REGISTER, resultRegister);
                            transport.sendPackage(pagRegister);
                            break;
                        // check account for label LOGIN
                        case LOGIN:
                            String strLog = (String) pagClient.getData();
                            String[] login = strLog.split(",");

                            String userName = login[0];
                            String password = login[1];

                            Account resultLogin = login(userName, password);
                            
                            Package pagLogin = new Package(Header.LOGIN, resultLogin);
                            transport.sendPackage(pagLogin);
                            break;
                        case LOGOUT:
                            // input: Header.OUTPUT
                            // output: void
                            break;
                        case SINGLECHAT:
                            
                            break;    
                        case MULTIPECHAT:
                            String messenger = (String) pagClient.getData();
                            Server.addMessengerIntoQueue(messenger);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ex.toString());
        }
        
        /// xử lý gửi
        new Thread(new Runnable() {
            @Override
            public void run() {
                Transport transport = new Transport(socket);
                while(true){
                    if(!Server.getQueueMessenger().isEmpty()){
                        try {
                            Package pag = new Package(Header.MULTIPECHAT, Server.getQueueMessenger().peek());
                            transport.sendPackage(pag);
                            ////////////////////////////////////////////////////
                        } catch (IOException ex) {
                            Logger.getLogger(ThreadService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }).start();
    }

    public boolean createAccount(Account act) throws IOException {
        // nhận vào thông tin tài khoản
        // kiểm tra tài khoản hợp lệ
        // tạo một tài khoản trong database
        // nếu thành công thì ~
        return false;
    }

    public void disConnect() {

    }

    private Account login(String userName, String password) {
        return Server.login(userName, password);
    }

    public void updateAccount() {

    }

    public void requestSendFile() {

    }

}
