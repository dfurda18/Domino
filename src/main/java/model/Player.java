package model;

import java.net.Socket;

/**
 * Model for a Player.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class Player {
    /**
     * The user id
     */
    private String id;
    /**
     * The username
     */
    private String name;
    /**
     * Constructor for a Player Data model
     * @param id The player ID
     * @param name The player name
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public Player (String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the player id
     * @return The player id.
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the player name
     * @return The player name.
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public String getName() {
        return name;
    }
}
