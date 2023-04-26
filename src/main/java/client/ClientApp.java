package client;

/**
 * Domino client Main app
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/04/2023
 */
public class ClientApp {
    /**
     * Main method that contains the client's domino main functionality.
     * @param args The console arguments (not used).
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public static void main(String[] args) {
        DominoClient game = new DominoClient();
        game.startGame();
    }
}
