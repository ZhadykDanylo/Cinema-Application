package com.example.danylozhadyk711473endassignment.service;

import com.example.danylozhadyk711473endassignment.database.Database;
import com.example.danylozhadyk711473endassignment.model.Employee;

public class EmployeeService {

    Database database = Database.getInstance();

    public EmployeeService() {
    }

    public Employee validateUser(String name, String password) {
        return database.validateUser(name, password);
    }
}
