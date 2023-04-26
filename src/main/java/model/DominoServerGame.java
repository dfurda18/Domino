package model;

import server.PlayerThread;
import utilities.NetworkProtocols;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a Domino Game for the server.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/06/2023
 */
public class DominoServerGame {
    /**
     * The starting team enum
     */
    public enum StartingPlayer { PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4, FIRST_GAME }
    /**
     * List of players
     */
    private ArrayList<PlayerThread> players;
    /**
     * The tile set
     */
    private DominoTiles tiles;
    /**
     * The Domino Game line
     */
    private DominoLine line;
    /**
     * The Domino Hands
     */
    private List<DominoHand> hands;
    /**
     * The game status
     */
    private boolean gameEnded;
    /**
     * The winning player
     */
    private int gameWinner;
    /**
     * The first team score
     */
    private int firstTeamScore;
    /**
     * The second team score
     */
    private int secondTeamScore;
    /**
     * The player that started the game
     */
    public int startedThisGame;
    /**
     * whether it is the first game
     */
    private boolean firstGame;
    /**
     * Times players have passed in a row
     */
    public int timesPassed;

    /**
     * Constructor for the Domino Game
     * @param players The list of players
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public DominoServerGame(ArrayList<PlayerThread> players) {
        this.players = players;
        tiles = new DominoTiles();
        line = new DominoLine();
        hands = new ArrayList<DominoHand>();
        firstTeamScore = 0;
        secondTeamScore = 0;
        firstGame = true;
    }

    /**
     * Starts a new Domino Game
     * @param startingPlayer The starting player
     * @return the starting player
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public int startNewGame(StartingPlayer startingPlayer, boolean firstGame) {
        int hand, nextPlayer = 0;
        this.gameEnded = false;
        this.gameWinner = -1;
        this.firstGame = firstGame;
        timesPassed = 0;

        // Create the line and lines
        line.clear();
        line = new DominoLine();
        hands.clear();

        // Shuffle and deal the tiles
        tiles.shuffle();
        for(hand = 0; hand < 4; hand++) {
            hands.add(new DominoHand(tiles.getHand()));
        }

        // Find the first player
        if(startingPlayer == StartingPlayer.FIRST_GAME) {
            for(int handCounter = 0; handCounter < 4; handCounter++) {
                if(hands.get(handCounter).hasDouble6()) {
                    nextPlayer = handCounter;
                }
            }
        } else {
            nextPlayer = startingPlayer.ordinal();
        }

        this.startedThisGame = nextPlayer;

        return nextPlayer;
    }

    /**
     * Sends a message to all the players.
     * @param message The message to send
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public void sendMessages(String message) {
        for(PlayerThread player : players) {
            System.out.println("SERVER GAME: Sending message: " + message);
            player.getUser().sendMessage(message);
        }
    }

    /**
     * Creates a message with action and data
     * @param action The action string
     * @param data The data
     * @return The message
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    private String createMessage (String action, String data) {
        return  "{ \"action\": \"" + action + "\", "
                + "\"data\": " + data + "}";
    }

    /**
     * Returns the player's list as a json
     * @return The player's list as a json.
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    private String getPlayerListAsJson () {
        String data = "[";
        for (PlayerThread player : players) {
            data += player.getUser().toJson();
        }
        data += "]";
        return data;
    }

    /**
     * Returns the players
     * @return The players
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public ArrayList<PlayerThread> getPlayers () {
        return players;
    }

    /**
     * Returns the specified hand
     * @param position The hand position in the array.
     * @return The player's hand in the specified position.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoHand getHand (int position) {
        return hands.get(position);
    }

    /**
     * Returns the players as a JSON
     * @return the players as a JSON
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public String getPlayersAsJson () {
        String json = "[";
        boolean first = true;
        for (PlayerThread connection : players) {
            if (first) {
                first = false;
            } else {
                json += ",";
            }
            json += connection.getUser().toJson();
        }
        json += "]";
        return json;
    }

    /**
     * Makes a CPU play
     * @param player The player position
     * @return The chip played or null if passed
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoChip makePlay(int player, boolean firstGame) {
        DominoChip playedChip = null;
        for (int chipCounter = 0; chipCounter < hands.get(player).getChips().size(); chipCounter++) {
            if(line.canPlay(hands.get(player).getChips().get(chipCounter), firstGame)) {
                playedChip = hands.get(player).getChips().remove(chipCounter);
                break;
            }
        }
        if(playedChip != null) {
            line.playChipCPU(playedChip);
            if(hands.get(player).getChips().size() <= 0) {
                this.victory(player);
            }
        }
        return playedChip;
    }

    /**
     * Performs the necessary steps when a player wins
     * @param player The player's position
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void victory(int player) {
        this.gameEnded = true;
        this.gameWinner = player;
        if(player == 0 || player == 2) {
            firstTeamScore += hands.get(1).count() + hands.get(3).count();
        } else {
            secondTeamScore += hands.get(2).count() + hands.get(0).count();
        }
    }

    /**
     * Performs the necessary steps when a game is closed
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void isClosed() {
        int firstTeamCount = hands.get(1).count() + hands.get(3).count();
        int secondTeamCount = hands.get(2).count() + hands.get(0).count();
        if(firstTeamCount > secondTeamCount ) {
            this.victory(0);
        } else if (secondTeamCount > firstTeamCount) {
            this.victory(1);
        } else {
            this.victory(this.startedThisGame);
        }
    }

    /**
     * Indicates ig the current game is over
     * @return True if the game is over
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public boolean hasEnded() {
        return this.gameEnded;
    }

    /**
     * Returns the winning player
     * @return The winning player
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public int getWinner() {
        return this.gameWinner;
    }

    /**
     * Returns the current first team score
     * @return the first team score
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public int getFirstTeamScore () {
        return this.firstTeamScore;
    }

    /**
     * Returns the current second team score
     * @return the second team score
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public int getSecondTeamScore () {
        return this.secondTeamScore;
    }

    /**
     * Performs a specific play
     * @param chip the Chip to play
     * @param player The player
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playChip (DominoChip chip, int player) {
        line.playChip(chip);
        for(DominoChip chipCounter : hands.get(player).getChips()) {
            if(chip.getFirst() == chipCounter.getFirst() && chip.getSecond() == chipCounter.getSecond()) {
                hands.get(player).getChips().remove(chipCounter);
                break;
            }
        }
    }

    /**
     * Returns the line as Json
     * @return The line as a JSON
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public String getLineAsJson () {
        return line.asJson();
    }

    /**
     * Plays for the CPU players until there's a human player.
     * @param lastPlayer The last player to play
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playCPU(int lastPlayer) {
        int nextPlayer = (lastPlayer + 1) % 4;
        DominoChip chip;
        String message;
        while (nextPlayer >= players.size()) {
            chip = this.makePlay(nextPlayer, this.firstGame);
            if (chip != null) {
                message = "{ \"action\": \"" + NetworkProtocols.PLAYER_PLAYED_RESPONSE + "\", "
                        + "\"data\": {\"chip\":" + chip.asJson() + ","
                        + "\"player\": " + nextPlayer + "," +
                        "\"line\":" + this.getLineAsJson() + "}}";
                this.sendMessages(message);
                timesPassed = 0;
            } else {
                timesPassed++;
            }
            nextPlayer = (nextPlayer + 1) % 4;
        }
    }
}
