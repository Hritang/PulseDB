package com.hritang.pulsedb.command;

import com.hritang.pulsedb.model.Request;

public class RequestParser {

    public Request parse(String input) {

        String[] parts = input.trim().split("\\s+");

        CommandType command =
                CommandType.valueOf(parts[0].toUpperCase());

        String key = parts.length > 1 ? parts[1] : null;

        String value = parts.length > 2 ? parts[2] : null;

        return new Request(command, key, value);

    }

}