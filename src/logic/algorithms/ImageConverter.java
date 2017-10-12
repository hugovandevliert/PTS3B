package logic.algorithms;

import javafx.scene.image.Image;
import utilities.enums.AuctionLoadingType;

import java.io.InputStream;

public class ImageConverter {

    public Image getImageFromInputStream(final InputStream inputStream, final AuctionLoadingType loadingType) {
        int width = 0;
        int height = 0;

        if (loadingType.equals(AuctionLoadingType.FOR_AUCTION_PAGE)) {
            width = 429;
            height = 277;
        }
        else if (loadingType.equals(AuctionLoadingType.FOR_LISTED_AUCTIONS)){
            width = 200;
            height = 150;
        }

        return new Image(inputStream, width, height, false, false);
    }
}
