package com.hritang.pulsedb.command;

import com.hritang.pulsedb.model.Request;
import com.hritang.pulsedb.persistence.WriteAheadLog;
import com.hritang.pulsedb.storage.StorageEngine;

public class CommandDispatcher {

    private final StorageEngine storage;
    private final WriteAheadLog wal;

    public CommandDispatcher(StorageEngine storage, WriteAheadLog wal) {

        this.storage = storage;
        this.wal = wal;

    }

    // Used by normal client requests
    public String execute(Request request) {
        return execute(request, true);
    }

    // Used internally (e.g. WAL recovery)
    public String execute(Request request, boolean writeToWal) {

        switch (request.getCommand()) {

            case SET:

                storage.set(request.getKey(), request.getValue());

                if (writeToWal) {
                    wal.append("SET "
                            + request.getKey()
                            + " "
                            + request.getValue());
                }

                return "OK";

            case GET:

                String value = storage.get(request.getKey());

                return value == null ? "NULL" : value;

            case DELETE:

                if (storage.delete(request.getKey())) {

                    if (writeToWal) {
                        wal.append("DELETE "
                                + request.getKey());
                    }

                    return "Deleted";
                }

                return "Key Not Found";

            case UPDATE:

                if (storage.update(request.getKey(), request.getValue())) {

                    if (writeToWal) {
                        wal.append("UPDATE "
                                + request.getKey()
                                + " "
                                + request.getValue());
                    }

                    return "Updated";
                }

                return "Key Not Found";

            case EXISTS:

                return storage.containsKey(request.getKey())
                        ? "true"
                        : "false";

            case COUNT:

                return String.valueOf(storage.count());

            case KEYS:

                return storage.keys().toString();

            case CLEAR:

                storage.clear();

                if (writeToWal) {
                    wal.append("CLEAR");
                }

                return "Database Cleared";

            case EXIT:

                return "BYE";

            default:

                return "Unsupported Command";

        }

    }

}