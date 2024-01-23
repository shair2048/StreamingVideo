package com.basicproject.streamingvideo.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*import Client.Controller;
import Client.User;*/

public class ClientHandlerTCP extends Thread {

    private ArrayList<ClientHandlerTCP> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandlerTCP(Socket socket, ArrayList<ClientHandlerTCP> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String comment;
            while ((comment = reader.readLine()) != null) {
                /*if (comment.equalsIgnoreCase( "exit")) {
                    break;
                }*/

                for (ClientHandlerTCP cl : clients) {
                    cl.writer.println(comment);
                    cl.writer.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}