package project.cloudmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CloudManager extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Add home view
        Parent root = FXMLLoader.load(getClass().getResource("home-view-new.fxml"));
        Scene scene = new Scene(root, Color.TRANSPARENT);

        // Remove title bar
        stage.initStyle(StageStyle.TRANSPARENT);

        // Set app icon and title
        Image appIcon = new Image(getClass().getResource("images/IconSmall.png").toExternalForm());
        stage.getIcons().add(appIcon);
        stage.setTitle("CloudManager");

        // Show app
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}