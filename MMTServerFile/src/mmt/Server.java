/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Keven
 */
public class Server {
    private static int PORT = 60602;
    private static ServerSocket socketServer = null;
    private static Socket socket = null;
    private static final int maxNumberClient = 20;
    private static final Service[] services = new Service[maxNumberClient];
    private static boolean running = true;
    
    public synchronized static void start() throws IOException {
        socketServer = new ServerSocket(PORT);
        System.out.println("Server send file listening...");
        while (running) {
            socket = socketServer.accept();
            System.out.println("Server connected!");
            
            int i;
            for(i = 0; i < maxNumberClient; i++){
                if(services[i] == null){
                    (services[i] = new Service(socket, services)).start();
                    break;
                }
            }
            
            if(i == maxNumberClient){
                Package pck = new Package(Header.BUSY, "Server file too busy. Try later.");
                Transport transport = new Transport(socket);
                transport.sendPackage(pck);
                transport.close();
                socket.close();
            }
        }
    }
}
