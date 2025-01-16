package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Employee;
import com.example.danylozhadyk711473endassignment.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private EmployeeService employeeService;
    private Database database;
    private Employee loggedInUser;

    private int loginAttempts;
    private static final int MAX_ATTEMPTS = 3;

    public LoginPageController() {
        this.database = Database.getInstance();
    }

    @FXML
    public void initialize() {
        usernameText.requestFocus();
        setupEnterKeyPress();
        employeeService = new EmployeeService();
        loginAttempts = 0;
    }

    public void LoginClickButtonAction() {
        handleLogin();
    }

    private void setupEnterKeyPress() {
        usernameText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordText.requestFocus();
            }
        });
    }

    private void handleLogin() {
        if (loginAttempts >= MAX_ATTEMPTS) {
            showAccountLockedException("Your account has been locked.");
            return;
        }

        String username = usernameText.getText();
        String password = passwordText.getText();

        loggedInUser = employeeService.validateUser(username, password);

        if (loggedInUser != null) {
            closeLoginWindow();
            openMainPage();
        } else {
            loginAttempts++;
            if (loginAttempts > MAX_ATTEMPTS) {
                showAccountLockedException("Your account has been locked.");
            } else {
                displayError();
            }
        }
    }

    private void showAccountLockedException(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Account Locked");
        alert.setHeaderText("Account Locked");
        alert.setContentText(message);

        alert.showAndWait();

        // Exit the application
        Stage stage = (Stage) usernameText.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    private void closeLoginWindow() {
        Stage stage = (Stage) usernameText.getScene().getWindow();
        stage.close();
    }

    private void displayError() {
        errorLabel.setText("Invalid username/password combination. Please try again.");
        passwordText.clear();
        passwordText.requestFocus();
    }

    private void openMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/danylozhadyk711473endassignment/main-page.fxml"));
            Parent root = loader.load();

            MainPageController controller = loader.getController();
            controller.setUser(loggedInUser);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/danylozhadyk711473endassignment/css/main-page.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}