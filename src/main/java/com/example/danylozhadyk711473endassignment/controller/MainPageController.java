package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.model.Role;
import com.example.danylozhadyk711473endassignment.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainPageController extends BaseController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private BorderPane borderPane;

    @FXML
    protected StackPane contentPane;

    @FXML
    private Button manageShowingsButton;

    @FXML
    private Button viewSalesHistoryButton;

    public void setUser(Employee user) {
        setupUserInfo(user);
        setupAccessRestrictions(user);
    }

    private void setupUserInfo(Employee user) {
        welcomeLabel.setText("Welcome " + user.getName());
        roleLabel.setText("You are logged in as " + user.getRole());
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        dateTimeLabel.setText("The current date and time is " + formattedDateTime);
    }

    private void setupAccessRestrictions(Employee user) {
        if (user.getRole() == Role.Sales) {
            manageShowingsButton.setDisable(true);
            viewSalesHistoryButton.setDisable(true);
        }
    }

    @FXML
    public void showManageShowingsPnl(ActionEvent event) {
        loadView("/com/example/danylozhadyk711473endassignment/manage-showings.fxml",
                "/com/example/danylozhadyk711473endassignment/css/manage-showings-page.css",
                (controller) -> {
                    ((ManageShowingsController)controller).initialize(this.contentPane);
                }, this.contentPane);
    }

    @FXML
    public void showSellTicketsPnl(ActionEvent event) {
        loadView("/com/example/danylozhadyk711473endassignment/sell-tickets.fxml",
                "/com/example/danylozhadyk711473endassignment/css/sell-tickets-page.css",
                controller -> {
                    SellTicketsController ticketsController = (SellTicketsController) controller;
                    ticketsController.initialize(contentPane);
                },
                contentPane);
    }

    @FXML
    public void showViewSalesHistoryPnl(ActionEvent event) {
        loadView("/com/example/danylozhadyk711473endassignment/view-sales-history.fxml",
                "/com/example/danylozhadyk711473endassignment/css/view-sales-history-page.css",
                null,
                contentPane);
    }
}
