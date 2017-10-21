package logic.algorithms;

import javafx.scene.image.Image;
import utilities.enums.ImageLoadingType;
import java.io.InputStream;

public class ImageConverter {

    public Image getImageFromInputStream(final InputStream inputStream, final ImageLoadingType loadingType) {
        if(inputStream == null) return null;
        int width;
        int height;

        if (loadingType.equals(ImageLoadingType.FOR_AUCTION_PAGE)) {
            width = 429;
            height = 277;
        }
        else if (loadingType.equals(ImageLoadingType.FOR_LISTED_AUCTIONS)){
            width = 200;
            height = 150;
        }
        else if (loadingType.equals(ImageLoadingType.FOR_PROFILE_PAGE)){
            width = 275;
            height = 196;
        }
        else return null;

        return new Image(inputStream, width, height, false, false);
    }
}
