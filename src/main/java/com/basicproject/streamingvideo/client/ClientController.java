package com.basicproject.streamingvideo.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;

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
    private InputStream inputStream;
    private OutputStream outputStream;
    private String loggedInUsername;
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlayed = false;
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
        File selectedFile = fileChooser.showOpenDialog(null);

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
        if (!isPlayed) {
            btnPlay.setText("Pause");
            mediaPlayer.play();
            isPlayed = true;
        } else {
            btnPlay.setText("Play");
            mediaPlayer.pause();
            isPlayed = false;
        }
    }

    @FXML
    public void timelinePressed(MouseEvent event) throws IOException {
        mediaPlayer.seek(Duration.seconds(timeline.getValue()));
    }
}
