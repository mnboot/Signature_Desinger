package com.mnboot.signaturedesinger.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainRunner extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainRunner.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 260);
        stage.setTitle("Signature Desinger!");
        stage.setScene(scene);
        stage.show();
    }
}
