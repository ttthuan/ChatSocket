/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    // Attributes
    public Socket socketClient = null;
    public static Account account = null;
    public static Service service = null;

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

//    public Client(Socket socketClient, Account account) {
//        this.socketClient = socketClient;
//        this.account = account;
//    }

    public Client() {
        socketClient = null;
        account = null;
        service = null;
    }

    // Hàm hành sự - Method
    // kết nối tới Client, tạo luồng phục vụ
    // chỗ này t muốn tách hàm ra nhưng không biết tách sao cho hay nên để vậy 
    public void connect(String address, int port){
        try {
            socketClient = new Socket(address, port);
            initService();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            // lỗi kết nối tới server
        }
    }
    
    public void initService(){
        if (socketClient != null) {
            service = new Service(socketClient);
        }
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

    // start hàm run bên service
    public void startReciveFormServer() throws IOException {
        new Thread(service).start();
    }

    // Login with username, password
    public Account signIn(String userName, String password) throws IOException, ClassNotFoundException {
        return service.signIn(userName, password);
    }

    public static void showListAccount(List<Account> listOfAccount) {
        ChatRoom.showListOfAccount(listOfAccount);
    }

    static void showListChatAll(PackageChat pckChat) {
        ChatRoom.showListChatAll(pckChat);
    }
    
    static void showListChatAPerson(PackageChat pckChat) {
        ChatRoom.showListChatAPerson(pckChat);
    }
    
    void chatAll(PackageChat pckChat){
        service.chatAll(pckChat);
    }
    
    void chatWithAPerson(PackageChat pckChat){
        service.chatWithAPerson(pckChat);
    }
    
    void notificationSendFile(String pckNotificationSendFile, String pathFile){
        service.notificationSendFile(pckNotificationSendFile, pathFile);
    }
    
    public void logout(){
        service.logout(new Package(Header.LOGOUT, account.getUserName()));
    }
    
    public static void setUsernameOnUi(String username){
        ChatRoom.setUsernameOnUi(username);
    }
    
    public void closeClient(){
        service.closeClient(new Package(Header.CLOSECLIENT, "close"));
    }
    
    public boolean signup(Account account){
        return service.signup(account);
    }
    
    public void changePassword(String passold, String passnew){
        service.changePassword(account.getUserName() + ";" + passold + ";" + passnew);
    }
    
    public static void showStatus(String content){
        ChangePassword.showStatus(content);
    }
}
