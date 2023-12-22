package com.basicproject.streamingvideo.server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.util.ArrayList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static ArrayList<ClientHandlerTCP> clients = new ArrayList<ClientHandlerTCP>();
    private static final int TCP_PORT = 8889;
    private static final int UDP_PORT = 9876;
    private static final int BUFFER_SIZE = 1024;
//    private static ServerSocket videoServerSocket;
    private static DatagramSocket udpSocket;

    public static void main(String[] args) {
        startTCPServer();
        startUDPServer();
    }

    private static void startTCPServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(TCP_PORT);
//            videoServerSocket = new ServerSocket(9888);

            while (true) {
                System.out.println("Waiting for clients...");
                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                ClientHandlerTCP clientThread = new ClientHandlerTCP(socket, clients);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void startUDPServer() {
        /*DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(UDP_PORT);

            while (true) {
                byte[] receiveData = new byte[62000];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
//                System.out.println("Đã nhận được dữ liệu video");

                *//*Thread thread = new Thread(new ClientHandlerUDP(serverSocket, receivePacket));
                thread.start();*//*

                // Process the received data in the same thread (no need for a new thread for each packet)
                processReceivedData(receivePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }*/


        try {
            udpSocket = new DatagramSocket(UDP_PORT);

            while (true) {
                receiveImageFromClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveImageFromClient() throws IOException {
        byte[] buffer = new byte[65507]; // Độ dài tối đa của một gói UDP

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        udpSocket.receive(packet);

        byte[] imageData = packet.getData();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(imageData);
        BufferedImage image = ImageIO.read(byteStream);

        saveImageToFile(image);
    }

    private static void saveImageToFile(BufferedImage image) throws IOException {
        // Đặt đường dẫn đến thư mục lưu ảnh trên server
        String folderPath = "C:\\Users\\SONHAI\\Videos\\Vdo";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Đặt tên file ảnh theo thời gian hoặc một phương thức nào đó
        String fileName = System.currentTimeMillis() + ".png";

        // Lưu ảnh vào thư mục
        File file = new File(folder, fileName);
        ImageIO.write(image, "png", file);
    }


    /*private static void processReceivedData(DatagramPacket receivePacket) {
        try {
            // Extract the received data from the packet
            byte[] receivedData = receivePacket.getData();

            // Get client address and port
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Send the received data back to the client using a new UDP socket
            try (DatagramSocket sendSocket = new DatagramSocket()) {
                DatagramPacket sendPacket = new DatagramPacket(receivedData, receivedData.length, clientAddress, clientPort);
                sendSocket.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}