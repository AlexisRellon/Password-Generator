package com.example.oop_gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Logger;

public class MainController {

    //JavaFX Components
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
        checkboxSymbols.setOnAction(this::onChkBoxSymbolsActions);
        removeScrollBar(tableOutputs);
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

    /*
    Methods and Functions for generating the passwords.
     */

    // Components for text-fields
    @FXML
    private TextField passwordLenField;
    @FXML
    private TextField symbolsField;
    @FXML
    private TextField quantityField;

    // Components: CheckBox
    @FXML
    private CheckBox checkboxNumbers;
    @FXML
    private CheckBox checkboxLowerCase;
    @FXML
    private CheckBox checkboxUpperCase;
    @FXML
    private CheckBox checkboxSymbols;
    @FXML
    private CheckBox checkboxDuplicates;
    @FXML
    private CheckBox checkboxSequential;

    //Components: Table
    @FXML
    private TableView<String> tableOutputs;
    @FXML
    private TableColumn<String, String> passwordColumn;

    // Components: Buttons
    @FXML
    private Button generateBtn;
    @FXML
    private Button copyFirstLineBtn;
    @FXML
    private Button copyAllBtn;


    //Global Variables
    Logger log = Logger.getLogger(getClass().getName());
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_CHARACTERS = "1234567890";
    StringBuilder allowedCharacters = new StringBuilder();

    //Methods
    @FXML
    private void onGenerateBtnPressed(ActionEvent event) {
        try {
            String length = passwordLenField.getText();

            int passwordLength = Integer.parseInt(length);

            if (checkboxNumbers.isSelected()) allowedCharacters.append(getNumbersCharacters());
            if (checkboxLowerCase.isSelected()) allowedCharacters.append(getLowercaseLetters());
            if (checkboxUpperCase.isSelected()) allowedCharacters.append(getUppercaseLetters());
            if (checkboxSymbols.isSelected() && !symbolsField.getText().isEmpty()) {
                allowedCharacters.append(symbolsField.getText());
            }

            if (passwordLength > 1) {
                generatePasswordToTable(passwordLength);
                allowedCharacters.setLength(0);
            }

        } catch (NumberFormatException exception) {
            log.info("Must Contain a Value");
            passwordLenField.setPromptText("Enter a value");
            passwordLenField.focusedProperty();
        }
    }

    private void generatePasswordToTable(int passwordLength) {
        ObservableList<String> passwords = FXCollections.observableArrayList();

        int quantity = 1;
        if (quantityField.getText().isEmpty()) {
            String password = generateRandomPassword(passwordLength);

            passwords.add(password);
        }
        else {
            quantity = Integer.parseInt(quantityField.getText());

            for(int count = 0; count < quantity; count++){
                String password = generateRandomPassword(passwordLength);

                passwords.add(password);
            }
        }

        addPasswordsToTable(passwords);
    }

    @FXML
    private void onChkBoxSymbolsActions(ActionEvent event) {
        boolean isSymbolsSelected = checkboxSymbols.isSelected();
        symbolsField.setDisable(!isSymbolsSelected);
        if (!isSymbolsSelected) symbolsField.clear();
    }
    /*
    To be Implemented
    @FXML
    private void onCopyFirstLineActions(ActionEvent event) {

    }
    @FXML
    private void onCopyAllActions(ActionEvent event) {

    }
     */


    //User Defined methods
    private String generateRandomPassword(int length) {
        Random random = new SecureRandom();
        var passwordBuilder = new StringBuilder();

        for (int i=0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());

            passwordBuilder.append(allowedCharacters.charAt(randomIndex));
        }

        return passwordBuilder.toString();
    }

    private void addPasswordsToTable(ObservableList<String> passwords) {
        TableColumn<String, String> passwordColumnField = (TableColumn<String, String>) tableOutputs.getColumns().get(0);
        passwordColumnField.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        tableOutputs.setItems(passwords);
    }


    // Getter-Setter Methods
    public String getUppercaseLetters() {
        return UPPERCASE_CHARACTERS;
    }

    public String getLowercaseLetters() {
        return LOWERCASE_CHARACTERS;
    }

    public String getNumbersCharacters() {
        return NUMBER_CHARACTERS;
    }


    public static <T extends Control> void removeScrollBar(T table) {
        ScrollBar scrollBar = (ScrollBar) table.queryAccessibleAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR);
        /*
         *This null-check is for safety reasons if you are using when the table's skin isn't yet initialized.
         * If you use this method in a custom skin you wrote, where you @Override the layoutChildren method,
         * use it there, and it should be always initialized, so null-check would be unnecessary.
         *
         */
        if (scrollBar != null) {
            scrollBar.setPrefHeight(0);
            scrollBar.setMaxHeight(0);
            scrollBar.setOpacity(1);
            scrollBar.setVisible(false); // If you want to keep the scrolling functionality then delete this row.
        }
    }
}