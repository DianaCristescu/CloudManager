package demo.cloudmanager;

import demo.cloudmanager.filecardview.CloudFilesWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ResourceBundle;

public class HomeViewController implements Initializable {
    private static final double MIN_SCREEN_HEIGHT = 500;
    private static final double MIN_SCREEN_WIDTH = 360;
    private double mouseNodeX = 0;
    private double mouseNodeY = 0;
    private double mousePreviousScreenX = 0;
    private double mousePreviousScreenY = 0;
    private CloudFilesWindowController cloudFilesWindowControllerTopL;
    private CloudFilesWindowController cloudFilesWindowControllerTopR;
    private CloudFilesWindowController cloudFilesWindowControllerBottom;

    @FXML
    private ButtonBar titleBar;
    @FXML
    private Button btMin;
    @FXML
    private Button btMax;
    @FXML
    private Button btClose;
    @FXML
    private Pane resizeBarLeft;
    @FXML
    private Pane resizeBarRight;
    @FXML
    private Pane resizeBarUp;
    @FXML
    private Pane resizeBarDown;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane cloudFileAnchorTopL;
    @FXML
    private AnchorPane cloudFileAnchorTopR;
    @FXML
    private AnchorPane cloudFileAnchorBottom;
    @FXML
    private TextField cloudFileSearchTopL;
    @FXML
    private TextField cloudFileSearchTopR;
    @FXML
    private TextField cloudFileSearchBottom;
//    @FXML
//    private Label label1;
//    @FXML
//    private Label label2;
//    @FXML
//    private Label label3;
//    @FXML
//    private Label label4;
//    @FXML
//    private Label label5;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cloudFilesWindowControllerTopL = setCloudWindow(cloudFileAnchorTopL);
        cloudFilesWindowControllerTopR = setCloudWindow(cloudFileAnchorTopR);
        cloudFilesWindowControllerBottom = setCloudWindow(cloudFileAnchorBottom);
    }

