import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    private final AdminDashboard dashboard;
    private final List<String> blockedAccounts;
    private final List<Listing> pendingListings;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/admin";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "root";

    public AdminController(AdminDashboard dashboard) {
        this.dashboard = dashboard;
        this.blockedAccounts = new ArrayList<>();
        this.pendingListings = new ArrayList<>();
    }

    public List<String> getBlockedAccounts() {
        return blockedAccounts;
    }

    public List<Listing> getPendingListings() {
        return pendingListings;
    }

    public void blockAccount(String accountId) {
        blockedAccounts.add(accountId);
        JOptionPane.showMessageDialog(dashboard, "Account ID: " + accountId + " has been blocked.");
        addAccountToBlockedAccountsTable(accountId);
    }

    private void addAccountToBlockedAccountsTable(String accountId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO blocked_accounts (account_id) VALUES (?)")) {
            stmt.setString(1, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBlockedAccount(int index) {
        String accountId = blockedAccounts.get(index);
        blockedAccounts.remove(index);
        JOptionPane.showMessageDialog(dashboard, "Blocked Account ID: " + accountId + " has been removed.");
        removeAccountFromBlockedAccountsTable(accountId);
    }

    private void removeAccountFromBlockedAccountsTable(String accountId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM blocked_accounts WHERE account_id = ?")) {
            stmt.setString(1, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeListing(String itemId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM listings WHERE id = ?")) {
            stmt.setString(1, itemId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(dashboard, "Item ID: " + itemId + " has been removed.");
            } else {
                JOptionPane.showMessageDialog(dashboard, "Item ID: " + itemId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPendingItemDetails(String itemId) {
        String details = fetchItemDetailsFromSellerTable(itemId);
        if (!details.isEmpty()) {
            JFrame detailsFrame = new JFrame("Item Details");
            detailsFrame.setSize(400, 300);
            detailsFrame.setLayout(new BorderLayout());

            JTextArea detailsTextArea = new JTextArea(details);
            detailsTextArea.setEditable(false);
            detailsFrame.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton approveButton = new JButton("Approve");
            JButton rejectButton = new JButton("Reject");
            buttonPanel.add(approveButton);
            buttonPanel.add(rejectButton);

            approveButton.addActionListener(e -> {
                updateListingStatus(itemId, "Approved");
                detailsFrame.dispose();
            });

            rejectButton.addActionListener(e -> {
                updateListingStatus(itemId, "Rejected");
                detailsFrame.dispose();
            });

            detailsFrame.add(buttonPanel, BorderLayout.SOUTH);
            detailsFrame.setLocationRelativeTo(dashboard);
            detailsFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(dashboard, "Details for Item ID " + itemId + " not found.");
        }
    }

    private String fetchItemDetailsFromSellerTable(String itemId) {
        StringBuilder details = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM listings WHERE id = ?")) {
            stmt.setString(1, itemId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                details.append("ID: ").append(rs.getString("id")).append("\n");
                details.append("Title: ").append(rs.getString("title")).append("\n");
                details.append("Description: ").append(rs.getString("description")).append("\n");
                details.append("Status: ").append(rs.getString("status")).append("\n");
                details.append("Seller ID: ").append(rs.getString("seller_id")).append("\n");
                details.append("Duration: ").append(rs.getString("duration")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details.toString();
    }

    private void updateListingStatus(String itemId, String status) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE listings SET status = ? WHERE id = ?")) {
            stmt.setString(1, status);
            stmt.setString(2, itemId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(dashboard, "Item ID: " + itemId + " has been " + status.toLowerCase() + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleAction(String actionCommand) {
        switch (actionCommand) {
            case "View Pending Lists":
                fetchPendingListings();
                dashboard.showPendingList();
                break;
            case "View Blocked Accounts":
                fetchBlockedAccounts();
                dashboard.showBlockedAccounts();
                break;
            case "Add Account to Block List":
                dashboard.showAddToBlockList();
                break;
            case "Remove Listing":
                dashboard.showRemoveListing();
                break;
            default:
                break;
        }
    }

    private void fetchPendingListings() {
        pendingListings.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM listings WHERE status = 'Pending'")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pendingListings.add(new Listing(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getString("seller_id"),
                    rs.getString("duration")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchBlockedAccounts() {
        blockedAccounts.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM blocked_accounts")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                blockedAccounts.add(rs.getString("account_id")); // Adjust column name as necessary
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String fetchAccountDetails(String accountId) {
        StringBuilder details = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stmt.setString(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                details.append("ID: ").append(rs.getString("id")).append("\n");
                details.append("Username: ").append(rs.getString("username")).append("\n");
                details.append("Email: ").append(rs.getString("email")).append("\n");
                details.append("User Type: ").append(rs.getString("user_type")).append("\n");
                details.append("Created At: ").append(rs.getString("created_at")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details.toString();
    }

    public void showAccountDetails(String accountId) {
        String details = fetchAccountDetails(accountId);
        if (!details.isEmpty()) {
            JFrame detailsFrame = new JFrame("Account Details");
            detailsFrame.setSize(400, 300);
            detailsFrame.setLayout(new BorderLayout());

            JTextArea detailsTextArea = new JTextArea(details);
            detailsTextArea.setEditable(false);
            detailsFrame.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton blockButton = new JButton("Block");
            JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(blockButton);
            buttonPanel.add(cancelButton);

            blockButton.addActionListener(e -> {
                blockAccount(accountId);
                detailsFrame.dispose();
            });

            cancelButton.addActionListener(e -> detailsFrame.dispose());

            detailsFrame.add(buttonPanel, BorderLayout.SOUTH);
            detailsFrame.setLocationRelativeTo(dashboard);
            detailsFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(dashboard, "Account ID: " + accountId + " not found.");
        }
    }

    public String fetchListingDetails(String itemId) {
        StringBuilder details = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM listings WHERE id = ?")) {
            stmt.setString(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                details.append("ID: ").append(rs.getString("id")).append("\n");
                details.append("Title: ").append(rs.getString("title")).append("\n");
                details.append("Description: ").append(rs.getString("description")).append("\n");
                details.append("Status: ").append(rs.getString("status")).append("\n");
                details.append("Seller ID: ").append(rs.getString("seller_id")).append("\n");
                details.append("Duration: ").append(rs.getString("duration")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details.toString();
    }

    public void showListingDetails(String itemId) {
        String details = fetchListingDetails(itemId);
        if (!details.isEmpty()) {
            JFrame detailsFrame = new JFrame("Listing Details");
            detailsFrame.setSize(400, 300);
            detailsFrame.setLayout(new BorderLayout());

            JTextArea detailsTextArea = new JTextArea(details);
            detailsTextArea.setEditable(false);
            detailsFrame.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton removeButton = new JButton("Remove");
            JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(removeButton);
            buttonPanel.add(cancelButton);

            removeButton.addActionListener(e -> {
                removeListing(itemId);
                detailsFrame.dispose();
            });

            cancelButton.addActionListener(e -> detailsFrame.dispose());

            detailsFrame.add(buttonPanel, BorderLayout.SOUTH);
            detailsFrame.setLocationRelativeTo(dashboard);
            detailsFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(dashboard, "Item ID: " + itemId + " not found.");
        }
    }
}
