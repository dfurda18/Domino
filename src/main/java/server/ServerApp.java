package server;

/**
 * Chat server Main app
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class ServerApp {
    /**
     * Main method that contains the chat's main functionality.
     * @param args The console arguments (not used).
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public static void main(String[] args) {
        DominoServer.startServer();
    }
}