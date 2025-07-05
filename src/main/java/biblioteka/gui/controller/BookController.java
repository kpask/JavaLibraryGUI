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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate; // Import LocalDate

/**
 * Controller for the "All Books" view, displaying a list of available books.
 * Provides functionality for adding, deleting, and borrowing books.
 * Manages user access based on their role (admin/reader).  Interacts with
 * the {@link LibraryRepository} to manage book data and the user session.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see Knyga
 * @see User
 * @see AddBooksController
 * @see BorrowedController
 * @see MainViewController
 */
public class BookController {

    // FXML Injekcijos: JavaFX komponentai, susieti su allBooks.fxml failu
    @FXML private TableView<Knyga> bookTable;
    @FXML private TableColumn<Knyga, String> authorColumn;
    @FXML private TableColumn<Knyga, String> titleColumn;
    @FXML private TableColumn<Knyga, String> genreColumn;
    @FXML private TableColumn<Knyga, String> languageColumn;
    @FXML private TableColumn<Knyga, String> publisherColumn;
    @FXML private TableColumn<Knyga, String> isbnColumn;
    @FXML private TableColumn<Knyga, Integer> yearColumn;
    @FXML private TableColumn<Knyga, Integer> pageCountColumn;

    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button borrowButton;

    private User currentUser;

    // Nustato esamą prisijungusį vartotoją ir atnaujina UI elementų prieigą.
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateAccessControls();
    }

    // Atnaujina mygtukų (Pridėti, Ištrinti, Skolintis) būseną (įjungta/išjungta), pagal prieigos lygi
    private void updateAccessControls() {
        boolean isAdmin = (currentUser != null && currentUser.isAdmin());
        if (addButton != null) addButton.setDisable(!isAdmin);
        if (deleteButton != null) deleteButton.setDisable(!isAdmin);
        // Borrow button should be disabled if no user is logged in OR if the book is already taken (though it's in the "available" list)
        if (borrowButton != null) borrowButton.setDisable(currentUser == null);
    }



     // Inicializacijos metodas, kviečiamas automatiškai po FXML failo įkėlimo.
     // Sukonfigūruoja lentelės stulpelius (kaip gauti duomenis iš Knyga objektų)
     // ir užpildo lentelę duomenimis iš LibraryRepository.
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

        bookTable.setItems(LibraryRepository.getBooks()); // Shows available books
        updateAccessControls(); // Initial call
    }

    @FXML
    private void onBorrowButtonPressed() {
        Knyga selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to borrow.");
            return;
        }

        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Login Required", "You must be logged in to borrow a book.");
            return;
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Borrow Book");
        alert.setHeaderText(null);
        alert.setContentText("Borrow selected book: " + selectedBook.getPavadinimas() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Set borrower information and date
                // Assuming a default borrow period, e.g., 2 weeks from today
                LocalDate takenUntilDate = LocalDate.now().plusWeeks(2);
                selectedBook.take(currentUser.getUsername(), takenUntilDate);

                // Move the book from available list to borrowed list
                LibraryRepository.removeBook(selectedBook);
                LibraryRepository.addBorrowedBook(selectedBook);

                System.out.println("Book '" + selectedBook.getPavadinimas() + "' borrowed by " + currentUser.getUsername() + " until " + takenUntilDate);
                showAlert(Alert.AlertType.INFORMATION, "Book Borrowed", selectedBook.getPavadinimas() + " has been borrowed successfully.");

                // Optional: Save data immediately
                // LibraryRepository.saveAll();
            }
        });
    }

    @FXML
    private void onDeleteButtonPressed() {
        if (currentUser == null || !currentUser.isAdmin()) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to delete books.");
            return;
        }
        Knyga selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Book");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to permanently delete the book: " + selectedBook.getPavadinimas() + "?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LibraryRepository.removeBook(selectedBook);
                    // LibraryRepository.saveAll(); // Optional
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING,"No Selection", "Please select a book to delete.");
        }
    }

    @FXML
    private void onBackButtonPress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/mainView.fxml"));
        Parent root = fxmlLoader.load();
        MainViewController controller = fxmlLoader.getController();
        controller.setCurrentUser(currentUser);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onAddButtonPressed() {
        if (currentUser == null || !currentUser.isAdmin()) {
            showAlert(Alert.AlertType.ERROR,"Access Denied", "You do not have permission to add books.");
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/addBook.fxml"));
            Parent root = fxmlLoader.load();

            // You could pass the current user to AddBooksController if needed, though it's not currently used there.
            // AddBooksController addController = fxmlLoader.getController();
            // addController.setCurrentUser(currentUser); // If you add setCurrentUser to AddBooksController

            Stage stage = new Stage();
            stage.setTitle("Add a New Book");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            if (bookTable.getScene() != null && bookTable.getScene().getWindow() != null) {
                stage.initOwner(bookTable.getScene().getWindow());
            }
            stage.showAndWait(); // Will refresh the table implicitly because LibraryRepository.addBook updates ObservableList
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Error", "Could not open the add book window: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}