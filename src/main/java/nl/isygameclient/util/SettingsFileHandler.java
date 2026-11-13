package nl.isygameclient.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.isygameclient.settings.Settings;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * This Class is for accessing and manipulating settings files.
 * This class handles the loading and saving of settings data.
 * @author A F Koens
 */
public abstract class SettingsFileHandler {
    public static final String SETTINGS_FILENAME = "settings.json";


    /**
     * Save a settings object to a json file.
     * @param settings a Settings object.
     * */
    public static void save(Settings settings) {
        try {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            FileWriter writer = new FileWriter(SETTINGS_FILENAME);
            writer.write(gson.toJson(settings));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read a json file and loads its values into a Settings object.
     * @return a Settings object.
     * */
    public static Settings load() {
        try {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            String inFile = new String(Files.readAllBytes(Paths.get(SETTINGS_FILENAME)));
            return gson.fromJson(inFile, Settings.class);
        } catch (IOException e) {
            System.err.println("Could Not Load Settings File, Using Defaults Instead");
            var defaults = new Settings();
            save(defaults);
            return defaults;
        }
    }
}

