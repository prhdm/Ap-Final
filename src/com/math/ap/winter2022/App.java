package com.math.ap.winter2022;

import com.math.ap.winter2022.model.Airline;
import com.math.ap.winter2022.server.Controller;
import com.math.ap.winter2022.server.Server;
import com.math.ap.winter2022.server.connection.ServerConnection;


import javax.xml.crypto.Data;
import java.io.*;
import java.util.Scanner;

public class App {



    public static void main(String[] args) {
        Controller controller = new Controller();

        Server server = new Server(controller);

        ServerConnection connection = server.connectToServer();
        try {
             controller.register("airline", "mahan", "1234");
             Airline airline = (Airline) controller.login("airline","mahan","1234");
             controller.insertFlight(airline,"737 tehran kyiv 08-01-2020 06:12 16000000 167");
             controller.insertFlight(airline,"900 kyiv ny 12-10-2022 00:12 162000000 300");
             controller.insertFlight(airline, "876 tehran frankfurt 01-02-2022 15:30 123457890 0");
             controller.insertFlight(airline, "123 frankfurt tehran 01-02-2022 15:31 123457890 0");
            controller.insertFlight(airline, "124 frankfurt tehran 01-02-2022 15:31 123457891 0");
         } catch (Exception e ) {
             e.printStackTrace();
         }

        // TODO : add flights using Controller.insertFlight here to test your code

        new Thread(() -> {
            while (true) {
                String response = connection.nextLine();

                System.out.println(response);
            }
        }).start();
        // start notificationsThread to see the notifications in the error stream
        Thread notificationsThread = new Thread(() -> {
            while (true) {
                System.err.println(connection.nextNotification());
            }
        });
        Scanner scanner = new Scanner(System.in);
        while (true){
            connection.send(scanner.nextLine());
        }
    }

}