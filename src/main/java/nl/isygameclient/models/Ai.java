package nl.isygameclient.models;

import java.security.SecureRandom;
import java.util.*;
import nl.isygameclient.util.Vector2D;

public class Ai extends Player {

	public static final int	   DEPTH	 = 5;
	public static final Random AI_RANDOM = new Random();

	private final int[][] heuristics;

	public Ai(String name, String playingAs, int[][] heuristics) {
		super(name, playingAs);
		this.heuristics = heuristics;
	}

	public static <T> T randomMove(Collection<T> possibleMoves) {
		int size = possibleMoves.size();
		int item = AI_RANDOM.nextInt(size);	   // In real life, the Random object should be rather more shared than this
		int i	 = 0;
		for (T obj : possibleMoves)
			if (i == item)
				return obj;
			else
				i++;
		return null;
	}

	private double miniMaxAlphaBeta(double alpha, double beta, Integer depth, boolean isMaximizing) {
		var playerManager = game.getPlayerManager();
		var currentPlayer = playerManager.getCurrentPlayer();
		var possibleMoves = game.getValidMoves(currentPlayer);

		if (depth == 0 || game.isGameOver()) {
			return heuristicScore(currentPlayer);
		}

		double bestValue;
		if (isMaximizing) {
			bestValue = Double.MIN_VALUE;
			for (Vector2D<Integer, Integer> move : possibleMoves) {
				game.move(currentPlayer, move);
				double value = miniMaxAlphaBeta(alpha, beta, depth - 1, false);
				game.undo();

				bestValue = Math.max(bestValue, value);
				alpha	  = Math.max(alpha, bestValue);
				if (beta <= alpha) {
					break;
				}
			}

		} else {
			bestValue = Double.MAX_VALUE;
			for (Vector2D<Integer, Integer> move : possibleMoves) {
				game.move(currentPlayer, move);
				double value = miniMaxAlphaBeta(alpha, beta, depth - 1, true);
				game.undo();

				bestValue = Math.min(bestValue, value);
				beta	  = Math.min(beta, bestValue);
				if (beta <= alpha) {
					break;
				}
			}
		}
		return bestValue;
	}

	@Override
	public Vector2D<Integer, Integer> onPlayerTurn() {
		var manager		  = game.getPlayerManager();
		var currentPlayer = manager.getCurrentPlayer();
		var possibleMoves = game.getValidMoves(currentPlayer);

		double bestValue = Integer.MIN_VALUE;
		var	   bestMove	 = randomMove(possibleMoves);

		for (Vector2D<Integer, Integer> move : possibleMoves) {
			game.move(currentPlayer, move);
			double moveValue = miniMaxAlphaBeta(Double.MIN_VALUE, Double.MAX_VALUE, DEPTH, false);
			if (moveValue > bestValue) {
				bestValue = moveValue;
				bestMove  = move;
			}
			game.undo();
		}
		return bestMove;
	}

	public int heuristicScore(Player player) {
		var board = game.getBoard();
		int score = 0;
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				if (Objects.equals(player, board.get(new Vector2D<>(x, y)))) {
					score += heuristics[y][x];
				}
			}
		}
		return score;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
