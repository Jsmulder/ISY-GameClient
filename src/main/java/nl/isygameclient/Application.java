package nl.isygameclient;

import javafx.stage.Stage;
import nl.isygameclient.util.StageHandler;
import nl.isygameclient.views.GameCardControl;

import java.util.List;

/**
 * This class is the start of the javafx application.
 * It holds the main function that launches this class.
 * As well as the start function that handles the application initialization.
 *
 * @author Niels Jager
 * @author Arend Koens
 * @author Johs Mulder
 * @author Friedel Schön
 */
public class Application extends javafx.application.Application {

    public static final List<GameCardControl> GAMES = List.of(
            new GameCardControl("Tic Tac Toe", "/nl/isygameclient/imgs/tictactoe_logo.png", "/nl/isygameclient/views/games/tictactoe/TicTacToeMainMenu.fxml"),
            new GameCardControl("Othello", "/nl/isygameclient/imgs/othello_logo.png", "/nl/isygameclient/views/games/othello/OthelloMainMenu.fxml"));

    public static void main(String[] args) {
        launch();
    }

    /**
     * The start preforms the initialization of the stage.
     *
     * @param stage a javafx stage.
     */
    @Override
    public void start(Stage stage) {
        StageHandler stageHandler = StageHandler.get();
        stageHandler.loadStages();

        stageHandler.changeSceneOfStage(Window.APP, "/nl/isygameclient/views/app.fxml");
        stageHandler.focusStage(Window.APP);

        stageHandler.getStage(Window.APP).setOnCloseRequest((e) -> {
            stageHandler.saveStages();
            stageHandler.closeStages();
        });
    }


}
