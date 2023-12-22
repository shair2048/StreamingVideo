package com.basicproject.streamingvideo.auth;

import com.basicproject.streamingvideo.client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class AuthController {
    public Stage stage;
    public Scene scene;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtUsernameRegister;
    @FXML
    private TextField txtPasswordRegister;
    @FXML
    private Label lbNotification;
    private String filePath = "D:\\StreamingVideo\\src\\main\\java\\com\\basicproject\\streamingvideo\\auth\\Account.txt";

    public void switchToLogin(ActionEvent event) throws IOException {
        String usernameRegister = txtUsernameRegister.getText().trim();
        String passwordRegister = txtPasswordRegister.getText().trim();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write(usernameRegister + "," + passwordRegister);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void handleLogin(ActionEvent event) throws IOException {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (!username.equals("") && !password.equals("")) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        // Lấy username và password từ mảng parts
                        String storedUsername = parts[0].trim();
                        String storedPassword = parts[1].trim();

                        if (username.equals(storedUsername) && password.equals(storedPassword)) {
//                            System.out.printf(username);
                            FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("/com/basicproject/streamingvideo/client.fxml"));
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            Scene scene = new Scene(fxmlLoader.load());

//                            Truyen username sang ClientController
                            ClientController clientController = fxmlLoader.getController();
                            clientController.setLoggedInUsername(username);

                            stage.setScene(scene);
                            stage.show();
                        } else {
                            lbNotification.setText("Username or Password is incorrect!");
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            lbNotification.setText("Username or Password is empty!");
        }

        txtUsername.clear();
        txtPassword.clear();
    }
}
