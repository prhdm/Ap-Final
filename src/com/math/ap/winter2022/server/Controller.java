package com.math.ap.winter2022.server;

import com.math.ap.winter2022.model.Airline;
import com.math.ap.winter2022.model.Flight;
import com.math.ap.winter2022.model.Passenger;
import com.math.ap.winter2022.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Controller {

    final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    final ConcurrentHashMap<Integer, Flight> flights = new ConcurrentHashMap<>();

    public Controller() {

    }

    public void register(String userType, String username, String password) throws Exception {
        if (userType.equals("airline")) {
            addUser(username, new Airline(username, password));
        } else if (userType.equals("passenger")) {
            addUser(username, new Passenger(username, password));
        } else {
            throw new Exception();
        }
    }

    private void addUser(String username, User user) throws Exception {
        if (users.containsKey(username)) {
            throw new Exception();
        }
        users.put(username, user);
    }

    public User login(String userType, String username, String password) throws Exception {
        User user;
        if (userType.equals("airline")) {
            user = getUser(username, password);
            if (user instanceof Airline) {
                return user;
            } else {
                throw new Exception();
            }
        }else if (userType.equals("passenger")) {
            user = getUser(username, password);
            if (user instanceof Passenger) {
                return user;
            } else {
                throw new Exception();
            }
        } else {
            throw new Exception();
        }
    }

    private User getUser(String username, String password) throws Exception {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password))
            throw new Exception();
        return user;
    }


    public void insertFlight(User user, String args) throws Exception {
        // args: [flight number: int] [to: String] [from: String] [date :dd-mm-yyyy] [time: hh:mm] [ticketPrice : long] [capacity: int]
        String[] argsArray = args.split(" ");
        int flightNumber = Integer.parseInt(argsArray[0]);
        String to = argsArray[1];
        String from = argsArray[2];
        String date = argsArray[3];
        String time = argsArray[4];
        long ticketPrice = Long.parseLong(argsArray[5]);
        int capacity = Integer.parseInt(argsArray[6]);

        if (!(user instanceof Airline))
            throw new Exception();

        if (argsArray.length != 7)
            throw new Exception();

        if (flights.containsKey(flightNumber))
            throw new Exception("Flight already exists");

        if (!date.matches("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}$"))
            throw new Exception();

        if (!time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
            throw new Exception();

        Flight flight = new Flight(user.getName(),flightNumber, to, from, date, time, ticketPrice, capacity);
        flights.put(flightNumber, flight);
    }

    public void purchase(User user, int flightNo, int n) throws Exception {
        if (!(user instanceof Passenger))
            throw new Exception("permission denied");
        Flight flight = flights.get(flightNo);
        if (flight == null)
            throw new Exception();
        if (flight.getCapacity() < n)
            throw new Exception("not enough seats available");
        flight.setCapacity(flight.getCapacity() - n);
        String reservation = flight + " " + "number:" + n;
        ((Passenger) user).addReservation(reservation);
    }


    public List<String> reservations(User user) throws Exception {
        if (!(user instanceof Passenger))
            throw new Exception();
        return ((Passenger) user).getReservations();
    }

    public List<String> search(String departure, String airline, String from, String to, String priceRange) {
        List<String> result = new ArrayList<>();
        for (Flight flight : flights.values()) {
            boolean isDepartureOk = departure.equals("") || flight.getDate().equals(departure);
            boolean isAirlineOk = airline.equals("") || flight.getAirline().equals(airline);
            boolean isFromOk = from.equals("") || flight.getFrom().equals(from);
            boolean isToOk = to.equals("") || flight.getTo().equals(to);
            boolean isPriceOk = priceRange.equals("") || flight.getTicketPrice() >= Long.parseLong(priceRange.split("-")[0])
                    && flight.getTicketPrice() <= Long.parseLong(priceRange.split("-")[1]);
            if (isDepartureOk && isAirlineOk && isFromOk && isToOk && isPriceOk)
                result.add(flight.toString());
        }
        return result;
    }

    public HashMap<String,ArrayList<String>> getAllFlights() {
        HashMap<String,ArrayList<String>> result = new HashMap<>();
        for (Flight flight : flights.values()) {
            result.putIfAbsent(flight.getFrom(), new ArrayList<>());
            result.get(flight.getFrom()).add(flight.toString());
        }
        return result;
    }

}
