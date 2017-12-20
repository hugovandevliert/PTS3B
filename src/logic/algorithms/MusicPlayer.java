package logic.algorithms;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MusicPlayer {

    private final String path;

    public MusicPlayer(final String path) {
        this.path = path;
    }

    public void playSound() {
        final Media hit = new Media(new File(path).toURI().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
}
