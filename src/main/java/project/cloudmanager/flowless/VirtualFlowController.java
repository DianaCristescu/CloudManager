package project.cloudmanager.flowless;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.fxmisc.flowless.Cell;
import org.fxmisc.flowless.VirtualFlow;
import org.fxmisc.flowless.VirtualizedScrollPane;
import project.cloudmanager.HomeViewController;

import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class VirtualFlowController<T> {
    public HomeViewController parent = null;
    public Image unloadedImage = new Image(getClass().getResourceAsStream("/project/cloudmanager/images/unloaded_image.png"));
    public Image folder = new Image(getClass().getResourceAsStream("/project/cloudmanager/images/SimpleFolder.png"));
    public Image file = new Image(getClass().getResourceAsStream("/project/cloudmanager/images/SimpleFile.png"));
    @FXML
    protected AnchorPane virtualFlowContainer;
    protected T currVirtualFlowDir = null;
    protected VirtualFlow<T, Cell<T, HBox>> virtualFlow = null;
    protected ObservableList<T> items = null;

    public VirtualFlowController(HomeViewController parent) {
        this.parent = parent;
    }

    public abstract void initVirtualFlow();

    public abstract void updateVirtualFlow(T item);
    public abstract void backToPrevFlow();

    public void createVirtualFlow(ObservableList<T> items, Function<T, Cell<T, HBox>> cellFactory) {
        // Save list of Cells for updating
        this.items = items;

        // VirtualFlow
        VirtualFlow<T, Cell<T, HBox>> flow = VirtualFlow.createVertical(items, cellFactory);
        flow.setOnDragOver(this::handleDragOver);
        flow.setOnDragDropped(this::handleDragDropped);
        this.virtualFlow = flow;

        // VirtualizedScrollPane
        VirtualizedScrollPane virtualizedScrollPane = new VirtualizedScrollPane(flow);

        // Create a container to apply the clip
        StackPane container = new StackPane(virtualizedScrollPane);
        container.setClip(createRoundedClip());

        // Add the container to the AnchorPane
        virtualFlowContainer.getChildren().add(container);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
    }

    protected Rectangle createRoundedClip() {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        clip.widthProperty().bind(virtualFlowContainer.widthProperty());
        clip.heightProperty().bind(virtualFlowContainer.heightProperty());
        return clip;
    }

    protected static String formatFileTime(FileTime fileTime) {
        // Convert FileTime to LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());

        // Define the formatter for day/month/year time AM/PM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

        // Format the LocalDateTime to the desired format
        return localDateTime.format(formatter);
    }

    private String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) //if the name is not empty
            return fileName.substring(i + 1).toLowerCase();

        return extension;
    }

    private void handleDragOver(DragEvent event) {
        // Extensions that are valid to be drag-n-dropped
        List<String> validExtensions = Arrays.asList("jpg", "png");

        if (event.getDragboard().hasFiles() && validExtensions.containsAll(
                event.getDragboard().getFiles().stream()
                        .map(file -> getExtension(file.getName()))
                        .collect(Collectors.toList()))) {

            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        boolean success = true;
        HomeViewController homeViewController = parent;

        // TO DO - transfer to location
        homeViewController.clearAppClipboard();
        parent.updateTextPrintLabel("Copy: " + event.getDragboard().getFiles().stream().map(file -> file.getName()).collect(Collectors.joining(" ")));

        event.setDropCompleted(success);
        event.consume();
    }
}

