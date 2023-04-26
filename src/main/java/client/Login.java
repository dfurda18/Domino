package client;

import utilities.NetworkProtocols;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Graphical container the login.
 * @author Dario Urdapilleta
 * @version 1.0
 * @since 04/04/2023
 */
public class Login extends JPanel {
    /**
     * The modal padding
     */
    private static final int PADDING = 50;
    /**
     * Static attribute that stores the login text field size
     */
    private static final Dimension textFieldSize = new Dimension(200, 25);
    /**
     * The login field to place the user ID
     */
    private final JTextField userIdField;
    /**
     * The username field
     */
    private final JTextField userNameField;
    /**
     * The server
     */
    private final JTextField serverField;
    /**
     * The login button
     */
    private final JButton loginButton;
    /**
     * The login error message
     */
    private final JLabel errorMessage;

    /**
     * The Login constructor.
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public Login () {
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
        constraint.anchor = GridBagConstraints.PAGE_START;
        constraint.gridx = 0;
        constraint.gridy = 0;

        // Add the user ID
        JLabel userLabel = new JLabel("User ID:");
        userLabel.setFont(DominoInterface.gameFont);
        userLabel.setForeground(Color.WHITE);
        content.add(userLabel, constraint);
        constraint.gridy++;
        userIdField = new JTextField(10);
        userIdField.setPreferredSize(textFieldSize);
        userIdField.setMaximumSize(textFieldSize);
        userIdField.setMinimumSize(textFieldSize);
        content.add(userIdField, constraint);

        // Add the Username
        constraint.gridy++;
        JLabel nameLabel = new JLabel("User Name:");
        nameLabel.setFont(DominoInterface.gameFont);
        nameLabel.setForeground(Color.WHITE);
        content.add(nameLabel, constraint);
        userNameField = new JTextField(10);
        userNameField.setPreferredSize(textFieldSize);
        userNameField.setMaximumSize(textFieldSize);
        userNameField.setMinimumSize(textFieldSize);
        constraint.gridy++;
        content.add(userNameField, constraint);

        // Add the Server IP
        constraint.gridy++;
        JLabel serverLabel = new JLabel("Server IP:");
        serverLabel.setFont(DominoInterface.gameFont);
        serverLabel.setForeground(Color.WHITE);
        content.add(serverLabel, constraint);
        serverField = new JTextField(10);
        serverField.setPreferredSize(textFieldSize);
        serverField.setMaximumSize(textFieldSize);
        serverField.setMinimumSize(textFieldSize);
        constraint.gridy++;
        content.add(serverField, constraint);

        // Add the login button
        loginButton = new JButton("Login");
        loginButton.setEnabled(false);
        constraint.gridy++;
        content.add(loginButton, constraint);

        // Add the Error message
        constraint.gridy++;
        errorMessage = new JLabel("");
        errorMessage.setMinimumSize(new Dimension(DominoInterface.modalSize.width, textFieldSize.height));
        errorMessage.setPreferredSize(new Dimension(DominoInterface.modalSize.width, textFieldSize.height));
        errorMessage.setMaximumSize(new Dimension(DominoInterface.modalSize.width, textFieldSize.height));
        errorMessage.setForeground(Color.WHITE);
        content.add(errorMessage, constraint);


        // Set the textFields functionality
        userIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ((Login)((JTextField)e.getSource()).getParent().getParent()).checkFields();
            }
        });
        userNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ((Login)((JTextField)e.getSource()).getParent().getParent()).checkFields();
            }
        });
        serverField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ((Login)((JTextField)e.getSource()).getParent().getParent()).checkFields();
            }
        });
    }

    /**
     * This method checks the fields and sets the login button accordingly
     * @author Dario Urdapilleta
     * @since 03/24/2023
     */
    public void checkFields () {
        if (userIdField.getText().equals("") || userNameField.getText().equals("") || !(serverField.getText().equals("") || NetworkProtocols.isValidIPAddress(serverField.getText()))) {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }
    }

    /**
     * Sets the login button functionality
     * @param listener The action listener for the Login Button
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public void setLoginOnClick (ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Returns the contents of the user id text field
     * @return the contents of the user id text field
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public String getUserId () {
        return userIdField.getText();
    }

    /**
     * Returns the contents of the server IP text field
     * @return the contents of the server IP text field
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public String getServerIP () {
        return serverField.getText();
    }

    /**
     * Returns the contents of the username text field
     * @return the contents of the username text field
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public String getUserName () {
        return userNameField.getText();
    }

    /**
     * Shows an error message
     * @param message The error message
     * @author Dario Urdapilleta
     * @since 04/04/2023
     */
    public void showError (String message) {
        errorMessage.setText("<html>"+ message +"</html>");
    }
}
