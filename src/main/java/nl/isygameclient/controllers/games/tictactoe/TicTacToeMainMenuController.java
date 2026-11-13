package nl.isygameclient.controllers.games.tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nl.isygameclient.Window;
import nl.isygameclient.controllers.games.GameMenuController;
import nl.isygameclient.util.StageHandler;

public class TicTacToeMainMenuController implements GameMenuController {
	@FXML
	public void onSinglePlayerButtonClick(ActionEvent e) {
		StageHandler.get().changeSceneOfStage(Window.GAME, "/nl/isygameclient/views/games/tictactoe/TicTacToeSinglePlayer.fxml");
	}

	@FXML
	public void onMultiPlayerButtonClick(ActionEvent e) {
		StageHandler.get().changeSceneOfStage(Window.GAME,"/nl/isygameclient/views/games/tictactoe/TicTacToeMultiPlayer.fxml");
	}

	@FXML
	public void onExitGameButtonClick(ActionEvent e) {
		var stageHandler = StageHandler.get();
		stageHandler.getStage(Window.GAME).hide();
		stageHandler.focusStage(Window.APP);
	}

}
