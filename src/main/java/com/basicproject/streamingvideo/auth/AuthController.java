package com.basicproject.streamingvideo.auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {
    public Stage stage;
    public Scene scene;

    public void switchToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("/com/basicproject/streamingvideo/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegister(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("/com/basicproject/streamingvideo/register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
