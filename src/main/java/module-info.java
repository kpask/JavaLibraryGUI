module biblioteka {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens biblioteka.gui to javafx.fxml;
    exports biblioteka.gui;

    opens biblioteka.core to javafx.fxml;
    exports biblioteka.core;
    exports biblioteka.gui.controller;
    opens biblioteka.gui.controller to javafx.fxml;
}
