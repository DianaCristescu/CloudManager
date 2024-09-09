package project.cloudmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import project.cloudmanager.flowless.FSVirtualFlowController;
import project.cloudmanager.flowless.FileCell;
import project.cloudmanager.flowless.GoogleDriveFlowController;
import project.cloudmanager.flowless.VirtualFlowController;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class HomeViewController implements Initializable {
    private static final double MIN_SCREEN_HEIGHT = 500;
    private static final double MIN_SCREEN_WIDTH = 360;
    private double mouseNodeX = 0;
    private double mouseNodeY = 0;
    private double mousePreviousScreenX = 0;
    private double mousePreviousScreenY = 0;
    private VirtualFlowController appClipboardSide;
    private Set<FileCell> appClipboard = new HashSet<>();
    private VirtualFlowController leftVirtualFlowController = null;
    private VirtualFlowController rightVirtualFlowController = null;


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
    private AnchorPane filesWindowAnchorL;
    @FXML
    private AnchorPane filesWindowAnchorR;
    @FXML
    private Label textPrintLabel;
    @FXML
    private Button backButtonLeft;
    @FXML
    private Button backButtonRight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftVirtualFlowController = setVirtualFlow(filesWindowAnchorL, "FSTreeItem");
        rightVirtualFlowController = setVirtualFlow(filesWindowAnchorR, "FSTreeItem");
    }

    public Set<FileCell> getAppClipboard() {
        return appClipboard;
    }

    public VirtualFlowController getAppClipboardSide() {
        return appClipboardSide;
    }

    public void addFileToAppClipboard(VirtualFlowController virtualFlowController, FileCell item) {
        if (virtualFlowController != appClipboardSide) {
            clearAppClipboard();
        }
        if (appClipboardSide == null || virtualFlowController != appClipboardSide) {
            appClipboardSide = virtualFlowController;
        }

        this.appClipboard.add(item);
        item.selectCell();
    }

    public void removeFileFromAppClipboard(VirtualFlowController virtualFlowController, FileCell item) {
        this.appClipboard.remove(item);
        item.deselectCell();

        if (appClipboard.size() == 0) {
            appClipboardSide = null;
        }
    }

    public void clearAppClipboard() {
        for (FileCell fileCell: appClipboard) {
            fileCell.deselectCell();
        }

        appClipboard = new HashSet<>();
        appClipboardSide = null;
    }

    public void updateTextPrintLabel(String s) {
        textPrintLabel.setText(s);
    }

    private VirtualFlowController setVirtualFlow(AnchorPane anchor, String type) {
        try {
            // Get FXML
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("flowless/virtual-flow-placeholder.fxml"));

            // Get Controller
            VirtualFlowController virtualFlowController = null;
            if (type.equals("FSTreeItem")) {
                virtualFlowController = new FSVirtualFlowController(this);
            }
            else if (type.equals("GoogleDrive")) {
                virtualFlowController = new GoogleDriveFlowController(this);
            }

            // Set Controller, load and add Virtual Flow
            fxmlLoader.setController(virtualFlowController);
            AnchorPane virtualFlowPlaceholder = fxmlLoader.load();
            virtualFlowController.initVirtualFlow();

            // Anchor Virtual Flow to the window
            anchor.getChildren().add(virtualFlowPlaceholder);
            AnchorPane.setTopAnchor(virtualFlowPlaceholder, 0.0);
            AnchorPane.setBottomAnchor(virtualFlowPlaceholder, 0.0);
            AnchorPane.setLeftAnchor(virtualFlowPlaceholder, 0.0);
            AnchorPane.setRightAnchor(virtualFlowPlaceholder, 0.0);

            return virtualFlowController;
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
        // TO DO
    }

    @FXML
    private void handleCloudFileSearchTopR(ActionEvent actionEvent) {
        // TO DO
    }

    @FXML
    private void handleLeftBackButtonAction(ActionEvent event) {
        leftVirtualFlowController.backToPrevFlow();
    }

    @FXML
    private void handleRightBackButtonAction(ActionEvent event) {
        rightVirtualFlowController.backToPrevFlow();
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