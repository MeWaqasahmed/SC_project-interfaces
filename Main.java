

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UserModel userModel = new UserModel();
        UserController userController = new UserController(userModel);

        // Create views
        LoginView loginView = new LoginView(userController);
        UserRegistrationForm registrationView = new UserRegistrationForm(userController);
        MainView mainView = new MainView();

        // Set views in the controller
        userController.setLoginView(loginView);
        userController.setRegistrationView(registrationView);
        userController.setMainView(mainView);

       
        SwingUtilities.invokeLater(() -> loginView.setVisible(true));
    }
}
