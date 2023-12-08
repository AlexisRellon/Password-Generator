package com.example.oop_gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutController {
    //Global Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Components
    @FXML private Label descriptionLabel;
    @FXML private AnchorPane windowPopUp;
    @FXML private Button closeButton;

    //Init Method
    @FXML
    public void initialize() {
        // Set up event handlers
        windowPopUp.setOnMousePressed(this::onMousePressedHandler);
        windowPopUp.setOnMouseDragged(this::onMouseDraggedHandler);
    }

    //Event Handler method
    @FXML private void onMousePressedHandler(MouseEvent event) {
        // Get the initial mouse cursor position relative to the scene
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML private void onMouseDraggedHandler(MouseEvent event) {
        // Calculate the new window position based on the mouse movement
        double x = event.getScreenX() - xOffset;
        double y = event.getScreenY() - yOffset;

        // Move the window
        windowPopUp.getScene().getWindow().setX(x);
        windowPopUp.getScene().getWindow().setY(y);
    }

    @FXML private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //Setter method for the label
    public void setDescription(String description) {
        descriptionLabel.setText(description);
    }
}
