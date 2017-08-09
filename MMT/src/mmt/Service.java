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
 * @author Totoro
 */
class Service extends Thread {

    Socket socket = null;

    public Service(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // xử lý nhận
        Transport transport = new Transport(socket);
        try {
            while (true) {
                Package pagClient = (Package) transport.recivePackage();
                if (null != pagClient.getHeader()) {
                    switch (pagClient.getHeader()) {

                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }

        /// xử lý gửi
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
