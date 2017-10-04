package logic.algorithms;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageConverter {

    public Image byteArrayToImage(final byte[] byteArray) {
        Image image = null;

        try {
            final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(byteArray));
            image = SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
