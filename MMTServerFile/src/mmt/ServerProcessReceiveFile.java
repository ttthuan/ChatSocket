/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.io.InputStream;
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
public class ServerProcessReceiveFile {

    private int PORT = 0;
    private ServerSocket socketServer = null;
    private Socket socket = null;
    private boolean running = true;
    private InputStream in = null;
    private OutputStream out = null;
    private ServerProcessSendFile serverProcessSendFileSend = null;
    private String userName;
    private final int NUMBER_KILOBYTE = 64;

    public Socket getSocket() {
        return socket;
    }

    public ServerProcessReceiveFile(ServerProcessSendFile serverProcessSendFileSend, String userName) {
        this.serverProcessSendFileSend = serverProcessSendFileSend;
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
                System.out.println("Server receive file listening on port: " + PORT);
                try {
                    socket = socketServer.accept();
                    System.out.println("a client connected to Server receive file " + userName);
                    in = socket.getInputStream();
                    
                    while(serverProcessSendFileSend.getOutPutStream()==null){
                        
                    }
                    receiveAndSendFile();
                    
                } catch (IOException ex) {
                    Logger.getLogger(ServerProcessReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("không thể accept client " + userName);
                }
            }
        }).start();
    }

    public void receiveAndSendFile(){
        byte[] bytes = new byte[NUMBER_KILOBYTE * 1024];

        out = serverProcessSendFileSend.getOutPutStream();
        
        int count;
        try {
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
                out.flush();
                // debug
                System.out.println("Server file nhận và gửi số byte " + count);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerProcessReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerProcessReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerProcessReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerProcessReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            serverProcessSendFileSend.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(ServerProcessReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int generaPort() {
        boolean isNotUse = true;
        while (isNotUse) {
            try {
                PORT = rand(49152, 65535);
                socketServer = new ServerSocket(PORT);
                isNotUse = false;
            } catch (IOException ex) {
                Logger.getLogger(ServerProcessReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
                isNotUse = true;
            }
        }
        return PORT;
    }
}
