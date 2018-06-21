/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keven
 */
public class MMTServer {

    private static Server server = null;
    private static Scanner scan = new Scanner(System.in);

    public static void startServer() {
        // nhập port
        initPort();

        // log file
        WriteFileLog.writeFileLog("Server", TYPELOG.INFORMATION, "Start server");

        server = new Server();

        // debug
        //System.out.println(server.getAccounts().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Server.start();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public static void initPort() {
        boolean isNotUse = true;
        while (isNotUse) {
            try {
                System.out.print("Enter port (49152–65535): ");
                Server.PORT = scan.nextInt();
                Server.socketServer = new ServerSocket(Server.PORT);
                isNotUse = false;
            } catch (IOException ex) {
                Logger.getLogger(MMTServer.class.getName()).log(Level.SEVERE, null, ex);
                isNotUse = true;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("############# menu ##############");
        System.out.println("Start server: start");
        System.out.println("Stop server: stop");
        System.out.println("Show list accout: show accounts");
        System.out.println("Delete file log: delete log");
        System.out.println("Delete user: delete user");
        System.out.println("Show menu: menu");
        System.out.println("################################");

        String command;
        while (true) {
            command = scan.nextLine();
            if (command.equalsIgnoreCase("show accounts")) {
                server.showAllAccount();
            } else if (command.equalsIgnoreCase("stop")) {
                // log file
                WriteFileLog.writeFileLog("Server", TYPELOG.INFORMATION, "Stop server");

                server.closeServer();
                break;
            } else if (command.equals("start")) {
                startServer();
            } else if (command.equals("menu")) {
                System.out.println("############# menu ##############");
                System.out.println("Start server: start");
                System.out.println("Stop server: stop");
                System.out.println("Show list accout: show accounts");
                System.out.println("Delete file log: delete log");
                System.out.println("Delete user: delete user");
                System.out.println("################################");
            } else if (command.equals("delete log")) {
                WriteFileLog.deleteFileLog();
            } else if (command.equals("delete user")) {
                System.out.print("Nhập username: ");
                String username = scan.nextLine();

                // log file
                WriteFileLog.writeFileLog("Server", TYPELOG.INFORMATION, "delete username: " + username);

                DataProvider dataProvider = new DataProvider();
                dataProvider.deleteNode(username);
                Server.removeUser(username);
            }
        }
    }
}
