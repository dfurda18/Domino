package client;

import javax.swing.*;
import java.awt.*;

/**
 * Static class that shows the player's list and its interactions
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/04/2023
 */
public class WaitingHall extends JPanel {
    /**
     * The modal padding
     */
    private static final int PADDING = 50;
    /**
     * Text label
     */
    private JLabel label;
    /**
     * The WaitingHall constructor.
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public WaitingHall() {
        super();

        // Set the component
        this.setBounds((DominoInterface.screenSize.width - DominoInterface.modalSize.width) / 2,
                (DominoInterface.screenSize.height - DominoInterface.modalSize.height) / 2,
                DominoInterface.modalSize.width,
                DominoInterface.modalSize.height
        );
        this.setOpaque(false);
        this.setLayout(null);

        // Add the content container
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        content.setBounds(  PADDING,
                            PADDING,
                            DominoInterface.modalSize.width - (2 * PADDING),
                            DominoInterface.modalSize.height - (2 * PADDING));
        content.setOpaque(false);
        this.add(content);

        // Add the background
        JLabel background = new JLabel(new ImageIcon(DominoInterface.class.getClassLoader().getResource("images/Modal.png")));
        background.setBounds(0,0, DominoInterface.modalSize.width, DominoInterface.modalSize.height);
        this.add(background);

        // Set the constraints
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(0,0,10,0);
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.gridx = 0;
        constraint.gridy = 0;

        // Add the user ID
        label = new JLabel("Waiting for a match to start...");
        label.setFont(DominoInterface.gameFont);
        label.setForeground(Color.WHITE);
        content.add(label, constraint);
    }

    /**
     * Gnages the Waiting Hall text
     * @param text The new text to display
     * @author Dario Urdapilleta
     * @since 04/05/2023
     */
    public void setText(String text) {
        this.label.setText(text);
    }

}
