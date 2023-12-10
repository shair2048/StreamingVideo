package com.basicproject.streamingvideo.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.net.Socket;

public class ClientController {
    @FXML
    private TextField txtMessage;
    @FXML
    private Button btnSend;
    @FXML
    private TextArea taMessages;
    @FXML
    private Label lbClientName;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String loggedInUsername;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    @FXML
    public void initialize() {
        try {
            socket = new Socket("localhost", 8889);
            System.out.println("Socket is connected with server!");
            outputStream = socket.getOutputStream();

            Thread thread = new Thread(() -> {
                try {
                    inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        String message = new String(buffer, 0, bytesRead);
                        taMessages.appendText(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
//            Thuc hien tren luong JavaFX de hien thi username
            Platform.runLater(() -> {
                lbClientName.setText(loggedInUsername);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSendingMessage(ActionEvent event) throws IOException {
        String message = txtMessage.getText().trim();
        if (!message.equals("")) {
//            taMessages.appendText("Me: " + message + "\n");
            sendMessage(loggedInUsername  + ": " + message + "\n");
//            System.out.println(message);
        }

        txtMessage.clear();
    }

    private void sendMessage(String message) {
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleSelectVideo(ActionEvent event) throws IOException {

    }
}
