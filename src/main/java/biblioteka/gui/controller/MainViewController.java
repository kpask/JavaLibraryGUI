package biblioteka.gui.controller;

import biblioteka.gui.LibraryRepository;
import biblioteka.gui.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the main menu or dashboard screen after successful login.
 * Manages navigation to other views (e.g., "All Books," "My Borrowed Books,"
 * "Borrowed Books (Admin)").  Handles user logout, saving/loading application
 * state, and the exit action.  It relies on the {@link LibraryRepository}
 * for data management and the {@link User} object to determine user access.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see User
 * @see BookController
 * @see BorrowedController
 * @see MyBorrowedBooksController
 * @see LoginController
 */

public class MainViewController {

    @FXML private Button logoutButton;
    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;

    // Navigation Buttons - FXML Declarations
    @FXML private Button allBooksButton;
    @FXML private Button myBorrowedButton;
    @FXML private Button borrowedButton;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateWelcomeAndUserInfo();
        updateAccessControls();
    }

    private void updateWelcomeAndUserInfo() {
        if (currentUser != null) {
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            }
            if (userInfoLabel != null) {
                String role = currentUser.isAdmin() ? "Admin" : "Reader";
                userInfoLabel.setText("User: " + currentUser.getUsername() + " (Role: " + role + ")");
            }
        } else {
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome!");
            }
            if (userInfoLabel != null) {
                userInfoLabel.setText("User: Not logged in");
            }
        }
    }

    private void updateAccessControls() {
        boolean isLoggedIn = (currentUser != null);

        if (allBooksButton != null) {
            allBooksButton.setDisable(!isLoggedIn);
        }
        if (myBorrowedButton != null) {
            myBorrowedButton.setDisable(!isLoggedIn);
        }
        if (borrowedButton != null) { // Button to view all borrowed books
            borrowedButton.setDisable(!isLoggedIn);
        }
    }

    @FXML
    public void initialize() {
        updateWelcomeAndUserInfo(); // Set initial text for labels
        updateAccessControls();     // Set initial button states
    }

    @FXML
    private void onAllBooksButtonPressed(ActionEvent event) throws IOException {
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "You must be logged in to view books.");
            return;
        }
        navigateToScene(event, "/gui/allBooks.fxml", BookController.class);
    }

    @FXML
    private void onBorrowedButtonPressed(ActionEvent event) throws IOException { // For "All Borrowed Books"
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "You must be logged in to view borrowed books.");
            return;
        }
        navigateToScene(event, "/gui/borrowed.fxml", BorrowedController.class);
    }

    @FXML
    private void onMyBorrowedButtonPressed(ActionEvent event) throws IOException {
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "You need to be logged in to see your borrowed books.");
            return;
        }
        navigateToScene(event, "/gui/myBorrowedBooksView.fxml", MyBorrowedBooksController.class);
    }

    private <T> void navigateToScene(ActionEvent event, String fxmlPath, Class<T> controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Object controllerInstance = loader.getController();
        try {
            java.lang.reflect.Method setUserMethod = controllerClass.getMethod("setCurrentUser", User.class);
            setUserMethod.invoke(controllerInstance, currentUser);
        } catch (NoSuchMethodException e) {
            // Controller doesn't have setCurrentUser, that's fine
        } catch (Exception e) {
            e.printStackTrace(); // Log other reflection errors
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onLogoutPressed(ActionEvent event) {
        LoginController.loggedInUser = null;
        this.currentUser = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/loginView.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Library Login");
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Logout Error", "Error loading login screen: " + e.getMessage());
        }
    }

    @FXML
    private void onSaveStatePressed() {
        LibraryRepository.saveAll();
        showAlert(Alert.AlertType.INFORMATION, "Save Successful", "Current library state has been saved.");
    }

    @FXML
    private void onLoadStatePressed() {
        LibraryRepository.loadAll(() -> {
            showAlert(Alert.AlertType.INFORMATION, "Load Successful", "Library state has been loaded.");
        });
    }

    @FXML
    private void onExitPressed() {
        // Optional: Add a confirmation dialog before exiting
        // LibraryRepository.saveAll(); // Optional: Auto-save on exit
        Platform.exit();
        System.exit(0);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}