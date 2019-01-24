module CopySave {
    requires java.logging;
    requires java.desktop;
    requires java.prefs;

    requires javafx.controls;
    requires javafx.media;
    requires javafx.web;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    requires org.json;
    requires jnativehook;

    exports com.CopySave;
}