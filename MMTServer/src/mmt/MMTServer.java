/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keven
 */
public class MMTServer {

    private static Server server = null;

    public static void main(String[] args) {
        try {
            server = new Server();
            System.out.println(server.getAccounts().size());
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
        } catch (SQLException ex) {
            System.out.println("Lỗi kết nối tới database");
            Logger.getLogger(MMTServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
