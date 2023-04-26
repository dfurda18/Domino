package client;

import model.DominoChip;
import model.DominoGame;
import model.Player;

import javax.swing.*;
import java.util.ArrayList;

/**
 * This clas handles all the domino gameplay interface elements
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/06/2023
 */
public class DominoGamePlayView extends JPanel {
    /**
     * The vertical hand width
     */
    private static final int V_WIDTH = 120;
    /**
     * The domino game
     */
    private static DominoGame game;
    /**
     * The player's hands
     */
    private static PlayerHandView[] players;
    /**
     * The game area
     */
    private static GameAreaView gameArea;
    /**
     * The Score count
     */
    private static ScoreView score;

    /**
     * Builds a new interface for the domino gameplay
     * @param game The game to display
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public DominoGamePlayView (DominoGame game) {
        super();
        Player tempPlayer;

        // Set the current object's attributes
        this.setLayout(null);
        this.setOpaque(false);
        this.game = game;

        // Create the player hand views
        players = new PlayerHandView[4];
        int currentPosition;
        for (PlayerHandView.Orientation orientation : PlayerHandView.Orientation.values()) {
            currentPosition = (orientation.ordinal() + game.getCurrentPlayer().getPosition()) % 4;
            tempPlayer = game.getPlayer(currentPosition);
            players[orientation.ordinal()] = new PlayerHandView(orientation, tempPlayer);
            players[orientation.ordinal()].setBounds(
                    (orientation == PlayerHandView.Orientation.SOUTH || orientation == PlayerHandView.Orientation.NORTH) ?
                            V_WIDTH * 2 :   // NORTH AND SOUTH
                            (orientation == PlayerHandView.Orientation.EAST ?
                                    DominoInterface.screenSize.width - V_WIDTH - 40 : // EAST
                                    0), // WEST
                    (orientation == PlayerHandView.Orientation.WEST || orientation == PlayerHandView.Orientation.EAST) ?
                            V_WIDTH :   // WEST and EAST
                            (orientation == PlayerHandView.Orientation.SOUTH ?
                                    DominoInterface.screenSize.height - V_WIDTH :   // SOUTH
                                    0), // NORTH
                    (orientation == PlayerHandView.Orientation.SOUTH || orientation == PlayerHandView.Orientation.NORTH) ?
                            DominoInterface.screenSize.width - (4 * V_WIDTH) :  // NORTH and SOUTH
                            V_WIDTH + 40,    // EAST and WEST
                    (orientation == PlayerHandView.Orientation.EAST || orientation == PlayerHandView.Orientation.WEST) ?
                            DominoInterface.screenSize.height - (2 * V_WIDTH) :  // EAST and WEST
                            V_WIDTH    // NORTH and SOUTH
                    );
            this.add(players[orientation.ordinal()]);
        }
        players[0].showHand(game.getCurrentPlayer().getHand());

        // Create the Game area
        gameArea = new GameAreaView();
        gameArea.setBounds(V_WIDTH + 40, V_WIDTH, DominoInterface.screenSize.width - (2 * V_WIDTH) - 80, DominoInterface.screenSize.height - (2 * V_WIDTH));
        gameArea.setTileButtons();
        this.add(gameArea);
    }

    /**
     * Plays a chip in the interface from the player position specified by the server
     * @param chip The chip
     * @param player The player position
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playChip(DominoChip chip, int player) {
        if(player == 0) {
            players[player].playChip(chip);
        } else {
            players[player].removeChip();
        }
    }

    /**
     * Updates the game area with the specified line
     * @param line The line
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void loadLine(ArrayList<DominoChip> line) {
        DominoGamePlayView.gameArea.loadLine(line);
    }

    /**
     * Adjust the interface after a chip was played
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void chipWasPlayed () {
        gameArea.chipWasPlayed();
        players[0].notYourTurn();
        for(PlayerHandView hand : players) {
            hand.deselect();
        }
    }

    /**
     * Sets the score
     * @param firstName The first team's name
     * @param secondName The secondTeam name
     * @param firstScore The first team's score
     * @param secondScore The second team's score
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void setScore(String firstName, String secondName, int firstScore, int secondScore) {
        gameArea.reset();
    }

    /**
     * Lets the interface know it is the player's turn
     * @author Dario Urdapilleta
     * @since 04/13/2023
     */
    public void yourTurn() {
        players[0].yourTurn();
    }
}
