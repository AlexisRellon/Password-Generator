package com.example.oop_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {

    //JavaFX Components
    @FXML private AnchorPane display;
    @FXML private Button closeButton;
    @FXML private MenuBar menuBar;

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;
    private int counter = 0;


    @FXML
    public void initialize() {
        // Set up event handlers
        display.setOnMousePressed(this::onMousePressedHandler);
        display.setOnMouseDragged(this::onMouseDraggedHandler);
        menuBar.setOnMouseDragged(this::onMouseDraggedHandler);
        menuBar.setOnMousePressed(this::onMousePressedHandler);

        display.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.ENTER) event.consume();
        });

        checkboxSymbols.setOnAction(this::onChkBoxSymbolsActions);
        passwordLenField.addEventFilter(KeyEvent.KEY_TYPED, this::consumeNonNumericKeys);
        quantityField.addEventFilter(KeyEvent.KEY_TYPED, this::consumeNonNumericKeys);
    }

    @FXML private void consumeNonNumericKeys(KeyEvent event) {
        if (!Character.isDigit(event.getCharacter().charAt(0))) {
            event.consume();
        }
    }

    @FXML private void minimizeActionEvent(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true); // Minimize the window
    }

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
        display.getScene().getWindow().setX(x);
        display.getScene().getWindow().setY(y);
    }

    @FXML private void closeActionEvent() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /*
    Methods and Functions for generating the passwords.
     */

    // Components for text-fields
    @FXML private TextField passwordLenField;
    @FXML private TextField symbolsField;
    @FXML private TextField quantityField;

    // Components: CheckBox
    @FXML private CheckBox checkboxNumbers;
    @FXML private CheckBox checkboxLowerCase;
    @FXML private CheckBox checkboxUpperCase;
    @FXML private CheckBox checkboxSymbols;
    @FXML private CheckBox checkboxDuplicates;
    @FXML private CheckBox checkboxSequential;

    //Component: TextArea
    @FXML private TextArea passwordTxtArea;

    //Components: Labels
    @FXML private Label windowTitle;


    //Global Variables
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_CHARACTERS = "1234567890";
    private final StringBuilder allowedCharacters = new StringBuilder();
