package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Sale;
import com.example.danylozhadyk711473endassignment.service.SaleService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.layout.StackPane;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    @FXML
    private Button exportButton;

    private ObservableList<Sale> sales;

    private Database database;

    private SaleService saleService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        saleService = new SaleService();
        super.initialize(location, resources);
        database = Database.getInstance();
        loadSalesData();
        setupTableColumns();
        salesTable.setItems(sales);
    }

    private void loadSalesData() {
        sales = FXCollections.observableArrayList(saleService.getSales());
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
                createFullShowing(cellData.getValue().getStartTime(), cellData.getValue().getShowingName())
        ));
    }

    private SimpleStringProperty formatDateTime(LocalDateTime dateTime) {
        String formattedTime = dateTime.format(formatter);
        return new SimpleStringProperty(formattedTime);
    }

    private String createFullShowing(LocalDateTime startTime, String showingName) {
        String formattedStartTime = startTime.format(formatter);
        return formattedStartTime + " " + showingName;
    }

    @FXML
    private void handleExportCustomers(ActionEvent event) {
        // Get unique customers with email
        Map<String, Sale> customerMap = new HashMap<>();
        for (Sale sale : sales) {
            if (sale.getCustomerEmail() != null && !sale.getCustomerEmail().isEmpty()) {
                String email = sale.getCustomerEmail();
                if (!customerMap.containsKey(email) || sale.getStartTime().isAfter(customerMap.get(email).getStartTime())) {
                    customerMap.put(email, sale);
                }
            }
        }

        // Prompt user for file location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Customer Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            saveCustomerDataToFile(customerMap, file);
        }
    }

    private void saveCustomerDataToFile(Map<String, Sale> customerMap, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("name,email,last_sale\n");

            for (Sale sale : customerMap.values()) {
                writer.write(String.format("%s,%s,%s\n",
                        sale.getCustomerName(),
                        sale.getCustomerEmail(),
                        sale.getStartTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
