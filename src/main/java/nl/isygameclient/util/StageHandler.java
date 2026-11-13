package nl.isygameclient.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.isygameclient.Window;
import nl.isygameclient.settings.Settings;
import nl.isygameclient.settings.StageSettings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nl.isygameclient.settings.StageSettings.configureStageFromSettings;
import static nl.isygameclient.settings.StageSettings.createSettingsFromStage;

/**
 * This class is a singleton that stores and manages all the active stages in the application.
 * @author A F Koens.
 */
public class StageHandler {
    private static StageHandler instance;
    private final Map<Window, Stage> stages = new HashMap<>();

    private StageHandler() {
    }

    /**
     * This function is for accessing singleton instance of this class.
     * @return instance op StageHandler.
     */
    public static StageHandler get() {
        if (instance == null) {
            instance = new StageHandler();
        }
        return instance;
    }

    /**
     * This function is for accessing a stage stored inside this class.
     * @param stageName the key for map of stages.
     * @return a stage object.
     */
    public Stage getStage(Window stageName) {
        Stage stage = stages.get(stageName);
        if (stage == null) stage = new Stage();
        stages.put(stageName, stage);
        return stage;
    }

    /**
     * This function is for iconifying also known as minifying a stage window.
     * @param stageName the key for map of stages.
     */
    public void iconifyStage(Window stageName) {
        stages.get(stageName).setIconified(true);
    }

    /**
     * This function is for bringing a stage into view and focussing on it.
     * @param stageName the key for map of stages.
     */
    public void focusStage(Window stageName) {
        Stage stage = stages.get(stageName);
        stage.setIconified(false);
        stage.show();
        stage.requestFocus();
    }

    /**
     * This function changes the scene of a given stage.
     * @param stageName the key for map of stages.
     * @param viewSource the source of the nieuw view for the scene.
     */
    public void changeSceneOfStage(Window stageName, String viewSource) {
        Stage stage = getStage(stageName);
        try {
            FXMLLoader fxmlSceneLoader = new FXMLLoader(getClass().getResource(viewSource));
            Scene scene = new Scene(fxmlSceneLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void closeStages() {
        for (Stage stage :
                stages.values()) {
            stage.hide();
        }
    }

    /**
     * This function loads all the stages stored in the settings file into the singleton.
     */
    public void loadStages() {
        Settings settings = SettingsFileHandler.load();
        for (Map.Entry<Window, StageSettings> entry : settings.stages.entrySet()) {
            stages.put(entry.getKey(), configureStageFromSettings(entry.getValue()));
        }
    }

    /**
     * This function saves all the stages from inside the singleton into the settings file.
     */
    public void saveStages() {
        Settings settings = SettingsFileHandler.load();
        for (Map.Entry<Window, Stage> entry : stages.entrySet()) {
            settings.stages.put(entry.getKey(), createSettingsFromStage(entry.getValue()));
        }
        SettingsFileHandler.save(settings);
    }
}
