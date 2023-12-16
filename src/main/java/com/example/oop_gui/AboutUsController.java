package com.example.oop_gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutUsController {
    //Global Variables
    private double xOffset = 0;
    private double yOffset = 0;

    private static final String MESSAGE = """
                                          We are proud to introduce ourselves as second-year students from the Lyceum of the Philippines - Cavite Campus who have developed this application as part of our Subject Project. Our team comprises highly skilled and passionate developers.
                                                                                    
                                          With a strong focus on professionalism and attention to detail, our team has strived to design and develop a user-friendly application that meets the needs of our clients. We have worked tirelessly to ensure that our app is not only functional but also aesthetically pleasing, with a modern and intuitive interface.
                                                                                    
                                          Our team is committed to delivering quality work, and we are confident that our application will meet and exceed your expectations. We are excited to share our creation with you, and we hope that you find it as useful as we do. Thank you for your support, and we look forward to serving you.
                                                                                    
                                                                                    
                                          Developers:
                                          Alexis John Rellon - Lead Programmer. Frontend and Backend Developer
                                          Prince Stephen Lacsa - Programmer
                                          Louis Clark Barbuco - Programmer
                                          """;

    // Components
    @FXML AnchorPane windowPopUp;
    @FXML Label messageArea;
    @FXML Button closeBtn;

    // Init Controller method
    @FXML public void initialize() {
        // Set up event handlers
        windowPopUp.setOnMousePressed(this::onMousePressedHandler);
        windowPopUp.setOnMouseDragged(this::onMouseDraggedHandler);
        messageArea.setText(MESSAGE);
    }

    // Event Handler
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
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
