/*
 * To change this license header, choose License Headers fileInputSend Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template fileInputSend the editor.
 */
package mmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class Service implements Runnable {

    private Socket socket = null;
    private Transport transport = null;
    public static String HOST_SERVER_FILE = "localhost";
    private String fileNameSend;
    private String fileNameReceive;
    private Account userSend = null;
    private final int NUMBER_KILOBYTE = 64;
    private boolean isRuning = true;

    public Service(Socket socket) {
        this.socket = socket;
        transport = new Transport(socket);
        userSend = null;
        isRuning = true;
        fileNameSend = null;
        fileNameReceive = null;
    }

    // Login with username, password
    public Account signIn(String userName, String password) throws IOException, ClassNotFoundException {
        // gửi 2 chuỗi "đăng nhập" sang cho server kiểm tra
        String pagData = userName + "," + password;
        Package pagLogin = new Package(Header.LOGIN, pagData);

        transport.sendPackage(pagLogin);

        Account account = null;

        Package pckResult = transport.recivePackage();
        if (pckResult.getHeader() == Header.LOGIN) {
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

                    // debug
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

                    // debug
                    System.out.println("Chat with a person " + pckChat);
                } catch (IOException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public void logout(Package pck) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    transport.sendPackage(pck);

                    // debug
                    System.out.println("client want to logout");
                } catch (IOException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public void closeClient(Package pck) {
        try {
            transport.sendPackage(pck);

            // debug
            System.out.println("client want to close");

            try {
                Package pagServer = (Package) transport.recivePackage();

                if (pagServer.getHeader() == Header.CLOSECLIENT) {
                    String messageClose = (String) pagServer.getData();
                    if (messageClose.equals("Successfull")) {
                        // close thành công

                        // debug
                        System.out.println("close thành công");

                        transport.close();
                        socket.close();
                        MMTClient.login.dispose();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void notificationSendFile(String pckNotificationSendFile, String pathFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fileNameSend = pathFile;
                    Package pag = new Package(Header.SENDFILE, pckNotificationSendFile);
                    transport.sendPackage(pag);

                    // debug
                    System.out.println("Thông báo send file " + pckNotificationSendFile);
                } catch (IOException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public boolean signup(Account account) {
        Boolean result = false;
        try {
            Package pckSendRegis = new Package(Header.REGISTER, account);
            transport.sendPackage(pckSendRegis);

            Package pckReceiveRegis = transport.recivePackage();
            if (pckReceiveRegis.getHeader() == Header.REGISTER) {
                result = (Boolean) pckReceiveRegis.getData();
            }
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public void changePassword(String content) {
        //String strReceive = null;

        try {
            Package pckChangePass = new Package(Header.CHANGEPASS, content);
            transport.sendPackage(pckChangePass);

            // debug
            System.out.println("Nội dung change pass " + content);

        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        // xử lý nhận
        try {
            while (isRuning) {
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
                        case RECEIVEFILE:
                            // debug
                            System.out.println("Thông báo gửi file từ server: " + pagServer.getData());

                            String[] contents = pagServer.getData().toString().split(";");
                            fileNameReceive = contents[2];
                            userSend = new Account(contents[0], "", contents[1]);

                            break;
                        case PORTSEND:
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String fileName = new String(fileNameSend);
                                    OutputStream outPutStreamSend = null;
                                    InputStream fileInputSend = null;
                                    Socket socketSendFile = null;
                                    try {
                                        // debug
                                        System.out.println("Port send từ server: " + pagServer.getData());

                                        int portSendServerFile = Integer.parseInt(pagServer.getData().toString());

                                        // connect tới server file
                                        socketSendFile = new Socket(HOST_SERVER_FILE, portSendServerFile);
                                        File file = new File(fileName);
                                        // Get the size of the file
                                        //long length = file.length();
                                        byte[] bytes = new byte[NUMBER_KILOBYTE * 1024];
                                        fileInputSend = new FileInputStream(file);
                                        outPutStreamSend = socketSendFile.getOutputStream();

                                        int count;
                                        while ((count = fileInputSend.read(bytes)) > 0) {
                                            outPutStreamSend.write(bytes, 0, count);
                                            outPutStreamSend.flush();

                                            // debug
                                            System.out.println("Số byte gửi: " + count);
                                        }

                                    } catch (IOException ex) {
                                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                    } finally {
                                        try {
                                            if (fileInputSend != null) {
                                                fileInputSend.close();
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        try {
                                            if (outPutStreamSend != null) {
                                                outPutStreamSend.close();
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                        try {
                                            if (socketSendFile != null) {
                                                socketSendFile.close();
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                }
                            }).start();

                            break;
                        case PORTRECEIVE:
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Socket socketReceiveFile = null;
                                    InputStream inputSteam = null;
                                    OutputStream fileOutputStream = null;
                                    try {
                                        // xem lại vấn đề file name receive khi có nhiều client cùng gửi tới
                                        String fileName = new String(fileNameReceive);

                                        // debug
                                        System.out.println("Port receive từ server: " + pagServer.getData());

                                        int portReceiveServerFile = Integer.parseInt(pagServer.getData().toString());
                                        // connect tới server file
                                        socketReceiveFile = new Socket(HOST_SERVER_FILE, portReceiveServerFile);
                                        inputSteam = socketReceiveFile.getInputStream();

                                        File file = new File(fileName);
                                        if (file.exists()) {
                                            // xóa file
                                            file.delete();
                                        }

                                        fileOutputStream = new FileOutputStream(fileName);
                                        byte[] bytesReceive = new byte[NUMBER_KILOBYTE * 1024];

                                        int countReceive;
                                        while ((countReceive = inputSteam.read(bytesReceive)) > 0) {
                                            fileOutputStream.write(bytesReceive, 0, countReceive);
                                            //debug
                                            System.out.println("số lượng byte nhận dc: " + countReceive);
                                        }

                                        // lấy date hệ thống
                                        Date date = new Date();
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);
                                        int h = calendar.get(Calendar.HOUR_OF_DAY);
                                        int m = calendar.get(Calendar.MINUTE);

                                        PackageChat pckFile = new PackageChat(userSend, fileName, h, m);
                                        pckFile.setIsFile(true);
                                        Client.showListChatAPerson(pckFile);

                                    } catch (IOException ex) {
                                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                    } finally {
                                        try {
                                            if (fileOutputStream != null) {
                                                fileOutputStream.close();
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        try {
                                            if (inputSteam != null) {
                                                inputSteam.close();
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        try {
                                            if (socketReceiveFile != null) {
                                                socketReceiveFile.close();
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                }
                            }).start();

                            break;

                        case LOGOUT:
                            String messageLogout = (String) pagServer.getData();

                            if (messageLogout.equals("Successfull")) {
                                // logout thành công

                                // debug
                                System.out.println("Logout thành công");

                                isRuning = false;

                                Client clientNew = Login.getClient();
                                Login.getChatRoom().dispose();
                                Login.chatRoom = null;
                                Login login = new Login(clientNew);
                                MMTClient.login = login;
                                login.showLogin();
                            }

                            break;

                        case CHANGEPASS:
                            String content = (String) pagServer.getData();
                            Client.showStatus(content);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
