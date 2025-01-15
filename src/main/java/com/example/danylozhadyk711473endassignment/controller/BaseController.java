package com.example.danylozhadyk711473endassignment.controller;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Showing;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public abstract class BaseController implements Initializable {
    protected StackPane currentView;
    protected Database db;

    public BaseController() {
        this.db = Database.getInstance();
    }
    public void initialize(URL location, ResourceBundle resources) {
    }

    protected <T> void loadView(String fxmlPath, String cssPath, Consumer<T> initializer, StackPane currentView) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(fxmlPath));
            Parent view = (Parent)fxmlLoader.load();
            T controller = fxmlLoader.getController();
            if (initializer != null) {
                initializer.accept(controller);
            }

            if (cssPath != null && !cssPath.isEmpty()) {
                view.getStylesheets().add(this.getClass().getResource(cssPath).toExternalForm());
            }

            currentView.getChildren().clear();
            currentView.getChildren().add(view);
        } catch (IOException var8) {
            IOException e = var8;
            e.printStackTrace();
        }

    }

    protected void setEnterKeyNavigation(Node[] inputFields, Node nextNode) {
        for(int i = 0; i < inputFields.length; ++i) {
            int index = i;
            Node currentNode = inputFields[i];
            currentNode.setOnKeyPressed((event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    event.consume();
                    if (index < inputFields.length - 1) {
                        inputFields[index + 1].requestFocus();
                    } else {
                        nextNode.requestFocus();
                    }
                }

            });
        }

    }

    protected void setTableView(TableView tableView, TableColumn<Showing, String> startColumn, TableColumn<Showing, String> endColumn, TableColumn<Showing, String> titleColumn, TableColumn<Showing, String> seatsColumn, ObservableList<Showing> showings) {
        startColumn.setCellValueFactory(new PropertyValueFactory("formattedStart"));
        endColumn.setCellValueFactory(new PropertyValueFactory("formattedEnd"));
        titleColumn.setCellValueFactory(new PropertyValueFactory("title"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory("seats"));
        tableView.setItems(showings);
    }
}
