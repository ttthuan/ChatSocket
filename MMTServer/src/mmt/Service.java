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
    private Account account = null;
    private final Service[] services;
    private int maxNumberClient;
    private Transport transport = null;
    private SendListAccountToUser sendListAccount = null;

    private boolean debug = true;

    public Socket getSocket() {
        return socket;
    }

    public Account getAccount() {
        return account;
    }

    public Transport getTransport() {
        return transport;
    }

    public Service(Socket socket, Service[] services) throws IOException {
        this.socket = socket;
        this.services = services;
        this.maxNumberClient = services.length;
        transport = new Transport(socket);
    }

    @Override
    public void run() {
        // nhận
        try {
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
                            // debug
                            if (debug) {
                                System.out.println(userName + " " + password);
                            }

                            Account resultLogin = null;

                            synchronized (this) {
                                resultLogin = login(userName, password);
                            }
                            account = resultLogin;

                            Package pagLogin = new Package(Header.LOGIN, resultLogin);
                            transport.sendPackage(pagLogin);

                            sendListAccount = new SendListAccountToUser(socket, services, this);
                            sendListAccount.start();

                            break;
                        case LOGOUT:
                            // input: Header.OUTPUT
                            // output: void
                            break;
                        case SINGLECHAT:
                            PackageChat pckChatAPerson = (PackageChat) pagClient.getData();
                            
                            // debug
                            System.out.println(pckChatAPerson);
                            
                            String[] content = pckChatAPerson.getContent().split(";");
                            pckChatAPerson.setContent(content[1]);
                            
                            synchronized (this) {
                                for (int i = 0; i < maxNumberClient; i++) {
                                    if (services[i] != null && services[i].getAccount().getUserName().equals(content[0])) {
                                        Package pckAPerson = new Package(Header.SINGLECHAT, pckChatAPerson);
                                        services[i].getTransport().sendPackage(pckAPerson);
                                        break;
                                    }
                                }
                            }
                            
                            break;
                        case MULTIPECHAT:
                            PackageChat pckChat = (PackageChat) pagClient.getData();
                            
                            // debug
                            System.out.println(pckChat);
                            
                            synchronized (this) {
                                Package pck = new Package(Header.MULTIPECHAT, pckChat);
                                for (int i = 0; i < maxNumberClient; i++) {
                                    if (services[i] != null && services[i] != this) {
                                        services[i].getTransport().sendPackage(pck);
                                    }
                                }
                            }

                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ex.toString());
            System.out.println("Lỗi nhận dữ liệu từ client");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Lỗi không tìm thấy class trong class transport");
        } finally {
            if (sendListAccount != null) {
                sendListAccount.setIsRunning(false);
            }

            synchronized (this) {
                for (int i = 0; i < maxNumberClient; i++) {
                    if (services[i] == this) {
                        services[i] = null;
                    }
                }
            }

            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Không thể đóng stream");
            }

            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Không thể đóng socket client");
            }
        }
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
