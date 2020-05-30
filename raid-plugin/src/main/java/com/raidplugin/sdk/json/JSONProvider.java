package com.raidplugin.sdk.json;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONProvider {

    private static JSONProvider jsonProvider;

    public static JSONProvider getInstance() {
        return jsonProvider == null ? (jsonProvider = new JSONProvider()) : jsonProvider;
    }

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public <T> T deserialize(Class<T> clazz, File file) {
        try {
            return gson.fromJson(new FileReader(file), clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } return null;
    }

    public <T> void serialize(T object, File target, Class<? extends T> clazz) {
        String text = gson.toJson(object, clazz);
        try {
            Files.write(text.getBytes(), target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
