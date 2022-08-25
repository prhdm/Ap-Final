package com.math.ap.winter2022.server.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class ServerConnection extends Connection {

    Scanner in;

    public ServerConnection(InputStream inputStream, OutputStream outputStream, InputStream notifcationInputStream) {
        super(inputStream, outputStream);
        in = new Scanner(notifcationInputStream);
    }

    public String nextNotification() {
        return in.nextLine();
    }

}