//    public void updateLabel1(String s) {
//        label1.setText(s);
//    }
//
//    public void updateLabel2(String s) {
//        label2.setText(s);
//    }
//
//    public void updateLabel3(String s) {
//        label3.setText(s);
//    }
//
//    public void updateLabel4(String s) {
//        label4.setText(s);
//    }
//
//    public void updateLabel5(String s) {
//        label5.setText(s);
//    }

    private CloudFilesWindowController setCloudWindow(AnchorPane anchor) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("filecardview/cloud-files-window.fxml"));
            ScrollPane cloudFileWindow = fxmlLoader.load();
            CloudFilesWindowController cloudFilesWindowController = fxmlLoader.getController();
            cloudFilesWindowController.parent = this;

            anchor.getChildren().add(cloudFileWindow);

            return cloudFilesWindowController;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void maximizeOrMinimizeWindow(Stage stage) {
        // Check if the stage is maximized
        if (stage.isMaximized()) {

            // Minimize stage
            stage.setMaximized(false);

            // Set correct CSS id
            rootPane.setId("root_pane");
            titleBar.setId("title_bar");

            // Add resize borders
            resizeBarLeft.setPrefWidth(10);
            resizeBarRight.setPrefWidth(10);
            resizeBarUp.setPrefHeight(10);
            resizeBarDown.setPrefHeight(10);
        } else {

            // Maximize stage
            stage.setMaximized(true);

            // Set correct CSS id
            rootPane.setId("root_pane_maximized");
            titleBar.setId("title_bar_maximized");

            // Remove resize borders
            resizeBarLeft.setPrefWidth(0);
            resizeBarRight.setPrefWidth(0);
            resizeBarUp.setPrefHeight(0);
            resizeBarDown.setPrefHeight(0);
        }
    }

    @FXML
    private void handleCloudFileSearchTopL(ActionEvent actionEvent) {
        cloudFilesWindowControllerTopL.searchCloudFiles(actionEvent.toString());
    }

    @FXML
    private void handleCloudFileSearchTopR(ActionEvent actionEvent) {
        cloudFilesWindowControllerTopR.searchCloudFiles(actionEvent.toString());
    }

    @FXML
    private void handleCloudFileSearchBottom(ActionEvent actionEvent) {
        cloudFilesWindowControllerBottom.searchCloudFiles(actionEvent.toString());
    }

    @FXML
    private void handleMinButtonAction(ActionEvent event) {
        Stage stage = (Stage) btMin.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleMaxButtonAction(ActionEvent event) {
        Stage stage = (Stage) btMax.getScene().getWindow();
        maximizeOrMinimizeWindow(stage);
    }

    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) btClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleTitleBarDoubleClickAction(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();

        // Check if double-clicked
        if (event.getClickCount() >= 2) {
            maximizeOrMinimizeWindow(stage);
        }
    }

    @FXML
    private void getMouseCoordinates(MouseEvent event) {
        mouseNodeX = event.getX();
        mouseNodeY = event.getY();
        mousePreviousScreenX = event.getScreenX();
        mousePreviousScreenY = event.getScreenY();
    }

    @FXML
    private void handleWindowDraggedAction(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setX(event.getScreenX() - mouseNodeX);
        stage.setY(event.getScreenY() - mouseNodeY);
    }

    @FXML
    private void handleLeftResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarLeft.getScene().getWindow();

        // Calculate the new width
        double newWidth = stage.getWidth() - (event.getScreenX() - mousePreviousScreenX);

        // Calculate the new X position
        double newX = stage.getX() + stage.getWidth() - newWidth;

        // Ensure the new width is within the minimum allowed
        if (newWidth > MIN_SCREEN_WIDTH) {
            stage.setX(newX);
            stage.setWidth(newWidth);
            mousePreviousScreenX = event.getScreenX();
        }
    }

    @FXML
    private void handleRightResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarRight.getScene().getWindow();

        // Calculate the new width
        double newWidth = stage.getWidth() + event.getScreenX() - mousePreviousScreenX;

        // Ensure the new width is within the minimum allowed
        if (newWidth >= MIN_SCREEN_WIDTH) {
            stage.setWidth(newWidth);
            mousePreviousScreenX = event.getScreenX();
        }
    }

    @FXML
    private void handleUpperResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarUp.getScene().getWindow();

        // Calculate the new height
        double newHeight = stage.getHeight() - (event.getScreenY() - mousePreviousScreenY);

        // Calculate the new Y position
        double newY = stage.getY() + stage.getHeight() - newHeight;

        // Ensure the new height is within the minimum allowed
        if (newHeight >= MIN_SCREEN_HEIGHT) {
            stage.setY(newY);
            stage.setHeight(newHeight);
            mousePreviousScreenY = event.getScreenY();
        }
    }

    @FXML
    private void handleBottomResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarDown.getScene().getWindow();

        // Calculate the new height
        double newHeight = stage.getHeight() + event.getScreenY() - mousePreviousScreenY;

        // Ensure the new height is within the minimum allowed
        if (newHeight >= MIN_SCREEN_HEIGHT) {
            stage.setHeight(newHeight);
            mousePreviousScreenY = event.getScreenY();
        }
    }

    @FXML
    private void handleUpperLeftCornerResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarLeft.getScene().getWindow();

        // Calculate the new width and height
        double newWidth = stage.getWidth() - (event.getScreenX() - mousePreviousScreenX);
        double newHeight = stage.getHeight() - (event.getScreenY() - mousePreviousScreenY);

        // Calculate the new X and Y coordinates
        double newX = stage.getX() + stage.getWidth() - newWidth;
        double newY = stage.getY() + stage.getHeight() - newHeight;

        // Ensure the new width is within the minimum allowed
        if (newWidth > MIN_SCREEN_WIDTH) {
            stage.setX(newX);
            stage.setWidth(newWidth);
            mousePreviousScreenX = event.getScreenX();
        }

        // Ensure the new height is within the minimum allowed
        if (newHeight >= MIN_SCREEN_HEIGHT) {
            stage.setY(newY);
            stage.setHeight(newHeight);
            mousePreviousScreenY = event.getScreenY();
        }
    }

    @FXML
    private void handleUpperRightCornerResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarRight.getScene().getWindow();

        // Calculate the new width and height
        double newWidth = stage.getWidth() + event.getScreenX() - mousePreviousScreenX;
        double newHeight = stage.getHeight() - (event.getScreenY() - mousePreviousScreenY);

        // Calculate the new Y position
        double newY = stage.getY() + stage.getHeight() - newHeight;

        // Ensure the new width is within the minimum allowed
        if (newWidth >= MIN_SCREEN_WIDTH) {
            stage.setWidth(newWidth);
            mousePreviousScreenX = event.getScreenX();
        }

        // Ensure the new height is within the minimum allowed
        if (newHeight >= MIN_SCREEN_HEIGHT) {
            stage.setY(newY);
            stage.setHeight(newHeight);
            mousePreviousScreenY = event.getScreenY();
        }
    }

    @FXML
    private void handleBottomLeftCornerResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarLeft.getScene().getWindow();

        // Calculate the new width and height
        double newWidth = stage.getWidth() - (event.getScreenX() - mousePreviousScreenX);
        double newHeight = stage.getHeight() + event.getScreenY() - mousePreviousScreenY;

        // Calculate the new X position
        double newX = stage.getX() + stage.getWidth() - newWidth;

        // Ensure the new width is within the minimum allowed
        if (newWidth > MIN_SCREEN_WIDTH) {
            stage.setX(newX);
            stage.setWidth(newWidth);
            mousePreviousScreenX = event.getScreenX();
        }

        // Ensure the new height is within the minimum allowed
        if (newHeight >= MIN_SCREEN_HEIGHT) {
            stage.setHeight(newHeight);
            mousePreviousScreenY = event.getScreenY();
        }
    }

    @FXML
    private void handleBottomRightCornerResizeAction(MouseEvent event) {
        Stage stage = (Stage) resizeBarRight.getScene().getWindow();

        // Calculate the new width and height
        double newWidth = stage.getWidth() + event.getScreenX() - mousePreviousScreenX;
        double newHeight = stage.getHeight() + event.getScreenY() - mousePreviousScreenY;

        // Ensure the new width is within the minimum allowed
        if (newWidth >= MIN_SCREEN_WIDTH) {
            stage.setWidth(newWidth);
            mousePreviousScreenX = event.getScreenX();
        }

        // Ensure the new height is within the minimum allowed
        if (newHeight >= MIN_SCREEN_HEIGHT) {
            stage.setHeight(newHeight);
            mousePreviousScreenY = event.getScreenY();
        }
    }
}