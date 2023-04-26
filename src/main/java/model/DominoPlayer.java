package model;

import utilities.NetworkProtocols;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Model for a Domino Player.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class DominoPlayer {
    /**
     * The user id
     */
    private String id;
    /**
     * The username
     */
    private String name;
    /**
     * The player's hand
     */
    private DominoHand hand;
    /**
     * The client's {@link java.net.Socket Socket}.
     */
    private Socket socket;
    /**
     * The player position according to the server
     */
    private int position;
    /**
     * The Object output stream.
     */
    private ObjectOutputStream output;
    /**
     * The Object input stream
     */
    private ObjectInputStream input;
    /**
     * The conection status
     */
    private boolean isConnected;

    /**
     * Constructor for the Domino Player for the server side
     * @param socket The communication socket
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoPlayer (Socket socket) {
        this.socket = socket;
        this.isConnected = true;
    }

    /**
     * Constructor for the Domino User for the client side
     * @param host The Host address.
     * @param port The port to create the Socket.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoPlayer (String host, int port) {
        // Connect to the server
        if(host.equals("localhost") || NetworkProtocols.isValidIPAddress(host)) {
            try {
                this.socket = new Socket(host, port);
                this.isConnected = true;
            } catch (IOException e) {
                this.isConnected = false;
            }
        }
    }

    /**
     * Sets the player position according to the server
     * @param position The player position
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Returns the player's position according to the server
     * @return the player's position according to the server
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public int getPosition() {
        return position;
    }

    /**
     * Receives a Message from this user
     * @return the message sent by the user
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public String getMessage () {
        String message = "";
        try {
            // Get the message
            this.input = new ObjectInputStream(this.socket.getInputStream());
            message = (String)this.input.readObject();
        } catch (IOException e) {
            this.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    /**
     * Sends a message through the output stream.
     * @param message The message to send.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void sendMessage(String message) {
        try {
            // Send the message
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.output.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes connections.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void close() {
        try {
            if(this.input != null) {
                this.input.close();
            }
            if(this.output != null) {
                this.output.close();
            }
            if(this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    /**
     * Returns the user's connection status
     * @return True if the user is connencted and false otherwise.
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Returns the user's id
     * @return The user's id
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public String getId () {
        return id;
    }

    /**
     * Returns the user's name
     * @return The user's name
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's credentials
     * @param userId The user id
     * @param userName The username
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void setCredentials (String userId, String userName) {
        this.id = userId;
        this.name = userName;
    }

    /**
     * Logins to the chat server
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void login () {
        String message = "{ \"action\": \"" + NetworkProtocols.LOGIN_ACTION + "\", "
                + "\"data\": {"
                +"\"user_id\": \"" + id + "\", "
                + "\"user_name\": \"" + name + "\"}}";
        sendMessage(message);
    }

    /**
     * Logs out to the chat server
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void logout () {
        this.isConnected = false;
        if(socket != null) {
            String message = "{ \"action\": \"" + NetworkProtocols.LOGOUT_ACTION + "\", "
                    + "\"data\": null}";
            sendMessage(message);
            close();
        }
    }

    /**
     * Sends the user action to the server
     * @param chatMessage The text to send
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void sendAction(String chatMessage) {
        chatMessage = chatMessage.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
        String message = "{ \"action\": \"" + NetworkProtocols.SEND_ACTION + "\", "
                + "\"data\": {"
                +"\"user_id\": \"" + id + "\", "
                +"\"user_name\": \"" + name + "\", "
            //    +"\"group_id\": \"" + currentGroup.getId() + "\", "
                + "\"message\": \"" + chatMessage + "\"}}";
        sendMessage(message);
    }

    /**
     * Returns a json object that represents this player
     * @return A Json object that represent the player
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public String toJson() {
        String json = "{";
        json += "\"user_id\":\"" + this.id + "\","
                + "\"user_name\":\"" + this.name + "\"";
        json += "}";
        return json;
    }

    /**
     * Sets the hand
     * @param hand The hand
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void setHand(DominoHand hand) {
        this.hand = hand;
    }

    /**
     * Gets the hand
     * @return The player's hand
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoHand getHand() {
        return this.hand;
    }

    /**
     * Passes their turn
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void pass() {
       String message = "{ \"action\": \"" + NetworkProtocols.PASS_ACTION + "\", "
               + "\"data\": null}";
       this.sendMessage(message);
    }
}
