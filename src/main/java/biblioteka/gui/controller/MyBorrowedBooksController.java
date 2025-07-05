package biblioteka.gui.controller;

import biblioteka.gui.LibraryRepository;
import biblioteka.core.Knyga;
import biblioteka.gui.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * Controller for the view displaying books borrowed by the currently logged-in user.
 * It filters the books from the {@link LibraryRepository} to show only those
 * borrowed by the user. Allows the user to return their borrowed books.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see Knyga
 * @see User
 * @see MainViewController
 */

public class MyBorrowedBooksController {

    @FXML private TableView<Knyga> myBookTable;
    @FXML private TableColumn<Knyga, String> authorColumn;
    @FXML private TableColumn<Knyga, String> titleColumn;
    @FXML private TableColumn<Knyga, String> genreColumn;
    @FXML private TableColumn<Knyga, String> languageColumn;
    @FXML private TableColumn<Knyga, String> publisherColumn;
    @FXML private TableColumn<Knyga, String> isbnColumn;
    @FXML private TableColumn<Knyga, Integer> yearColumn;
    @FXML private TableColumn<Knyga, Integer> pageCountColumn;

    @FXML private Button returnMyBookButton;
    @FXML private Button backButton;

    private User currentUser;
    private ObservableList<Knyga> userBorrowedBooks = FXCollections.observableArrayList();

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserBooks();
    }

    private void loadUserBooks() {
        userBorrowedBooks.clear();
        if (currentUser != null) {
            for (Knyga book : LibraryRepository.getBorrowedBooks()) {
                if (currentUser.getUsername().equals(book.getBorrowerID())) {
                    userBorrowedBooks.add(book);
                }
            }
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

        myBookTable.setItems(userBorrowedBooks);

        // Listen for changes in the main borrowed books list to refresh this view
        LibraryRepository.getBorrowedBooks().addListener((javafx.collections.ListChangeListener.Change<? extends Knyga> c) -> {
            loadUserBooks(); // Reload/refilter when the main list changes
        });
    }

    @FXML
    private void onReturnMyBookButtonPressed() {
        Knyga selectedBook = myBookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Double check it's actually this user's book (though the list is filtered)
            if (currentUser != null && currentUser.getUsername().equals(selectedBook.getBorrowerID())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Return Book");
                alert.setHeaderText(null);
                alert.setContentText("Return your borrowed book: " + selectedBook.getPavadinimas() + "?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        selectedBook.returnFromBorrow(); // Clear borrower info
                        LibraryRepository.removeBorrowedBook(selectedBook);
                        LibraryRepository.addBook(selectedBook);
                        // LibraryRepository.saveAll(); // Optional
                        // The list listener should refresh userBorrowedBooks
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "This book does not seem to be borrowed by you.");
            }
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