package model;

import java.util.ArrayList;

/**
 * Model for a Domino Hand.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/11/2023
 */
public class DominoHand {
    /**
     * The chips
     */
    private ArrayList<DominoChip> chips;

    /**
     * Constructor for the Domino Hand
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoHand (ArrayList<DominoChip> chips) {
        this.chips = chips;
    }

    /**
     * Gets the chip in the specified position or null if there's none
     * @param position The chip's position
     * @return The chip in the specified position or null if there's none
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoChip getChip (int position) {
        return chips.get(position);
    }

    /**
     * Returns the hand chips
     * @return the hand chips
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public ArrayList<DominoChip> getChips () {
        return chips;
    }

    /**
     * Plays a chip
     * @param position The chip's position
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playChip (int position) {
        chips.remove(position);
    }

    /**
     * Tells if the hand has the double 6.
     * @return True if the hand has the double 6 and false otherwise
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean hasDouble6() {
        boolean hasDouble6 = false;
        for(DominoChip chip : chips) {
            if(chip.getFirst() == 6 && chip.getSecond() == 6) {
                hasDouble6 = true;
            }
        }
        return hasDouble6;
    }

    /**
     * Returns the hand information as JSON.
     * @return The hand information as JSON
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public String asJson() {
        String json = "[";
        boolean first = true;
        for (DominoChip chip : chips) {
            if (first) {
                first = false;
            } else {
                json += ",";
            }
            json += chip.asJson();
        }
        json += "]";
        return json;
    }

    /**
     * Removes the matching chip
     * @param chip The chip to remove
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void removeChip(DominoChip chip){
        this.getChips().remove(chip);
    }

    /**
     * Counts the hand value
     * @return The hand count
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public int count() {
        int count = 0;
        for(DominoChip chip : this.chips) {
            count += chip.getFirst() + chip.getSecond();
        }
        return count;
    }
}
