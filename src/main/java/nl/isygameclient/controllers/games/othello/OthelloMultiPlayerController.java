package nl.isygameclient.controllers.games.othello;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
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
import nl.isygameclient.network.GameClient;
import nl.isygameclient.network.GameType;
import nl.isygameclient.network.Match;
import nl.isygameclient.util.StageHandler;
import nl.isygameclient.util.Vector2D;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class OthelloMultiPlayerController implements GameController, Initializable, Runnable {

    private Game game;
    private Thread gameThread;
    private boolean running;

    private GameClient client;
    private Match match;
    private final String playerSelf = "X";
    private final String playerOther = "O";

    private JFXButton[][] boardButtons;

    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nameField;

    @FXML
    private TextField opponentField;
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
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                JFXButton button = new JFXButton();
                var index = i + "," + j;
                button.setId(index);
                button.setMinSize(60.0, 60.0);
                button.setMaxSize(60.0, 60.0);

                var styleClass = button.getStyleClass();
                styleClass.add("othello-button");
                styleClass.add("headline-small");
                button.setOnAction((ActionEvent event) -> onMoveButtonClick(button));
                boardButtons[i][j] = button;
                boardGrid.add(button, i, j);
            }
        }
    }

    private void updateButtons() {
        clearBoardButtons();
        var board = game.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                var index = board.get(new Vector2D<>(i, j));
                if (index == null) continue;
                if (Objects.equals("white", index.getPlayingAs())) {
                    addStone(boardButtons[i][j], Color.WHITE);
                } else if (Objects.equals("black", index.getPlayingAs())) {
                    addStone(boardButtons[i][j], Color.BLACK);
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

    private void onMoveButtonClick(JFXButton button) {
        // Move
        var id = button.getId().split(",");
        var index = Stream.of(id).mapToInt(Integer::parseInt).toArray();
        var manager = game.getPlayerManager();
        var currentPlayer = manager.getCurrentPlayer();
        if (game.getValidMoves(currentPlayer).isEmpty()) {
            manager.nextPlayer();
            return;
        }
        if (game.isMoveValid(currentPlayer, new Vector2D<>(index[0], index[1]))) {
            game.move(currentPlayer, new Vector2D<>(index[0], index[1]));
            updateButtons();
        }

        if (game.isGameOver()) {
            disableBoardButtons();
        }
    }

    public void run() {
        running = true;
//        try {
//            client = new GameClient(hostField.getText(), Integer.parseInt(portField.getText()));
//            client.login(nameField.getText());
//
//            if (opponentField.getText().length() == 0)
//                match = client.match(GameType.TICTACTOE);
//            else
//                match = client.match(GameType.TICTACTOE, opponentField.getText());
//
//            while (running) {
//                match.update();
//
//                if (!match.isStarted())
//                    continue;
//
//                // game.setCurrentPlayer(match.isYourTurn() ? playerSelf : playerOther);
//                // game.move(Integer.parseInt(moveMap.get("move")));
//                Platform.runLater(() -> {
//                    playingAgainstLabel.setText(match.getOpponent());
//
//                    if (match.isYourTurn()) {
//                        currentPlayer.setText(playerSelf);
//                        enableBoardButtons();
//                    } else {
//                        currentPlayer.setText(playerOther);
//                        disableBoardButtons();
//                    }
//                    for (int i = 0; i < 3 * 3; i++) {
//                        switch (match.getMove(i)) {
//                            case -1 -> boardButtons.get(i).setText(playerOther);
//                            case 0 -> boardButtons.get(i).setText("");
//                            case 1 -> boardButtons.get(i).setText(playerSelf);
//                        }
//                    }
//                });
//
//                if (match.getOutcome() == null)
//                    continue;
//
//                running = false;
//
//                Platform.runLater(() -> {
//                    switch (match.getOutcome().type) {
//                        case DRAW:
//                            System.out.println("Draw!");
//                            gameOverText.setText("Draw!");
//                            gameOverText.setVisible(true);
//                            break;
//                        case LOSS:
//                            System.out.printf("%s, Is the Winner!\n\n", playerOther);
//                            gameOverText.setText(String.format("%s, is the Winner!\n\n", playerOther));
//                            gameOverText.setVisible(true);
//                            break;
//                        case WIN:
//                            System.out.printf("%s, Is the Winner!\n\n", playerSelf);
//                            gameOverText.setText(String.format("%s, is the Winner!\n\n", playerSelf));
//                            gameOverText.setVisible(true);
//                        default:
//                    }
//                });
//            }
//            match.abort();
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

    @FXML
    public void onNewGameButtonClick(ActionEvent e) throws NumberFormatException, InterruptedException {
        game.restart();
//        gameOverText.setVisible(false);

        if (gameThread != null && running) {
            running = false;
            gameThread.join();
        }
        gameThread = new Thread(this);
        gameThread.start();

        updateButtons();
        enableBoardButtons();
    }

    @Override
    public void onMainMenuButtonClick(ActionEvent e) {
        StageHandler.get().changeSceneOfStage(Window.GAME, "/nl/isygameclient/views/games/othello/OthelloMainMenu.fxml");

    }
}
