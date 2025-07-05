package biblioteka.gui;

import biblioteka.core.Knyga;
// PersistenceManager is now in the same package 'biblioteka'
// or import biblioteka.persistence.PersistenceManager if you created a subpackage
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;

/**
 * Manages the in-memory data for the library application, including lists of
 * available books, borrowed books, and users. This class acts as a central
 * repository for application data, providing {@link ObservableList} instances
 * to allow UI components to react to data changes. It also delegates
 * data persistence (saving and loading) to the {@link PersistenceManager}.
 * All data modification methods ensure operations are run on the
 * JavaFX Application Thread.
 *
 * @author kapa1135
 * @version 1.1
 * @see Knyga
 * @see User
 * @see PersistenceManager
 * @see ObservableList
 */

public class LibraryRepository {
    private static final ObservableList<Knyga> books = FXCollections.observableArrayList();
    private static final ObservableList<Knyga> borrowedBooks = FXCollections.observableArrayList();
    private static final ObservableList<User> users = FXCollections.observableArrayList();

    public static final File DEFAULT_DATA_FILE = new File("libraryData.dat");

    public static ObservableList<Knyga> getBooks() {
        return books;
    }

    public static ObservableList<Knyga> getBorrowedBooks() {
        return borrowedBooks;
    }

    public static ObservableList<User> getUsers() {
        return users;
    }

    public static void addBook(Knyga book) {
        // Ensure updates happen on the FX Application Thread if called from background
        if (Platform.isFxApplicationThread()) {
            books.add(book);
        } else {
            Platform.runLater(() -> books.add(book));
        }
    }

    public static void removeBook(Knyga book) {
        if (Platform.isFxApplicationThread()) {
            books.remove(book);
        } else {
            Platform.runLater(() -> books.remove(book));
        }
    }

    public static void addBorrowedBook(Knyga book) {
        if (Platform.isFxApplicationThread()) {
            borrowedBooks.add(book);
        } else {
            Platform.runLater(() -> borrowedBooks.add(book));
        }
    }

    public static void removeBorrowedBook(Knyga book) {
        if (Platform.isFxApplicationThread()) {
            borrowedBooks.remove(book);
        } else {
            Platform.runLater(() -> borrowedBooks.remove(book));
        }
    }

    public static void addUser(User user) {
        if (Platform.isFxApplicationThread()) {
            users.add(user);
        } else {
            Platform.runLater(() -> users.add(user));
        }
    }

    public static void removeUser(User user) {
        if (Platform.isFxApplicationThread()) {
            users.remove(user);
        } else {
            Platform.runLater(() -> users.remove(user));
        }
    }


    public static void saveAll() {
        PersistenceManager.saveAll(DEFAULT_DATA_FILE, books, borrowedBooks, users);
    }

    public static void saveAll(File file) {
        PersistenceManager.saveAll(file, books, borrowedBooks, users);
    }

    public static void loadAll(Runnable onSuccess) {
        PersistenceManager.loadAll(DEFAULT_DATA_FILE, onSuccess, books, borrowedBooks, users);
    }

    public static void loadAll(File file, Runnable onSuccess) {
        PersistenceManager.loadAll(file, onSuccess, books, borrowedBooks, users);
    }
}