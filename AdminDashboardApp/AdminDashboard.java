import java.awt.*;
import java.util.List;
import javax.swing.*;

public class AdminDashboard extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final AdminController controller;

    public AdminDashboard() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        controller = new AdminController(this);

        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header panel with welcome label and profile button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the admin dashboard", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        JButton profileButton = new JButton("Profile");
        profileButton.addActionListener(e -> showAdminProfile());
        headerPanel.add(profileButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        mainPanel.add(createWelcomePanel(), "Welcome");
        mainPanel.add(createPendingListPanel(), "PendingList");
        mainPanel.add(createBlockedAccountsPanel(), "BlockedAccounts");
        mainPanel.add(createAddToBlockListPanel(), "AddToBlockList");
        mainPanel.add(createRemoveListingPanel(), "RemoveListing");

        add(mainPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton viewPendingButton = new JButton("View Pending Lists");
        JButton viewBlockedButton = new JButton("View Blocked Accounts");
        JButton addToBlockButton = new JButton("Add Account to Block List");
        JButton removeListingButton = new JButton("Remove Listing");

        centerPanel.add(viewPendingButton);
        centerPanel.add(viewBlockedButton);
        centerPanel.add(addToBlockButton);
        centerPanel.add(removeListingButton);

        add(centerPanel, BorderLayout.SOUTH);

        viewPendingButton.addActionListener(e -> controller.handleAction("View Pending Lists"));
        viewBlockedButton.addActionListener(e -> controller.handleAction("View Blocked Accounts"));
        addToBlockButton.addActionListener(e -> controller.handleAction("Add Account to Block List"));
        removeListingButton.addActionListener(e -> controller.handleAction("Remove Listing"));

        cardLayout.show(mainPanel, "Welcome");
    }

    private void showAdminProfile() {
        JFrame profileFrame = new JFrame("Admin Profile");
        profileFrame.setSize(300, 200);
        profileFrame.setLayout(new BorderLayout());

        // Assuming admin details are fetched here. Replace with actual implementation if needed.
        String adminDetails = "Admin ID: 1\nUsername: admin\nEmail: admin@example.com\n";

        JTextArea detailsTextArea = new JTextArea(adminDetails);
        detailsTextArea.setEditable(false);
        profileFrame.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        buttonPanel.add(logoutButton);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        profileFrame.setLocationRelativeTo(this);
        profileFrame.setVisible(true);
    }

    private void logout() {
        // Perform logout actions here. For now, just close the application.
        System.exit(0);
    }

    public void showPendingList() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        List<Listing> listings = controller.getPendingListings();
        for (Listing listing : listings) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            JLabel itemLabel = new JLabel("Pending Item: " + listing.getTitle());
            JButton viewDetailsButton = new JButton("View Details");

            viewDetailsButton.addActionListener(e -> {
                controller.viewPendingItemDetails(listing.getId());
            });

            itemPanel.add(itemLabel);
            itemPanel.add(viewDetailsButton);

            panel.add(itemPanel, gbc);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));

        gbc.gridy = GridBagConstraints.RELATIVE;
        panel.add(backButton, gbc);

        mainPanel.add(panel, "PendingList");
        cardLayout.show(mainPanel, "PendingList");
    }

    public void showBlockedAccounts() {
        cardLayout.show(mainPanel, "BlockedAccounts");
    }

    public void showAddToBlockList() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel instructionLabel = new JLabel("Enter Account ID to Block:");
        JTextField accountIdField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(150, 30));

        searchButton.addActionListener(e -> {
            String accountId = accountIdField.getText();
            controller.showAccountDetails(accountId);
        });

        panel.add(instructionLabel, gbc);
        panel.add(accountIdField, gbc);
        panel.add(searchButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(150, 30));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));

        panel.add(backButton, gbc);

        mainPanel.add(panel, "AddToBlockList");
        cardLayout.show(mainPanel, "AddToBlockList");
    }

    public void showRemoveListing() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel instructionLabel = new JLabel("Enter Item ID to Remove:");
        JTextField itemIdField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(150, 30));

        searchButton.addActionListener(e -> {
            String itemId = itemIdField.getText();
            controller.showListingDetails(itemId);
        });

        panel.add(instructionLabel, gbc);
        panel.add(itemIdField, gbc);
        panel.add(searchButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(150, 30));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));

        panel.add(backButton, gbc);

        mainPanel.add(panel, "RemoveListing");
        cardLayout.show(mainPanel, "RemoveListing");
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the admin dashboard", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPendingListPanel() {
        return new JPanel(); // Placeholder for compilation. Actual implementation done in showPendingList.
    }

    private JPanel createBlockedAccountsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            JLabel accountLabel = new JLabel("Blocked Account " + (index + 1));
            JButton removeButton = new JButton("Remove from Block List");

            removeButton.addActionListener(e -> {
                controller.removeBlockedAccount(index);
            });

            accountPanel.add(accountLabel);
            accountPanel.add(removeButton);

            panel.add(accountPanel, gbc);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));

        gbc.gridy = GridBagConstraints.RELATIVE;
        panel.add(backButton, gbc);

        return panel;
    }

    private JPanel createAddToBlockListPanel() {
        return new JPanel(); // Placeholder for compilation. Actual implementation done in showAddToBlockList.
    }

    private JPanel createRemoveListingPanel() {
        return new JPanel(); // Placeholder for compilation. Actual implementation done in showRemoveListing.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}
