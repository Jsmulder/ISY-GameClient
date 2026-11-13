package nl.isygameclient.controllers.games.tictactoe;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import nl.isygameclient.Window;
import nl.isygameclient.controllers.games.GameController;
import nl.isygameclient.models.Game;
import nl.isygameclient.models.Player;
import nl.isygameclient.models.PlayerManager;
import nl.isygameclient.models.games.TicTacToe;
import nl.isygameclient.util.StageHandler;
import nl.isygameclient.util.Vector2D;

public class TicTacToeSinglePlayerController implements GameController {

	private Game game;
	private final ArrayList<JFXButton> boardButtons = new ArrayList<>();

	@FXML
	protected Label currentPlayer;
	@FXML
	public Label gameOverText;

	@FXML
	protected GridPane grid;


	@FXML
	protected void initialize() {
		initializeGame();
		initializeBoard();
		updateCurrentPlayerLabel();
	}

	private void  initializeGame() {
		var players = new ArrayList<>(List.of(new Player("player1", "x") {
			@Override
			public Vector2D<Integer, Integer> onPlayerTurn() {
				return null;
			}
		}, new Player("player2", "o") {
			@Override
			public Vector2D<Integer, Integer> onPlayerTurn() {
				return null;
			}
		}));
		var manager = new PlayerManager(0, players);
		game = new TicTacToe(manager);

	}


	private void initializeBoard() {
		var board = game.getBoard();
		for (int y = 0; y < board.getWidth(); y++) {
			for (int x = 0; x < board.getHeight(); x++) {
				JFXButton button = new JFXButton();

				var index = x + "," + y;
				button.setId(index);
				button.setMinSize(200.0, 200.0);
				var styleClass = button.getStyleClass();
				styleClass.add("ttt-button");
				styleClass.add("display-large");
				button.setOnAction((ActionEvent event) -> onMoveButtonClick(button));
				boardButtons.add(button);
				grid.add(button, x, y);
			}
		}
	}

	private void updateCurrentPlayerLabel() {
		currentPlayer.setText(game.getPlayerManager().getCurrentPlayer().getPlayingAs());
	}

	private void onMoveButtonClick(JFXButton button) {
		// Move
		var id = button.getId().split(",");
		var index = Stream.of(id).mapToInt(Integer::parseInt).toArray();
		var manager = game.getPlayerManager();
		var currentPlayer = manager.getCurrentPlayer();
		if (game.isMoveValid(currentPlayer, new Vector2D<>(index[0], index[1]))) {
			game.move(currentPlayer, new Vector2D<>(index[0], index[1]));
			button.setText(currentPlayer.getPlayingAs());
			updateCurrentPlayerLabel();
		}

		// Game Over
		if (game.isGameOver()) {
			onGameOver();
		}
	}

	private void onGameOver() {
		disableBoardButtons();
		if (game.isDraw()) {
			gameOverText.setText("Draw!");
			gameOverText.setVisible(true);
		} else {
			var winner = game.getWinners().get(0).getPlayingAs().toUpperCase();
			gameOverText.setText(String.format("%s, is the Winner!\n\n", winner));
			gameOverText.setVisible(true);
		}
	}

	private void clearBoardButtons() {
		for (JFXButton button : boardButtons) {
			button.setText("");
		}
	}

	private void disableBoardButtons() {
		for (JFXButton button : boardButtons) {
			button.setDisable(true);
		}
	}

	private void enableBoardButtons() {
		for (JFXButton button : boardButtons) {
			button.setDisable(false);
		}
	}

	@Override
	public void onNewGameButtonClick(ActionEvent e) {
		// Make new Game
		game.restart();

		clearBoardButtons();
		enableBoardButtons();
		gameOverText.setVisible(false);
	}

	@Override
	public void onMainMenuButtonClick(ActionEvent e) {
		StageHandler.get().changeSceneOfStage(Window.GAME,"/nl/isygameclient/views/games/tictactoe/TicTacToeMainMenu.fxml");
	}
}
