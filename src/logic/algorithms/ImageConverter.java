package logic.algorithms;

import javafx.scene.image.Image;
import java.io.InputStream;

public class ImageConverter {

    public Image getImageFromInputStream(final InputStream inputStream) {
        return new Image(inputStream);
    }
}
