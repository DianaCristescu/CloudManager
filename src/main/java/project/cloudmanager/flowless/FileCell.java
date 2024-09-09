package project.cloudmanager.flowless;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.fxmisc.flowless.Cell;

public abstract class FileCell<T, C> implements Cell<T, HBox> {
    protected T item = null;
    @FXML
    protected HBox layout = null;
    @FXML
    protected Label name = null;
    @FXML
    protected Label date = null;
    @FXML
    protected Label size = null;
    @FXML
    protected ImageView imageView = null;
    protected C parent = null;

    @Override
    public abstract void updateItem(T item);

    @Override
    public HBox getNode() {
        return layout;
    }

    public T getItem() {
        return item;
    }

    public void deselectCell() {
        name.setId("normal");
        layout.setId("normal_hbox");
    }

    public void selectCell() {
        name.setId("selected");
        layout.setId("selected_hbox");
    }

    public boolean isSelected() {
        if (name.getId() == "selected") {
            return true;
        }
        return false;
    }

    protected abstract void handleDragDetected(MouseEvent event);

    protected abstract void handleMouseClicked(MouseEvent event);
}
