package nl.isygameclient.controllers.games.tictactoe;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import nl.isygameclient.Window;
import nl.isygameclient.controllers.games.GameController;
import nl.isygameclient.models.Game;
import nl.isygameclient.network.GameClient;
import nl.isygameclient.network.GameType;
import nl.isygameclient.network.Match;
import nl.isygameclient.util.StageHandler;

public class TicTacToeMultiPlayerController implements GameController,  Initializable, Runnable {
	private final ArrayList<JFXButton> boardButtons = new ArrayList<>();
	private final String[] players = { "X", "O" };
	private Game game;

	@FXML
	private Label playingAgainstLabel;
	@FXML
	private TextField hostField;
	@FXML
	private TextField portField;
	@FXML
	private TextField nameField;
	@FXML
	private TextField opponentField;
	@FXML
	private JFXComboBox<String> playingAsCombo;
	@FXML
	private Label currentPlayer;
	@FXML
	private Label gameOverText;
	@FXML
	private GridPane grid;

	private GameClient client;
	private Match	   match;
	private String	   playerSelf = "X", playerOther = "O";
	private boolean	   running;

	private Thread gameThread;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		for (int i = 0; i < 3 * 3; i++) {
			JFXButton button = new JFXButton();
			button.setId(Integer.toString(i));
			button.setMinSize(200.0, 200.0);
			var styleClass = button.getStyleClass();
			styleClass.add("ttt-button");
			styleClass.add("display-large");
			button.setOnAction((ActionEvent event) -> onMoveButtonClick(button));
			boardButtons.set(i, button);
			grid.add(button, i / 3, i % 3);
		}

		playingAsCombo.getItems().setAll(players);
		playingAsCombo.getSelectionModel().selectFirst();

		disableBoardButtons();
		//		currentPlayer.setText();
	}

	public void run() {
		running = true;
		try {
			client = new GameClient(hostField.getText(), Integer.parseInt(portField.getText()));
			client.login(nameField.getText());

			if (opponentField.getText().length() == 0)
				match = client.match(GameType.TICTACTOE);
			else
				match = client.match(GameType.TICTACTOE, opponentField.getText());

			while (running) {
				match.update();

				if (!match.isStarted())
					continue;

				// game.setCurrentPlayer(match.isYourTurn() ? playerSelf : playerOther);
				// game.move(Integer.parseInt(moveMap.get("move")));
				Platform.runLater(() -> {
					playingAgainstLabel.setText(match.getOpponent());

					if (match.isYourTurn()) {
						currentPlayer.setText(playerSelf);
						enableBoardButtons();
					} else {
						currentPlayer.setText(playerOther);
						disableBoardButtons();
					}
					for (int i = 0; i < 3 * 3; i++) {
						switch (match.getMove(i)) {
							case -1 -> boardButtons.get(i).setText(playerOther);
							case 0 -> boardButtons.get(i).setText("");
							case 1 -> boardButtons.get(i).setText(playerSelf);
						}
					}
				});

				if (match.getOutcome() == null)
					continue;

				running = false;

				Platform.runLater(() -> {
					switch (match.getOutcome().type) {
						case DRAW:
							System.out.println("Draw!");
							gameOverText.setText("Draw!");
							gameOverText.setVisible(true);
							break;
						case LOSS:
							System.out.printf("%s, Is the Winner!\n\n", playerOther);
							gameOverText.setText(String.format("%s, is the Winner!\n\n", playerOther));
							gameOverText.setVisible(true);
							break;
						case WIN:
							System.out.printf("%s, Is the Winner!\n\n", playerSelf);
							gameOverText.setText(String.format("%s, is the Winner!\n\n", playerSelf));
							gameOverText.setVisible(true);
						default:
					}
				});
			}
			match.abort();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private void onMoveButtonClick(JFXButton button) {
		try {
			// Move
			int pos = Integer.parseInt(button.getId());
			//			if (game.isMoveValid(pos))
			match.move(pos);
			//	currentPlayer.setText(playerOther);

			//			updateBoard();
			//			currentPlayer.setText(playerSelf);
		} catch (Exception exc) {
			throw new RuntimeException(exc);
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

	@FXML
	protected void onPlayingAsComboSelect() {
		System.out.printf("Now playing As: %s\n", playingAsCombo.getValue());
		switch (playingAsCombo.getValue()) {
			case "X" -> {
				playerSelf = "X";
				playerOther = "O";
			}
			case "O" -> {
				playerSelf = "O";
				playerOther = "X";
			}
		}
	}

	@Override
	public void onNewGameButtonClick(ActionEvent e) throws NumberFormatException, InterruptedException {
		gameOverText.setVisible(false);

		if (gameThread != null && running) {
			running = false;
			gameThread.join();
		}
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void onMainMenuButtonClick(ActionEvent e) {
		StageHandler.get().changeSceneOfStage(Window.GAME,"/nl/isygameclient/views/games/tictactoe/TicTacToeMainMenu.fxml");
	}
}
