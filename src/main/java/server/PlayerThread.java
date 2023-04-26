package server;

import model.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utilities.NetworkProtocols;

import java.net.Socket;
import java.util.*;

/**
 * This class handles a match thread.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class PlayerThread implements Runnable {
    /**
     * The user list
     */
    private static ArrayList<DominoPlayer> users = new ArrayList<DominoPlayer>();
    /**
     * Keeps the status of the match
     */
    private boolean running;
    /**
     * Thread number
     */
    public final int threadNumber;
    /**
     * The user associated with this thread
     */
    private DominoPlayer user;
    /**
     * The players hand
     */
    private DominoHand hand;
    /**
     * The game the User is on
     */
    private DominoServerGame game;
    /**
     * The players number in the game
     */
    private int playerCounter;

    /**
     * Constructor for the domino thread
     * @param number The thread Number
     * @param socket The client socket
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public PlayerThread(int number, Socket socket) {
        super();
        this.user = new DominoPlayer(socket);
        this.threadNumber = number;
        this.running = false;
    }
    /**
     * Runs the thread.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void run()
    {
        this.running = true;
        while(running) {
            this.handleMessage();
        }
        this.user.close();
    }

    /**
     * Sets the player number in the server
     * @param playerCounter The player number in the server
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void setPlayerCounter(int playerCounter) {
        this.playerCounter = playerCounter;
    }

    /**
     * Handles a received message;
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void handleMessage() {
        String outputMessage = "";
        String outputAction = "";
        String action = "";
        String message;
        String userID;
        String userName;
        DominoChip chip;
        boolean sendMessage = true;

        // Get the message
        System.out.println("SERVER: Thread: " + this.threadNumber + ". Waiting for client's message.");
        message = user.getMessage();
        System.out.println("SERVER: Thread: " + this.threadNumber + ". Message: " + message + " received");

        // Parse the message
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            action = jsonNode.get("action").asText();

            // Handle the message according to the action
            switch (action) {
                case NetworkProtocols.LOGIN_ACTION:
                    boolean first = true;
                    userID = jsonNode.get("data").get("user_id").asText();
                    userName = jsonNode.get("data").get("user_name").asText();

                    // Check if there's an existing used signed in
                    if(PlayerThread.hasUser(userID)) {
                        outputMessage = "\"Error This user has already signed in.\"";
                        outputAction = NetworkProtocols.SAME_USER_RESPONSE;
                    } else {
                        // Set the user's credentials
                        user.setCredentials(userID, userName);

                        // Checks if a game can be assigned to the user
                        game = DominoServer.makeGame(this, true);

                        // Prepare the response
                        if(game != null) {
                            sendMessage = false;
                        } else {
                            outputMessage = "\"\"";
                            outputAction = NetworkProtocols.LOGIN_ACTION;
                        }
                    }
                    break;
                case NetworkProtocols.PLAY_CHIP_ACTION:
                    // Get the data
                    chip = new DominoChip(jsonNode.get("data").get("first").asInt(), jsonNode.get("data").get("second").asInt());
                    chip.setPlaceFirst(jsonNode.get("data").get("place_first").asBoolean());
                    game.playChip(chip, playerCounter);
                    message = "{ \"action\": \"" + NetworkProtocols.PLAYER_PLAYED_RESPONSE + "\", "
                        + "\"data\": {\"chip\":" + chip.asJson() + ","
                        + "\"player\": " + playerCounter + "," +
                        "\"line\":" + game.getLineAsJson() + "}}";

                    // Send the play to all players
                    for (PlayerThread thread : game.getPlayers()) {
                        System.out.println("SERVER: Thread: " + thread.threadNumber + ". Sending message: " + outputMessage);
                        thread.getUser().sendMessage(message);
                    }

                    // Check if the player won
                    if(game.getHand(playerCounter).getChips().size() <= 0) {
                        game.victory(playerCounter);
                    } else {
                        // Send the next turn message to the next player after taking all the CPU actions
                        message = "{ \"action\": \"" + NetworkProtocols.PLAYER_TURN_RESPONSE + "\", "
                                + "\"data\": {}}";
                        if(playerCounter + 1 >= game.getPlayers().size()) {
                            // Do the rest of the actions
                            game.playCPU(playerCounter);

                            if(game.timesPassed >= 4) {
                                // The game has closed
                                game.isClosed();
                            } else {
                                // Send the message to the first player
                                game.getPlayers().get(0).getUser().sendMessage(message);
                            }
                        } else {
                            // Send the message to the next player
                            game.getPlayers().get(playerCounter + 1).getUser().sendMessage(message);
                        }
                    }

                    // End the game
                    if(game.hasEnded()) {
                        this.endGame();
                    }
                    break;
                case NetworkProtocols.PASS_ACTION:
                    game.timesPassed++;
                    if(game.timesPassed >= 4) {
                        // The game has closed
                        game.isClosed();
                    } else {
                        // Send the next turn message to the next player after taking all the CPU actions
                        message = "{ \"action\": \"" + NetworkProtocols.PLAYER_TURN_RESPONSE + "\", "
                                + "\"data\": {}}";
                        if(playerCounter + 1 >= game.getPlayers().size()) {
                            // Do the rest of the actions
                            game.playCPU(playerCounter);

                            // Send the message to the first player
                            game.getPlayers().get(0).getUser().sendMessage(message);
                        } else {
                            // Send the message to the next player
                            game.getPlayers().get(playerCounter + 1).getUser().sendMessage(message);
                        }
                    }
                    // End the game
                    if(game.hasEnded()) {
                        this.endGame();
                    }
                    break;
                case NetworkProtocols.LOGOUT_ACTION:
                    user.close();
                    users.remove(user);
                    this.running = false;
                    System.out.println("SERVER: Thread: " + this.threadNumber + ". Logged out.");
                    sendMessage = false;
                    DominoServer.remove(this);
                    break;
                default:
                    System.out.println("SERVER: Thread: " + this.threadNumber + ". ERROR unhandled action.");
                    outputMessage = "\"Error understanding your action.\"";
                    outputAction = NetworkProtocols.ERROR_ACTION;
            }
        } catch (JsonProcessingException exception) {
            System.out.println("SERVER: Thread: " + this.threadNumber + ". ERROR parsing json.");
            outputMessage = "\"Error parsing the json, please try again.\"";
            outputAction = NetworkProtocols.ERROR_ACTION;
        }

        // Send a message
        if(this.running && sendMessage) {
            System.out.println("SERVER: Thread: " + this.threadNumber + ". Sending message: " + outputMessage);
            message =  "{ \"action\": \"" + outputAction + "\", "
                    + "\"data\": " + outputMessage + "}";
            user.sendMessage(message);
        }
    }

    /**
     * This method checks if a user has already signed in with an id
     * @param id The user id
     * @return True if the server already has a user signed in
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public static boolean hasUser (String id) {
        boolean hasUser = false;

        // Search for the user
        for (DominoPlayer user : PlayerThread.users) {
            if (user.getId() != null && user.getId().equals(id)) {
                hasUser = true;
            }
        }
        return hasUser;
    }

    /**
     * Returns this thread's user
     * @return this thread's user.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoPlayer getUser() {
        return user;
    }

    /**
     * Sets the game to the player thread
     * @param game The game
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void setGame (DominoServerGame game) {
        this.game = game;
    }
    /**
     * Terminates the thread
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void terminate() {
        user.close();
        users.remove(user);
        this.running = false;
        System.out.println("SERVER: Thread: " + this.threadNumber + ". Was terminated.");
    }

    /**
     * Sends the starting message to the player, and lets the player know if they start the game or not.
     * @param players The Json version of the player's list
     * @param hand The current player's hand
     * @param start True if the player will start, false otherwise.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void sendStartGame(String players, DominoHand hand, boolean start) {
        String message = "";
        String outputMessage = "{\"hand\":" + hand.asJson() + "," +
                "\"start\":" + start + "," +
                "\"players\":" + players + "}";

        this.hand = hand;
        System.out.println("SERVER: Thread: " + this.threadNumber + ". Sending message: " + outputMessage);
        message =  "{ \"action\": \"" + NetworkProtocols.GAME_READY_RESPONSE + "\", "
                + "\"data\": " + outputMessage + "}";
        user.sendMessage(message);
    }

    /**
     * Ends a game and starts a new one
     */
    private void endGame() {
        int playerCoutner;
        int startingPlayer = game.getWinner();
        DominoChip chip;
        // Send the game over message
        String message = "{ \"action\": \"" + NetworkProtocols.WINNER_RESPONSE + "\", "
                + "\"data\": {\"winner\":" + game.getWinner() + ","
                + "\"first_score\": " + game.getFirstTeamScore() + ","
                + "\"second_score\":" + game.getSecondTeamScore() + "}}";
        for (PlayerThread thread : game.getPlayers()) {
            System.out.println("SERVER: Thread: " + thread.threadNumber + ". Sending message: " + message);
            thread.getUser().sendMessage(message);
        }

        if(game.getFirstTeamScore() < 100 && game.getSecondTeamScore() < 100) {
            // Start a new game
            game.startNewGame(DominoServerGame.StartingPlayer.values()[startingPlayer], false);

            // Send the responses
            for(playerCoutner = 0; playerCoutner < game.getPlayers().size(); playerCoutner++) {
                game.getPlayers().get(playerCoutner).sendStartGame(game.getPlayersAsJson(), game.getHand(playerCoutner), playerCoutner == startingPlayer);
            }

            // Check if the next user is a player
            if(startingPlayer >= game.getPlayers().size()) {
                for(playerCoutner = startingPlayer; playerCoutner < 4; playerCoutner++) {
                    chip = game.makePlay(playerCoutner, true);
                    if(chip != null) {
                        message = "{ \"action\": \"" + NetworkProtocols.PLAYER_PLAYED_RESPONSE + "\", "
                                + "\"data\": {\"chip\":" + chip.asJson() + ","
                                + "\"player\": " + playerCoutner + "," +
                                "\"line\":" + game.getLineAsJson() + "}}";
                        game.sendMessages(message);
                    }
                }
                startingPlayer = playerCoutner % 4;
            }
            // Send a message to the first player so they can start
            message = "{ \"action\": \"" + NetworkProtocols.PLAYER_TURN_RESPONSE + "\", "
                    + "\"data\": {}}";
            game.getPlayers().get(startingPlayer).getUser().sendMessage(message);
        }
    }

}
