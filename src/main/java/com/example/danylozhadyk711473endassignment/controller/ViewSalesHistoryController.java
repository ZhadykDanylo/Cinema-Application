package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Sale;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewSalesHistoryController extends BaseController {

    @FXML
    private TableView<Sale> salesTable;

    @FXML
    private TableColumn<Sale, String> startDateColumn;

    @FXML
    private TableColumn<Sale, Integer> numberOfTicketsColumn;

    @FXML
    private TableColumn<Sale, String> customerColumn;

    @FXML
    private TableColumn<Sale, String> showingColumn;

    private ObservableList<Sale> sales;

    private Database database;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        database = Database.getInstance();
        loadSalesData();
        setupTableColumns();
        salesTable.setItems(sales);
    }

    private void loadSalesData() {
        sales = FXCollections.observableArrayList(database.getSales());
    }

    private void setupTableColumns() {
        // Format start date
        startDateColumn.setCellValueFactory(cellData -> formatDateTime(cellData.getValue().getStartTime()));

        // Set number of tickets
        numberOfTicketsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberOfTickets()).asObject());

        // Set customer name
        customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));

        // Combine showing details
        showingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                createFullShowing(cellData.getValue().getEndTime(), cellData.getValue().getShowingName())
        ));
    }

    private SimpleStringProperty formatDateTime(LocalDateTime dateTime) {
        String formattedTime = dateTime.format(formatter);
        return new SimpleStringProperty(formattedTime);
    }

    private String createFullShowing(LocalDateTime endTime, String showingName) {
        String formattedEndTime = endTime.format(formatter);
        return formattedEndTime + " " + showingName;
    }
}