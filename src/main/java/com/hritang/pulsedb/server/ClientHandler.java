package com.hritang.pulsedb.server;

import com.hritang.pulsedb.command.CommandDispatcher;
import com.hritang.pulsedb.command.RequestParser;
import com.hritang.pulsedb.storage.StorageEngine;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final RequestParser parser;
    private final CommandDispatcher dispatcher;

    public ClientHandler(Socket clientSocket,
                         StorageEngine storageEngine) {

        this.clientSocket = clientSocket;
        this.parser = new RequestParser();
        this.dispatcher = new CommandDispatcher(storageEngine);
    }

    @Override
    public void run() {

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));

                PrintWriter writer =
                        new PrintWriter(clientSocket.getOutputStream(), true)
        ) {

            writer.println("Welcome to PulseDB!");

            String input;

            while ((input = reader.readLine()) != null) {

                writer.println(dispatcher.execute(parser.parse(input)));

            }

        } catch (IOException e) {

            System.out.println("Client disconnected.");

        }

    }

}