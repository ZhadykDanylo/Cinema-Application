package com.example.danylozhadyk711473endassignment.model;
import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;


    private String name;
    private String password;
    private Role role;

    public Employee(String name, String password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
