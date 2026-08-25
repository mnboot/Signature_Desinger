package com.mnboot.signaturedesinger;

import com.mnboot.signaturedesinger.Crypto.RSA;
import com.mnboot.signaturedesinger.GUI.HelloApplication;
import javafx.application.Application;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
        //new RSA().bruteForce("He");

    }
}
