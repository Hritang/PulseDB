package com.hritang.pulsedb.command;

import com.hritang.pulsedb.model.Request;
import com.hritang.pulsedb.storage.StorageEngine;

public class CommandDispatcher {

    private final StorageEngine storage;

    public CommandDispatcher(StorageEngine storage) {

        this.storage = storage;

    }

    public String execute(Request request) {

        switch (request.getCommand()) {

            case SET:

                storage.set(request.getKey(), request.getValue());

                return "OK";

            case GET:

                String value = storage.get(request.getKey());

                return value == null ? "NULL" : value;

            case DELETE:

                return storage.delete(request.getKey())
                        ? "Deleted"
                        : "Key Not Found";

            case COUNT:

                return String.valueOf(storage.count());

            case KEYS:

                return storage.keys().toString();

            case CLEAR:

                storage.clear();

                return "Database Cleared";

            case UPDATE:

                return storage.update(request.getKey(), request.getValue())
                        ? "Updated"
                        : "Key Not Found";

            case EXISTS:

                return storage.containsKey(request.getKey())
                        ? "true"
                        : "false";


            case EXIT:

                return "BYE";

            default:

                return "Unsupported Command";

        }

    }

}