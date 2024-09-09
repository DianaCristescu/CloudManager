module project.cloudmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.drive;
    requires jdk.httpserver;
    requires org.fxmisc.flowless;
    requires jdk.javadoc;

    exports project.cloudmanager;
    opens project.cloudmanager to javafx.fxml;

    exports project.cloudmanager.flowless;
    opens project.cloudmanager.flowless to javafx.fxml;

    exports project.cloudmanager.other;
    opens project.cloudmanager.other to javafx.fxml;
}