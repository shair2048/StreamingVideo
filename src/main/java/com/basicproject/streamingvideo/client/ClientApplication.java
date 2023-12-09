package com.basicproject.streamingvideo.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("/com/basicproject/streamingvideo/client.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
