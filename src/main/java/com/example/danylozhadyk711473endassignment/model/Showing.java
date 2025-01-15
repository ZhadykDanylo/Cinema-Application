package com.example.danylozhadyk711473endassignment.model;

import java.time.LocalDateTime;

public class Showing {

    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean[][] seatsLeft;

    public Showing(String title, LocalDateTime startTime, LocalDateTime endTime)
    {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seatsLeft = new boolean[6][12];
    }


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean[][] getSeatsLeft() { return seatsLeft; }
    public void setSeatsLeft(boolean[][] seatsLeft) {}
}
