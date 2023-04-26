package client;

import model.DominoGame;
import model.DominoChip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Static class that handles the Domino Interface.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/04/2023
 */
public class DominoInterface {
    /**
     * Static attribute that stores the screen size.
     */
    public static final Dimension screenSize = new Dimension(1080, 720);
    /**
     * Static attribute that stores the login size
     */
    public static final Dimension modalSize = new Dimension(535, 411);
    /**
     * The game's font
     */
    public static Font gameFont;
    /**
     * Static attribute that stores the GUI main window.
     */
    private static JFrame frame;
    /**
     * Static attribute that stores the main game interface.
     */
    public static JLayeredPane gameInterface;
    /**
     * Login component
     */
    private static Login login;
    /**
     * The players List group list
     */
    private static WaitingHall waitingInterface;
    /**
     * Win interface
     */
    private static GameOverInterface gameOverInterface;
    /**
     * The Gameplay container
     */
    private static JPanel gameplayContainer;
    /**
     * The Gameplay interface elements
     */
    private static DominoGamePlayView gameplay;
    /**
     * Class constructor
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public DominoInterface() {
        try {
            String url = DominoInterface.class.getClassLoader()
                            .getResource("font/GloriaHallelujah-Regular.ttf")
                            .getFile().replace("%20", " ");
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(url));
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        gameFont = new Font("Gloria Hallelujah", Font.BOLD, 15);

    }
    /**
     * Creates the interface elements.
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public void createInterface() {
        // Set up the window frame
        frame = new JFrame("Domino");
        frame.setSize(DominoInterface.screenSize.width, DominoInterface.screenSize.height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );

        // Set up the Layered panel
        gameInterface = new JLayeredPane();
        gameInterface.setBounds(0,-5, DominoInterface.screenSize.width, DominoInterface.screenSize.height);
        gameInterface.setPreferredSize(DominoInterface.screenSize);
        frame.add(gameInterface);

        // Paint the background
        JPanel background = new JPanel();
        background.setBounds(0,-5, DominoInterface.screenSize.width, DominoInterface.screenSize.height);
        JLabel backgroundImage = new JLabel(new ImageIcon(DominoInterface.class.getClassLoader().getResource("images/Background.png")));
        backgroundImage.setBounds(0,0, DominoInterface.screenSize.width, DominoInterface.screenSize.height);
        backgroundImage.setOpaque(true);
        background.add(backgroundImage);
        gameInterface.add(background, JLayeredPane.FRAME_CONTENT_LAYER);

        // Set up the login screen
        login = new Login();
        gameInterface.add(login, JLayeredPane.MODAL_LAYER);

        // Create the waiting hall
        waitingInterface = new WaitingHall();
        waitingInterface.setVisible(false);
        gameInterface.add(waitingInterface, JLayeredPane.MODAL_LAYER);

        // Create the gameover interface
        gameOverInterface = new GameOverInterface();
        gameOverInterface.setVisible(false);
        gameInterface.add(gameOverInterface, JLayeredPane.MODAL_LAYER);

        // Create the gameplay container
        gameplayContainer = new JPanel();
        gameplayContainer.setBounds(0, 0, DominoInterface.screenSize.width, DominoInterface.screenSize.height);
        gameplayContainer.setLayout(null);
        gameplayContainer.setVisible(false);
        gameplayContainer.setOpaque(false);
        gameInterface.add(gameplayContainer, JLayeredPane.DEFAULT_LAYER);

        // Show the frame
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Sets the frame's window adapter.
     * @param handle The {@link java.awt.event.WindowAdapter Window Adapter} to set to the frame.
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public void setWindowAdapter(WindowAdapter handle) {
        this.frame.addWindowListener(handle);
    }

    /**
     * Shows the interface after a user has successfuly logged in
     * @author Dario Urdapilleta
     * @since 03/24/2023
     */
    public void logIn () {
        login.setVisible(false);
        waitingInterface.setVisible(true);
    }

    /**
     * Sets the login button click action
     * @param listener The Action Listener
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public void setLoginOnClick (ActionListener listener) {
        login.setLoginOnClick(listener);
    }

    /**
     * Shows an error message in the login page
     * @param message The error message
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public void showLoginErrorMessage (String message) {
        login.showError(message);
    }

    /**
     * Returns the login user id
     * @return The login user id
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public String getLoginUserId () {
        return login.getUserId();
    }

    /**
     * Returns the server IP
     * @return The server IP
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public String getLoginServer () {
        return login.getServerIP();
    }

    /**
     * Returns the login username
     * @return The login username
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public String getLoginUserName () {
        return login.getUserName();
    }

    /**
     * Loads the Game Elements
     * @param game The current game
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public void startGame (DominoGame game) {
        gameplayContainer.removeAll();

        // Create the gameplay elements
        DominoInterface.gameplay = new DominoGamePlayView(game);
        DominoInterface.gameplay.setBounds(0, 0, DominoInterface.screenSize.width, DominoInterface.screenSize.height);
        gameplayContainer.add(gameplay);

        // Show and hide
        login.setVisible(false);
        waitingInterface.setVisible(false);
        gameplayContainer.setVisible(true);

        frame.pack();
    }

    /**
     * Plays a chip in the interface from the player position specified by the server
     * @param chip The chip
     * @param player The player position
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playChip (DominoChip chip, int player) {
        DominoInterface.gameplay.playChip(chip, player);
    }

    /**
     * Updates the game area with the specified line
     * @param line The line
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void loadLine(ArrayList<DominoChip> line) {
        DominoInterface.gameplay.loadLine(line);
    }

    /**
     * updates the interface
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void update() {
        frame.repaint();
        gameplay.repaint();
    }

    /**
     * Adjust the interface after a chip was played
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void chipWasPlayed () {
        gameplay.chipWasPlayed();
    }

    /**
     * Shows the game over itnerface
     * @param winner The winner's name
     * @param firstName The first team's name
     * @param secondName The secondTeam name
     * @param firstScore The first team's score
     * @param secondScore The second team's score
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void showWinScreen(String winner, String firstName, String secondName, int firstScore, int secondScore) {
        gameplay.setScore(firstName, secondName, firstScore, secondScore);
        gameOverInterface.show(winner, firstName, secondName, firstScore, secondScore);
        this.update();
    }

    /**
     * Lets the interface know it is the player's turn
     * @author Dario Urdapilleta
     * @since 04/13/2023
     */
    public void yourTurn() {
        gameplay.yourTurn();
    }
}
