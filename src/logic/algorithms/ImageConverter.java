package logic.algorithms;

import javafx.scene.image.Image;
import utilities.enums.ImageLoadingType;

import java.io.InputStream;

public class ImageConverter {

    public Image getImageFromInputStream(final InputStream inputStream, final ImageLoadingType loadingType) {
        if (inputStream != null){
            int width = 0;
            int height = 0;

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

            return new Image(inputStream, width, height, false, false);
        }
        return null;
    }
}
