package nl.isygameclient.controllers.games;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public interface GameMenuController {

    @FXML
    void onSinglePlayerButtonClick(ActionEvent e);
    @FXML
    void onMultiPlayerButtonClick(ActionEvent e);

    @FXML
    void onExitGameButtonClick(ActionEvent e);
}
