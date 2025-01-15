package com.example.danylozhadyk711473endassignment.model;

import java.time.LocalDateTime;

public class Sale {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int numberOfTickets;

    private String customerName;

    private String showingName;

    public Sale( LocalDateTime startTime , LocalDateTime endTime, int numberOfTickets, String customerName, String showingName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfTickets = numberOfTickets;
        this.customerName = customerName;
        this.showingName = showingName;
    }

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
