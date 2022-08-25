package com.math.ap.winter2022.server;

import com.math.ap.winter2022.server.connection.ClientConnection;
import com.math.ap.winter2022.server.connection.ServerConnection;
import com.math.ap.winter2022.server.connection.StreamConnector;
import com.math.ap.winter2022.server.connection.StreamConnector.PipeConnection;

public class Server {

    Controller controller;
    public Server(Controller controller) {
        this.controller = controller;
    }

    public ServerConnection connectToServer(){
        StreamConnector streamConnector = new StreamConnector();
        PipeConnection clientInput = streamConnector.getPipedConnection();
        PipeConnection serverInput = streamConnector.getPipedConnection();
        PipeConnection serverNotificationInput = streamConnector.getPipedConnection();
        ClientConnection clientConnection = new ClientConnection(clientInput.inputStream,serverInput.outputStream,serverNotificationInput.outputStream);
        new RequestHandler(clientConnection,controller);
        return new ServerConnection(serverInput.inputStream, clientInput.outputStream, serverNotificationInput.inputStream);
    }
}
