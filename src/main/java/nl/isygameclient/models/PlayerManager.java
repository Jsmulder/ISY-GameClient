package nl.isygameclient.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerManager {
	protected int	startingPlayerIndex;
	protected int	currentPlayerIndex;
	protected final List<Player> players;

	public PlayerManager(int startingPlayerIndex, List<Player> players) {
		this.startingPlayerIndex = startingPlayerIndex;
		this.currentPlayerIndex	 = startingPlayerIndex;
		this.players			 = players;
	}

	public void restart() {
		currentPlayerIndex = startingPlayerIndex;
	}

	public void nextPlayer() {
		currentPlayerIndex += 1;
		if (currentPlayerIndex >= players.size()) {
			currentPlayerIndex = 0;
		}
	}
	public void previousPlayer() {
		currentPlayerIndex -= 1;
		if (currentPlayerIndex < 0) {
			currentPlayerIndex = players.size() - 1;
		}
	}

	public boolean isCurrentPlayer(Player player) {
		return Objects.equals(player, getCurrentPlayer());
	}

	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public Player getStartingPlayer() {
		return players.get(startingPlayerIndex);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getStartingPlayerIndex() {
		return startingPlayerIndex;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setStartingPlayerIndex(int startingPlayerIndex) {
		this.startingPlayerIndex = startingPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}
}
