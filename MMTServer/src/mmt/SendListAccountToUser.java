/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keven
 */
public class SendListAccountToUser extends Thread {

    private Socket socket = null;
    private Account account = null;
    private final Service[] services;
    private int maxNumberClient;
    private Transport transport;
    private int time = 0;
    private boolean isFirstRun = true;
    private boolean isRunning = true;
    private Service service = null;

    public boolean isIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public SendListAccountToUser(Socket socket, Service[] services, Service service) throws IOException {
        this.socket = socket;
        this.services = services;
        this.maxNumberClient = services.length;
        transport = new Transport(socket);
        this.service = service;
    }

    public void sleepWithTime() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(SendListAccountToUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        while (isRunning) {
            sleepWithTime();

            List<Account> accounts = new ArrayList<>();
            synchronized (this) {
                for (int i = 0; i < maxNumberClient; i++) {
                    if (services[i] != null && services[i].getAccount() != null && services[i] != service) {
                        accounts.add(services[i].getAccount());
                    }
                }
            }

            Package pck = new Package(Header.LISTACCOUNT, accounts);

            //debug
            System.out.println(service.getAccount().getUserName() + " đã gửi list: " + accounts.size());

            try {
                transport.sendPackage(pck);
            } catch (IOException ex) {
                Logger.getLogger(SendListAccountToUser.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (isFirstRun) {
                time = 5000;
                isFirstRun = false;
            }
        }
        
        try {
            transport.close();
        } catch (IOException ex) {
            Logger.getLogger(SendListAccountToUser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("lỗi đóng stream gửi list account cho user");
        }
    }

}
