package biblioteka.gui;

import biblioteka.core.Knyga;
import javafx.application.Platform;
import javafx.collections.ObservableList; // Keep this for the method signature
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.List; // Use java.util.List for fields in DataContainer


/**
 * Handles the persistence of application data (books, borrowed books, and users)
 * to and from a file using Java Serialization. This class encapsulates the
 * logic for saving the current state of {@link ObservableList}s by first
 * converting them to {@link ArrayList}s within a {@link DataContainer}
 * and then serializing this container. Load operations deserialize the
 * container and populate the target {@link ObservableList}s.
 * All file operations are performed on a separate thread to avoid blocking
 * the JavaFX Application Thread, and UI updates (like dialogs or callbacks)
 * are posted back to the FX thread.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see Knyga
 * @see User
 * @see Serializable
 */

public class PersistenceManager {

    // DataContainer should store standard List implementations
    private static class DataContainer implements Serializable {
        List<Knyga> books;             // Should be java.util.List
        List<Knyga> borrowedBooks;     // Should be java.util.List
        List<User> users;              // Should be java.util.List

        // Constructor takes ObservableLists but stores them as ArrayLists
        DataContainer(ObservableList<Knyga> books,
                      ObservableList<Knyga> borrowedBooks,
                      ObservableList<User> users) {
            this.books = new ArrayList<>(books); // Convert to ArrayList for serialization
            this.borrowedBooks = new ArrayList<>(borrowedBooks); // Convert to ArrayList
            this.users = new ArrayList<>(users); // Convert to ArrayList
        }
    }

    public static void saveAll(File file,
                               ObservableList<Knyga> booksToSave,
                               ObservableList<Knyga> borrowedBooksToSave,
                               ObservableList<User> usersToSave) {
        new Thread(() -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                // Ensure DataContainer is created with the ObservableLists
                DataContainer data = new DataContainer(booksToSave, borrowedBooksToSave, usersToSave);
                oos.writeObject(data); // This 'data' object now contains ArrayLists
                System.out.println("All data saved to " + file.getName());
                Platform.runLater(() -> showInfoDialog("Save Successful", "All data saved to " + file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> showErrorDialog("Save Error", "Could not save data: " + e.getMessage()));
            }
        }).start();
    }

    public static void loadAll(File file, Runnable onSuccess,
                               ObservableList<Knyga> booksTarget,
                               ObservableList<Knyga> borrowedBooksTarget,
                               ObservableList<User> usersTarget) {
        if (!file.exists()) {
            System.out.println("Data file " + file.getName() + " not found. Starting with empty data.");
            if (onSuccess != null) {
                Platform.runLater(onSuccess);
            }
            return;
        }

        new Thread(() -> {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof DataContainer) {
                    DataContainer data = (DataContainer) obj;
                    Platform.runLater(() -> {
                        // 'data.books', 'data.borrowedBooks', 'data.users' are ArrayLists
                        booksTarget.setAll(data.books != null ? data.books : new ArrayList<>());
                        borrowedBooksTarget.setAll(data.borrowedBooks != null ? data.borrowedBooks : new ArrayList<>());
                        usersTarget.setAll(data.users != null ? data.users : new ArrayList<>());

                        System.out.println("All data loaded from " + file.getName());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    });
                } else {
                    System.err.println("Error: Data file format is incorrect.");
                    Platform.runLater(() -> showErrorDialog("Load Error", "Data file format is incorrect."));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Platform.runLater(() -> showErrorDialog("Load Error", "Could not load data: " + e.getMessage()));
            }
        }).start();
    }

    private static void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}