package biblioteka.gui.controller;

import biblioteka.gui.LibraryRepository;
import biblioteka.core.InvalidISBNException;
import biblioteka.core.Knyga; // Your Knyga class
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the "Add Book" window.
 * Manages the GUI elements and user interactions for adding a new book
 * to the library. Validates user input and adds the book to the
 * {@link LibraryRepository}.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see Knyga
 * @see BookController
 */

public class AddBooksController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField languageField;
    @FXML private TextField publisherField;
    @FXML private TextField isbnField;
    @FXML private TextField yearField;
    @FXML private TextField pageCountField; // For 'sk' (page count)

    @FXML
    public void onCancelButtonPressed(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addBookButton() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String language = languageField.getText().trim();
            String publisher = publisherField.getText().trim();
            String isbn = isbnField.getText().trim();
            String yearStr = yearField.getText().trim();
            String pageCountStr = pageCountField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || yearStr.isEmpty() || pageCountStr.isEmpty()) {
                showAlert("Missing Information", "Please fill in Title, Author, ISBN, Year, and Page Count.");
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
                if (year < 0 || year > java.time.Year.now().getValue() + 5) {
                    showAlert("Invalid Year", "Please enter a realistic publication year.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Year", "Please enter a valid number for the year.");
                return;
            }

            int pageCount; // This is 'sk'
            try {
                pageCount = Integer.parseInt(pageCountStr);
                if (pageCount <= 0) {
                    showAlert("Invalid Page Count", "Page count must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Page Count", "Please enter a valid number for page count.");
                return;
            }

            // Using your Knyga constructor:
            // public Knyga(String pavadinimas, int metai, int sk, String autorius, String zanras, String kalba, String leidykla, String isbn)
            Knyga book = new Knyga(
                    title,
                    year,
                    pageCount, // This is 'sk'
                    author,
                    genre,
                    language,
                    publisher,
                    isbn
            );

            // Check for duplicates based on ISBN (assuming Knyga.equals() is based on ISBN)
            if (LibraryRepository.getBooks().contains(book) || LibraryRepository.getBorrowedBooks().contains(book)) {
                showAlert("Duplicate Book", "A book with this ISBN (" + isbn + ") already exists in the library.");
                return;
            }

            LibraryRepository.addBook(book);
            clearFields();
            System.out.println("Book added: " + book);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully: " + book.getPavadinimas());

            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();

        } catch (InvalidISBNException e) {
            showAlert("ISBN Error", e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert("Input Error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        genreField.clear();
        languageField.clear();
        publisherField.clear();
        isbnField.clear();
        yearField.clear();
        pageCountField.clear();
    }

    private void showAlert(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}