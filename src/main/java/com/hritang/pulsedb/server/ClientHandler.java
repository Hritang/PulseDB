package com.hritang.pulsedb.server;

import com.hritang.pulsedb.command.CommandDispatcher;
import com.hritang.pulsedb.command.RequestParser;
import com.hritang.pulsedb.persistence.WriteAheadLog;
import com.hritang.pulsedb.storage.StorageEngine;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final RequestParser parser;
    private final CommandDispatcher dispatcher;

    public ClientHandler(Socket clientSocket,
                         StorageEngine storageEngine,
                         WriteAheadLog wal) {

        this.clientSocket = clientSocket;
        this.parser = new RequestParser();
        this.dispatcher = new CommandDispatcher(storageEngine, wal);

    }

    @Override
    public void run() {

        try (

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));

                PrintWriter writer =
                        new PrintWriter(
                                clientSocket.getOutputStream(),
                                true)

        ) {

            writer.println("Welcome to PulseDB!");

            String input;

            while ((input = reader.readLine()) != null) {

                String response =
                        dispatcher.execute(
                                parser.parse(input)
                        );

                writer.println(response);

                if ("BYE".equals(response)) {
                    break;
                }

            }

        } catch (IOException e) {

            System.out.println("Client disconnected.");

        }

    }

}