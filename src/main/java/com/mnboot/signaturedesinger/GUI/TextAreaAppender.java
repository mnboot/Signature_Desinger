package com.mnboot.signaturedesinger.GUI;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class TextAreaAppender extends AbstractAppender {

    private final TextArea textArea;

    public TextAreaAppender(String name, TextArea textArea) {
        super(
                name,
                null,
                PatternLayout.createDefaultLayout(),
                false,
                null
        );

        this.textArea = textArea;
    }

    @Override
    public void append(LogEvent event) {

        String message = event.getMessage().getFormattedMessage();

        Platform.runLater(() -> {
            if (textArea != null) {
                textArea.appendText(message + System.lineSeparator());
            }
        });
    }
}