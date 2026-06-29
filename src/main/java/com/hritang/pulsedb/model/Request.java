package com.hritang.pulsedb.model;

import com.hritang.pulsedb.command.CommandType;

public class Request {

    private final CommandType command;
    private final String key;
    private final String value;

    public Request(CommandType command, String key, String value) {
        this.command = command;
        this.key = key;
        this.value = value;
    }

    public CommandType getCommand() {
        return command;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command=" + command +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}