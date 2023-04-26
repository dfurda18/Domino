package client;

import model.DominoChip;
import model.DominoLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Model for a Domino game area.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/06/2023
 */
public class GameAreaView extends JPanel {
    /**
     * TileButton size
     */
    private static final Dimension tileButtonSize = new Dimension(97, 92);
    /**
     * Enum for the directions
     */
    private enum TileDirection {WEST, NORTH, EAST, SOUTH}
    /**
     * The distance along the tile length
     */
    private static final int LONG_GAP = 51;
    /**
     * The distance alont the tile width
     */
    private static final int SHORT_GAP = 26;
    /**
     * The first side button
     */
    private static JLabel firstLabel;
    /**
     * The last side button
     */
    private static JLabel lastLabel;
    /**
     * The domino line
     */
    private static DominoLine tempLine;

    /**
     * Builds a new interface for the game area
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public GameAreaView () {
        super();
        this.setLayout(null);
        this.setOpaque(false);
        tempLine = new DominoLine();

        // Set the tile buttons
        firstLabel = new JLabel(new ImageIcon(DominoChip.class.getClassLoader().getResource("images/TileButton.png")));
        firstLabel.setOpaque(false);
        firstLabel.setVisible(false);
        lastLabel = new JLabel(new ImageIcon(DominoChip.class.getClassLoader().getResource("images/TileButton.png")));
        lastLabel.setOpaque(false);
        lastLabel.setVisible(false);
    }

    /**
     * Sets the tile buttons for the first time
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void setTileButtons() {
        // Set the select buttons
        firstLabel.setBounds(this.getWidth() / 2 - tileButtonSize.width / 2,
                this.getHeight() / 2 - tileButtonSize.height / 2,
                tileButtonSize.width,
                tileButtonSize.height);
        lastLabel.setBounds(this.getWidth() / 2 - tileButtonSize.width / 2,
                this.getHeight() / 2 - tileButtonSize.height / 2,
                tileButtonSize.width,
                tileButtonSize.height);
        firstLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DominoClient.playChip(true);
            }
        });
        lastLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DominoClient.playChip(false);
            }
        });
        this.add(firstLabel);
        this.add(lastLabel);
    }

    /**
     * Updates the game area with the specified line
     * @param line The line
     * @author Dario Urdapilleta
     * @since 04/11/2023
     */
    public void loadLine(ArrayList<DominoChip> line) {
        DominoChipView chipView;
        DominoChip tempChip;
        boolean chipViewVertical = true;
        boolean firstChip = true;
        boolean mustFlip = false;
        int x, y, width, height, xGap, yGap;
        Dimension first = new Dimension(this.getWidth() / 2, this.getHeight() / 2);
        Dimension last = new Dimension(this.getWidth() / 2, this.getHeight() / 2);
        TileDirection firstDirection = TileDirection.WEST;
        TileDirection lastDirection = TileDirection.EAST;
        tempLine = new DominoLine();
        this.removeAll();
        for(DominoChip chip : line) {
            mustFlip = false;

            // Check if there's room for the next chip and adjust the place for mules
            if(chip.isPlacedFirst()) {

                // For the first side
                switch(firstDirection) {
                    case WEST -> {
                        if(first.width < 3 * (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            firstDirection = TileDirection.NORTH;
                            first.height = first.height - SHORT_GAP;
                            first.width = first.width + SHORT_GAP;
                        }
                        first.width = first.width + (chip.isMule() && !firstChip ? SHORT_GAP : 0);
                    }
                    case EAST -> {
                        if(first.width > this.getWidth() - 2 * (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            firstDirection = TileDirection.SOUTH;
                            first.width = first.width - SHORT_GAP;
                            first.height = first.height + SHORT_GAP;
                        }
                        first.width = first.width - (chip.isMule() ? SHORT_GAP : 0);
                    }
                    case NORTH -> {
                        if(first.height < 3 * (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            firstDirection = TileDirection.EAST;
                            first.width = first.width + SHORT_GAP;
                            first.height = first.height + SHORT_GAP;
                        }
                        first.height = first.height + (chip.isMule() ? SHORT_GAP : 0);
                    }
                    case SOUTH -> {
                        if(first.height > this.getHeight() - 2 * (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            firstDirection = TileDirection.WEST;
                            first.width = first.width - SHORT_GAP;
                            first.height = first.height - SHORT_GAP;
                        }
                        first.height = first.height - (chip.isMule() ? SHORT_GAP : 0);
                    }
                }

                // For the last side
            } else {
                switch(lastDirection) {
                    case WEST -> {
                        if(last.width < (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            lastDirection = TileDirection.NORTH;
                            last.height = last.height - SHORT_GAP;
                            last.width = last.width + SHORT_GAP;
                        }
                        last.width = last.width + (chip.isMule() ? SHORT_GAP : 0);
                    }
                    case EAST -> {
                        if(last.width > this.getWidth() - 4 * (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            lastDirection = TileDirection.SOUTH;
                            last.width = last.width - SHORT_GAP;
                            last.height = last.height + SHORT_GAP;
                        }
                        last.width = last.width - (chip.isMule() && !firstChip ? SHORT_GAP : 0);
                    }
                    case NORTH -> {
                        if(last.height < 2 * (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            lastDirection = TileDirection.EAST;
                            last.width = last.width + SHORT_GAP;
                            last.height = last.height + SHORT_GAP;
                        }
                        last.height = last.height + (chip.isMule() ? SHORT_GAP : 0);
                    }
                    case SOUTH -> {
                        if(last.height > this.getHeight() - 4 * (chip.isMule() ? SHORT_GAP : LONG_GAP)) {
                            lastDirection = TileDirection.WEST;
                            last.width = last.width - SHORT_GAP;
                            last.height = last.height - SHORT_GAP;
                        }
                        last.height = last.height - (chip.isMule() ? SHORT_GAP : 0);
                    }
                }
            }

            // Flip vertically or horizontally depending on the direction and if they are mules
            if(chip.isPlacedFirst()) {
                if(firstDirection == TileDirection.NORTH || firstDirection == TileDirection.SOUTH) {
                    chipViewVertical = !chip.isMule();
                } else {
                    chipViewVertical = chip.isMule();
                }
            } else {
                if (lastDirection == TileDirection.NORTH || lastDirection == TileDirection.SOUTH) {
                    chipViewVertical = !chip.isMule();
                } else {
                    chipViewVertical = chip.isMule();
                }
            }

            // Check if the chip must be flipped to match the line
            if(chip.isPlacedFirst() && !tempLine.isEmpty()) {
                switch (firstDirection) {
                    case WEST, NORTH -> {
                        if(tempLine.getFirst() != chip.getSecond()) {
                            mustFlip = true;
                        }
                    }
                    case EAST, SOUTH -> {
                        if(tempLine.getFirst() != chip.getFirst()) {
                            mustFlip = true;
                        }
                    }
                }
            } else if (!tempLine.isEmpty()) {
                switch(lastDirection) {
                    case WEST, NORTH -> {
                        if(tempLine.getLast() != chip.getSecond()) {
                            mustFlip = true;
                        }
                    }
                    case EAST, SOUTH -> {
                        if(tempLine.getLast() != chip.getFirst()) {
                            mustFlip = true;
                        }
                    }
                }
            }

            // Create the chip
            chipView = new DominoChipView(chipViewVertical);

            // Flip if necessary
            if( mustFlip ) {
                tempChip = new DominoChip(chip.getSecond(), chip.getFirst());
                tempChip.setPlaceFirst(chip.isPlacedFirst());
                chipView.setChip(tempChip);
                tempLine.playChip(tempChip);
            } else {
                chipView.setChip(chip);
                tempLine.playChip(chip);
            }

            // Adjust the position depending on the orientation
            xGap = chipViewVertical ? - SHORT_GAP : - SHORT_GAP * 2;
            yGap = chipViewVertical ? - SHORT_GAP * 2 : - SHORT_GAP;

            // Get the distance adjustment
            x = (chip.isPlacedFirst() ? first.width : last.width) + xGap;
            y = (chip.isPlacedFirst() ? first.height : last.height) + yGap;
            if(x < 0) {
                x = 0;
            }
            if(y < 0) {
                y = 0;
            }
            width = chipViewVertical ? LONG_GAP : LONG_GAP * 2;
            height = chipViewVertical ? LONG_GAP * 2 : LONG_GAP;

            // Position the chip and add it
            chipView.setBounds(x, y, width, height);
            this.add(chipView);

            // Adjust first and last locations for the next chip
            xGap = 0;
            yGap = 0;
            if(chip.isPlacedFirst()) {
                switch(firstDirection) {
                    case WEST -> {
                        xGap = -(chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                    case EAST -> {
                        xGap = (chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                    case NORTH -> {
                        yGap = -(chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                    case SOUTH -> {
                        yGap = (chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                }
                first.setSize(first.width + xGap, first.height + yGap);
                if(firstChip) {
                    yGap = 0;
                    xGap = (chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    last.setSize(last.width + xGap, last.height + yGap);
                }
            } else {
                switch(lastDirection) {
                    case WEST -> {
                        xGap = -(chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                    case EAST -> {
                        xGap = (chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                    case NORTH -> {
                        yGap = -(chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                    case SOUTH -> {
                        yGap = (chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    }
                }
                last.setSize(last.width + xGap, last.height + yGap);
                if(firstChip) {
                    yGap = 0;
                    xGap = -(chip.isMule() ? SHORT_GAP * 3 : SHORT_GAP * 4);
                    first.setSize(first.width + xGap, first.height + yGap);
                }
            }
            firstChip = false;
        }

        // Set the select buttons
        firstLabel.setBounds(first.width - tileButtonSize.width / 2,
                first.height - tileButtonSize.height / 2,
                tileButtonSize.width,
                tileButtonSize.height);
        lastLabel.setBounds(last.width - tileButtonSize.width / 2,
                last.height - tileButtonSize.height / 2,
                tileButtonSize.width,
                tileButtonSize.height);
        this.add(firstLabel);
        this.add(lastLabel);
    }

    /**
     * Adjust the interface after a chip was played
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void chipWasPlayed () {
        firstLabel.setVisible(false);
        lastLabel.setVisible(false);
    }

    /**
     * Shows the line select buttons
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public static void showButtons(DominoChip chip) {
        firstLabel.setVisible(false);
        lastLabel.setVisible(false);
        if(tempLine.isEmpty() && DominoClient.firstGame) {
            if(chip.getFirst() == 6 && chip.getSecond() == 6) {
                firstLabel.setVisible(true);
            }
        }
        if((tempLine.isEmpty() && !DominoClient.firstGame) || tempLine.canPlayFirst(chip, DominoClient.firstGame)) {
            firstLabel.setVisible(true);
        }
        if((tempLine.isEmpty() && !DominoClient.firstGame) || tempLine.canPlayLast(chip, DominoClient.firstGame)) {
            lastLabel.setVisible(true);
        }
    }

    /**
     * Resets the gameplay
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void reset() {
        tempLine.clear();
    }
}
