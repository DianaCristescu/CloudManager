package demo.cloudmanager.filecardview;

import demo.cloudmanager.CloudFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileCardBatch {
    private final CloudFilesWindowController parent;
    private List<FileCardController> batch = null;
    private boolean loaded = false;
    private double minFileCardIdxInWindow = -1;
    private double maxFileCardIdxInWindow = -1;

    public FileCardBatch(CloudFilesWindowController parent) {
        this.parent = parent;
    }

    protected void updateFileCardIdxInWindow(double fileCardIdxInWindow) {
        if (fileCardIdxInWindow >= minFileCardIdxInWindow && fileCardIdxInWindow <= maxFileCardIdxInWindow) {
            if (!loaded) {
                parent.changeLoadedBatches(this);
            }
        }
    }

    protected void setBatch(List<CloudFile> files, TilePane tilePane, double fileCardIdxInWindow) {
        batch = new ArrayList<>();
        FileCardController card;
        FXMLLoader fxmlLoader;

        try {
            for (CloudFile file : files) {
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("file-card.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();

                card = fxmlLoader.getController();
                card.setData(file);

                tilePane.getChildren().add(anchorPane);
                batch.add(card);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loaded = true;
        minFileCardIdxInWindow = fileCardIdxInWindow;
        maxFileCardIdxInWindow = fileCardIdxInWindow + files.size() - 1;
    }

    protected void load() {
        if (batch != null && !loaded) {
            for (FileCardController card : batch) {
                card.load();
            }
            loaded = true;
        }
    }

    protected void unload(Image unloadedImage) {
        if (batch != null && loaded) {
            for (FileCardController card : batch) {
                card.unload(unloadedImage);
            }
            loaded = false;
        }
    }
}
