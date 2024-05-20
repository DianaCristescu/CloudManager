module demo.cloudmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.auth;
    requires com.google.api.client.json.gson;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.services.drive;


    opens demo.cloudmanager to javafx.fxml;
    exports demo.cloudmanager;
    exports demo.cloudmanager.filecardview;
    opens demo.cloudmanager.filecardview to javafx.fxml;
}