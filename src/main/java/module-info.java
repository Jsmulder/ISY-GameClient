module nl.isygameclient {

	// JavaFX - FXMl
	requires javafx.fxml;
	requires javafx.controls;
	requires org.controlsfx.controls;

	// Material Design
	requires de.jensd.fx.glyphs.materialicons;
	requires de.jensd.fx.glyphs.commons;
	requires com.jfoenix;

	//	Json Class Data Saving
	requires com.google.gson;

    exports nl.isygameclient;

	exports nl.isygameclient.controllers.games.tictactoe;


	exports nl.isygameclient.controllers;
	exports nl.isygameclient.models;
	exports nl.isygameclient.views;
	exports nl.isygameclient.util;

	exports nl.isygameclient.models.board;
	exports nl.isygameclient.models.games;
	exports nl.isygameclient.settings;


	opens nl.isygameclient to javafx.fxml;
	opens nl.isygameclient.views to javafx.fxml;

	opens nl.isygameclient.controllers to javafx.fxml;
	opens nl.isygameclient.controllers.games to javafx.fxml;
	opens nl.isygameclient.controllers.games.tictactoe to javafx.fxml;
    opens nl.isygameclient.controllers.games.othello to javafx.fxml;

	opens nl.isygameclient.models to com.google.gson;
	opens nl.isygameclient.models.board to com.google.gson;
	opens nl.isygameclient.settings to com.google.gson;
	opens nl.isygameclient.models.games to com.google.gson;
	opens nl.isygameclient.util to com.google.gson;

}
