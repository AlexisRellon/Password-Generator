module com.example.oop_gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oop_gui to javafx.fxml;
    exports com.example.oop_gui;
}