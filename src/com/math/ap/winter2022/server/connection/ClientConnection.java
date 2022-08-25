package com.math.ap.winter2022.server.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientConnection extends Connection {

    PrintWriter out;

    public ClientConnection(InputStream inputStream, OutputStream outputStream, OutputStream notificationOutputStream) {
        super(inputStream, outputStream);
        out = new PrintWriter(notificationOutputStream);
    }

    public void sendNotification(String message) {
        out.println(message);
        out.flush();
    }

}
