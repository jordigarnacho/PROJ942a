package com.facefinder.jordigarnacho.facefinder;

/**
 * Created by Jordi GARNACHO on 15/11/2016.
 */

import java.io.*;
import java.net.*;

public class SocketConnection {
    public Socket socket;

    public void SocketConnection(int port, String serverIP) throws IOException {
        this.socket = new Socket(serverIP, port);
    }

    public Boolean getSocketStatus(){
        return (this.socket.isConnected());
    }

    public String ReadDataText() throws Exception {
            return (this.socket.getInputStream().toString());
    }

    public void SendPicture(String pathPicture) throws Exception {
        String pathFile = pathPicture;
        File file = new File(pathFile);
        byte [] fileSize = new byte [(int)file.length()];
        FileInputStream fileToSend = new FileInputStream(pathFile);
        BufferedInputStream fileBufferedInputStream = new BufferedInputStream(fileToSend);
        OutputStream fileOutputStream = this.socket.getOutputStream();
        System.out.println("Sending " + pathFile + "(" + fileSize.length + " bytes)");
        fileOutputStream.write(fileSize,0,fileSize.length);
        fileOutputStream.flush();
        System.out.println("Done.");
        if (fileBufferedInputStream != null) fileBufferedInputStream.close();
        if (fileOutputStream != null) fileOutputStream.close();
    }

    public void SocketStop() throws Exception {
        this.socket.close();
    }
}