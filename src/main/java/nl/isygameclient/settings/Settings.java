package nl.isygameclient.settings;

import nl.isygameclient.Window;

import java.util.HashMap;
import java.util.Map;


/**
 * This Class holds default settings for the application.
 * It also provides the format of the settings that are saved to a file.
 *
 * @author A F Koens
 */
public class Settings {
    public Map<Window, StageSettings> stages = new HashMap<>();

    public boolean darkMode = true;
    public boolean tournamentMode = false;
}
