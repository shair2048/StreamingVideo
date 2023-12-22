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
    private InputStream videoInputStream;
    private static final int BUFFER_SIZE = 62000;
//    private ServerSocket videoServerSocket;


    public ClientHandlerTCP(Socket socket, ArrayList<ClientHandlerTCP> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
//            this.videoServerSocket = videoServerSocket;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
//                System.out.println(msg);
                if (msg.equalsIgnoreCase( "exit")) {
                    break;
                } /*else if (msg.startsWith("video:")) {
                    // Handle video file transfer
                    receiveAndForwardVideo(msg.substring("video:".length()));
                } else {
                    // Forward regular messages
                    for (ClientHandlerTCP cl : clients) {
                        cl.writer.println(msg);
                        cl.writer.flush();
                    }
                }*/

                for (ClientHandlerTCP cl : clients) {
                    cl.writer.println(msg);
                    cl.writer.flush(); // Flush PrintWriter
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

    /*private void receiveAndForwardVideo(String videoFileName) throws IOException {
        try (Socket videoClientSocket = videoServerSocket.accept();
             InputStream videoInputStream = videoClientSocket.getInputStream()) {

            // Forward video file to other clients
            for (ClientHandlerTCP cl : clients) {
                if (cl != this) {
                    cl.writer.println("video:" + videoFileName);
                    cl.writer.flush();
                }
            }

            // Receive and save video file
            File receivedVideoFile = new File("C:\\Users\\SONHAI\\Videos\\Vdo" + videoFileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(receivedVideoFile)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;

                while ((bytesRead = videoInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }

            // Broadcast to other clients that the video file is ready
            for (ClientHandlerTCP cl : clients) {
                if (cl != this) {
                    cl.writer.println("video_ready:" + videoFileName);
                    cl.writer.flush();
                }
            }
        }
    }*/
}