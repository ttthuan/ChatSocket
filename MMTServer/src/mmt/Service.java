/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Totoro
 */
class Service extends Thread {

    private Socket socket = null;
    private ArrayList<Account> listOfAccount = null;
    Transport transport;

    public Service(Socket socket) {
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
                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
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
