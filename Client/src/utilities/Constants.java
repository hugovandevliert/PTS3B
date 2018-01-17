package utilities;

public class Constants {

    /* RMI */
    public static final String SERVER_NAME_THAT_PUSHES_TO_CLIENTS = "Server_Pusher";
    public static final String SERVER_NAME_THAT_RECEIVES_FROM_CLIENTS = "Server_Receiver";
    public static final String CHANGED_PROPERTY_BID_SERVER = "newBid";
    public static final String CHANGED_PROPERTY_TIME_SERVER = "newTime";
    public static final String SERVER_IP = "localhost";

    public static final int PORT_NUMBER = 1098;

    /* Music Player */
    public static final String BEEP_NEW_BID_MP3 = "Client/src/utilities/sounds/Beep_NewBid.mp3";
    public static final String SOUND_AUCTION_WON_MP3 = "Client/src/utilities/sounds/Sound_Auction_Won.mp3";
    public static final String SOUND_AUCTION_LOST_MP3 = "Client/src/utilities/sounds/Sound_Auction_Lost.mp3";

    private Constants() {
    }
}
