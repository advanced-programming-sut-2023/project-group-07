module project.group {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;
    requires java.desktop;
    requires junit;
    requires jme3.core;

    exports view;
    exports model;
    opens view to javafx.fxml;
    opens model to com.google.gson;
}