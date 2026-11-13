package nl.isygameclient.controllers.games.othello;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import nl.isygameclient.Window;
import nl.isygameclient.controllers.games.GameController;
import nl.isygameclient.models.Ai;
import nl.isygameclient.models.Game;
import nl.isygameclient.models.Player;
import nl.isygameclient.models.PlayerManager;
import nl.isygameclient.models.games.Othello;
import nl.isygameclient.util.StageHandler;
import nl.isygameclient.util.Vector2D;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class OthelloSinglePlayerController implements GameController, Initializable {

    private Game game;
    private JFXButton[][] boardButtons;

    @FXML
    private GridPane boardGrid;

    private final int[][] heuristics = {
            {4, 3, 3, 3, 3, 3, 3, 4},
            {3, 2, 2, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 3},
            {4, 3, 3, 3, 3, 3, 3, 4}
    };

    private final boolean hasClicked = false;
    private Vector2D<Integer, Integer> clickedPos;
    private final boolean againstAi = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var player = new Player("player1", "white") {
            @Override
            public Vector2D<Integer, Integer> onPlayerTurn() {
                return clickedPos;
            }
        };
        var players = List.of(player, new Ai("player2", "black", heuristics));
        var manager = new PlayerManager(0, new ArrayList<>(players));
        game = new Othello(manager);
        initializeBoard();
        updateButtons();
    }

    private void initializeBoard() {
        var board = game.getBoard();
        boardButtons = new JFXButton[board.getHeight()][board.getWidth()];
        for (int x = 0; x < board.getHeight(); x++) {
            for (int y = 0; y < board.getWidth(); y++) {
                JFXButton button = new JFXButton();
                var index = x + "," + y;
                button.setId(index);
                button.setMinSize(60.0, 60.0);
                button.setMaxSize(60.0, 60.0);

                var styleClass = button.getStyleClass();
                styleClass.add("othello-button");
                styleClass.add("headline-small");
                button.setOnAction((ActionEvent event) -> onMoveButtonClick(button));
                boardButtons[y][x] = button;
                boardGrid.add(button, x, y);
            }
        }
    }

    private void updateButtons() {
        clearBoardButtons();
        var board = game.getBoard();
        for (int x = 0; x < board.getHeight(); x++) {
            for (int y = 0; y < board.getWidth(); y++) {
                var index = board.get(new Vector2D<>(x, y));
                if (index == null) continue;
                if (Objects.equals("white", index.getPlayingAs())) {
                    addStone(boardButtons[y][x], Color.WHITE);
                } else if (Objects.equals("black", index.getPlayingAs())) {
                    addStone(boardButtons[y][x], Color.BLACK);
                }
            }
        }
    }

    private void addStone(JFXButton button, Color color) {
        var circle = new Circle();
        circle.setRadius(20);
        circle.setFill(color);
        button.setGraphic(circle);
    }
    private void doMove(Vector2D<Integer, Integer> move) {
        var manager = game.getPlayerManager();
        var currentPlayer = manager.getCurrentPlayer();

        if (game.getValidMoves(currentPlayer).isEmpty()) {
            manager.nextPlayer();
            return;
        }
        if (game.isMoveValid(currentPlayer, move)) {
            game.move(currentPlayer, move);
            updateButtons();
        }
    }

    private void onMoveButtonClick(JFXButton button) {
        // Move
        var id = button.getId().split(",");
        var index = Stream.of(id).mapToInt(Integer::parseInt).toArray();

        var playerMove = new Vector2D<>(index[0], index[1]);
        doMove(playerMove);

        if (againstAi) {
            var manager = game.getPlayerManager();
            var move = manager.getCurrentPlayer().onPlayerTurn();
            doMove(move);
        }

        if (game.isGameOver()) {
            disableBoardButtons();
        }
    }

    private void clearBoardButtons() {
        for (JFXButton[] row : boardButtons) {
            for (JFXButton button : row) {
                button.setGraphic(null);
            }
        }
    }

    private void disableBoardButtons() {
        for (JFXButton[] row : boardButtons) {
            for (JFXButton button : row) {
                button.setDisable(true);
            }
        }
    }

    private void enableBoardButtons() {
        for (JFXButton[] row : boardButtons) {
            for (JFXButton button : row) {
                button.setDisable(false);
            }
        }
    }

    @Override
    public void onNewGameButtonClick(ActionEvent actionEvent) {
        game.restart();
        updateButtons();
        enableBoardButtons();
    }

    @Override
    public void onMainMenuButtonClick(ActionEvent actionEvent) {
        StageHandler.get().changeSceneOfStage(Window.GAME, "/nl/isygameclient/views/games/othello/OthelloMainMenu.fxml");
    }
}
