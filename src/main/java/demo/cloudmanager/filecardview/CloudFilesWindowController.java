package demo.cloudmanager.filecardview;

import demo.cloudmanager.CloudFile;
import demo.cloudmanager.HomeViewController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CloudFilesWindowController implements Initializable {
    private String searchStr = "";
    private int viewedBatchIdx = 0;
    private final int MIN_TILE_GAP = 5;
    public HomeViewController parent = null;
    private double fileCardBatchIdxInWindow = 0;
    private final List<FileCardBatch> files = new ArrayList<>();
    private final Image unloadedImage = new Image(getClass().getResource("images/unloaded_image.png").toExternalForm());

    @FXML
    private TilePane tilePane;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);

        setTilePane(tilePane);

        FileCardBatch batch = new FileCardBatch(this);
        batch.setBatch(getData(50), tilePane, getAndIncFileCardBatchIdxInWindow(50));
        registerObserver(batch);

        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {

            double firstVisibleFileCardIdx = calculateFirstVisibleFileCardIdx(newValue.doubleValue());
            notifyObserversOfFileCardIdxInWindow(firstVisibleFileCardIdx);

            if (newValue.doubleValue() == 1.0) {
                final FileCardBatch newBatch = new FileCardBatch(this);
                newBatch.setBatch(getData(50), tilePane, getAndIncFileCardBatchIdxInWindow(50));
                registerObserver(newBatch);
            }
        });
    }

    public void searchCloudFiles(String searchStr) {
        // TO DO
    }

    protected void changeLoadedBatches(FileCardBatch batch) {
        int newViewedBatchIdx = files.indexOf(batch);
        if (newViewedBatchIdx > viewedBatchIdx) {
            if (viewedBatchIdx - 1 >= 0) {
                FileCardBatch prevBatch = files.get(viewedBatchIdx - 1);
                prevBatch.unload(unloadedImage);
            }
            if (viewedBatchIdx + 2 < files.size()) {
                FileCardBatch nextNextBatch = files.get(viewedBatchIdx + 2);
                nextNextBatch.load();
            }
        } else if (newViewedBatchIdx < viewedBatchIdx) {
            if (viewedBatchIdx - 2 >= 0) {
                FileCardBatch prevPrevBatch = files.get(viewedBatchIdx - 2);
                prevPrevBatch.load();
            }
            if (viewedBatchIdx + 1 < files.size()) {
                FileCardBatch nextNextBatch = files.get(viewedBatchIdx + 1);
                nextNextBatch.unload(unloadedImage);
            }
        }
        viewedBatchIdx = newViewedBatchIdx;
    }

    private void registerObserver(FileCardBatch batch) {
        files.add(batch);
    }

    private double getAndIncFileCardBatchIdxInWindow(int nrOfFilesInBatch) {
        double oldIdxInWindow = fileCardBatchIdxInWindow;
        fileCardBatchIdxInWindow += nrOfFilesInBatch;
        return oldIdxInWindow;
    }

    private double calculateAbsoluteScrollPaneVerticalPosition(double relativePosition) {
        Bounds contentBounds = scrollPane.getContent().getLayoutBounds();
        double contentHeight = contentBounds.getHeight();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();
        double absolutePosition = (contentHeight - viewportHeight) * relativePosition;

        return absolutePosition;
    }

    private double calculateFirstVisibleFileCardIdx(double relativeScrollPanePosition) {
        int numColumns = calculateColumns(tilePane.getWidth());
        double absoluteScrollPaneVerticalPosition = calculateAbsoluteScrollPaneVerticalPosition(relativeScrollPanePosition);
        double lastInvisibleRow = absoluteScrollPaneVerticalPosition / (FileCardController.MAX_CARD_HEIGHT + MIN_TILE_GAP);
        double firstVisibleFileCardIdx = numColumns * lastInvisibleRow;

        return firstVisibleFileCardIdx;
    }

    private void notifyObserversOfFileCardIdxInWindow(double firstVisibleFileCardIdxInWindow) {
        for (FileCardBatch batch: files) {
            batch.updateFileCardIdxInWindow(firstVisibleFileCardIdxInWindow);
        }
    }

    private List<CloudFile> getData(int nrOfFiles) {
        List<CloudFile> fileList = new ArrayList<>();
        CloudFile file;

        for (int i = 0; i < nrOfFiles / 3; i++) {
            file = new CloudFile();
            file.setThumbnailLinkOrPath(getClass().getResource("images/Minimize.png").toExternalForm());
            file.setName("Minimize.png");
            fileList.add(file);
            file = new CloudFile();
            file.setThumbnailLinkOrPath(getClass().getResource("images/Resize.png").toExternalForm());
            file.setName("Resize.png");
            fileList.add(file);
            file = new CloudFile();
            file.setThumbnailLinkOrPath(getClass().getResource("images/Exit.png").toExternalForm());
            file.setName("Exit.png");
            fileList.add(file);
        }

        if (nrOfFiles % 3  >= 1) {
            file = new CloudFile();
            file.setThumbnailLinkOrPath(getClass().getResource("images/Minimize.png").toExternalForm());
            file.setName("Minimize.png");
            fileList.add(file);

            if (nrOfFiles % 3  == 2) {
                file = new CloudFile();
                file.setThumbnailLinkOrPath(getClass().getResource("images/Resize.png").toExternalForm());
                file.setName("Resize.png");
                fileList.add(file);
            }
        }

        return fileList;
    }

    private int calculateColumns(double viewportWidth) {
        return Math.max(1, (int) Math.floor((viewportWidth) / (FileCardController.MAX_CARD_WIDTH + MIN_TILE_GAP)));
    }

    private void setTilePane(TilePane tilePane) {
        tilePane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double availableWidth = newValue.doubleValue();
            int numColumns = calculateColumns(availableWidth);
            double totalGap = availableWidth - (numColumns * FileCardController.MAX_CARD_WIDTH);
            double hgap = totalGap / (numColumns + 1); // Adding 1 to include the gap before the first tile
            tilePane.setHgap(hgap);
        });
        tilePane.setVgap(MIN_TILE_GAP);
    }
}
