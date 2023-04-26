package client;

import model.DominoChip;
import model.DominoHand;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Model for a Domino Player hand.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/06/2023
 */
public class PlayerHandView extends JPanel {
    /**
     * The orientation enumerator
     */
    public enum Orientation {SOUTH, WEST, NORTH, EAST};
    /**
     * The list of tiles
     */
    public DominoChipView[] tiles;
    /**
     * The player
     */
    public Player player;
    /**
     * Whether the chip is vertical
     */
    private boolean isVertical;
    /**
     * The player name's label
     */
    private JLabel playerName;

    /**
     * Creates a player interface element depending on the orientation.
     * @param orientation The player orientation 0 = SOUTH, 1 = WEST, 2 = NORTH, 3 = EAST
     * @param player The player.
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public PlayerHandView (Orientation orientation, Player player) {
        super();

        // set the current object attributes
        this.player = player;
        this.setOpaque(false);

        // Create the player name label
        playerName = new JLabel(player.getName());
        playerName.setFont(DominoInterface.gameFont);
        playerName.setForeground(Color.WHITE);

        switch(orientation) {
            case SOUTH:
            case NORTH:
                this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
                isVertical = false;
                break;
            case EAST:
            case WEST:
                this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                isVertical = true;
                break;
        }
        playerName.setPreferredSize(DominoChipView.sizeR);
        playerName.setMaximumSize(DominoChipView.sizeR);
        playerName.setMinimumSize(DominoChipView.sizeR);
        this.add(playerName);

        // Create the tiles
        tiles = new DominoChipView[7];
        for (int tile = 0; tile < 7; tile++) {
            tiles[tile] = new DominoChipView(!isVertical);
            //tiles[tile].setVisible(false);
            this.add(tiles[tile]);
        }
    }

    /**
     * Replaces the empty chips with an actual hand.
     * @param hand The hand to show
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void showHand(DominoHand hand) {
        PlayerHandView curerntObject = this;
        for (int tile = 0; tile < 7; tile++) {
            tiles[tile].setChip(hand.getChip(tile));
            tiles[tile].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DominoClient.selectedChip = ((DominoChipView)e.getSource()).getChip();
                    curerntObject.deselect();
                    ((DominoChipView)e.getSource()).setOpaque(true);
                    GameAreaView.showButtons(DominoClient.selectedChip);
                    DominoClient.gameInterface.update();
                }
            });
        }
    }

    /**
     * Removes a chip from the list of chips.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void removeChip () {
        tiles[tiles.length - 1].setVisible(false);
        this.remove(this.countComponents() - 1);
    }

    /**
     * Plays a chip from the list of chips.
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void playChip (DominoChip chip) {
        for(DominoChipView chipView : tiles) {
            if(chip.getFirst() == chipView.getChip().getFirst() && chip.getSecond() == chipView.getChip().getSecond()) {
                this.remove(chipView);
            }
        }
    }

    /**
     * Deselects all tiles from the hand
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void deselect() {
        for (DominoChipView chipView : tiles) {
            chipView.setOpaque(false);
        }
    }

    /**
     * Lets the interface know it is the player's turn
     * @author Dario Urdapilleta
     * @since 04/13/2023
     */
    public void yourTurn() {
        playerName.setForeground(Color.MAGENTA);
    }

    /**
     * Lets the interface know it is not the player's turn
     * @author Dario Urdapilleta
     * @since 04/13/2023
     */
    public void notYourTurn() {
        playerName.setForeground(Color.WHITE);
    }

}
