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

class Service extends Thread {

    private Socket socket = null;
    private Account account = null;
    private final Service[] services;
    private int maxNumberClient;
    private Transport transport = null;
    private SendListAccountToUser sendListAccount = null;
    private static int PORT_SERVER_FILE = 60602;
    private static String HOST_SERVER_FILE = "localhost";
    private boolean isRuning = true;

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

    public Service(Socket socket, Service[] services) {
        this.socket = socket;
        this.services = services;
        this.maxNumberClient = services.length;
        transport = new Transport(socket);
    }

    @Override
    public void run() {
        // nhận
        try {
            while (isRuning) {
                Package pagClient = (Package) transport.recivePackage();
                if (null != pagClient.getHeader()) {
                    switch (pagClient.getHeader()) {

                        case REGISTER:
                            Account act = ((Account) pagClient.getData());

                            // debug
                            //System.out.println("đăng ký " + act.getUserName() + " " + act.getPassword());
                            boolean resultRegister = signup(act);
                            if (resultRegister) {
                                // log file
                                WriteFileLog.writeFileLog(act.getUserName(), TYPELOG.INFORMATION, "Register successfull");
                            } else {
                                // log file
                                WriteFileLog.writeFileLog(act.getUserName(), TYPELOG.INFORMATION, "Register fail");
                            }

                            Package pagRegister = new Package(Header.REGISTER, new Boolean(resultRegister));
                            transport.sendPackage(pagRegister);

                            // debug
                            //System.out.println("đã có kết quả đăng ký " + resultRegister);
                            break;

                        case LOGIN:
                            String strLog = (String) pagClient.getData();
                            String[] login = strLog.split(",");

                            String userName = login[0];
                            String password = login[1];
                            // debug
//                            if (debug) {
//                                System.out.println(userName + " " + password);
//                            }

                            Account resultLogin = null;

                            synchronized (this) {
                                resultLogin = signIn(userName, password);
                            }

                            account = resultLogin;
                            if (resultLogin != null) {
                                // log file
                                WriteFileLog.writeFileLog(userName, TYPELOG.INFORMATION, "SignIn successfull");
                            } else {
                                // log file
                                WriteFileLog.writeFileLog(userName, TYPELOG.INFORMATION, "SignIn fail");
                            }

                            Package pagLogin = new Package(Header.LOGIN, resultLogin);
                            transport.sendPackage(pagLogin);

                            if (resultLogin != null) {
                                if (sendListAccount == null) {
                                    sendListAccount = new SendListAccountToUser(socket, services, account);
                                    sendListAccount.start();
                                }
                            }
                            break;

                        case LOGOUT:
//                            synchronized (this) {
//                                for (int i = 0; i < maxNumberClient; i++) {
//                                    if (services[i] != null && 
//                                            services[i].getAccount().getUserName().equals(account.getUserName())) {
//                                        services[i].account = null;
//                                        if (services[i].sendListAccount != null) {
//                                            services[i].sendListAccount.stop();
//                                            services[i].sendListAccount = null;
//                                        }
//                                    }
//                                }
//                            }
                            synchronized (this) {
                                if(account != null)
                                    account = null;
                                if (sendListAccount != null) {
                                    sendListAccount.setIsRunning(false);
                                    sendListAccount.stop();
                                    sendListAccount = null;
                                }
                            }

                            String userLogout = (String) pagClient.getData();

                            // debug
                            //System.out.println("user " + userLogout + " logout");
                            // log file
                            WriteFileLog.writeFileLog(userLogout, TYPELOG.INFORMATION, "Request logout");

                            Package pckLogout = new Package(Header.LOGOUT, new String("Successfull"));
                            transport.sendPackage(pckLogout);

                            // debug
                            //System.out.println(userLogout + " logout thành công");
                            // log file
                            WriteFileLog.writeFileLog(userLogout, TYPELOG.INFORMATION, "Logout successfull");

                            break;
                        case SINGLECHAT:
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    PackageChat pckChatAPerson = (PackageChat) pagClient.getData();

                                    // debug
                                    //System.out.println(pckChatAPerson);
                                    String[] content = pckChatAPerson.getContent().split(";");
                                    pckChatAPerson.setContent(content[1]);

                                    synchronized (this) {
                                        for (int i = 0; i < maxNumberClient; i++) {
                                            if (services[i] != null && services[i].getAccount().getUserName().equals(content[0])) {
                                                Package pckAPerson = new Package(Header.SINGLECHAT, pckChatAPerson);
                                                try {
                                                    services[i].getTransport().sendPackage(pckAPerson);
                                                } catch (IOException ex) {
                                                    // log file
                                                    WriteFileLog.writeFileLog("service " + account.getUserName(), TYPELOG.ERROR, ex.getMessage());
                                                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }).start();

                            break;
                        case MULTIPECHAT:
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    PackageChat pckChat = (PackageChat) pagClient.getData();

                                    // debug
                                    //System.out.println(pckChat);
                                    synchronized (this) {
                                        Package pck = new Package(Header.MULTIPECHAT, pckChat);
                                        for (int i = 0; i < maxNumberClient; i++) {
                                            if (services[i] != null && !services[i].getAccount().getUserName().equals(account.getUserName())) {
                                                try {
                                                    services[i].getTransport().sendPackage(pck);
                                                } catch (IOException ex) {
                                                    // log file
                                                    WriteFileLog.writeFileLog("service " + account.getUserName(), TYPELOG.ERROR, ex.getMessage());
                                                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    }
                                }
                            }).start();
                            break;
                        case SENDFILE:
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String notifiSendFile = (String) pagClient.getData();
                                        String[] contents = notifiSendFile.split(";");
                                        // [0] username send; [1] full name; [2] user recive; [3] file name

                                        // thông báo cho user nhận file
                                        synchronized (this) {
                                            Package pckNotifiSendFile = new Package(Header.RECEIVEFILE, contents[0] + ";" + contents[1] + ";" + contents[3]);
                                            for (int i = 0; i < maxNumberClient; i++) {
                                                if (services[i] != null && services[i].getAccount().getUserName().equals(contents[2])) {
                                                    try {
                                                        services[i].getTransport().sendPackage(pckNotifiSendFile);
                                                        break;
                                                    } catch (IOException ex) {
                                                        // log file
                                                        WriteFileLog.writeFileLog("service " + account.getUserName(), TYPELOG.ERROR, ex.getMessage());
                                                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            }
                                        }

                                        // kết nối qua server gửi file
                                        Socket socketConnectServerFile = new Socket(HOST_SERVER_FILE, PORT_SERVER_FILE);
                                        Transport transportFile = new Transport(socketConnectServerFile);

                                        // thông báo cho server file, có 2 user cần trao đổi file
                                        Package pckRequest = new Package(Header.HASREQUEST, contents[0] + ";" + contents[2]);
                                        transportFile.sendPackage(pckRequest);

                                        // nhận port server file đã mở
                                        Package pckFile = transportFile.recivePackage();

                                        // đóng transport
                                        transportFile.close();
                                        socketConnectServerFile.close();

                                        if (pckFile != null) {
                                            switch (pckFile.getHeader()) {
                                                case BUSY:
                                                    // debug
                                                    //System.out.println(pckFile.getData().toString());
                                                    break;
                                                case PORT:
                                                    // debug
                                                    //System.out.println("Thông tin 2 port " + pckFile.getData());

                                                    String[] ports = pckFile.getData().toString().split(";");

                                                    // lấy port chuyển về cho 2 client
                                                    // chuyển port cho client gửi
                                                    Package pckPortSend = new Package(Header.PORTSEND, ports[0]);
                                                    transport.sendPackage(pckPortSend);

                                                    // chuyển port cho client nhận
                                                    synchronized (this) {
                                                        Package pckPortReceive = new Package(Header.PORTRECEIVE, ports[1]);
                                                        for (int i = 0; i < maxNumberClient; i++) {
                                                            if (services[i] != null && services[i].getAccount().getUserName().equals(contents[2])) {
                                                                services[i].getTransport().sendPackage(pckPortReceive);
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    break;
                                            }
                                        } else {
                                            // debug
                                            System.out.println("Thông tin server file gửi rỗng");
                                        }
                                    } catch (IOException ex) {
                                        // log file
                                        WriteFileLog.writeFileLog("service " + account.getUserName(), TYPELOG.ERROR, ex.getMessage());
                                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ClassNotFoundException ex) {
                                        // log file
                                        WriteFileLog.writeFileLog("service " + account.getUserName(), TYPELOG.ERROR, ex.getMessage());
                                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }).start();
                            break;

                        case CLOSECLIENT:
                            // debug
                            System.out.println("client muốn close");

                            Package pckCloseClient = new Package(Header.CLOSECLIENT, new String("Successfull"));
                            transport.sendPackage(pckCloseClient);

                            // debug
                            System.out.println("close thành công");
                            break;
                        case CHANGEPASS:
                            String contentChangePass = (String) pagClient.getData();
                            String[] contentsCP = contentChangePass.split(";");
                            // [0] usename, [1] passwordold, [2] passwordnew

                            // debug
                            System.out.println("Nội dung change pass client gửi " + contentChangePass);
                            // change pass
                            // kiểm tra username và password old có hợp lệ hay không
                            // hợp lệ thì thay đổi pass, ngược lại thì cảnh báo
                            boolean result = changePass(contentsCP[0], contentsCP[1], contentsCP[2]);
                            if (result == true) {
                                // hợp lệ
                                account.setPassword(contentsCP[2]);
                                Package pckCPSuccess = new Package(Header.CHANGEPASS, "Successfull;" + contentsCP[2]);
                                transport.sendPackage(pckCPSuccess);

                                // debug
                                System.out.println("Hợp lệ " + pckCPSuccess.getData());
                                // log file
                                WriteFileLog.writeFileLog(contentsCP[0], TYPELOG.INFORMATION, "Change password");

                            } else {
                                Package pckCPFail = new Package(Header.CHANGEPASS, "Fail");
                                transport.sendPackage(pckCPFail);

                                // debug
                                System.out.println("Không hợp lệ " + pckCPFail.getData());
                                // log file
                                WriteFileLog.writeFileLog(contentsCP[0], TYPELOG.WARNING, "Change password illegal");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            // log file
            WriteFileLog.writeFileLog("service", TYPELOG.ERROR, ex.getMessage());
            //Logger.getLogger(ex.toString());
        } catch (ClassNotFoundException ex) {
            // log file
            WriteFileLog.writeFileLog("service", TYPELOG.ERROR, ex.getMessage());
            //Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            synchronized (this) {
                for (int i = 0; i < maxNumberClient; i++) {
                    if (services[i] == this) {
                        services[i] = null;
                    }
                }
            }

        }
    }

    public boolean signup(Account act) {
        return Server.signup(act);
    }

    public void closeService() {
        if (sendListAccount != null) {
            sendListAccount.setIsRunning(false);
        }
        isRuning = false;
    }

    private Account signIn(String userName, String password) {
        return Server.signIn(userName, password);
    }

    public boolean changePass(String username, String passwordold, String passwordnew) {
        return Server.changePass(username, passwordold, passwordnew);
    }
}
