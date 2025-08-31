package com.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.nio.file.Paths;

public class ConfigReader {
    private static JsonObject config;

    public static JsonObject getConfig() {
        if (config == null) {
            String env = System.getProperty("env", "prod"); // default → staging
            String path = Paths.get("src", "test", "resources", "config", "config." + env + ".json").toString();

            try (FileReader reader = new FileReader(path)) {
                config = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (Exception e) {
                throw new RuntimeException("❌ Failed to load config file for env: " + env + " | path: " + path, e);
            }
        }
        return config;
    }
}
