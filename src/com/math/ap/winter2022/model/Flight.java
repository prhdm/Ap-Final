package com.math.ap.winter2022.model;


public class Flight{

    String airline;

    int flightNumber;

    String from;

    String to;

    String date;

    String time;

    long ticketPrice;

    int capacity;


    public Flight(String airline, int flightNumber, String from, String to, String date, String time, long ticketPrice, int capacity) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.ticketPrice = ticketPrice;
        this.capacity = capacity;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public long getPrice() {
        return ticketPrice;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public long getTicketPrice() {
        return ticketPrice;
    }

    public String getAirline() {
        return airline;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    @Override
    public String toString() {
        return "airline:" + airline + ' ' +
                "flight_no:" + flightNumber + ' '+
                "from:" + from + ' ' +
                "to:" + to + ' ' +
                "departure_date:" + date + ' ' +
                "departure_time:" + time + ' ' +
                "price:" + ticketPrice + ' ' +
                "available_seats:" + (capacity==0?"full":capacity);
    }
}
