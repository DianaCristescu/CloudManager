package project.cloudmanager.flowless;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import project.cloudmanager.other.GoogleDrive;

public class GoogleDriveCell extends FileCell<GoogleDrive, GoogleDriveFlowController> {
    public GoogleDriveCell(String text1, String text2, String text3, Image image, GoogleDriveFlowController parent) {
        // Save parent
        this.parent = parent;

        // Image
        imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        // Labels
        name = new Label(text1);
        date = new Label(text2);
        size = new Label(text3);
        name.setStyle("-fx-text-fill: -fx-my-light-font-color;");
        date.setStyle("-fx-text-fill: -fx-my-light-font-color;");
        size.setStyle("-fx-text-fill: -fx-my-light-font-color;");

        // HBox Layout
        layout = new HBox(10, imageView, name, date, size);
        layout.getStyleClass().add("custom-hbox");
        layout.setOnDragDetected(this::handleDragDetected);
        layout.setOnMouseClicked(this::handleMouseClicked);
    }

    public void updateItem(GoogleDrive item) {
    }

    protected void handleDragDetected(MouseEvent event) {
//        HomeViewController homeViewController = this.parent.parent;
//
//        ClipboardContent content = new ClipboardContent();
//        homeViewController.addFileToAppClipboard(this.parent, this);
//        content.putFiles(homeViewController.getAppClipboard().stream()
//                .map(p -> (FSTreeItem)p.getItem())
//                .map(p -> p.getValue())
//                .collect(Collectors.toList()));
//
//        Dragboard dragboard = layout.startDragAndDrop(TransferMode.COPY_OR_MOVE);
//        dragboard.setContent(content);
//        dragboard.setDragView(parent.unloadedImage);
//
//        homeViewController.updateTextPrintLabel("Drag detected: " + name.getText());
////        homeViewController.updateTextPrintLabel("Drag detected: " + homeViewController.getAppClipboard().stream()
////                        .map(fileCell -> fileCell.name.getText()));
//        event.consume();
    }

    protected void handleMouseClicked(MouseEvent event) {
//        HomeViewController homeViewController = this.parent.parent;
//
//        if(!isSelected()) {
//            if (!event.isShiftDown()) {
//                homeViewController.clearAppClipboard();
//            }
//            homeViewController.addFileToAppClipboard(this.parent, this);
//        } else {
//            homeViewController.removeFileFromAppClipboard(this.parent, this);
//            if (!event.isShiftDown()) {
//                homeViewController.clearAppClipboard();
//            }
//        }
    }
}
