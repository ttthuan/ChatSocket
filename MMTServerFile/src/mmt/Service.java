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
 * @author Keven
 */
public class Service extends Thread {

    private Socket socket = null;
    private Account account = null;
    private final Service[] services;
    private int maxNumberClient;
    private Transport transport = null;
    private ServerProcessReceiveFile serverProcessReceiveFile = null;
    private ServerProcessSendFile serverProcessSendFile = null;
    private boolean isRunning = true;

    public Service(Socket socket, Service[] services) {
        this.socket = socket;
        this.services = services;
        this.maxNumberClient = services.length;
        transport = new Transport(socket);
    }

    @Override
    public void run() {
        // nhận yêu cầu gửi file
        try {
            Package pagServer = (Package) transport.recivePackage();
            if (null != pagServer.getHeader()) {
                switch (pagServer.getHeader()) {
                    // có request từ server
                    case HASREQUEST:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String content = (String) pagServer.getData();

                                // user[0] send, user[1] receive
                                String[] userNames = content.split(";");

                                // debug
                                System.out.println("Có yêu cầu gửi file " + content);

                                serverProcessSendFile = new ServerProcessSendFile(userNames[1]);
                                serverProcessReceiveFile = new ServerProcessReceiveFile(serverProcessSendFile, userNames[0]);

                                // gửi 2 port về cho server chính
                                String ports = serverProcessReceiveFile.generaPort() + ";" + serverProcessSendFile.generaPort();
                                mmt.Package pck = new mmt.Package(Header.PORT, ports);
                                try {
                                    transport.sendPackage(pck);
                                } catch (IOException ex) {
                                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                // start lắng nghe client kết nối
                                serverProcessSendFile.startServerProcessFile();
                                serverProcessReceiveFile.startServerProcessFile();

                                isRunning = false;

                                synchronized (this) {
                                    for (int i = 0; i < maxNumberClient; i++) {
                                        if (services[i] != null && services[i].socket == socket) {
                                            services[i] = null;
                                        }
                                    }
                                }
                            }
                        }).start();

                        break;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
    }

}
