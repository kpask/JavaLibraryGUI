package biblioteka.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The main entry point for the JavaFX Library Application.
 * This class extends {@link javafx.application.Application} and is responsible for
 * initializing the application, loading initial data via {@link LibraryRepository},
 * setting up the primary stage, and displaying the initial login view.
 * It also handles application shutdown procedures, ensuring data is saved.
 *
 * @author kapa1135
 * @version 1.0
 * @see LibraryRepository
 * @see biblioteka.gui.controller.LoginController
 * @see javafx.application.Application
 */
public class BookApplication extends Application {
    // Stage window; // Not used directly here anymore
    // Scene scene1, scene2; // Not used directly here anymore

    @Override
    public void start(Stage stage) throws IOException {
        // Load data first
        LibraryRepository.loadAll(() -> {
            System.out.println("Initial data load complete or file not found.");
            // Create a default admin user if no users exist (e.g., first run)
            if (LibraryRepository.getUsers().isEmpty()) {
                LibraryRepository.addUser(new User("admin", "admin", true)); // insecure default
                LibraryRepository.saveAll(); // Save this initial admin
                System.out.println("Created default admin user.");
            }
        });


        // Then load the login view
        FXMLLoader fxmlLoader = new FXMLLoader(BookApplication.class.getResource("/gui/loginView.fxml")); // << Changed to loginView.fxml
        Scene scene = new Scene(fxmlLoader.load(), 600, 450); // Adjusted size slightly
        stage.setTitle("Library Login"); // << Changed title
        stage.setScene(scene);
        stage.show();

        // Handle application close request to save data
        stage.setOnCloseRequest(event -> {
            System.out.println("Application is closing. Saving data...");
            LibraryRepository.saveAll();
            // Platform.exit(); // This might already be handled by JavaFX default close
            // System.exit(0); // Force exit if needed
        });
    }

    public static void main(String[] args) {
        launch(args); // Pass args to launch
    }
}