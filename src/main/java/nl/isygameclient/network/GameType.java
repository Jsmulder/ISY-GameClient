package nl.isygameclient.network;

public enum GameType {
	TICTACTOE("tic-tac-toe", 9),
	TICTACTOE4("tic-tac-toe-4", 16),
	REVERSI("reversi", 64);

	public final String name;
	public final int	maxMoves;

	GameType(String name, int maxMoves) {
		this.name	  = name;
		this.maxMoves = maxMoves;
	}

	public static GameType byName(String name) {
		for (GameType game : GameType.values())
			if (game.name.equals(name))
				return game;
		return null;
	}
}