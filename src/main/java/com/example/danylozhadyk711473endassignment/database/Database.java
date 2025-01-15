package com.example.danylozhadyk711473endassignment.database;
import com.example.danylozhadyk711473endassignment.model.Role;
import com.example.danylozhadyk711473endassignment.model.Sale;
import com.example.danylozhadyk711473endassignment.model.Showing;
import com.example.danylozhadyk711473endassignment.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database instance;
    private List<User> users;
    private ObservableList<Showing> showings;
    private ObservableList<Sale> sales;

    private Database() {
        users = new ArrayList<>();
        showings = FXCollections.observableArrayList();
        sales = FXCollections.observableArrayList();

        users.add(new User("Mike", "12345678", Role.Admin));
        users.add(new User("Anka", "87654321", Role.User));

        showings.add(new Showing("Whispers of the Night", LocalDateTime.of(2024, 10, 15, 18, 30), LocalDateTime.of(2024, 10, 15, 20, 30)));
        showings.add(new Showing("Galactic Odyssey", LocalDateTime.of(2025, 10, 15, 21, 00), LocalDateTime.of(2025, 10, 15, 23, 00)));
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ObservableList<Showing> getShowings() {
        return showings;
    }

    public void setShowings(ObservableList<Showing> showings) {
        this.showings = showings;
    }

    public ObservableList<Sale> getSales() {
        return sales;
    }

    public void setSales(ObservableList<Sale> sales) {
        this.sales = sales;
    }

    public User validateUser(String name, String password) {
        for (User user : users) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                return new User(user.getName(), user.getPassword(), user.getRole());
            }
        }
        return null;
    }
}
