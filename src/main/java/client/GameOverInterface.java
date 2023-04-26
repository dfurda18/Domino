package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Static class that shows the game over information
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/12/2023
 */
public class GameOverInterface extends JPanel {
    /**
     * The modal padding
     */
    private static final int PADDING = 50;
    /**
     * Winner Label
     */
    private JLabel winner;
    /**
     * FirstTeam score
     */
    private JLabel firstTeam;
    /**
     * Second Team score
     */
    private JLabel secondTeam;
    /**
     * The close button
     */
    private JButton closeButton;
    /**
     * The GameOverInterface constructor.
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public GameOverInterface() {
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

        // Add the winner
        winner = new JLabel("Winner...");
        winner.setFont(DominoInterface.gameFont);
        winner.setForeground(Color.WHITE);
        content.add(winner, constraint);

        // Add first team score
        firstTeam = new JLabel("First team 1");
        firstTeam.setFont(DominoInterface.gameFont);
        firstTeam.setForeground(Color.WHITE);
        constraint.gridy++;
        content.add(firstTeam, constraint);

        // Add second team score
        secondTeam = new JLabel("First team 1");
        secondTeam.setFont(DominoInterface.gameFont);
        secondTeam.setForeground(Color.WHITE);
        constraint.gridy++;
        content.add(secondTeam, constraint);

        closeButton = new JButton("Close");
        closeButton.setEnabled(false);
        constraint.gridy++;
        content.add(closeButton, constraint);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JButton)e.getSource()).getParent().getParent().setVisible(false);
                if(DominoClient.checkTurn) {
                    DominoClient.checkTurn();
                }
                DominoClient.gameInterface.update();
            }
        });
    }

    /**
     * Gnages the Waiting Hall text
     * @param winner The winner's team name
     * @param firstName The first team's name
     * @param secondName The secondTeam name
     * @param firstScore The first team's score
     * @param secondScore The second team's score
     * @author Dario Urdapilleta
     * @since 04/12/2023
     */
    public void show(String winner, String firstName, String secondName, int firstScore, int secondScore) {
        this.winner.setText("The winner is: " + winner);
        this.firstTeam.setText(firstName + ": " + firstScore);
        this.secondTeam.setText(secondName + ": " + secondScore);
        if(firstScore > 100 || secondScore > 100) {
            closeButton.setEnabled(false);
        } else {
            closeButton.setEnabled(true);
        }
        this.setVisible(true);
    }

}
