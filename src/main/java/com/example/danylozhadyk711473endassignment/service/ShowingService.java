package com.example.danylozhadyk711473endassignment.service;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Showing;
import javafx.collections.ObservableList;

import java.util.List;

public class ShowingService {

    private final Database database = Database.getInstance();

    public ShowingService() {
    }

    public List<Showing> getShowings() {
        return database.getShowings();
    }

    public void setShowings(ObservableList<Showing> showings) {
        database.setShowings(showings);
    }
}
