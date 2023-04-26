package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model for a Domino Tiles.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 03/31/2023
 */
public class DominoTiles {
    /**
     * The list of chips
     */
    private List<DominoChip> chips;
    /**
     * The current tile pointer
     */
    private int next;

    /**
     * Constructor for the Domino Tiles
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public DominoTiles() {
        int first, second;
        next = 0;
        chips = new ArrayList<DominoChip>();

        // Create all the chips
        for(first = 0; first < 7; first++) {
            for(second = 0; second <= first; second ++) {
                chips.add(new DominoChip(first, second));
            }
        }
    }

    /**
     * Shuffles the tiles
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public void shuffle() {
        Collections.shuffle(chips);
        next = 0;
    }

    /**
     * Gets a hand from the tileset.
     * @return A list of 7 Domino Chips
     * @author Dario Urdapilleta
     * @since 03/31/2023
     */
    public ArrayList<DominoChip> getHand() {
        ArrayList<DominoChip> hand = new ArrayList<DominoChip>();
        int tile;

        // Get 7 chips
        if(next < chips.size()) {
            for(tile = next; tile < next + 7; tile++) {
                hand.add(chips.get(tile));
            }
            next = tile;
        }
        return hand;
    }
}
