package com.hritang.pulsedb.server;

import com.hritang.pulsedb.config.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private volatile boolean running;

    public void start() {

        try {

            running = true;

            serverSocket = new ServerSocket(Constants.PORT);

            System.out.println("==================================================");
            System.out.println("              PulseDB Server v1.0");
            System.out.println("==================================================");
            System.out.println("Server started successfully on port " + Constants.PORT);
            System.out.println("Waiting for client connections...\n");

            acceptClients();

        } catch (IOException e) {

            System.err.println("Failed to start server.");
            e.printStackTrace();

        }
    }

    private void acceptClients() {

        while (running) {

            try {

                Socket clientSocket = serverSocket.accept();

                System.out.println(
                        "Client connected from: "
                                + clientSocket.getInetAddress()
                                + ":" + clientSocket.getPort()
                );

                // ClientHandler will be added in the next step.

            } catch (IOException e) {

                if (running) {
                    System.err.println("Error while accepting client connection.");
                    e.printStackTrace();
                }

            }

        }

    }

    public void stop() {

        running = false;

        try {

            if (serverSocket != null && !serverSocket.isClosed()) {

                serverSocket.close();

                System.out.println("Server stopped successfully.");

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}