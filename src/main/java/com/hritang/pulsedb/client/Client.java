package com.hritang.pulsedb.client;

import com.hritang.pulsedb.config.Constants;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    public void connect() {

        try {

            socket = new Socket("localhost", Constants.PORT);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            PrintWriter writer =
                    new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            System.out.println(reader.readLine());

            while (true) {

                System.out.print("PulseDB > ");

                String command = scanner.nextLine();

                writer.println(command);

                String response = reader.readLine();

                System.out.println(response);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}