module com.basicproject.streamingvideo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;

    requires com.dlsc.formsfx;
    requires java.desktop;
//    requires jmf;

    opens com.basicproject.streamingvideo to javafx.fxml;
    exports com.basicproject.streamingvideo.auth;
    opens com.basicproject.streamingvideo.auth to javafx.fxml;
    exports com.basicproject.streamingvideo.client;
    opens com.basicproject.streamingvideo.client to javafx.fxml;
}