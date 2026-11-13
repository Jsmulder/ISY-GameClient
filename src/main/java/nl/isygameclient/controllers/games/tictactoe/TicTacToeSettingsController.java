package nl.isygameclient.controllers.games.tictactoe;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class TicTacToeSettingsController implements Initializable {
    public JFXComboBox<String> opponentCombo;
    public JFXComboBox<String> playingAsCombo;
    public JFXComboBox<String> difficultyCombo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onDifficultyComboSelect(ActionEvent actionEvent) {
    }

    public void onPlayingAsComboSelect(ActionEvent actionEvent) {
    }

    public void onOpponentComboSelect(ActionEvent actionEvent) {
    }
}
