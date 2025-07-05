package biblioteka.gui.controller;

import biblioteka.gui.LibraryRepository;
import biblioteka.core.Knyga;
import biblioteka.gui.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the "Borrowed Books" view, which displays all books
 * currently borrowed from the library. This view is accessible to administrators
 * and allows them to return any book. It interacts with the
 * {@link LibraryRepository} and manages the display of borrowed book information.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see Knyga
 * @see User
 * @see MainViewController
 */
public class BorrowedController {

    @FXML private TableView<Knyga> bookTable;
    @FXML private TableColumn<Knyga, String> authorColumn;
    @FXML private TableColumn<Knyga, String> titleColumn;
    @FXML private TableColumn<Knyga, String> genreColumn;
    @FXML private TableColumn<Knyga, String> languageColumn;
    @FXML private TableColumn<Knyga, String> publisherColumn;
    @FXML private TableColumn<Knyga, String> isbnColumn;
    @FXML private TableColumn<Knyga, Integer> yearColumn;
    @FXML private TableColumn<Knyga, Integer> pageCountColumn;
    @FXML private TableColumn<Knyga, String> borrowedByColumn;
    @FXML private TableColumn<Knyga, String> takenUntilColumn;

    @FXML private Button returnButton; // This is the button for admins to return any book
    @FXML private Button backButton;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // This view is now accessible to all users, so we just update control states
        updateAccessControls();
    }

    private void updateAccessControls() {
        boolean isAdmin = (currentUser != null && currentUser.isAdmin());
        if (returnButton != null) {
            returnButton.setDisable(!isAdmin); // Greyed out if not admin
            // Optionally, you could also change its text or style slightly
            // if (!isAdmin) {
            // returnButton.setText("Return (Admin Only)");
            // returnButton.setStyle("-fx-opacity: 0.7;"); // Example: make it look more disabled
            // } else {
            // returnButton.setText("Return Selected");
            // returnButton.setStyle(""); // Reset style
            // }
        }
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPavadinimas()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutorius()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getZanras()));
        languageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKalba()));
        publisherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLeidykla()));
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        yearColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMetai()).asObject());
        pageCountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSk()).asObject());
        borrowedByColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorrowerID()));

        if (takenUntilColumn != null) {
            takenUntilColumn.setCellValueFactory(cellData -> {
                if (cellData.getValue().getTakenUntil() != null) {
                    return new SimpleStringProperty(cellData.getValue().getTakenUntil().toString());
                }
                return new SimpleStringProperty("");
            });
        }

        bookTable.setItems(LibraryRepository.getBorrowedBooks());
        updateAccessControls(); // Initial call to set button state based on no user (or default)
    }

    @FXML
    private void onReturnButtonPressed() {
        // Explicitly check for admin rights here before performing the action
        if (currentUser == null || !currentUser.isAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Permission Denied", "Only administrators can return books from this view.");
            return; // Do nothing if not an admin, even if button was somehow enabled
        }

        Knyga selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Admin Return Book");
            alert.setHeaderText("Book: " + selectedBook.getPavadinimas() + "\nBorrowed by: " + selectedBook.getBorrowerID());
            alert.setContentText("Are you sure you (as Admin) want to return this book to the library?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedBook.returnFromBorrow();
                    LibraryRepository.removeBorrowedBook(selectedBook);
                    LibraryRepository.addBook(selectedBook);
                    // LibraryRepository.saveAll(); // Optional
                    System.out.println("Admin returned: " + selectedBook.getPavadinimas());
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to return.");
        }
    }

    @FXML
    private void onBackButtonPress(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/mainView.fxml"));
        Parent root = loader.load();
        MainViewController controller = loader.getController();
        controller.setCurrentUser(currentUser);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}