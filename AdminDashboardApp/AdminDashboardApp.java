import javax.swing.SwingUtilities;

public class AdminDashboardApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboardController controller = new AdminDashboardController();
            AdminDashboardView view = new AdminDashboardView(controller);
            view.setVisible(true);
        });
    }
}
