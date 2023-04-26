package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a Domino Game.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class DominoGame {
    public enum TeamNumber {FIRST, SECOND};
    /**
     * The default player names
     */
    public static Player[] defaultPlayers;
    /**
     * List of players
     */
    private List<Player> players;
    /**
     * The current Player
     */
    private DominoPlayer currentPlayer;
    /**
     * The Domino Game line
     */
    private DominoLine line;

    /**
     * Constructor for the Domino Game
     * @param players The list of players
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoGame(ArrayList<Player> players, DominoPlayer currentPlayer) {
        // Set the default players
        defaultPlayers = new Player[4];
        defaultPlayers[0] = new Player("102946901#$%", "Rockadoodle");
        defaultPlayers[1] = new Player("102946902#$%", "Monstruon");
        defaultPlayers[2] = new Player("102946903#$%", "Rulpestinsky");
        defaultPlayers[3] = new Player("102946904#$%", "Manigoldo");

        // Set the object attributes
        this.players = players;
        this.currentPlayer = currentPlayer;
        line = new DominoLine();
    }

    /**
     * Gets a specific player
     * @param position The position in the player's list
     * @return The player in the speficied position.
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public Player getPlayer(int position) {
        if(position < players.size()) {
            return players.get(position);
        } else {
            return defaultPlayers[position];
        }
    }

    /**
     * Returns the current player
     * @return the current player
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoPlayer getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Returns a team's name
     * @param number The team number
     * @return The Team's name
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public String getTeamName(TeamNumber number) {
        return number == TeamNumber.FIRST ?
                this.getPlayer(0).getName() + "-" + this.getPlayer(2).getName() :
                this.getPlayer(1).getName() + "-" + this.getPlayer(3).getName();
    }

    /**
     * Updates the line with a list of chips
     * @param chips The list of chips
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void updateLine(ArrayList<DominoChip> chips) {
        line.clear();
        line.update(chips);
    }

    /**
     * Checks if a chip can be played
     * @param chip The chip to be played
     * @return True if the chip can be played and false otherwise.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean canPlay (DominoChip chip, boolean firstGame) {
        return line.canPlay(chip, firstGame);
    }

    /**
     * Checks if any chip can be player
     * @return True if the player can play a chip, false otherwise.
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public boolean canPlayAny () {
        return line.canPlayAny(this.currentPlayer.getHand());
    }

    /**
     * Passes their turn
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void pass() {
        currentPlayer.pass();
    }

    /**
     * Tells whether the player can play on the first end
     * @param chip The chip to play
     * @param firstGame True if it is the first game
     * @return True if the player can play in the first end, and false otherwise.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean canPlayFirst (DominoChip chip, boolean firstGame) {
        return line.canPlayFirst(chip, firstGame);
    }

    /**
     * Tells whether the player can play on the last end
     * @param chip The chip to play
     * @param firstGame True if it is the first game
     * @return True if the player can play in the last end, and false otherwise.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean canPlayLast (DominoChip chip, boolean firstGame) {
        return line.canPlayLast(chip, firstGame);
    }

    /**
     * Returns whether the line has different ends
     * @return True if the line has different ends and false otherwise
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean hasDifferentEnds() {
        return line.hasDifferentEnds();
    }
}
