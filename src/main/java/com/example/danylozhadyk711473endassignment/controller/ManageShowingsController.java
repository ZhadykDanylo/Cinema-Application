package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Showing;
import com.example.danylozhadyk711473endassignment.service.ShowingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManageShowingsController extends BaseController {

    private static final String numberOfSeats = "72";

    @FXML
    private TableView<Showing> showingsTable;

    @FXML
    private TableColumn<Showing, String> titleColumn;

    @FXML
    private TableColumn<Showing, String> startColumn;

    @FXML
    private TableColumn<Showing, String> endColumn;

    @FXML
    private TableColumn<Showing, String> seatsLeftColumn;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addShowingButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private BorderPane borderPane;

    private ObservableList<Showing> showings;
    private Database database;
    private MainPageController mainPageController;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private ShowingService showingService;

    @FXML
    private RadioButton allShowingsRadioButton;

    @FXML
    private RadioButton futureShowingsRadioButton;

    private ToggleGroup showingFilterGroup;

    @FXML
    public void initialize(StackPane currentView) {
        showingService = new ShowingService();
        this.currentView = currentView;
        setupDatabaseAndShowings();
        setupTableColumns();
        showingsTable.setItems(showings);
        configureButtons();

        // Configure the radio buttons
        setupRadioButtons();
    }

    private void setupRadioButtons() {
        showingFilterGroup = new ToggleGroup();
        allShowingsRadioButton.setToggleGroup(showingFilterGroup);
        futureShowingsRadioButton.setToggleGroup(showingFilterGroup);

        showingFilterGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> filterShowings());
    }

    private void filterShowings() {
        if (futureShowingsRadioButton.isSelected()) {
            showFutureShowings();
        } else {
            showAllShowings();
        }
    }

    private void showAllShowings() {
        showings = FXCollections.observableArrayList(showingService.getShowings());
        showingsTable.setItems(showings);
    }

    private void showFutureShowings() {
        ObservableList<Showing> futureShowings = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        for (Showing showing : showingService.getShowings()) {
            if (showing.getStartTime().isAfter(now)) {
                futureShowings.add(showing);
            }
        }
        showingsTable.setItems(futureShowings);
    }

    private void setupDatabaseAndShowings() {
        database = Database.getInstance();
        showings = FXCollections.observableArrayList(showingService.getShowings());;
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        setupStartColumn();
        setupEndColumn();
        setupTitleColumn();
        setupSeatsLeftColumn();
    }

    private void setupStartColumn() {
        startColumn.setCellValueFactory(cellData -> {
            LocalDateTime startTime = cellData.getValue().getStartTime();
            return new SimpleStringProperty(startTime.format(formatter));
        });
    }

    private void setupEndColumn() {
        endColumn.setCellValueFactory(cellData -> {
            LocalDateTime endTime = cellData.getValue().getEndTime();
            return new SimpleStringProperty(endTime.format(formatter));
        });
    }

    private void setupTitleColumn() {
        titleColumn.setCellValueFactory(cellData -> {
            String title = cellData.getValue().getTitle();
            return new SimpleStringProperty(title);
        });
    }

    private void setupSeatsLeftColumn() {
        seatsLeftColumn.setCellValueFactory(cellData -> {
            int seatsLeftCount = getSeatsLeftCount(cellData.getValue().getSeatsLeft());
            return new SimpleStringProperty(seatsLeftCount + "/" + numberOfSeats);
        });
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

    private void configureButtons() {
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        showingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            editButton.setDisable(!isSelected);
            deleteButton.setDisable(!isSelected);
        });
    }

    @FXML
    public void handleDeleteShowingButtonClick() {
        Showing selectedShowing = showingsTable.getSelectionModel().getSelectedItem();
        if (selectedShowing != null && canDeleteShowing(selectedShowing)) {
            showings.remove(selectedShowing);
            showingService.setShowings(showings);
        } else {
            showError("Cannot delete showing: Tickets have already been sold.");
        }
    }

    private boolean canDeleteShowing(Showing showing) {
        return getSeatsLeftCount(showing.getSeatsLeft()) == 72;
    }

    private void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }


    @FXML
    private void onAddButtonClicked() {
        this.loadView("/com/example/danylozhadyk711473endassignment/add-showing.fxml",
                "/com/example/danylozhadyk711473endassignment/css/add-showing.css",
                controller -> {
                    AddShowingController addController = (AddShowingController) controller;
                    addController.initialize(this.currentView, this); // Pass `ManageShowingsController` for refresh
                }, this.currentView);
    }

    @FXML
    private void onEditButtonClicked() {
        Showing selectedShowing = showingsTable.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
            this.loadView("/com/example/danylozhadyk711473endassignment/add-showing.fxml",
                    "/com/example/danylozhadyk711473endassignment/css/add-showing.css",
                    controller -> {
                        AddShowingController addController = (AddShowingController) controller;
                        addController.initialize(this.currentView, this);
                        addController.setShowingToEdit(selectedShowing); // Pass selected showing for editing
                    }, this.currentView);
        } else {
            showError("Cannot edit showing: No showing selected.");
        }
    }

    public void refreshShowings() {
        showings = FXCollections.observableArrayList(showingService.getShowings());; // Reload showings from database
        showingsTable.setItems(showings);  // Update TableView
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}