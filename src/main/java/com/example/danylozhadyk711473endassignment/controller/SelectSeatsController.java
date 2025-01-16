package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.model.Sale;
import com.example.danylozhadyk711473endassignment.model.Showing;
import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.service.SaleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SelectSeatsController extends BaseController {

    @FXML
    private Label showingLabel;

    @FXML
    private GridPane seatGrid;

    @FXML
    private ListView<String> selectedSeatsList;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerEmailField;

    @FXML
    private Button sellButton;

    @FXML
    private BorderPane borderPane;

    private Showing currentShowing;

    private List<String> selectedSeats = new ArrayList<>();

    private boolean[][] seatsLeft;

    private Database database;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private SaleService saleService;

    @FXML
    public void initialize(StackPane currentView) {
        this.currentView = currentView;
        database = Database.getInstance();
        customerNameField.textProperty().addListener((observable, oldValue, newValue) -> updateSellButton());
        saleService = new SaleService();
    }

    public void setShowing(Showing showing) {
        this.currentShowing = showing;
        showingLabel.setText(formatShowingLabel(showing));
        loadSeats(showing);
    }

    private String formatShowingLabel(Showing showing) {
        String formattedStartTime = currentShowing.getStartTime().format(formatter);
        return "Selected Showing: " + showing.getTitle() + " - " + formattedStartTime;
    }

    private void loadSeats(Showing showing) {
        seatsLeft = showing.getSeatsLeft();
        seatGrid.getChildren().clear();
        initializeSeatGrid();
    }

    private void initializeSeatGrid() {
        for (int row = 0; row < seatsLeft.length; row++) {
            for (int col = 0; col < seatsLeft[row].length; col++) {
                Button seatButton = createSeatButton(row, col);
                seatGrid.add(seatButton, col, row);
            }
        }
    }

    private Button createSeatButton(int row, int col) {
        Button seatButton = new Button(String.valueOf(col + 1));
        seatButton.getStyleClass().add("seat-button");
        seatButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        if (seatsLeft[row][col]) {
            markSeatAsSold(seatButton);
        } else {
            enableSeatSelection(seatButton, row, col);
        }

        return seatButton;
    }

    private void markSeatAsSold(Button seatButton) {
        seatButton.getStyleClass().add("sold");
        seatButton.setDisable(true);
    }

    private void enableSeatSelection(Button seatButton, int row, int col) {
        seatButton.setOnMouseClicked(event -> handleSeatSelection(seatButton, row, col));
    }

    private void handleSeatSelection(Button clickedButton, int row, int col) {
        String seat = "Row " + (row + 1) + " / Seat " + (col + 1);
        toggleSeatSelection(clickedButton, seat);
        updateSellButton();
    }

    private void toggleSeatSelection(Button clickedButton, String seat) {
        if (selectedSeats.contains(seat)) {
            deselectSeat(clickedButton, seat);
        } else {
            selectSeat(clickedButton, seat);
        }
    }

    private void deselectSeat(Button clickedButton, String seat) {
        selectedSeats.remove(seat);
        clickedButton.getStyleClass().remove("selected");
        selectedSeatsList.getItems().remove(seat);
    }

    private void selectSeat(Button clickedButton, String seat) {
        selectedSeats.add(seat);
        clickedButton.getStyleClass().add("selected");
        selectedSeatsList.getItems().add(seat);
    }

    private void updateSellButton() {
        int selectedCount = selectedSeats.size();
        boolean hasName = !customerNameField.getText().trim().isEmpty();
        sellButton.setText("Sell " + selectedCount + " ticket(s)");
        sellButton.setDisable(selectedCount == 0 || !hasName);
    }

    @FXML
    private void handleSellTickets(ActionEvent event) {
        if (selectedSeats.isEmpty()) return;

        String customerName = customerNameField.getText().trim();
        String customerEmail = customerEmailField.getText().trim(); // Optional field
        if (customerEmail.isEmpty()) {
            customerEmail = null; // Explicitly set to null if empty
        }

        commitSeatSelections();
        updateDatabase(customerName, customerEmail);
        resetSelection();
        loadSeats(currentShowing);
        handleCancel(event);
    }

    private void commitSeatSelections() {
        for (String seat : selectedSeats) {
            String[] parts = seat.split(" / ");
            int row = Integer.parseInt(parts[0].replace("Row ", "")) - 1;
            int col = Integer.parseInt(parts[1].replace("Seat ", "")) - 1;
            seatsLeft[row][col] = true;
        }
        currentShowing.setSeatsLeft(seatsLeft);
    }

    private void updateDatabase(String customerName, String customerEmail) {
        ObservableList<Showing> showings = FXCollections.observableArrayList(database.getShowings());
        updateShowingList(showings);
        database.setShowings(showings);

        ObservableList<Sale> sales = FXCollections.observableArrayList(saleService.getSales());
        addNewSale(sales, customerName, customerEmail);
        saleService.setSales(sales);
    }

    private void updateShowingList(ObservableList<Showing> showings) {
        for (int i = 0; i < showings.size(); i++) {
            if (isCurrentShowing(showings.get(i))) {
                showings.set(i, currentShowing);
                break;
            }
        }
    }

    private boolean isCurrentShowing(Showing showing) {
        return showing.getTitle().equals(currentShowing.getTitle()) &&
                showing.getStartTime().equals(currentShowing.getStartTime());
    }

    private void addNewSale(ObservableList<Sale> sales, String customerName, String customerEmail) {
        Sale sale = new Sale(LocalDateTime.now(), selectedSeats.size(), customerName, customerEmail, currentShowing.getTitle());
        sales.add(sale);
    }

    private void resetSelection() {
        selectedSeats.clear();
        selectedSeatsList.getItems().clear();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        loadView("/com/example/danylozhadyk711473endassignment/sell-tickets.fxml",
                "/com/example/danylozhadyk711473endassignment/css/sell-tickets-page.css",
                controller -> {
                    SellTicketsController ticketsController = (SellTicketsController) controller;
                    ticketsController.initialize(this.currentView);
                },
                this.currentView);
    }
}

