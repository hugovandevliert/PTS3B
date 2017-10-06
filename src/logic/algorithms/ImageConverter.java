package logic.algorithms;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ImageConverter {

    public Image byteArrayToImage(final byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        File file = (File)objectInputStream.readObject();

        return new Image("file:" +  file.getAbsolutePath());
    }
}
