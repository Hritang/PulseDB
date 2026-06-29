package com.hritang.pulsedb.storage;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StorageEngine {

    private final ConcurrentHashMap<String, String> storage;

    public StorageEngine() {
        storage = new ConcurrentHashMap<>();
    }

    public void set(String key, String value) {
        storage.put(key, value);
    }

    public String get(String key) {
        return storage.get(key);
    }

    public boolean delete(String key) {
        return storage.remove(key) != null;
    }

    public boolean containsKey(String key) {
        return storage.containsKey(key);
    }

    public int count() {
        return storage.size();
    }

    public void clear() {
        storage.clear();
    }

    public Set<String> keys() {
        return storage.keySet();
    }

    public boolean update(String key, String value) {

        if (!storage.containsKey(key)) {
            return false;
        }

        storage.put(key, value);
        return true;
    }

}