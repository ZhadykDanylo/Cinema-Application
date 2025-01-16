package com.example.danylozhadyk711473endassignment.model;

import java.time.LocalDateTime;

import java.io.Serializable;

public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int numberOfTickets;

    private String customerName;

    private String customerEmail;

    private String showingName;

    public Sale(LocalDateTime startTime, int numberOfTickets, String customerName, String customerEmail, String showingName) {
        this.startTime = startTime;
        this.numberOfTickets = numberOfTickets;
        this.customerName = customerName;
        this.customerEmail = (customerEmail == null || customerEmail.isEmpty()) ? null : customerEmail;
        this.showingName = showingName;
    }

    public String getCustomerEmail() { return customerEmail; }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getShowingName() {
        return showingName;
    }
}
