

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserRegistrationForm extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox termsCheckBox;
    private JRadioButton adminRadioButton;
    private JRadioButton sellerRadioButton;
    private JRadioButton buyerRadioButton;
    private JLabel statusLabel;
    private UserController userController;

    public UserRegistrationForm(UserController userController) {
        this.userController = userController;
        setTitle("User Registration");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        termsCheckBox = new JCheckBox("I agree to the terms and conditions.");
        adminRadioButton = new JRadioButton("Admin");
        sellerRadioButton = new JRadioButton("Seller");
        buyerRadioButton = new JRadioButton("Buyer");
        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(adminRadioButton);
        userTypeGroup.add(sellerRadioButton);
        userTypeGroup.add(buyerRadioButton);
        buyerRadioButton.setSelected(true);
        JButton registerButton = new JButton("Register");
        statusLabel = new JLabel();

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Username:"), constraints);

        constraints.gridx = 1;
        add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(new JLabel("Email:"), constraints);

        constraints.gridx = 1;
        add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new JLabel("Password:"), constraints);

        constraints.gridx = 1;
        add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        add(new JLabel("Confirm Password:"), constraints);

        constraints.gridx = 1;
        add(confirmPasswordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        add(new JLabel("User Type:"), constraints);

        JPanel userTypePanel = new JPanel();
        userTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        userTypePanel.add(adminRadioButton);
        userTypePanel.add(sellerRadioButton);
        userTypePanel.add(buyerRadioButton);
        constraints.gridx = 1;
        add(userTypePanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        add(termsCheckBox, constraints);

        constraints.gridx = 1;
        constraints.gridy = 6;
        add(registerButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 7;
        add(statusLabel, constraints);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        boolean termsAccepted = termsCheckBox.isSelected();
        String userType = "";

        if (adminRadioButton.isSelected()) {
            userType = "Admin";
        } else if (sellerRadioButton.isSelected()) {
            userType = "Seller";
        } else if (buyerRadioButton.isSelected()) {
            userType = "Buyer";
        } else {
            statusLabel.setText("Please select user type.");
            return;
        }

        String message = userController.registerUser(username, email, password, confirmPassword, termsAccepted, userType);
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        UserController userController = new UserController(new UserModel());
        UserRegistrationForm registrationView = new UserRegistrationForm(userController);
        LoginView loginView = new LoginView(userController);
        MainView mainView = new MainView();

        userController.setRegistrationView(registrationView);
        userController.setLoginView(loginView);
        userController.setMainView(mainView);

        SwingUtilities.invokeLater(() -> loginView.setVisible(true));
    }
}
