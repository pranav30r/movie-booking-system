package com.example.moviebooking.models;

public class SeatModel {
    public static final int AVAILABLE = 0;
    public static final int SELECTED = 1;
    public static final int BOOKED = 2;

    private String seatNumber;
    private int status;

    public SeatModel(String seatNumber, int status) {
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public String getSeatNumber() { return seatNumber; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
