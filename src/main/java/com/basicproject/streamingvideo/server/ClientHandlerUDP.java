package com.basicproject.streamingvideo.server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientHandlerUDP implements Runnable{
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;

    public ClientHandlerUDP(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        try {
            // Lấy địa chỉ IP và cổng của client
            InetAddress diaChiClient = receivePacket.getAddress();
            int congClient = receivePacket.getPort();

            // Xử lý dữ liệu video nhận được
            byte[] duLieuVideo = receivePacket.getData();
            int doDaiVideo = receivePacket.getLength();

            // Thực hiện bất kỳ xử lý video cần thiết ở đây

            // Tạo một DatagramPacket mới để gửi video trở lại cho client
            DatagramPacket goiGui = new DatagramPacket(duLieuVideo, doDaiVideo, diaChiClient, congClient);

            // Tạo một DatagramSocket mới để gửi video
            DatagramSocket socketGui = new DatagramSocket();

            // Gửi video trở lại cho client
            socketGui.send(goiGui);

            // Đóng socket gửi
            socketGui.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

