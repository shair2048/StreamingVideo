package com.basicproject.streamingvideo.auth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("/com/basicproject/streamingvideo/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Auth");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
