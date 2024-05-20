package demo.cloudmanager.filecardview;

import demo.cloudmanager.CloudFile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class FileCardController implements Initializable {
    public static final double MAX_ICON_WIDTH = 110;
    public static final double MAX_ICON_HEIGHT = 100;
    public static final double MAX_CARD_WIDTH = 110;
    public static final double MAX_CARD_HEIGHT = 140;

    private CloudFile file;

    @FXML
    private ImageView fileIcon;
    @FXML
    private Label fileName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMaxImageSize(fileIcon, MAX_ICON_WIDTH, MAX_ICON_HEIGHT);
    }

    protected void setData(CloudFile file) {
        this.file = file;

        Image image = new Image(file.getThumbnailLinkOrPath());
        fileIcon.setImage(image);

        fileName.setText(file.getName());
    }

    protected void load() {
        Image image = new Image(file.getThumbnailLinkOrPath());
        fileIcon.setImage(image);
    }

    protected void unload(Image unloadedImage) {
        fileIcon.setImage(unloadedImage);
    }

    private static void setMaxImageSize(ImageView imageView, double maxWidth, double maxHeight) {
        Image image = imageView.getImage();
        double width = image.getWidth();
        double height = image.getHeight();

        if (width <= maxWidth && height <= maxHeight) {
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
        } else {
            double ratio = Math.min(maxWidth / width, maxHeight / height);
            imageView.setFitWidth(width * ratio);
            imageView.setFitHeight(height * ratio);
        }
    }
}

