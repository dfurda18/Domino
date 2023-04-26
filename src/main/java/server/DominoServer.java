package server;


import model.DominoChip;
import model.DominoGame;
import model.DominoPlayer;
import model.DominoServerGame;
import utilities.NetworkProtocols;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Main class of the Domino Game for the Server.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class DominoServer{
    /**
     * The number of players needed to start a game
     */
    public static final int GAME_SIZE = 2;
    /**
     * The Server socket used for the communication.
     */
    private ServerSocket server = null;
    /**
     * The list of threads
     */
    private static List<PlayerThread> connections = Collections.synchronizedList(new ArrayList<PlayerThread>());
    /**
     * The list of games
     */
    private static List<DominoGame> games = Collections.synchronizedList((new ArrayList<DominoGame>()));
    /**
     * The player pool
     */
    private static ArrayList<PlayerThread> playerPool = new ArrayList<PlayerThread>();
    /**
     * Max number of threads
     */
    private static final int MAX_THREADS = 45;

    /**
     * Main method that contains the chat's main functionality.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public static void startServer() {

        // Load the host and port
        try {
            NetworkProtocols.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Prepare the variables
        PlayerThread thread;
        Boolean keepGoing = true;
        DominoServer application = new DominoServer();
        Socket currentClient;
        int threadNumber = 1;
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        System.out.println("SERVER: Listening on PORT: " + NetworkProtocols.PORT);

        try {
            // Keep accepting connections
            while(keepGoing) {
                System.out.println("SERVER: waiting for the client connection ...");
                currentClient = application.server.accept();
                System.out.println("SERVER: Connected to client: "+ threadNumber);

                // Start a new thread
                thread = new PlayerThread(threadNumber, currentClient);
                connections.add(thread);
                executor.execute(thread);
                threadNumber++;
            }

            // Close the server Connection
            application.server.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    /**
     * Contructor method for the BattleshipNetwork server class.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoServer() {
        try {
            this.server = new ServerSocket(NetworkProtocols.PORT);
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    /**
     * Gets the list of connected users in a JSON.
     * @return A string of the connected players as a Json
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public static String usersAsJson() {
        String json = "[";
        DominoPlayer player;
        boolean first = true;
        for (PlayerThread connection : connections) {
            player = connection.getUser();
            if (first) {
               first = false;
            } else {
                json += ",";
            }
            json += player.toJson();
        }
        json += "]";
        return json;
    }

    /**
     * Checks if there's enough players to make a game after a new thread has logged in.
     * @param thread The player thread
     * @param firstGame Whether this is the first game
     * @return The game or null if not able to
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public static synchronized DominoServerGame makeGame(PlayerThread thread, boolean firstGame) {
        DominoServerGame game = null;
        DominoChip chip = null;
        boolean firstPlay = true;
        String message = "";
        int playerCoutner, startingPlayer;

        // Check if there's enough player to play
        if(playerPool.size() >= GAME_SIZE -1) {
            ArrayList<PlayerThread> players = new ArrayList<PlayerThread>();

            // add the players
            players.add(thread);
            for(playerCoutner = 1; playerCoutner < GAME_SIZE; playerCoutner++) {
                players.add(playerPool.get(0));
                playerPool.remove(0);
            }

            // Create and start the game
            game = new DominoServerGame(players);
            startingPlayer = game.startNewGame(DominoServerGame.StartingPlayer.FIRST_GAME, firstGame);

            // set the game for each player
            for(int playerCounter = 0; playerCounter < game.getPlayers().size(); playerCounter++) {
                game.getPlayers().get(playerCounter).setGame(game);
                game.getPlayers().get(playerCounter).setPlayerCounter(playerCounter);
            }

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
                    + "\"data\": null}";
            game.getPlayers().get(startingPlayer).getUser().sendMessage(message);

        } else {
            playerPool.add(thread);
        }
        return game;
    }

    /**
     * Removes a thread from the player pool
     * @param thread The thread to remove
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public static void remove(PlayerThread thread) {
        playerPool.remove(thread);
    }
}
