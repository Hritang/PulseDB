package com.hritang.pulsedb.client;

import com.hritang.pulsedb.config.Constants;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket socket;

    public void connect() {

        try {

            socket = new Socket("localhost", Constants.PORT);

            System.out.println("Connected to PulseDB Server.");

        } catch (IOException e) {

            System.err.println("Unable to connect to the server.");
            e.printStackTrace();

        }

    }

}