package project.cloudmanager.flowless;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import org.fxmisc.flowless.Cell;
import project.cloudmanager.HomeViewController;
import project.cloudmanager.other.GoogleDrive;

import java.util.function.Function;

public class GoogleDriveFlowController extends VirtualFlowController{
    public GoogleDriveFlowController(HomeViewController parent) {
        super(parent);
    }

    @Override
    public void initVirtualFlow() {
        // Sample data
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 1; i <= 30000; i += 3) {
            items.add("Name " + i + ",Date " + (i+1) + ",Size " + (i+2) + ",/project/googledrive/images/Minimize.png");
        }

        // Image to be used in each cell
        Image image = new Image(getClass().getResource("/project/cloudmanager/images/Minimize.png").toExternalForm());

        // Cell Factory
        Function<String, Cell<GoogleDrive, HBox>> cellFactory = item -> {
            // Assuming the item string contains comma-separated text values
            // example: "Text1,Text2,Text3,ImagePath"
            String[] parts = item.split(",", 4);
            GoogleDriveCell cell = new GoogleDriveCell(
                    parts.length > 0 ? parts[0] : "Error",
                    parts.length > 1 ? parts[1] : "Error",
                    parts.length > 2 ? parts[2] : "Error",
                    parts.length > 3 ? new Image(getClass().getResourceAsStream(parts[3])) : unloadedImage,
                    this
            );

            // Check if item is selected
            if (this.parent.getAppClipboard().contains(item)) {
                cell.selectCell();
            } else {
                cell.deselectCell();
            }

            return cell;
        };

        createVirtualFlow(items, cellFactory);
    }

    @Override
    public void updateVirtualFlow(Object item) {

    }

    @Override
    public void backToPrevFlow() {

    }
}
