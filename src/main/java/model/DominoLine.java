package model;

import client.DominoClient;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Model for a Domino Line.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/11/2023
 */
public class DominoLine {
    /**
     * The Domino Game line
     */
    private LinkedList<Integer> line;
    /**
     * The list of chips
     */
    private ArrayList<DominoChip> chips;

    /**
     * Constructor for the Domino Line
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoLine () {
        line = new LinkedList<Integer>();
        chips = new ArrayList<DominoChip>();
    }

    /**
     * Clears the line
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void clear () {
        chips.clear();
        line.clear();
    }

    /**
     * Returns whether the line has different ends
     * @return True if the line has different ends and false otherwise
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean hasDifferentEnds() {
        return line.isEmpty() ? false : line.getFirst() != line.getLast();
    }

    /**
     * Checks if a hand can play a chip
     * @param hand The hand to check
     * @return True if there's at least one chip that can be played
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean canPlayAny(DominoHand hand) {
        for (DominoChip chip : hand.getChips()) {
            if(this.canPlay(chip, DominoClient.firstGame)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a chip can be played
     * @param chip The chip to be played
     * @return True if the chip can be played and false otherwise.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean canPlay(DominoChip chip, boolean firstGame) {
        if (!firstGame && line.isEmpty()) {
            // The player can start with any chip
            return true;
        } else if(line.isEmpty()) {
            // If it's the first chip, it must be the mule of sixes
            return chip.getFirst() == 6 && chip.getSecond() == 6;
        }
        if(chip.getFirst() == line.getFirst() ||
                chip.getSecond() == line.getFirst() ||
                chip.getFirst() == line.getLast() ||
                chip.getSecond() == line.getLast()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tells whether the player can play on the first end
     * @param chip The chip to play
     * @param firstGame True if it is the first game
     * @return True if the player can play in the first end, and folse otherwise.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public boolean canPlayFirst (DominoChip chip, boolean firstGame) {
        if (!firstGame && line.isEmpty()) {
            // The player can start with any chip
            return true;
        } else if(line.isEmpty()) {
            // If it's the first chip, it must be the mule of sixes
            return chip.getFirst() == 6 && chip.getSecond() == 6;
        }
        if(chip.getFirst() == line.getFirst() ||
                chip.getSecond() == line.getFirst()) {
            return true;
        } else {
            return false;
        }
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
        if (!firstGame && line.isEmpty()) {
            // The player can start with any chip
            return true;
        } else if(line.isEmpty()) {
            // If it's the first chip, it must be the mule of sixes
            return chip.getFirst() == 6 && chip.getSecond() == 6;
        }
        if(chip.getFirst() == line.getLast() ||
                chip.getSecond() == line.getLast()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Plays a specified chip on the specified side
     * @param chip The chip to play
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playChip(DominoChip chip) {
        if(line.isEmpty()) {
            line.addFirst(chip.getFirst());
            line.addLast(chip.getSecond());
        } else if(chip.isPlacedFirst()) {
            if(line.getFirst() == chip.getFirst()) {
                line.addFirst(chip.getSecond());
            } else {
                line.addFirst(chip.getFirst());
            }
        } else {
            if(line.getLast() == chip.getFirst()) {
                line.addLast(chip.getSecond());
            } else {
                line.addLast(chip.getFirst());
            }
        }
        chips.add(chip);
    }
    /**
     * Plays a specified chip anywhere it can
     * @param chip The chip to play
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playChipCPU(DominoChip chip) {
        if(line.isEmpty()) {
            line.addFirst(chip.getFirst());
            line.addLast(chip.getSecond());
            chip.setPlaceFirst(true);
        } else if(line.getFirst() == chip.getFirst()) {
            line.addFirst(chip.getSecond());
            chip.setPlaceFirst(true);
        } else if (line.getFirst() == chip.getSecond()) {
            line.addFirst(chip.getFirst());
            chip.setPlaceFirst(true);
        } else if (line.getLast() == chip.getFirst()) {
            line.addLast(chip.getSecond());
            chip.setPlaceFirst(false);
        } else {
            line.addLast(chip.getFirst());
            chip.setPlaceFirst(false);
        }
        chips.add(chip);
    }

    /**
     * Returns the line as Json
     * @return The line as a JSON
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
     * Loads the line from a list of chips
     * @param chips The list of chips
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void update(ArrayList<DominoChip> chips) {
        for(DominoChip chip : chips) {
            this.playChip(chip);
        }
    }

    /**
     * Gets the number in the first end
     * @return the number in the first end
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public int getFirst() {
        return line.getFirst();
    }

    /**
     * Gets the number in the last end
     * @return the number in the last end
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public int getLast() {
        return line.getLast();
    }

    /**
     * Returns whether the line is empty.
     * @return True if the line is empty and fase otherwise.
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public boolean isEmpty() {
        return line.isEmpty();
    }
}
