package com.hritang.pulsedb.persistence;

import com.hritang.pulsedb.command.RequestParser;
import com.hritang.pulsedb.command.CommandDispatcher;
import com.hritang.pulsedb.model.Request;
import com.hritang.pulsedb.storage.StorageEngine;

import java.io.*;

public class WriteAheadLog {

    private static final String LOG_FILE = "wal.log";

    public synchronized void append(String command) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(LOG_FILE, true))) {

            writer.write(command);
            writer.newLine();

        } catch (IOException e) {

            System.err.println("Failed to write WAL.");
            e.printStackTrace();

        }

    }

    public void recover(StorageEngine storage) {

        File file = new File(LOG_FILE);

        if (!file.exists()) {
            return;
        }

        RequestParser parser = new RequestParser();
        CommandDispatcher dispatcher =
                new CommandDispatcher(storage, this);;

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                Request request = parser.parse(line);

                dispatcher.execute(request, false);

            }

        } catch (Exception e) {

            System.err.println("Recovery failed.");
            e.printStackTrace();

        }

    }

}