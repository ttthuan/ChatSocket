/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.Socket;
/**
 *
 * @author Totoro
 */
public class Client {

    // Attributes
    private Socket socketClient = null;
    private Account account = null;
    private Service service = null;

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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Client(Socket socketClient, Account account) {
        this.socketClient = socketClient;
        this.account = account;
    }

    public Client() {

    }

    // Hàm hành sự - Method
    // kết nối tới Client, tạo luồng phục vụ
    // chỗ này t muốn tách hàm ra nhưng không biết tách sao cho hay nên để vậy 
    public void connect(String address, int port) throws IOException {
        socketClient = new Socket(address, port);

        service = new Service(socketClient);
//        service.start();
    }

    public void disConnect() throws IOException {
        socketClient.close();
    }

    public void sendMessage() {
        // gửi 1 tin nhắn tới server 
        // server gửi đi cho 1 luồng #
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
        return service.login(userName, password);
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
