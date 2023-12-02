package com.example.oop_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private AnchorPane display;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button closeButton;

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;


    @FXML
    public void initialize() {
        // Set up event handlers
        display.setOnMousePressed(this::onMousePressedHandler);
        display.setOnMouseDragged(this::onMouseDraggedHandler);
    }

    @FXML
    private void onMousePressedHandler(MouseEvent event) {
        // Get the initial mouse cursor position relative to the scene
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void onMouseDraggedHandler(MouseEvent event) {
        // Calculate the new window position based on the mouse movement
        double x = event.getScreenX() - xOffset;
        double y = event.getScreenY() - yOffset;

        // Move the window
        display.getScene().getWindow().setX(x);
        display.getScene().getWindow().setY(y);
    }

    @FXML
    private void closeActionEvent(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}