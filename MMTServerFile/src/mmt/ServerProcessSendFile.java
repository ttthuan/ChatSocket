/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keven
 */
public class ServerProcessSendFile {

    private int PORT = 0;
    private ServerSocket socketServer = null;
    private Socket socket = null;
    private OutputStream outPutStream = null;
    private String userName = null;

    public Socket getSocket() {
        return socket;
    }

    public OutputStream getOutPutStream() {
        return outPutStream;
    }
    

    public ServerProcessSendFile(String userName) {
        this.userName = userName;
    }

    public int rand(int min, int max) {
        try {
            Random rn = new Random();
            int range = max - min + 1;
            int randomNum = min + rn.nextInt(range);
            return randomNum;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void startServerProcessFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Server send file listening on port: " + PORT);
                try {
                    socket = socketServer.accept();
                    System.out.println("a client connected to Server send file " + userName);
                    outPutStream = socket.getOutputStream();

                } catch (IOException ex) {
                    Logger.getLogger(ServerProcessSendFile.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("không thể accept client " + userName);
                }
            }
        }).start();
    }

    public int generaPort() {
        boolean isNotUse = true;
        while (isNotUse) {
            try {
                PORT = rand(49152, 65535);
                socketServer = new ServerSocket(PORT);
                isNotUse = false;
            } catch (IOException ex) {
                Logger.getLogger(ServerProcessSendFile.class.getName()).log(Level.SEVERE, null, ex);
                isNotUse = true;
            }
        }
        return PORT;
    }
}
