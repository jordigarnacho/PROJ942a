package com.facefinder.jordigarnacho.facefinder;

/**
 * Created by Jordi GARNACHO on 15/11/2016.
 */

import java.io.*;
import java.net.*;


/** Le processus client se connecte au site fourni dans la commande
 *   d'appel en premier argument et utilise le port distant 8080.
 */
public class SocketConnection {
    public int port = 8000;
    public Socket socket;

    public SocketConnection() throws IOException {
        this.socket = new Socket("192.168.118.102", port);
    }

    public BufferedReader ReadDataText() throws Exception {
            BufferedReader data = new BufferedReader(new InputStreamReader(this.socket.getInputStream())

        );
        return data;
    }

    public void SendDataText() throws Exception {
        PrintWriter data = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())),
                true);
    }

    public void SendPicture(String pathPicture) throws Exception {
        String pathFile = pathPicture;
        new FileInputStream(pathFile);
    }

    public void SocketStop() throws Exception {
        this.socket.close();
    }
}