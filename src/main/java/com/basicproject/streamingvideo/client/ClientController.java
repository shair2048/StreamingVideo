package com.basicproject.streamingvideo.client;

import com.basicproject.streamingvideo.auth.AuthApplication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

public class ClientController {
    @FXML
    private TextField txtMessage;
    @FXML
    private Slider timeline;
    @FXML
    private Button btnPlay;
    @FXML
    private TextArea taMessages;
    @FXML
    private Label lbClientName;
    @FXML
    private MediaView mediaView;
    public Stage stage;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String loggedInUsername;
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlayed = false;
    private boolean timelineRunning = false;
    private static final int SERVER_PORT = 9876;
    private static final int BUFFER_SIZE = 62000;
    private DatagramSocket screenshotSocket;
    private DatagramSocket videoDatagramSocket;
    private DatagramSocket clientSocket;
    private InetAddress serverAddress;
    private ServerSocket videoServerSocket;
    private Socket videoSocket;
    private OutputStream videoOutputStream;
//    private byte[] receivedData;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    private File selectedFile;
    private ScheduledExecutorService executorService;
    private ByteArrayOutputStream byteArrayOutputStream;
    private static final String OUTPUT_FOLDER = "C:\\Users\\SONHAI\\Videos\\Vdo";
    private Timeline snapshotTimeline;
    private static DatagramSocket udpSocket;



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

            /*Thread udpThread = new Thread(() -> {
                try {
                    clientSocket = new DatagramSocket();
                    while (true) {
                        byte[] receiveData = new byte[62000];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);

                        byte[] receivedData = receivePacket.getData();

                        Platform.runLater(() -> {
                            media = new Media(new ByteArrayInputStream(receivedData).toString());
                            mediaPlayer = new MediaPlayer(media);
                            mediaPlayer.setAutoPlay(true);
                            mediaView.setMediaPlayer(mediaPlayer);
                        });
                    }

//                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                }

            });
            udpThread.start();*/

            udpSocket = new DatagramSocket();
            serverAddress = InetAddress.getByName("localhost");

//            Thuc hien tren luong JavaFX de hien thi username
            Platform.runLater(() -> {
                lbClientName.setText(loggedInUsername);

            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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

    @FXML
    public void handleSelectVideo(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video");
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String url = selectedFile.toURI().toString();

            media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
                timeline.setValue(newValue.toSeconds());
            }));

            mediaPlayer.setOnReady(() -> {
                Duration totalDuration = media.getDuration();
                timeline.setMax(totalDuration.toSeconds());
            });

//            mediaPlayer.setAutoPlay(false);
        }
    }

    @FXML
    public void handlePlayVideo(ActionEvent event) throws IOException {
        if (!isPlayed && selectedFile != null) {
            btnPlay.setText("Pause");
            mediaPlayer.play();


            /*mediaPlayer.setOnPlaying(() -> {
                // Trích xuất hình ảnh từ frame
                Image frameImage = mediaView.snapshot(null, null);

                // Lưu hình ảnh vào thư mục
                saveImage(frameImage, "C:\\Users\\SONHAI\\Videos\\Vdo", "frame" + mediaPlayer.getCurrentTime().toSeconds() + ".png");
            });*/

            /*snapshotTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.1), event1 -> {
                        WritableImage frameImage = mediaView.snapshot(null, null);

                        saveImage(frameImage, "C:\\Users\\SONHAI\\Videos\\Vdo", "frame" + mediaPlayer.getCurrentTime().toSeconds() + ".png");

                        *//*try {
                            sendImageToServer(frameImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*//*
                    })
            );
            snapshotTimeline.setCycleCount(Timeline.INDEFINITE);
            snapshotTimeline.play();

            mediaPlayer.setOnEndOfMedia(() -> {
                stopCapturing();
            });*/

            isPlayed = true;

        } else {
            btnPlay.setText("Play");
            mediaPlayer.pause();
            isPlayed = false;

            /*if (snapshotTimeline != null) {
                snapshotTimeline.stop();
            }*/

//            stopCapturing();
        }
    }

    private void stopCapturing() {
        if (snapshotTimeline != null) {
            snapshotTimeline.stop();
        }
    }

    /*private void sendImageToServer(WritableImage image) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteStream);

        byte[] imageData = byteStream.toByteArray();
//        System.out.println(imageData.length);
        DatagramPacket packet = new DatagramPacket(imageData, imageData.length, serverAddress, SERVER_PORT);
        udpSocket.send(packet);
    }*/

    private void saveImage(Image image, String folderPath, String fileName) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File outputFile = new File(folder, fileName);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
            System.out.println("Saved: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private void sendImage(byte[] imageData) throws IOException {
        try (Socket imageSocket = new Socket("localhost", SERVER_PORT);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byteArrayOutputStream.write(imageData);
            byteArrayOutputStream.writeTo(imageSocket.getOutputStream());
        }
    }*/

    @FXML
    public void timelinePressed(MouseEvent event) throws IOException {
        mediaPlayer.seek(Duration.seconds(timeline.getValue()));
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("/com/basicproject/streamingvideo/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
