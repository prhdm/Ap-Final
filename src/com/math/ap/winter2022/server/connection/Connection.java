package com.math.ap.winter2022.server.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *  wrapper around socket which can be used to send and receive string messages
 */
public class Connection {

    private final Scanner in;
    private final PrintWriter out;


    public Connection(InputStream inputStream, OutputStream outputStream){
        in = new Scanner(inputStream);
        out = new PrintWriter(outputStream);
    }

    public void send(String message) {
        out.println(message);
        out.flush();
    }


    public String nextLine() {
        return in.nextLine();
    }

}
