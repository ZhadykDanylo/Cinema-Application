package com.example.danylozhadyk711473endassignment.database;
import com.example.danylozhadyk711473endassignment.model.Role;
import com.example.danylozhadyk711473endassignment.model.Sale;
import com.example.danylozhadyk711473endassignment.model.Showing;
import com.example.danylozhadyk711473endassignment.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Database instance;
    private List<User> users;
    private List<Showing> showings;
    private List<Sale> sales;

    private static final String DATABASE_FILE = "database_state.bin";

    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            oos.writeObject(new ArrayList<>(users));
            oos.writeObject(new ArrayList<>(showings));
            oos.writeObject(new ArrayList<>(sales));
            System.out.println("Database state saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save database state.");
            e.printStackTrace();
        }
    }

    // Load the database state
    public static Database loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            List<User> loadedUsers = (List<User>) ois.readObject();
            List<Showing> loadedShowings = (List<Showing>) ois.readObject();
            List<Sale> loadedSales = (List<Sale>) ois.readObject();

            instance = new Database();
            instance.users = loadedUsers;
            instance.showings = FXCollections.observableArrayList(loadedShowings);
            instance.sales = FXCollections.observableArrayList(loadedSales);

            System.out.println("Database state loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous database state found. Starting fresh.");
            instance = new Database();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load database state.");
            e.printStackTrace();
            instance = new Database();
        }
        return instance;
    }

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

    public List<Showing> getShowings() {
        return showings;
    }

    public void setShowings(ObservableList<Showing> showings) {
        this.showings = showings;
    }

    public List<Sale> getSales() {
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
