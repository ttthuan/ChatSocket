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

/**
 *
 * @author Totoro
 */
class Service implements Runnable {

    Socket socket = null;
    Transport transport = null;

    public Service(Socket socket) throws IOException {
        this.socket = socket;
        transport = new Transport(socket);
    }

    // Login with username, password
    public Account signIn(String userName, String password) throws IOException, ClassNotFoundException {
        // gửi 2 chuỗi "đăng nhập" sang cho server kiểm tra
        String pagData = userName + "," + password;
        Package pagLogin = new Package(Header.LOGIN, pagData);

        transport.sendPackage(pagLogin);
        
        Account account = null;
        
        Package pckResult = transport.recivePackage();
        if(pckResult.getHeader() == Header.LOGIN){
            account = (Account) pckResult.getData();
        }
        
        return account;
    }

    void chatAll(PackageChat pckChat) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Package pag = new Package(Header.MULTIPECHAT, pckChat);
                    transport.sendPackage(pag);
                    System.out.println(pckChat);
                } catch (IOException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
            }
        }).start();
    }
    
    void chatWithAPerson(PackageChat pckChat) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Package pag = new Package(Header.SINGLECHAT, pckChat);

                    transport.sendPackage(pag);
                    System.out.println("Chat with a person " + pckChat);
                } catch (IOException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    @Override
    public void run() {
        // xử lý nhận
        try {
            while (true) {
                Package pagServer = (Package) transport.recivePackage();
                if (null != pagServer.getHeader()) {
                    switch (pagServer.getHeader()) {
                        case LISTACCOUNT:
                            List<Account> list = (List<Account>) pagServer.getData();
                            Client.showListAccount(list);
                            break;
                        case MULTIPECHAT:
                            PackageChat pckChat = (PackageChat) pagServer.getData();
                            Client.showListChatAll(pckChat);
                            break;
                        case SINGLECHAT:
                            PackageChat pckChatAPerson = (PackageChat) pagServer.getData();
                            Client.showListChatAPerson(pckChatAPerson);
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
