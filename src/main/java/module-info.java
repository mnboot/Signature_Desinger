module com.mnboot.signaturedesinger {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires static lombok;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens com.mnboot.signaturedesinger.GUI to javafx.fxml;
    exports com.mnboot.signaturedesinger;
    exports com.mnboot.signaturedesinger.GUI;
}