//    private final StringBuilder allPasswords = new StringBuilder();

    //Methods
    @FXML private void showAboutPopUp() {
        try {
            // Load the FXML file for the About Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("About.fxml"));
            Parent root = loader.load();

            // Create a new stage for the About Dialog
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Get the controller of the About Dialog
            AboutController aboutController = loader.getController();

            // Set the message
            String message = new StringBuilder()
                    .append("About this program\n\n\n")
                    .append("This application generates secure passwords based on user-defined criteria.\n\n")
                    .append("It utilizes various parameters such as length, character types (uppercase, lowercase, numbers, symbols), ")
                    .append("and options to prevent duplicate or sequential characters.")
                    .toString();

            // Set the message in the About Dialog controller
            aboutController.setDescription(message);

            // Show the About Dialog
            stage.showAndWait();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    @FXML private void onGenerateBtnPressed() {
        try {
            String length = passwordLenField.getText();
            int passwordLength = Integer.parseInt(length);

            if (checkboxNumbers.isSelected()) allowedCharacters.append(getNumbersCharacters());
            if (checkboxLowerCase.isSelected()) allowedCharacters.append(getLowercaseLetters());
            if (checkboxUpperCase.isSelected()) allowedCharacters.append(getUppercaseLetters());
            if (checkboxSymbols.isSelected() && !symbolsField.getText().isEmpty()) {
                allowedCharacters.append(symbolsField.getText());
            }

            if (isNoneSelectedOnCheckBox(checkboxNumbers, checkboxLowerCase, checkboxUpperCase, checkboxSymbols)) {
                // Handle scenario where no character options are selected
                passwordTxtArea.setPromptText("Select at least one character option");
                return; // Exit the method to prevent further execution
            }

            StringBuilder generatedPasswords = new StringBuilder();

            int quantity = (
                    (quantityField.getText().isEmpty())
                    ? 1 // Default Value
                    : Integer.parseInt(quantityField.getText()) // User-defined value
                    );

            for (int i = 0; i < quantity; i++) {
                String password = generateRandomPassword(passwordLength);
                generatedPasswords.append(password).append(System.lineSeparator());
            }

            // Set generated passwords to the TextArea
            passwordTxtArea.setText(generatedPasswords.toString());
            allowedCharacters.setLength(0);

        } catch (NumberFormatException exception) {
            passwordLenField.setPromptText("Enter a value");
            passwordLenField.focusedProperty();
        }
    }

    @FXML private void onChkBoxSymbolsActions(ActionEvent event) {
        boolean isSymbolsSelected = checkboxSymbols.isSelected();
        symbolsField.setDisable(!isSymbolsSelected);
        if (!isSymbolsSelected) symbolsField.clear();
    }

    @FXML private void onCopyFirstLineActions() {
        String content = passwordTxtArea.getText();
        String[] lines = content.split("\\R"); // Split by any line break

        if (lines.length > 0) {
            String firstLine = lines[0]; // Retrieve the first line

            // Access the System clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            // Set the content on the Clipboard of the system
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(firstLine); // Get the first line
            clipboard.setContent(clipboardContent);
        }
    }
    @FXML private void onCopyAllActions() {
        String content = passwordTxtArea.getText();
        if (!content.isEmpty()) {
            // Access the System clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            // Set the content on the Clipboard of the system
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(content);
            clipboard.setContent(clipboardContent);
        }
    }

    // Import and Export file (File handler)
    @FXML private void setCmdImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Import");

        // Set extension filters if needed
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        Stage stage = (Stage) display.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                String filename = file.getName();
                List<String> lines = Files.readAllLines(file.toPath());
                StringBuilder importedPasswords = new StringBuilder();

                // Appending all lines to the StringBuilder
                lines.forEach(line -> importedPasswords.append(line).append(System.lineSeparator()));

                // Set imported text to the TextArea
                passwordTxtArea.setText(importedPasswords.toString());
                windowTitle.setText(filename);
                stage.setTitle(filename);
            } catch (IOException e) {
                showAlertDialog("Error Importing File", "An error occurred while importing the file.");
                e.printStackTrace();
            }
        }
    }
    @FXML private void setCmdExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File As");

        // Set extension filters if needed
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        Stage stage = (Stage) display.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                writePasswordsToFile(file);
            } catch (IOException e) {
                showAlertDialog("Error Exporting File", "An error occurred while exporting the file.");
            }
        }
    }

    private void writePasswordsToFile(File file) throws IOException {
        Stage stage = (Stage) display.getScene().getWindow();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String filename = file.getName();
            String content = passwordTxtArea.getText(); // Get the content from the TextArea

            writer.write(content);
            windowTitle.setText(filename);
            stage.setTitle(filename);
        }
    }
    private void showAlertDialog(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private void setCmdNew() {
        Stage stage = (Stage) display.getScene().getWindow();
        String title = "New File (" + (++counter) + ")";

        // Clear the table contents by clearing the items in the ObservableList
        passwordTxtArea.setText("");

        uncheckAllCheckBox();
        setTextToEmpty();
        windowTitle.setText(title);
        stage.setTitle(title);
    }

    //User Defined methods

    private void uncheckAllCheckBox () {
        for(CheckBox checkboxes : Arrays.asList(checkboxNumbers, checkboxLowerCase,
                checkboxUpperCase, checkboxSymbols, checkboxDuplicates,
                checkboxSequential))
        {
            checkboxes.setSelected(false);
        }
    }

    private void setTextToEmpty() {
        for(TextField textfields : Arrays.asList(passwordLenField, quantityField, symbolsField)) {
            textfields.setText("");
        }
        symbolsField.setDisable(true);
    }
    
    private String shuffleSequentialChars(String password) {
        StringBuilder result = new StringBuilder();
        StringBuilder sequentialChars = new StringBuilder();

        for (char c : password.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                if (sequentialChars.length() > 0 && sequentialChars.charAt(sequentialChars.length() - 1) != c - 1) {
                    result.append(shuffleString(sequentialChars.toString()));
                    sequentialChars.setLength(0);
                }
                sequentialChars.append(c);
            } else {
                if (sequentialChars.length() > 0) {
                    result.append(shuffleString(sequentialChars.toString()));
                    sequentialChars.setLength(0);
                }
                result.append(c);
            }
        }

        if (sequentialChars.length() > 0) {
            result.append(shuffleString(sequentialChars.toString()));
        }

        return result.toString();
    }

    private String shuffleString(String input) {
        List<Character> characters = input.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(characters);
        return characters.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private String generateRandomPassword(int length) {
        Random random = new SecureRandom();
        var passwordBuilder = new StringBuilder();

        // Used to check for duplicates
        Set<Character> usedCharacters = new HashSet<>();

        // Generate the password according to the checkboxes
        while (passwordBuilder.length() < length) {

            int randomIndex = random.nextInt(allowedCharacters.length());
            char character = allowedCharacters.charAt(randomIndex);

            if (!checkboxDuplicates.isSelected() || !usedCharacters.contains(character)) {
                passwordBuilder.append(character);
                usedCharacters.add(character);
            }
        }

        // Apply shuffling for sequential characters if selected
        if (checkboxSequential.isSelected()) {
            return shuffleSequentialChars(passwordBuilder.toString());
        }

        return passwordBuilder.toString();
    }

    private boolean isNoneSelectedOnCheckBox(CheckBox checkboxNumbers,
                                             CheckBox checkboxLowerCase,
                                             CheckBox checkboxUpperCase,
                                             CheckBox checkboxSymbols) {
        return !(checkboxNumbers.isSelected() ||
                checkboxLowerCase.isSelected() ||
                checkboxUpperCase.isSelected() ||
                checkboxSymbols.isSelected());

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
}