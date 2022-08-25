package com.math.ap.winter2022.server.connection;

import java.io.*;

public class StreamConnector {
    public class PipeConnection{
        public InputStream inputStream;
        public OutputStream outputStream;
    }

    public PipeConnection getPipedConnection(){
        PipeConnection res = new PipeConnection();
        res.outputStream = new PipedOutputStream();
        try {
            res.inputStream = new PipedInputStream((PipedOutputStream) res.outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

}
