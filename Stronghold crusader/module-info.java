module project.group {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;
    requires java.desktop;
    requires junit;
    requires jme3.core;

    exports Client.view;
    exports Client.model;
    opens Client.view to javafx.fxml;
    opens Client.model to com.google.gson;
}