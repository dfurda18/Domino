package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dictionary class to store all the communications.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class NetworkProtocols {
    /**
     * Static PORT to use during the communication.
     */
    public static int PORT = 9876;
    /**
     * Static host to connect to the server
     */
    public static String HOST = "localhost";
    /**
     * Action: LOGIN
     */
    public static final String LOGIN_ACTION = "LOGIN";
    /**
     * Action: ERROR
     */
    public static final String ERROR_ACTION = "ERROR";
    /**
     * Action: PLAY_CHIP_ACTION
     */
    public static final String PLAY_CHIP_ACTION = "PLAY_CHIP";
    /**
     * Action: LOGOUT
     */
    public static final String LOGOUT_ACTION = "LOGOUT";
    /**
     * Action: SEND_ACTION
     */
    public static final String SEND_ACTION = "SEND";
    /**
     * Action: PASS_ACTION
     */
    public static final String PASS_ACTION = "PASS";

    /**
     * Response: SAME USER
     */
    public static final String SAME_USER_RESPONSE = "SAME_USER";
    /**
     * Response: GAME_READY_RESPONSE
     */
    public static final String GAME_READY_RESPONSE = "GAME_READY";
    /**
     * Response: PLAYER_PLAYED_RESPONSE
     */
    public static final String PLAYER_PLAYED_RESPONSE = "PLAYER_PLAYED";
    /**
     * Response: MESSAGE_RECEIVED_RESPONSE
     */
    public static final String MESSAGE_RECEIVED_RESPONSE = "MESSAGE_RECEIVED";
    /**
     * Response: PLAYER_TURN_RESPONSE
     */
    public static final String PLAYER_TURN_RESPONSE = "PLAYER_TURN";

    /**
     * Response: WINNER_RESPONSE
     */
    public static final String WINNER_RESPONSE = "WINNER";

    /**
     * Server NAME
     */
    public static final String SERVER_NAME = "SERVER";
    /**
     * Checks if a string is an IP Address V4
     * @param address The string to check if it is an IP address
     * @return True if the string is an IP address and False otherwise
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public static boolean isValidIPAddress(String address) {
        String regex = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }

    /**
     * Loads the host and port from a file.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public static void loadFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(NetworkProtocols.class.getClassLoader().getResourceAsStream("config.dat")));
        if(reader.ready()) {
            NetworkProtocols.HOST = reader.readLine();
        }
        if(reader.ready()) {
            NetworkProtocols.PORT = Integer.parseInt(reader.readLine());
        }
    }
}
