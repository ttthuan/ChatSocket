/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

enum Header {
    REGISTER, LOGIN, LOGOUT
};

/**
 *
 * @author Totoro
 */
public class Client {

    // Attributes
    Socket socketClient;
    Account account;

    public Socket getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(Socket socketClient) {
        this.socketClient = socketClient;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Client(Socket socketClient, Account account) {
        this.socketClient = socketClient;
        this.account = account;
    }

    public Client() {

    }

    // Method
    public void sendMessage() {
        // gửi 1 tin nhắn tới server 
        // server gửi đi cho 1 luồng #
    }

    public void connect(String address, int port) throws IOException {
        socketClient = new Socket(address, port);
    }

    public void disConnect() throws IOException {
        socketClient.close();
    }

    // Sent to server of register
    public boolean registerAccount(Account act) throws IOException, ClassNotFoundException {
        boolean result = false;
        Package pagRegister = new Package(Header.REGISTER, act);
        Transport transport = new Transport(socketClient);

        transport.sendPackage(pagRegister);

        Package pagServer = transport.recivePackage();
        if (pagServer.getHeader() == Header.REGISTER) {
            result = (boolean) pagServer.getData();
        }
        return result;
    }

    // Login with username, password
    public Account login(String userName, String password) throws IOException, ClassNotFoundException {
        // gửi 2 chuỗi "đăng nhập" sang cho server kiểm tra
        Account result = null;
        String pagData = userName + "," + password;
        Package pagLogin = new Package(Header.LOGIN, pagData);

        Transport transport = new Transport(socketClient);
        transport.sendPackage(pagLogin);

        // nhận thông tin đăng nhập hợp lệ: không hợp lệ ?
        Package pagServer = transport.recivePackage();
        if (pagServer.getHeader() == Header.LOGIN) {
            result = (Account) pagServer.getData();
        }
        return result;
    }

    // - 1 = logout
    public boolean logout() throws IOException, ClassNotFoundException {
        int out = -1;
        Package pagLogout = new Package(Header.LOGOUT, out);

        Transport transport = new Transport(socketClient);
        transport.sendPackage(pagLogout);

        transport.recivePackage();
        return false;
    }

    public void chat(Account act) {

    }

    public void sendFile(String fileName) {

    }

    public void testGitHub() {
        System.out.println("run GitHub !");
    }

}

class Service extends Thread {

    Socket socket = null;

    @Override
    public void run() {
        // xử lý nhận
        Transport transport = new Transport(socket);
        try {
            while (true) {
                Package pagClient = (Package) transport.recivePackage();
                if (null != pagClient.getHeader()) {
                    switch (pagClient.getHeader()) {
                        
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /// xử lý gửi
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
