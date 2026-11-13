package nl.isygameclient.controllers.games.othello;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nl.isygameclient.Window;
import nl.isygameclient.controllers.games.GameController;
import nl.isygameclient.controllers.games.GameMenuController;
import nl.isygameclient.util.StageHandler;

public class OthelloMainMenuController implements GameMenuController {
	@Override
	public void onSinglePlayerButtonClick(ActionEvent e) {
		StageHandler.get().changeSceneOfStage(Window.GAME, "/nl/isygameclient/views/games/othello/OthelloSinglePlayer.fxml");
	}

	@Override
	public void onMultiPlayerButtonClick(ActionEvent e) {
		StageHandler.get().changeSceneOfStage(Window.GAME,"/nl/isygameclient/views/games/othello/OthelloMultiPlayer.fxml");
	}

	@Override
	public void onExitGameButtonClick(ActionEvent e) {
		var stageHandler = StageHandler.get();
		stageHandler.getStage(Window.GAME).hide();
		stageHandler.focusStage(Window.APP);
	}
}
