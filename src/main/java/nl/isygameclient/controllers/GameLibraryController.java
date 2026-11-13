package nl.isygameclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import nl.isygameclient.views.GameCardControl;

import java.net.URL;
import java.util.ResourceBundle;

import static nl.isygameclient.Application.GAMES;

public class GameLibraryController implements Initializable {
    @FXML
    public FlowPane gameCards;
    @FXML
    public JFXTextField searchTextField;
    @FXML
    public JFXButton searchButton;
    @FXML
    public JFXButton searchClear;
    @FXML
    public JFXButton searchOptions;
    private FilteredList<GameCardControl> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filteredList = new FilteredList<>(FXCollections.observableList(GAMES));
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = searchTextField.getText().toLowerCase();
            filteredList.setPredicate(n -> n.getGameTitle().toLowerCase().contains(searchText));
        });
        Bindings.bindContent(gameCards.getChildren(), filteredList);
    }

    @FXML
    protected void onClearSearchButtonClick() {
        searchTextField.clear();
    }


}