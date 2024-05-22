

public class UserController {
    private UserModel userModel;
    private LoginView loginView;
    private UserRegistrationForm registrationView;
    private MainView mainView;

    public UserController(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setRegistrationView(UserRegistrationForm registrationView) {
        this.registrationView = registrationView;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void showLoginView() {
        loginView.setVisible(true);
        if (registrationView != null) {
            registrationView.setVisible(false);
        }
    }

    public void showRegistrationView() {
        registrationView.setVisible(true);
        if (loginView != null) {
            loginView.setVisible(false);
        }
    }

    public void showMainView() {
        mainView.setVisible(true);
        if (loginView != null) {
            loginView.setVisible(false);
        }
        if (registrationView != null) {
            registrationView.setVisible(false);
        }
    }

    public String registerUser(String username, String email, String password, String confirmPassword, boolean termsAccepted, String userType) {
        if (!termsAccepted) {
            return "You must accept the terms and conditions.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        boolean success = userModel.createUserAccount(username, email, password);
        if (success) {
            showLoginView();
            return "Registration successful. Please log in.";
        } else {
            return "Username or email already exists.";
        }
    }

    public String loginUser(String username, String password) {
        User user = userModel.getUserByUsername(username);
        if (user != null && user.authenticate(username, password)) {
            showMainView();
            return "Login successful.";
        } else {
            return "Invalid username or password.";
        }
    }
}
