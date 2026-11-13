package nl.isygameclient.models.games;

import java.util.*;
import java.util.stream.Collectors;
import nl.isygameclient.models.Game;
import nl.isygameclient.models.Player;
import nl.isygameclient.models.PlayerManager;
import nl.isygameclient.models.board.HistoryBoard;
import nl.isygameclient.util.Vector2D;

public class Othello extends Game {

	public Othello(PlayerManager playerManager) {
		super(playerManager, new HistoryBoard<>(8, 8));
		initializeBoard();
	}

	@Override
	public void restart() {
		playerManager.restart();
		board.clear();
		initializeBoard();
	}

	public void initializeBoard() {
		board.set(playerManager.getCurrentPlayer(), new Vector2D<>(3, 4));
		board.set(playerManager.getCurrentPlayer(), new Vector2D<>(4, 3));
		playerManager.nextPlayer();
		board.set(playerManager.getCurrentPlayer(), new Vector2D<>(3, 3));
		board.set(playerManager.getCurrentPlayer(), new Vector2D<>(4, 4));
		playerManager.nextPlayer();
	}

	@Override
	public boolean isDraw() {
		var scores = getScores();
		return scores.values().stream().distinct().count() == 1;
	}

	@Override
	public boolean isGameOver() {
		for (Player player : playerManager.getPlayers()) {
			if (getValidMoves(player).size() > 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isWinner(Player player) {
		Player winner = null;
		int	   score  = 0;
		for (Map.Entry<Player, Integer> entry : getScores().entrySet()) {
			if (entry.getValue() > score) {
				winner = entry.getKey();
				score  = entry.getValue();
			}
		}
		return Objects.equals(player, winner);
	}


	@Override
	public boolean move(Player player, Vector2D<Integer, Integer> pos) {
		if (!isMoveValid(player, pos)) {
			return false;
		}

		var flippable = checkNeighbours(player, pos);

		Map<Vector2D<Integer, Integer>, Player> changes = flippable.stream().collect(Collectors.toMap(e -> e, e -> player));
		board.add(player, changes);
		playerManager.nextPlayer();
		return true;
	}


	@Override
	public boolean isMoveValid(Player player, Vector2D<Integer, Integer> pos) {
		if (board.get(pos) != null)
			return false;

		var flippable = checkNeighbours(player, pos);
		return !flippable.isEmpty();
	}


	private List<Vector2D<Integer, Integer>> rangeContains(Player player, Vector2D<Integer, Integer> pos, Vector2D<Integer, Integer> dir) {
		List<Vector2D<Integer, Integer>> flippable = new ArrayList<>();
		int								 x		   = pos.getX() + dir.getX();
		int								 y		   = pos.getY() + dir.getY();
		while (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight()) {
			var newPos = new Vector2D<>(x, y);
			flippable.add(newPos);
			if (Objects.equals(board.get(newPos), player)) {
				return flippable;
			} else if (board.get(newPos) == null) {
				return null;
			}
			x += dir.getX();
			y += dir.getY();
		}
		return null;
	}


	private Set<Vector2D<Integer, Integer>> checkNeighbours(Player player, Vector2D<Integer, Integer> pos) {
		Set<Vector2D<Integer, Integer>> flippable = new HashSet<>();
		for (int dx = -1; dx <= 1; dx++) {
			if ((pos.getX() + dx) < 0 || (pos.getX() + dx) >= board.getWidth())
				continue;

			for (int dy = -1; dy <= 1; dy++) {
				if ((pos.getY() + dy) < 0 || (pos.getY() + dy) >= board.getHeight())
					continue;

				if (dx == 0 && dy == 0)
					continue;

				var checkPos = board.get(new Vector2D<>(pos.getX() + dx, pos.getY() + dy));
				if (checkPos != null && checkPos != player) {
					List<Vector2D<Integer, Integer>> flip = rangeContains(player, pos, new Vector2D<>(dx, dy));
					if (flip != null)
						flippable.addAll(flip);
				}
			}
		}
		if (!flippable.isEmpty()) {
			flippable.add(pos);
		}
		return flippable;
	}

	@Override
	public List<Vector2D<Integer, Integer>> getValidMoves(Player player) {
		List<Vector2D<Integer, Integer>> valid = new ArrayList<>();
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				if (isMoveValid(player, new Vector2D<>(x, y))) {
					valid.add(new Vector2D<>(x, y));
				}
			}
		}
		return valid;
	}

	@Override
	public List<Player> getWinners() {
		ArrayList<Player> winners = new ArrayList<>();
		for (Player player : playerManager.getPlayers()) {
			if (isWinner(player)) {
				winners.add(player);
			}
		}
		return winners;
	}

	@Override
	public int getPlayerScore(Player player) {
		int score = 0;
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				if (Objects.equals(player, board.get(new Vector2D<>(x, y)))) {
					score += 1;
				}
			}
		}
		return score;
	}

	private HashMap<Player, Integer> getScores() {
		HashMap<Player, Integer> scores = new HashMap<>();
		for (Player player : playerManager.getPlayers()) {
			int score = getPlayerScore(player);
			scores.put(player, score);
		}
		return scores;
	}
}
