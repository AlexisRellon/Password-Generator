package com.example.oop_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Set Icon for the application
        stage.getIcons().add(
                new Image(
                        MainApplication.class.getResourceAsStream("/IMG/fav-icon.png")
                )
        );

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Password Generator");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}