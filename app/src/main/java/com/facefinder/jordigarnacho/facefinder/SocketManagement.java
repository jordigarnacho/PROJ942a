package com.facefinder.jordigarnacho.facefinder;

/**
 * Created by Jordi GARNACHO on 15/11/2016.
 */

import java.io.*;
import java.net.*;

public class SocketManagement {
    public Socket socket;

    public SocketManagement(String serverIP,int serverPort) throws IOException {
        this.socket = new Socket(serverIP, serverPort);
    }

    public Boolean getSocketStatus(){
        return (this.socket.isConnected());
    }

    public String readDataText() throws Exception {
        return (this.socket.getInputStream().toString());
    }

    public void sendPicture(File Picture) throws Exception {
        byte [] fileSize = new byte [(int)Picture.length()];
        FileInputStream fileToSend = new FileInputStream(Picture);
        BufferedInputStream fileBufferedInputStream = new BufferedInputStream(fileToSend);
        OutputStream fileOutputStream = this.socket.getOutputStream();
        fileOutputStream.write(fileSize,0,fileSize.length);
        fileOutputStream.flush();
        if (fileBufferedInputStream != null) fileBufferedInputStream.close();
        if (fileOutputStream != null) fileOutputStream.close();
    }

    public void socketUpdate(String serverIP, int serverPort) throws IOException {
        this.socket.close();
        this.socket = new Socket(serverIP, serverPort);
    }

    public void socketStop() throws IOException {
        this.socket.close();
    }
}