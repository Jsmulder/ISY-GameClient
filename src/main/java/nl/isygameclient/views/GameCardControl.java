package nl.isygameclient.views;

import com.jfoenix.controls.JFXButton;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import nl.isygameclient.Window;
import nl.isygameclient.util.StageHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class GameCardControl extends VBox implements Initializable {

    private final String gameTitle;
    private final String gameImageUrl;
    private final String gameSource;

    @FXML
    private Label label;

    @FXML
    private JFXButton button;

    @FXML
    private ImageView imageView;


    public GameCardControl(@NamedArg("gameTitle") String gameTitle, @NamedArg("gameImageUrl") String gameImageUrl, @NamedArg("gameSource") String gameSource) {
        this.gameTitle = gameTitle;
        this.gameImageUrl = gameImageUrl;
        this.gameSource = gameSource;

        URL url = getClass().getResource("/nl/isygameclient/views/game-card.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.label.textProperty().set(gameTitle);

        InputStream imageURL = getClass().getResourceAsStream(gameImageUrl);
        if (imageURL != null) {
            Image image = new Image(imageURL, 250, 200, false, false);
            this.imageView.setImage(image);
        }

        button.setOnAction(this::onStartGameButtonClick);
    }

    public void onStartGameButtonClick(ActionEvent e) {
        var stageHandler = StageHandler.get();
        stageHandler.changeSceneOfStage(Window.GAME, gameSource);
        stageHandler.focusStage(Window.GAME);
    }

    public String getGameTitle() {
        return gameTitle;
    }
}
