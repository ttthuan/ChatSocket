/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Totoro
 */
public class Transport {

    private Socket socket = null;
    private ObjectOutputStream send = null;
    private ObjectInputStream recive = null;
    
    public Transport(Socket socket){
        this.socket = socket;
    }
    
    // send a package through network
    public void sendPackage(Package pag) throws IOException{
        send = new ObjectOutputStream(socket.getOutputStream());
        send.writeObject(pag);
        send.flush();
    }

    // recive a package through network
    public Package recivePackage() throws IOException, ClassNotFoundException{
        recive = new ObjectInputStream(socket.getInputStream());
        Package pag = null;
        pag = (Package) recive.readObject();
        return pag;
    }
    
    public void close() throws IOException{
        if(send != null)
            send.close();
        if(recive != null)
            recive.close();
    }

}
