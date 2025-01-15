package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Employee;
import com.example.danylozhadyk711473endassignment.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.control.Button;
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

    public LoginPageController() {
        this.database = Database.getInstance();
    }

    @FXML
    public void initialize() {
        usernameText.requestFocus();
        setupEnterKeyPress();
        employeeService = new EmployeeService();
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
        String username = usernameText.getText();
        String password = passwordText.getText();

        loggedInUser = employeeService.validateUser(username, password);

        if (loggedInUser != null) {
            closeLoginWindow();
            openMainPage();
        } else {
            displayError();
        }
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