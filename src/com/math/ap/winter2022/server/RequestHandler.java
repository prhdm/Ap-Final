package com.math.ap.winter2022.server;

import com.math.ap.winter2022.model.Passenger;
import com.math.ap.winter2022.server.connection.ClientConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class RequestHandler {

    private enum path {
        register,
        login,
        search,
        reserve,
        reservations,
        search_route
    }

    private final ClientConnection clientConnection;
    private final Controller controller;

    private Passenger passenger;

    private String[] lastSearch;

    public RequestHandler(ClientConnection clientConnection, Controller controller) {
        this.clientConnection = clientConnection;
        this.controller = controller;
        new Thread(() ->  {
            while (true) {
                clientConnection.send(">>");
                try {
                    String request = clientConnection.nextLine();
                    String[] query = request.split(" ");
                    if (passenger == null) {
                        switch (path.valueOf(query[0])) {
                            case register:
                                register(query[1], query[2]);
                                break;
                            case login:
                                login(query[1], query[2]);
                                break;
                            case search:
                                lastSearch = query;
                                List<String> result = search(query);
                                sort(result);
                                result.forEach(clientConnection::send);
                                break;
                            default:
                                throw new Exception("");
                        }
                    } else {
                        switch (path.valueOf(query[0])) {
                            case reserve:
                                if (passenger == null) {
                                    throw new Exception();
                                }
                                reserve(query[1], query[2]);
                                break;
                            case reservations:
                                if (passenger == null) {
                                    throw new Exception();
                                }
                                reservations();
                                break;
                            case search:
                                lastSearch = query;
                                List<String> result = search(query);
                                sort(result);
                                result.forEach(clientConnection::send);
                                break;
                            case search_route:
                                if (passenger == null) {
                                    throw new Exception();
                                }
                                searchRoute(query[1], query[2]);
                                break;
                            default:
                                throw new Exception();
                        }
                    }
                } catch (Exception e) {
                    clientConnection.send("error!");
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    if (lastSearch != null) {
                        List<String> result = search(lastSearch);
                        clientConnection.sendNotification(result.size()+"");
                    }
                    Thread.sleep(100);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    private void searchRoute(String from, String to) {
    }

    private void reserve(String flightNo, String n) throws Exception {
        controller.purchase(passenger, Integer.parseInt(flightNo), Integer.parseInt(n));
    }

    private void register(String username,String password) throws Exception {
        controller.register("passenger",username,password);
    }

    private void login(String username,String password) throws Exception {
        passenger = (Passenger) controller.login("passenger",username,password);
    }

    private List<String> search(String[] args) {
        lastSearch = args;
        String departure = "";
        String airline = "";
        String from = "";
        String to = "";
        String priceRange = "";
        for (String arg : args) {
            if (arg.contains("departure:"))
                departure = arg.replace("departure:", "");
            else if (arg.contains("airline:"))
                airline = arg.replace("airline:", "");
            else if (arg.contains("from:"))
                from = arg.replace("from:", "");
            else if (arg.contains("to:"))
                to = arg.replace("to:", "");
            else if (arg.contains("price_range:"))
                priceRange = arg.replace("price_range:", "");
        }
        return controller.search(departure, airline, from, to, priceRange);
    }

    private void reservations() throws Exception {
        List<String> result = controller.reservations(passenger);
        sort(result);
        for (String flight : result) {
            clientConnection.send(flight);
        }
    }

    private void sort(List<String> flights) {
        flights.sort((o1, o2) -> {
            String[] date1 = o1.split(" ")[4].replace("departure_date:", "").split("-");
            String[] date2 = o2.split(" ")[4].replace("departure_date:", "").split("-");
            String[] time1 = o1.split(" ")[5].replace("departure_time:", "").split(":");
            String[] time2 = o2.split(" ")[5].replace("departure_time:", "").split(":");
            String money1 = o1.split(" ")[6].replace("price:", "");
            String money2 = o2.split(" ")[6].replace("price:", "");
            int reulst = Integer.compare(Integer.parseInt(date1[2]), Integer.parseInt(date2[2]));
            if (reulst == 0)
                reulst = Integer.compare(Integer.parseInt(date1[1]), Integer.parseInt(date2[1]));
            if (reulst == 0)
                reulst = Integer.compare(Integer.parseInt(date1[0]), Integer.parseInt(date2[0]));
            if (reulst == 0)
                reulst = Integer.compare(Integer.parseInt(time1[0]), Integer.parseInt(time2[0]));
            if (reulst == 0)
                reulst = Integer.compare(Integer.parseInt(time1[1]), Integer.parseInt(time2[1]));
            if (reulst == 0)
                reulst = Integer.compare(Integer.parseInt(money1), Integer.parseInt(money2));
            return reulst;
        });
    }
}
