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

/**
 *
 * @author Totoro
 */
class Service implements Runnable {

    Socket socket = null;

    public Service(Socket socket) {
        this.socket = socket;
    }
    
    // Login with username, password
    public Account signIn(String userName, String password) throws IOException, ClassNotFoundException {
        // gửi 2 chuỗi "đăng nhập" sang cho server kiểm tra
        Account result = null;
        String pagData = userName + "," + password;
        Package pagLogin = new Package(Header.LOGIN, pagData);

        Transport transport = new Transport(socket);
        transport.sendPackage(pagLogin);

        // nhận thông tin đăng nhập hợp lệ: không hợp lệ ?
        Package pagServer = transport.recivePackage();
        if (pagServer.getHeader() == Header.LOGIN) {
            result = (Account) pagServer.getData();
        }
        return result;
    }

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
    }
}
