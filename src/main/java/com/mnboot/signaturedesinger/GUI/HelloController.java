package com.mnboot.signaturedesinger.GUI;

import com.mnboot.signaturedesinger.Crypto.Hash;
import com.mnboot.signaturedesinger.Crypto.HashBase;
import com.mnboot.signaturedesinger.Crypto.RSA;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class HelloController implements Initializable {

    private static final Logger log =
            LogManager.getLogger(HelloController.class);


    public TextField txtfieldPrefix;
    public Label lblMessage;
    @FXML
    public TextArea txtareaProgress;
    String prefix;
    RSA rsa = new RSA();


    private Thread th;

    public HelloController() throws NoSuchAlgorithmException {}
    public void ocGenerate(ActionEvent actionEvent) {
        prefix = txtfieldPrefix.getText();

        if (th != null && th.isAlive()) {
            th.interrupt();
            txtareaProgress.clear();
        }

        th = new Thread(
                () -> {
                    Platform.runLater(() -> {
                        lblMessage.setText("Generating RSA key...");
                        lblMessage.setTextFill(Color.rgb(217, 186, 61));
                    });
                    rsa.bruteForce(prefix);
                    Platform.runLater(() -> {
                        lblMessage.setTextFill(Color.GREEN);
                        lblMessage.setText("Found Public Key!");
                    });
                }
        );
        
        th.start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextAreaAppender appender =
                new TextAreaAppender("UI", txtareaProgress);

        appender.start();

        LoggerContext context =
                (LoggerContext) LogManager.getContext(false);

        Configuration config = context.getConfiguration();

        config.getRootLogger().setLevel(Level.ALL);

        config.getRootLogger()
                .addAppender(appender, Level.ALL, null);

        context.updateLoggers();

        Platform.runLater(() -> {
            Stage stage = (Stage) txtfieldPrefix.getScene().getWindow();

            stage.setOnCloseRequest(event -> {
                System.exit(0);

                Platform.exit();
            });
        });

    }

    public void ocExportPrivate(ActionEvent actionEvent) throws IOException {
        if (!rsa.isKeyPairGenerated()){
            new Alert(Alert.AlertType.ERROR, "Key Pair Is Not Generated Yet", ButtonType.OK).showAndWait();
            return;
        }

        Window window = ((Node)actionEvent.getSource()).getScene().getWindow();

        FileChooser fc = new FileChooser();
        fc.setTitle("Export Private Key");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private Key", "*.pem"));
        fc.setInitialFileName("private_key.pem");

        File file = fc.showSaveDialog(window);
        saveFile(file, rsa.getStringPrivateKeyPem());


    }

    public void ocExportPublic(ActionEvent actionEvent) throws IOException {
        if (!rsa.isKeyPairGenerated()){
            new Alert(Alert.AlertType.ERROR, "Key Pair Is Not Generated Yet", ButtonType.OK).showAndWait();
            return;
        }

        Window window = ((Node)actionEvent.getSource()).getScene().getWindow();

        FileChooser fc = new FileChooser();
        fc.setTitle("Export Public Key");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public Key", "*.pem"));
        fc.setInitialFileName("public_key.pem");

        File file = fc.showSaveDialog(window);
        saveFile(file, rsa.getStringPublicKeyPem());

    }

    public void saveFile(File file, String content) throws IOException {

        if (file == null) {
            new Alert(Alert.AlertType.ERROR, "No file selected", ButtonType.OK).showAndWait();
            return;
        }


        try(FileWriter fw = new FileWriter(file)){
            fw.write(content);
        }
    }


    public void ocVerify(ActionEvent actionEvent) throws IOException {
        Window window = ((Node)actionEvent.getSource()).getScene().getWindow();
        HashBase hashBase = new HashBase(Hash.SHA256);


        AtomicReference<String> prefixAtom = new AtomicReference<>();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Prefix");
        dialog.showAndWait().ifPresent(prefixAtom::set);

        String prefix;
        if((prefix = prefixAtom.get()) == null){
            new Alert(Alert.AlertType.ERROR, "Error in Prefix", ButtonType.OK).showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Verify Public Key");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public Key", "*.pem"));
        File file = fileChooser.showOpenDialog(window);

        try(FileReader fileReader = new FileReader(file)){
            String fileContent = fileReader.readAllAsString();
            if(hashBase.verify_key(fileContent, prefix)){
                new Alert(Alert.AlertType.INFORMATION, "Public Key Verified (%s)".formatted(hashBase.getInnerKey(fileContent)), ButtonType.OK).showAndWait();
            }else{
                new Alert(Alert.AlertType.ERROR, "Invalid Public Key (%s)".formatted(hashBase.getInnerKey(fileContent)), ButtonType.OK).showAndWait();
            }
        }


    }
}
