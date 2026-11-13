package nl.isygameclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import nl.isygameclient.util.StageHandler;
import nl.isygameclient.views.NavButtonControl;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class AppController implements Initializable {

    private final TreeMap<Integer, NavButtonControl> navButtons = new TreeMap<>(Map.ofEntries(
            Map.entry(0, new NavButtonControl("Store", "LOCAL_MALL", null)),
            Map.entry(1, new NavButtonControl("Library", "GAMES", "/nl/isygameclient/views/game-library.fxml")),
            Map.entry(2, new NavButtonControl("Community", "QUESTION_ANSWER", null)),
            Map.entry(3, new NavButtonControl("Servers", "FORMAT_LIST_BULLETED", null))
    ));
    private NavButtonControl activeButton;
    @FXML
    public BorderPane app;
    @FXML
    private VBox navBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navButtons.forEach((index, button) -> {
            button.setOnAction(this::onNavButtonPress);
            navBar.getChildren().add(button);
        });

        activeButton = navButtons.get(1);
        activeButton.activate();
        loadScene(activeButton.getSource());
    }

    @FXML
    public void onNavButtonPress(ActionEvent e) {
        var newButton = (NavButtonControl) e.getSource();
        activeButton.deactivate();
        activeButton = newButton;
        newButton.activate();

        loadScene(activeButton.getSource());
    }

    @FXML
    public void onSettingsButtonPressed(){
        loadScene("/nl/isygameclient/views/app-settings.fxml");
    }

    private void loadScene(String path) {
        app.getChildren().remove(app.getCenter());

        if (path == null) return;

        URL url = StageHandler.class.getResource(path);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            app.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
