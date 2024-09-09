package project.cloudmanager.flowless;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import project.cloudmanager.HomeViewController;
import project.cloudmanager.other.FSTreeItem;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;

public class FSCell extends FileCell<FSTreeItem, FSVirtualFlowController> {
    public FSCell(FSTreeItem fsTreeItem, FSVirtualFlowController parent) {
        // Save fsTreeItem
        this.item = fsTreeItem;

        // Save parent
        this.parent = parent;

        // HBox Layout
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("file-hbox.fxml"));
        fxmlLoader.setController(this);

        try {
            layout = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        layout.setOnDragDetected(this::handleDragDetected);
        layout.setOnMouseClicked(this::handleMouseClicked);

        // Get name, date(dd/mm/yyyy tt:tt am/pm) and size(in bytes)
        String name, date;
        Long size;
        try {
            File file = item.getValue();
            Path path = item.getValue().toPath();
            BasicFileAttributes atr = Files.readAttributes(path, BasicFileAttributes.class);

            name = file.getName().toString();
            if (name.length() == 0) {
                name = file.getPath().toString();
            }
            date = this.parent.formatFileTime(atr.lastModifiedTime());
            size = atr.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Image
        if (this.item.isLeaf()) {
            imageView.setImage(this.parent.file);
        } else {
            imageView.setImage(this.parent.folder);
        }

        // Labels
        this.name.setText(name);
        this.date.setText(date);
        this.size.setText(size + " B");
    }

    public void updateItem(FSTreeItem item) {
        String name, date;
        Long size;

        if (item != null) {
            // Get name, date(dd/mm/yyyy hh:mm a) and size(in bytes)
            try {
                File file = item.getValue();
                Path path = item.getValue().toPath();
                BasicFileAttributes atr = Files.readAttributes(path, BasicFileAttributes.class);

                name = file.getName().toString();
                if (name.length() == 0) {
                    name = file.getPath().toString();
                }
                date = this.parent.formatFileTime(atr.lastModifiedTime());
                size = atr.size();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Clear if null
            name = "----";
            date = "--/--/---- --:--";
            size = 0l;
        }

        this.name.setText(name);
        this.date.setText(date);
        this.size.setText(size + " B");
        imageView.setImage(parent.unloadedImage);
        this.item = item;
    }

    protected void handleDragDetected(MouseEvent event) {
        HomeViewController homeViewController = this.parent.parent;

        ClipboardContent content = new ClipboardContent();
        homeViewController.addFileToAppClipboard(this.parent, this);
        content.putFiles(homeViewController.getAppClipboard().stream()
                .map(p -> (FSTreeItem)p.getItem())
                .map(p -> p.getValue())
                .collect(Collectors.toList()));

        Dragboard dragboard = layout.startDragAndDrop(TransferMode.COPY_OR_MOVE);
        dragboard.setContent(content);
        dragboard.setDragView(imageView.getImage());

        event.consume();
    }

    protected void handleMouseClicked(MouseEvent event) {
        if (event.getClickCount() >= 2 && !this.item.isLeaf()) {
            FSVirtualFlowController fsVirtualFlowController = this.parent;
            fsVirtualFlowController.updateVirtualFlow(this.getItem());
        } else {
            HomeViewController homeViewController = this.parent.parent;

            if(!isSelected()) {
                if (!event.isShiftDown()) {
                    homeViewController.clearAppClipboard();
                }
                homeViewController.addFileToAppClipboard(this.parent, this);
            } else {
                homeViewController.removeFileFromAppClipboard(this.parent, this);
                if (!event.isShiftDown()) {
                    homeViewController.clearAppClipboard();
                }
            }
        }
    }
}
