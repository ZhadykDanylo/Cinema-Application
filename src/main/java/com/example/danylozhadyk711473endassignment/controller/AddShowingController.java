package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.model.Showing;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddShowingController extends BaseController {

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField startTimeField;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField endTimeField;

    @FXML
    private Button addShowingButton;

    @FXML
    private Label errorLabel;

    private StackPane currentView;
    private ManageShowingsController manageController;

    private static final String TIME_PATTERN = "HH:mm";
    private boolean isEditMode = false;
    private Showing showingToEdit;

    @FXML
    public void initialize(StackPane currentView, ManageShowingsController manageController) {
        this.currentView = currentView;
        this.manageController = manageController;
    }

    @FXML
    public void handleCreationButtonClick(ActionEvent event) {
        if (validateInputs()) {
            if (!isEditMode) {
                createShowing();
            } else {
                updateShowing();
            }
            handleCancelButtonClick(event);
        }
    }

    private boolean validateInputs() {
        if (areFieldsEmpty() || !isValidTimeFormat()) {
            return false;
        }

        try {
            LocalDateTime startDateTime = getLocalDateTime(startDatePicker, startTimeField);
            LocalDateTime endDateTime = getLocalDateTime(endDatePicker, endTimeField);
            if (!startDateTime.isBefore(endDateTime)) {
                showError("End date and time must be after start date and time.");
                return false;
            }
        } catch (DateTimeParseException e) {
            showError("Invalid time format. Please use HH:mm format.");
            return false;
        }

        return true;
    }

    private boolean areFieldsEmpty() {
        if (titleField.getText().isEmpty() || startDatePicker.getValue() == null ||
                startTimeField.getText().isEmpty() || endDatePicker.getValue() == null ||
                endTimeField.getText().isEmpty()) {
            showError("Please fill in all fields.");
            return true;
        }
        return false;
    }

    private boolean isValidTimeFormat() {
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        if (!validateTimeFormat(startTime) || !validateTimeFormat(endTime)) {
            showError("Time must be in HH:mm format.");
            return false;
        }
        return true;
    }

    private boolean validateTimeFormat(String time) {
        return time.matches("\\d{2}:\\d{2}");
    }

    private LocalDateTime getLocalDateTime(DatePicker datePicker, TextField timeField) {
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.parse(timeField.getText(), DateTimeFormatter.ofPattern(TIME_PATTERN));
        return LocalDateTime.of(date, time);
    }

    private void createShowing() {
        String title = titleField.getText();
        LocalDateTime startDateTime = getLocalDateTime(startDatePicker, startTimeField);
        LocalDateTime endDateTime = getLocalDateTime(endDatePicker, endTimeField);

        ObservableList<Showing> showings = db.getShowings(); // Use inherited `db`
        showings.add(new Showing(title, startDateTime, endDateTime));
        db.setShowings(showings);
    }

    private void updateShowing() {
        if (showingToEdit != null) {
            showingToEdit.setTitle(titleField.getText());
            showingToEdit.setStartTime(getLocalDateTime(startDatePicker, startTimeField));
            showingToEdit.setEndTime(getLocalDateTime(endDatePicker, endTimeField));
        }
    }

    private void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    public void setShowingToEdit(Showing selectedShowing) {
        isEditMode = true;
        showingToEdit = selectedShowing;
        fillFormFields();
        addShowingButton.setText("Save Changes");
    }

    private void fillFormFields() {
        titleField.setText(showingToEdit.getTitle());
        startDatePicker.setValue(showingToEdit.getStartTime().toLocalDate());
        startTimeField.setText(showingToEdit.getStartTime().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        endDatePicker.setValue(showingToEdit.getEndTime().toLocalDate());
        endTimeField.setText(showingToEdit.getEndTime().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
    }

    public void clearFields() {
        titleField.clear();
        startDatePicker.setValue(null);
        startTimeField.clear();
        endDatePicker.setValue(null);
        endTimeField.clear();
    }

    @FXML
    public void handleCancelButtonClick(ActionEvent event) {
        loadView("/com/example/danylozhadyk711473endassignment/manage-showings.fxml",
                "/com/example/danylozhadyk711473endassignment/css/manage-showings-page.css",
                (controller) -> {
                    ((ManageShowingsController)controller).initialize(this.currentView);
                }, this.currentView);
    }
}