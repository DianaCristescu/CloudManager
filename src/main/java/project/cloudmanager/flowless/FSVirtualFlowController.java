package project.cloudmanager.flowless;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import org.fxmisc.flowless.Cell;
import project.cloudmanager.HomeViewController;
import project.cloudmanager.other.FSTreeItem;
import java.util.stream.Collectors;

public class FSVirtualFlowController extends VirtualFlowController<FSTreeItem>{
    private FSTreeItem rootFSTreeItem = null;

    public FSVirtualFlowController(HomeViewController parent) {
        super(parent);
    }

    @Override
    public void initVirtualFlow() {
        FSTreeItem root = new FSTreeItem("");
        this.currVirtualFlowDir = root;
        this.rootFSTreeItem = root;
        ObservableList<FSTreeItem> items = root.getChildren().stream()
                .map(p -> (FSTreeItem) p)
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
        createVirtualFlow(items, this::cellFactory);
    }

    @Override
    public void updateVirtualFlow(FSTreeItem item) {
        ObservableList<FSTreeItem> newItems = item.getChildren().stream()
                .map(p -> (FSTreeItem) p)
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
        this.currVirtualFlowDir = item;

        this.items.clear();
        this.items.addAll(newItems);
        this.virtualFlow.scrollYToPixel(0.0);
        this.parent.clearAppClipboard();
    }

    @Override
    public void backToPrevFlow() {
        if (currVirtualFlowDir == rootFSTreeItem) {
            return;
        }

        FSTreeItem prevDir = (FSTreeItem) currVirtualFlowDir.getParent();
        if (prevDir == null) {
            prevDir = rootFSTreeItem;
        }

        updateVirtualFlow(prevDir);
    }

    private Cell<FSTreeItem, HBox> cellFactory(FSTreeItem item) {
        FSCell cell = new FSCell(
                item,
                this
        );

        // Check if item is selected
        if (this.parent.getAppClipboard().contains(item)) {
            cell.selectCell();
        } else {
            cell.deselectCell();
        }

        return cell;
    }
}
