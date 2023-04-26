package client;

import javax.swing.*;
import java.awt.*;

/**
 * Model for a Domino game score.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/06/2023
 */
public class ScoreView extends JPanel {
    /**
     * The first team's score
     */
    private static JLabel team1Score;
    /**
     * The second team's score
     */
    private static JLabel team2Score;

    /**
     * Builds a new interface for the game score
     * @param team1 The first team name
     * @param team2 The second team name
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public ScoreView(String team1, String team2) {
        super();

        // Set this object's attributes
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        // Set the constraints
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(0,0,5,5);
        constraint.anchor = GridBagConstraints.PAGE_START;
        constraint.gridx = 0;
        constraint.gridy = 0;

        // Add the first team Label
        JLabel team1Name = new JLabel(team1);
        team1Name.setFont(DominoInterface.gameFont);
        team1Name.setForeground(Color.WHITE);
        this.add(team1Name, constraint);

        // add the first team score
        team1Score = new JLabel("0");
        team1Score.setFont(DominoInterface.gameFont);
        team1Score.setForeground(Color.WHITE);
        constraint.gridx++;
        this.add(team1Score, constraint);

        // Add the first team Label
        JLabel team2Name = new JLabel(team2);
        team2Name.setFont(DominoInterface.gameFont);
        team2Name.setForeground(Color.WHITE);
        constraint.gridy++;
        constraint.gridx = 0;
        this.add(team2Name, constraint);

        // add the first team score
        team2Score = new JLabel("0");
        team2Score.setFont(DominoInterface.gameFont);
        team2Score.setForeground(Color.WHITE);
        constraint.gridx++;
        this.add(team2Score, constraint);
    }

    /**
     * Refreshes the game scores
     * @param team1Score The first team's score
     * @param team2Score The second team's score
     * @author Dario Urdapilleta
     * @since 04/06/2023
     */
    public void setScores(int team1Score, int team2Score) {
        ScoreView.team1Score.setText(Integer.toString(team1Score));
        ScoreView.team2Score.setText(Integer.toString(team2Score));
    }
}
