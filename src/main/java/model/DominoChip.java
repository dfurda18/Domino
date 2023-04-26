package model;

import javax.swing.*;

/**
 * Model for a Domino Chip.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class DominoChip {
    /**
     * First value
     */
    private int first;
    /**
     * Second value
     */
    private int second;
    /**
     * The image Icon
     */
    private ImageIcon image;
    /**
     * Whether the chip is placed first or last
     */
    private boolean placeFirst;

    /**
     * Default constructor for the Domino Chip
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoChip () {
        this.first = -1;
        this.second = -1;
        this.placeFirst = true;
        this.image = new ImageIcon(DominoChip.class.getClassLoader().getResource("images/Back.png"));
    }
    /**
     * Constructor for the Domino Chip
     * @param first The first value
     * @param second The second value
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoChip (int first, int second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the chip first value
     * @return The chip's first value
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public int getFirst() {
        return first;
    }

    /**
     * Returns the chip second value
     * @return The chip's second value
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public int getSecond() {
        return second;
    }

    /**
     * Sets the location of the chip
     * @param first Whether the chip is placed first.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void setPlaceFirst(boolean first) {
        this.placeFirst = first;
    }

    /**
     * Whether the cup is placed first or last.
     * @return True if it is placed first, and false otherwise.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean isPlacedFirst() {
        return placeFirst;
    }

    /**
     * Returns the chip cound
     * @return The chip cound
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public int getCount() {
        return first + second;
    }

    /**
     * Shows if the chip is a mule
     * @return True if the chip is a mule (same value), and false otherwise
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public boolean isMule() {
        return first == second;
    }

    /**
     * Returns the chip's image
     * @return The chip's image
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * Returns the chip's information as JSON
     * @return the chip's information as JSON
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public String asJson() {
        return "{\"first\":" + this.first + ",\"second\":" + this.second + ",\"place_first\":" + this.placeFirst + "}";
    }
}
