package com.example.danylozhadyk711473endassignment.service;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Sale;
import javafx.collections.ObservableList;

import java.util.List;

public class SaleService {

    private final Database database = Database.getInstance();

    public SaleService()
    {}

    public List<Sale> getSales() {
        return database.getSales();
    }

    public void setSales(ObservableList<Sale> sales) {
        database.setSales(sales);
    }
}
