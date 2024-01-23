package com.basicproject.streamingvideo.server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private static DatagramSocket udpSocket;

    public static void main(String[] args) {
        startTCPServer();
        startUDPServer();
    }

    private static void startTCPServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(TCP_PORT);

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
        try {
            udpSocket = new DatagramSocket(UDP_PORT);

            /*Path saveDirectory = Paths.get("C:\\Users\\SONHAI\\Videos\\Vdo");
            if (!Files.exists(saveDirectory)) {
                Files.createDirectories(saveDirectory);
            }*/

            byte[] buffer = new byte[62000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                udpSocket.receive(packet);

                /*String fileName = "image_" + System.currentTimeMillis() + ".png";
                Path filePath = saveDirectory.resolve(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
                fileOutputStream.write(packet.getData(), packet.getOffset(), packet.getLength());
                fileOutputStream.close();

                System.out.println("Đã nhận và lưu hình ảnh: " + fileName);*/

                new Thread(() -> {
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                        udpSocket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (udpSocket != null && !udpSocket.isClosed()) {
                udpSocket.close();
            }
        }
    }

    /*private static void receiveImageFromClient() throws IOException {
        ByteArrayOutputStream completeImageData = new ByteArrayOutputStream();
        int packetSize = 62000;
        byte[] buffer = new byte[packetSize];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            udpSocket.receive(packet);

            if (packet.getLength() == 0) {
                break;
            }

            byte[] packetData = packet.getData();
            completeImageData.write(packetData, 0, packet.getLength());
        }

        if (completeImageData.size() > 0) {
            byte[] imageData = completeImageData.toByteArray();
            ByteArrayInputStream byteStream = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(byteStream);

            saveImageToFile(image);
            System.out.println("da nhan");
        } else {
            System.out.println("Khong nhan duoc");
        }
    }*/

    /*private static void saveImageToFile(BufferedImage image) throws IOException {
        String folderPath = "C:\\Users\\SONHAI\\Videos\\Vdo";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = System.currentTimeMillis() + ".png";

        File file = new File(folder, fileName);
        ImageIO.write(image, "png", file);
    }*/
}