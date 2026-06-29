package com.hritang.pulsedb.server;

import com.hritang.pulsedb.config.Constants;
import com.hritang.pulsedb.persistence.WriteAheadLog;
import com.hritang.pulsedb.storage.StorageEngine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    private final StorageEngine storageEngine;
    private final ExecutorService threadPool;
    private final WriteAheadLog wal;

    private ServerSocket serverSocket;
    private volatile boolean running;

    public Server() {

        this.storageEngine = new StorageEngine();
        this.wal = new WriteAheadLog();

        this.threadPool =
                Executors.newFixedThreadPool(Constants.MAX_CLIENTS);

    }

    public void start() {

        try {

            running = true;

            serverSocket = new ServerSocket(Constants.PORT);

            System.out.println("==================================================");
            System.out.println("              PulseDB Server v1.0");
            System.out.println("==================================================");
            System.out.println("Server started successfully on port " + Constants.PORT);
            System.out.println("Recovering database from WAL...");

            wal.recover(storageEngine);

            System.out.println("Database recovery completed.");

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
                        "Client connected : "
                                + clientSocket.getRemoteSocketAddress()
                );

                threadPool.submit(
                        new ClientHandler(clientSocket, storageEngine, wal)
                );

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

            }

            threadPool.shutdown();

            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {

                threadPool.shutdownNow();

            }

            System.out.println("Server stopped successfully.");

        } catch (IOException e) {

            System.err.println("Error while stopping server.");
            e.printStackTrace();

        } catch (InterruptedException e) {

            threadPool.shutdownNow();
            Thread.currentThread().interrupt();

        }

    }

}