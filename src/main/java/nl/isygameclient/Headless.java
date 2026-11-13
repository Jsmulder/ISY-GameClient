package nl.isygameclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import nl.isygameclient.models.Ai;
import nl.isygameclient.models.Game;
import nl.isygameclient.models.Player;
import nl.isygameclient.models.PlayerManager;
import nl.isygameclient.models.games.Othello;
import nl.isygameclient.util.DataSaver;
import nl.isygameclient.util.Vector2D;


public class Headless {
	private static final String JSON_FILENAME	 = "heuristics.json";
	private static final int	THREAD_POOL_SIZE = 10;
	private static final int	ROUNDS_PER_GAME	 = 100;

	private static long calculateAverage(List<Long> values) {
		long sum = values.stream().mapToLong(Long::longValue).sum();
		return sum / values.size();
	}

	private static Map<String, Game> createGames(Map<String, int[][]> heuristics) {
		Map<String, Game> games = new HashMap<>();
		for (Map.Entry<String, int[][]> heuristic1 : heuristics.entrySet()) {
			for (Map.Entry<String, int[][]> heuristic2 : heuristics.entrySet()) {

				ArrayList<Player> players = new ArrayList<>();
				players.add(new Ai(heuristic1.getKey(), "black", heuristic1.getValue()));
				players.add(new Ai(heuristic2.getKey(), "white", heuristic2.getValue()));

				var		playerManager = new PlayerManager(0, players);
				Othello othello		  = new Othello(playerManager);
				games.put(heuristic1.getKey() + ", " + heuristic2.getKey(), othello);
			}
		}
		return games;
	}

	private static String buildDataString(
		String gameName, int gameTurns, boolean isDraw, Player winner, long totalGameTime,
		Map<Player, ArrayList<Long>>					   playersTimePerMoves,
		Map<Player, ArrayList<Vector2D<Integer, Integer>>> playersMovesMade) {
		StringBuilder builder = new StringBuilder();

		String joined = String.join(",", gameName, String.valueOf(gameTurns), String.valueOf(isDraw),
									winner.getPlayerName(), winner.getPlayingAs(), String.valueOf(totalGameTime)) +
						",";
		builder.append(joined);

		playersTimePerMoves.forEach((k, v) -> {
			var average = calculateAverage(v);
			builder.append(average).append(",");
		});
		playersMovesMade.forEach((k, v) -> {
			String listString = v.stream().map(Object::toString).collect(Collectors.joining("; "));
			builder.append(listString).append(",");
		});
		return builder.toString();
	}

	private static Map<String, int[][]> loadHeuristics() {
		try {
			Type mapType = new TypeToken<Map<String, int[][]>>() {
			}.getType();
			String inFile = new String(Files.readAllBytes(Paths.get(JSON_FILENAME)));
			return new Gson().fromJson(inFile, mapType);
		} catch (NoSuchFileException e) {
			System.err.println("NO HEURISTICS JSON HAS BEEN PROVIDED.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		var heuristics = loadHeuristics();
		if (heuristics == null) {
			System.err.println("No heuristics in file.");
			return;
		}

		var executor  = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		var games	  = createGames(heuristics);
		var dataSaver = new DataSaver();
		dataSaver.saveData("Name Ai1, Name Ai2, Game Turns, Draw, Winner Name, Winner Playing As, Total Game Time,  Average Time Per Move Ai1, Average Time Per Move Ai2, Moves Ai1, Moves Ai2");
		for (Map.Entry<String, Game> game : games.entrySet()) {
			Runnable simulation = new Simulation(game.getKey(), game.getValue(), ROUNDS_PER_GAME, new ReentrantLock(), dataSaver);
			executor.execute(simulation);
		}
		executor.shutdown();

		while (!executor.isTerminated()) {
		}

		System.out.println("All simulations completed successfully.");
	}

	private record Simulation(String gameName, Game game, int rounds, Lock lock, DataSaver dataSaver) implements Runnable {
		@Override
		public void run() {
			System.out.println("Starting simulation: " + gameName);

			var playerManager = game.getPlayerManager();
			for (int i = 0; i < rounds; i++) {
				System.out.println("Start round" + i + " " + gameName);

				Map<Player, ArrayList<Long>>					   playersTimePerMoves = new HashMap<>();
				Map<Player, ArrayList<Vector2D<Integer, Integer>>> PlayersMovesMade	   = new HashMap<>();

				long totalGameTime = 0;
				int	 gameTurns	   = 0;
				while (!game.isGameOver()) {
					gameTurns++;
					var currentPlayer = playerManager.getCurrentPlayer();
					if (game.getValidMoves(currentPlayer).isEmpty()) {
						playerManager.nextPlayer();
						continue;
					}

					final long startTime = System.currentTimeMillis();
					var		   move		 = currentPlayer.onPlayerTurn();
					game.move(currentPlayer, move);
					final long endTime	 = System.currentTimeMillis();
					final long deltaTime = endTime - startTime;
					totalGameTime += deltaTime;

					PlayersMovesMade.computeIfAbsent(currentPlayer, k -> new ArrayList<>()).add(move);
					playersTimePerMoves.computeIfAbsent(currentPlayer, k -> new ArrayList<>()).add(deltaTime);
				}

				var winner = game.getWinners().get(0);
				var data   = buildDataString(gameName, gameTurns, game.isDraw(), winner, totalGameTime, playersTimePerMoves, PlayersMovesMade);

				lock.lock();
				try {
					dataSaver.saveData(data);
				} finally {
					lock.unlock();
				}
				game.restart();
				System.out.println("end round" + i + " " + gameName);
			}
			System.out.println(gameName + " has finished");
		}
	}
}
