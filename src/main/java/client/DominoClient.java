package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import utilities.NetworkProtocols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class of the Domino game for the Client.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/04/2023
 */
public class DominoClient implements Runnable {
    /**
     * The Client Instance
     */
    public static DominoClient instance;
    /**
     * The Domino Player
     */
    private static DominoPlayer player;
    /**
     * The currentDomino Game
     */
    private static DominoGame game;
    /**
     * The gameInterface
     */
    public static DominoInterface gameInterface;
    /**
     * Keeps track if the player is waiting to be matched for a gmae
     */
    private static boolean isWaiting;
    /**
     * Whether or not is the first game
     */
    public static boolean firstGame;
    /**
     * Whether is the player's turn
     */
    private static boolean yourTurn;
    /**
     * The selected chip
     */
    public static DominoChip selectedChip;
    /**
     * Keeps Track if the interface is ready to play
     */
    private static boolean gameOver;
    /**
     * Keeps track if the player needs to check if they pass
     */
    public static boolean checkTurn;

    /**
     * Class constructor
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public DominoClient () {
        // set the singleton instance
        instance = this;
        firstGame = true;
        yourTurn = false;
        selectedChip = null;
        // Load the host and port
        try {
            NetworkProtocols.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Create the interface
        gameInterface = new DominoInterface();
        gameInterface.createInterface();
    }

    /**
     * This method handles all the received messages
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    @Override
    public void run() {
        String response;

        // handle the messages while the user is connected
        while (player.isConnected()) {
            response = player.getMessage();
            handleResponse(response);
        }
    }

    /**
     * Creates the domino interface and prepares it to start playing
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public static void startGame () {
        isWaiting = false;
        // Set the closing method to close the socket connection
        gameInterface.setWindowAdapter(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(player != null) {
                    player.logout();
                }
            }
        });

        // Set the login action
        gameInterface.setLoginOnClick(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String response;
                player = new DominoPlayer(gameInterface.getLoginServer().equals("") ? NetworkProtocols.HOST : gameInterface.getLoginServer(), NetworkProtocols.PORT);
                if (player.isConnected()) {
                    // Run the listening thread
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    executor.execute(instance);

                    // Set the user credentials and login
                    player.setCredentials(gameInterface.getLoginUserId(), gameInterface.getLoginUserName());
                    player.login();
                } else {
                    gameInterface.showLoginErrorMessage("Could not connect to the server.");
                }

            }
        });
    }

    /**
     * Handles a response from the server
     * @param response The server response
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    private static void handleResponse (String response) {
        String action = "";
        JsonNode data;
        ObjectMapper objectMapper = new ObjectMapper();
        if(!response.equals("")) {
            try {
                // Parse the Action and Get the data
                response = response.replaceAll("(\r\n|\n)", "<br />");
                JsonNode jsonNode = objectMapper.readTree(response);
                action = jsonNode.get("action").asText();
                data = jsonNode.get("data");

                // Handle each action differently
                switch (action) {
                    case NetworkProtocols.LOGIN_ACTION:
                        System.out.println(NetworkProtocols.LOGIN_ACTION);
                        isWaiting = true;
                        gameInterface.logIn();
                        break;
                    case NetworkProtocols.GAME_READY_RESPONSE:
                        System.out.println(NetworkProtocols.GAME_READY_RESPONSE);
                        gameOver = false;
                        int playerPosition = 0;
                        if(isWaiting) {
                            isWaiting = false;
                        }

                        // Load the players
                        ArrayList<Player> players = new ArrayList<Player>();
                        for(JsonNode playerNode : data.get("players")) {
                            players.add(new Player(playerNode.get("user_id").asText(), playerNode.get("user_name").asText()));
                            if(playerNode.get("user_id").asText().equals(player.getId())) {
                                player.setPosition(playerPosition);
                            }
                            playerPosition++;
                        }

                        // Load the hand
                        ArrayList<DominoChip> chips = new ArrayList<DominoChip>();
                        for(JsonNode playerNode : data.get("hand")) {
                            chips.add(new DominoChip(playerNode.get("first").asInt(), playerNode.get("second").asInt()));
                        }
                        player.setHand(new DominoHand(chips));

                        // Update the interface
                        game = new DominoGame(players, player);
                        gameInterface.startGame(game);

                        yourTurn = data.get("start").asBoolean();
                        break;
                    case NetworkProtocols.PLAYER_PLAYED_RESPONSE:
                        System.out.println(NetworkProtocols.PLAYER_PLAYED_RESPONSE);
                        // Get the played chip
                        DominoChip playedChip = new DominoChip(data.get("chip").get("first").asInt(), data.get("chip").get("second").asInt());
                        playedChip.setPlaceFirst(data.get("chip").get("place_first").asBoolean());
                        int player = ((data.get("player").asInt() - DominoClient.player.getPosition()) + 4) % 4;
                        if(playedChip.getFirst() != -1 && playedChip.getSecond() != -1) {
                            gameInterface.playChip(playedChip, player);
                        }

                        // Get the line information
                        ArrayList<DominoChip> line = new ArrayList<DominoChip>();
                        for(JsonNode playerNode : data.get("line")) {
                            playedChip = new DominoChip(playerNode.get("first").asInt(), playerNode.get("second").asInt());
                            playedChip.setPlaceFirst(playerNode.get("place_first").asBoolean());
                            line.add(playedChip);
                        }
                        DominoClient.gameInterface.loadLine(line);

                        game.updateLine(line);

                        gameInterface.update();
                        break;
                    case NetworkProtocols.PLAYER_TURN_RESPONSE:
                        System.out.println(NetworkProtocols.PLAYER_TURN_RESPONSE);
                        if(gameOver) {
                            checkTurn = true;
                        } else {
                            checkTurn();
                        }
                        break;
                    case NetworkProtocols.WINNER_RESPONSE:
                        System.out.println(NetworkProtocols.WINNER_RESPONSE);
                        gameOver = true;
                        firstGame = false;
                        int winner = data.get("winner").asInt();
                        int firstTeamScore = data.get("first_score").asInt();
                        int secondTeamScore = data.get("second_score").asInt();
                        DominoGame.TeamNumber winnerTeam = winner == 0 || winner == 2 ? DominoGame.TeamNumber.FIRST : DominoGame.TeamNumber.SECOND;
                        gameInterface.showWinScreen(game.getTeamName(winnerTeam),
                                game.getTeamName(DominoGame.TeamNumber.FIRST),
                                game.getTeamName(DominoGame.TeamNumber.SECOND),
                                firstTeamScore, secondTeamScore);
                        break;
                    case NetworkProtocols.SAME_USER_RESPONSE:
                        System.out.println(NetworkProtocols.SAME_USER_RESPONSE);
                        gameInterface.showLoginErrorMessage("A user with the same id has signed in already.");
                        break;
                    case NetworkProtocols.ERROR_ACTION:
                        gameInterface.showLoginErrorMessage(data.asText());
                        break;
                    case NetworkProtocols.MESSAGE_RECEIVED_RESPONSE:
                        break;
                }
            } catch (JsonProcessingException exception) {
                gameInterface.showLoginErrorMessage("There was an error reading the response.");
            }
        }
    }

    /**
     * Plays a chip if possible
     * @param firstSide True if the player wants to play on the first side
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public static void playChip (boolean firstSide) {
        if(yourTurn && selectedChip != null) {
            String message = "";
            boolean canPlayFirst = game.canPlayFirst(selectedChip, firstGame);
            boolean canPlayLast = game.canPlayLast(selectedChip, firstGame);
            boolean sendMessage = false;
            if ((canPlayFirst && firstSide) || (canPlayLast && !firstSide)) {
                selectedChip.setPlaceFirst(firstSide);
                sendMessage = true;
            }
            game.getCurrentPlayer().getHand().removeChip(selectedChip);
            if(sendMessage) {
                // Make the message
                message = "{ \"action\": \"" + NetworkProtocols.PLAY_CHIP_ACTION + "\", "
                        + "\"data\": " + selectedChip.asJson() + "}";
                player.sendMessage(message);
            }
            yourTurn = false;
            selectedChip = null;
            gameInterface.chipWasPlayed();
            gameInterface.update();
        }
    }

    /**
     * Checks if the user should pass
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public static void checkTurn() {
        if(game.canPlayAny()) {
            yourTurn = true;
            gameInterface.yourTurn();
        } else {
            game.pass();
        }
        checkTurn = false;
    }
}
