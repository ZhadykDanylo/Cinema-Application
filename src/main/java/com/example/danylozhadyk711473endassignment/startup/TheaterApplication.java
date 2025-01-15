package com.example.danylozhadyk711473endassignment.startup;

import com.example.danylozhadyk711473endassignment.database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TheaterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Database.loadState();
        FXMLLoader fxmlLoader = new FXMLLoader(TheaterApplication.class.getResource("/com/example/danylozhadyk711473endassignment/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/com/example/danylozhadyk711473endassignment/css/login-page.css").toExternalForm());
        stage.setTitle("Movie Theater - Login");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Database.getInstance().saveState(); // Save database state at exit
    }

    public static void main(String[] args) {
        launch();
    }
}