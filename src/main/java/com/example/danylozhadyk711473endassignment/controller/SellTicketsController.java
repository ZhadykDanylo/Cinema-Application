package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Showing;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SellTicketsController extends BaseController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<Showing> showingTable;

    @FXML
    private TableColumn<Showing, String> startColumn;

    @FXML
    private TableColumn<Showing, String> endColumn;

    @FXML
    private TableColumn<Showing, String> titleColumn;

    @FXML
    private TableColumn<Showing, String> seatsColumn;

    @FXML
    private Button selectSeatsButton;

    @FXML
    private Label selectedLabel;

    private ObservableList<Showing> showings;

    private Database database;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private Showing selectedShowing;

    private MainPageController mainPageController;

    @FXML
    public void initialize(StackPane currentView) {
        this.currentView = currentView;
        database = Database.getInstance();
        loadShowings();
        setupTableColumns();
        showingTable.setOnMouseClicked(this::onShowingSelected);
        clearSelectionDetails();
    }

    private void loadShowings() {
        ObservableList<Showing> allShowings = FXCollections.observableArrayList(database.getShowings());
        showings = allShowings.filtered(showing -> showing.getStartTime().isAfter(LocalDateTime.now()));
        showingTable.setItems(showings);
    }

    private void setupTableColumns() {
        startColumn.setCellValueFactory(cellData -> formatDateTime(cellData.getValue().getStartTime()));
        endColumn.setCellValueFactory(cellData -> formatDateTime(cellData.getValue().getEndTime()));
        titleColumn.setCellValueFactory(cellData -> {
            String title = cellData.getValue().getTitle();
            return new SimpleStringProperty(title);
        });
        seatsColumn.setCellValueFactory(cellData -> {
            int seatsLeftCount = getSeatsLeftCount(cellData.getValue().getSeatsLeft());
            return new SimpleStringProperty(seatsLeftCount + "/72");
        });
    }

    private SimpleStringProperty formatDateTime(LocalDateTime dateTime) {
        return new SimpleStringProperty(dateTime.format(formatter));
    }

    private void onShowingSelected(MouseEvent event) {
        selectedShowing = showingTable.getSelectionModel().getSelectedItem();
        updateSelectionDetails();
    }

    private void updateSelectionDetails() {
        if (selectedShowing != null) {
            String showingDetails = String.format("Selected: %s %s",
                    selectedShowing.getStartTime().format(formatter), selectedShowing.getTitle());
            selectedLabel.setText(showingDetails);
            selectSeatsButton.setDisable(false);
        } else {
            clearSelectionDetails();
        }
    }

    private void clearSelectionDetails() {
        selectedLabel.setText("Selected: ");
        selectSeatsButton.setDisable(true);
    }

    private int getSeatsLeftCount(boolean[][] seats) {
        int count = 0;
        for (boolean[] row : seats) {
            for (boolean seat : row) {
                if (!seat) {
                    count++;
                }
            }
        }
        return count;
    }

    @FXML
    private void onSelectSeatsButtonClicked(ActionEvent event) {
        if (selectedShowing != null) {
            loadView("/com/example/danylozhadyk711473endassignment/select-seats.fxml",
                    "/com/example/danylozhadyk711473endassignment/css/select-seats-page.css",
                    controller -> {
                        SelectSeatsController seatsController = (SelectSeatsController) controller;
                        seatsController.setShowing(selectedShowing);
                        seatsController.initialize(this.currentView);
                    },
                    currentView);
        }
    }
}