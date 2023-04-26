package client;

import model.DominoChip;

import javax.swing.*;
import java.awt.*;

/**
 * Model for a Domino Chip.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/06/2023
 */
public class DominoChipView extends JPanel {
    /**
     * The chip size
     */
    public static final Dimension size = new Dimension(60, 120);
    /**
     * The chip size rotated
     */
    public static final Dimension sizeR = new Dimension(120, 60);
    /**
     * The chip Label
     */
    private JLabel label;
    /**
     * Whether the chip is vertical
     */
    private boolean isVertical;
    /**
     * The logical representation of the chip
     */
    private DominoChip chip;

    /**
     * Creates a domino chip interface element.
     * @param isVertical True if the chio is vertical
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public DominoChipView (boolean isVertical) {
        super();

        // Set all the current element information
        this.isVertical = isVertical;
        this.setLayout(null);
        this.setOpaque(false);
        this.setBackground(Color.BLUE);
        this.setPreferredSize(isVertical ? size : sizeR);
        this.setMinimumSize(isVertical ? size : sizeR);
        this.setMaximumSize(isVertical ? size : sizeR);
        this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        this.setChip(new DominoChip());
    }

    /**
     * sets the tile wo be a new chip
     * @param chip The new chip
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void setChip(DominoChip chip) {
        this.removeAll();
        this.chip = chip;
        // Create the label
        if(chip.getFirst() == -1 || chip.getSecond() == -1) {
            label = new JLabel(new ImageIcon(DominoChip.class.getClassLoader().getResource("images/Back" + (isVertical ? "" : "r") +".png")));
        } else {
            label = new JLabel(new ImageIcon(DominoChip.class.getClassLoader().getResource("images/" + chip.getFirst() + chip.getSecond() + (isVertical ? "" : "r") +".png")));
        }

        // position the label
        label.setBounds(
                isVertical ? -5 : -10,
                isVertical ? -10 : -5,
                (isVertical ? size.width : sizeR.width),
                (isVertical ? size.height : sizeR.height)
        );
        label.setOpaque(false);
        this.add(label);
    }

    /**
     * Returns the chip
     * @return The chip
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public DominoChip getChip() {
        return chip;
    }
}
