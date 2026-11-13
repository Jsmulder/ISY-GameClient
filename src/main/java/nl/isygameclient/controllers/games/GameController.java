package nl.isygameclient.controllers.games;

import javafx.event.ActionEvent;

public interface GameController {

    void onNewGameButtonClick(ActionEvent e) throws InterruptedException;
    void onMainMenuButtonClick(ActionEvent e);
}
