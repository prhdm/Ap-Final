package com.math.ap.winter2022.model;


import java.util.ArrayList;
import java.util.List;

public class Passenger extends User {


    private final List<String> reservations = new ArrayList<>();

    public Passenger(String name, String password) {
        super(name, password);
    }

    public List<String> getReservations() {
        return reservations;
    }

    public void addReservation(String reservation) {
        reservations.add(reservation);
    }
}
