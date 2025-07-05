package biblioteka.gui.controller;

import biblioteka.gui.LibraryRepository;
import biblioteka.gui.User; // << Import User from core
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the login and registration screen.
 * Handles user authentication (login) and registration (creating new users).
 * Interacts with the {@link LibraryRepository} to manage user data and
 * navigates to the main application screen upon successful login.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see User
 * @see MainViewController
 */
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public static User loggedInUser = null; // Static field to hold logged-in user session-wide

    @FXML
    public void initialize() {
        // Clear previous error messages
        errorLabel.setText("");
        // Auto-focus username field
        usernameField.requestFocus();
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText(); // No trim on password

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        for (User user : LibraryRepository.getUsers()) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                loggedInUser = user;
                System.out.println("User logged in: " + user.getUsername() + ", isAdmin: " + user.isAdmin());
                goToMainView(event);
                return;
            }
        }
        errorLabel.setText("Invalid username or password.");
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password for registration cannot be empty.");
            return;
        }
        // Basic validation
        if (username.length() < 3) {
            errorLabel.setText("Username must be at least 3 characters long.");
            return;
        }
        if (password.length() < 4) { // Example: enforce minimum password length
            errorLabel.setText("Password must be at least 4 characters long.");
            return;
        }


        if (LibraryRepository.getUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            errorLabel.setText("User '" + username + "' already exists.");
            return;
        }

        // By default, new users are not admins.
        boolean isAdmin = false;
        if (LibraryRepository.getUsers().isEmpty() && username.equalsIgnoreCase("admin")) {
            // Make the first registered user "admin" an actual admin
            isAdmin = true;
        }

        User newUser = new User(username, password, isAdmin);
        LibraryRepository.addUser(newUser);
        LibraryRepository.saveAll(); // Save after adding new user
        errorLabel.setText("Registered '" + username + "' successfully! Please log in.");
        usernameField.clear(); // Clear fields for login
        passwordField.clear();
    }

    private void goToMainView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/mainView.fxml"));
            Parent root = loader.load();

            MainViewController controller = loader.getController();
            controller.setCurrentUser(loggedInUser); // Pass the logged-in user object

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Library Main Menu - User: " + loggedInUser.getUsername());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error loading main view: " + e.getMessage());
        }
    }
}