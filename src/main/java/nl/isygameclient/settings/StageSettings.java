package nl.isygameclient.settings;

import javafx.stage.Stage;

public class StageSettings {
    public String Title = "ISYGameClient";
    public double x = 0;
    public double y = 0;
    public double width = 680;
    public double height = 480;
    public double minWidth = 0;
    public double minHeight = 0;
    public double maxWidth = 1.7976931348623157E308;
    public double maxHeight = 1.7976931348623157E308;
    public boolean isIconified = false;
    public boolean isAlwaysOnTop = false;
    public boolean isFullScreen = true;
    public boolean isResizable = true;
    public boolean isMaximized = true;
    public boolean isShowing = false;


    public static StageSettings createSettingsFromStage(Stage stage) {
        var stageSettings = new StageSettings();
        stageSettings.Title = stage.getTitle();
        stageSettings.x = stage.getX();
        stageSettings.y = stage.getY();
        stageSettings.width = stage.getWidth();
        stageSettings.height = stage.getHeight();
        stageSettings.minWidth = stage.getMinWidth();
        stageSettings.minHeight = stage.getMinHeight();
        stageSettings.maxWidth = stage.getMaxWidth();
        stageSettings.maxHeight = stage.getMaxHeight();
        stageSettings.isIconified = stage.isIconified();
        stageSettings.isAlwaysOnTop = stage.isAlwaysOnTop();
        stageSettings.isFullScreen = stage.isFullScreen();
        stageSettings.isResizable = stage.isResizable();
        stageSettings.isMaximized = stage.isMaximized();
        stageSettings.isShowing = stage.isShowing();
        return stageSettings;
    }

    public static Stage configureStageFromSettings(StageSettings stageSettings) {
        Stage stage = new Stage();
        stage.setTitle(stageSettings.Title);
        stage.setX(stageSettings.x);
        stage.setY(stageSettings.y);
        stage.setWidth(stageSettings.width);
        stage.setHeight(stageSettings.height);
        stage.setMinWidth(stageSettings.minWidth);
        stage.setMinHeight(stageSettings.minHeight);
        stage.setMaxWidth(stageSettings.maxWidth);
        stage.setMaxHeight(stageSettings.maxHeight);
        stage.setIconified(stageSettings.isIconified);
        stage.setAlwaysOnTop(stageSettings.isAlwaysOnTop);
        stage.setFullScreen(stageSettings.isFullScreen);
        stage.setResizable(stageSettings.isResizable);
        stage.setMaximized(stageSettings.isMaximized);
        return stage;
    }
}
