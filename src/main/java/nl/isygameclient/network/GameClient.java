package nl.isygameclient.network;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameClient extends GameClientBase {
	private String name;

	public GameClient(String host, int port) throws IOException {
		super(host, port);
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public List<String> games() throws IOException {
		send("get gamelist");

		return (List<String>) event(EventType.GAMELIST).data;
	}

	@SuppressWarnings("unchecked")
	public List<String> players() throws IOException {
		send("get playerlist");

		return (List<String>) event(EventType.GAMELIST).data;
	}

	public void login(String name) throws IOException {
		this.name = name;

		send("login", name);
	}

	public Match match(GameType game, String other) throws IOException {
		send("challenge", other, game.name);

		return new Match(this);
	}

	public Match match(GameType game) throws IOException {
		send("subscribe", game.name);

		return new Match(this);
	}

	public Match match() throws IOException {
		@SuppressWarnings("unchecked")
		var challengeEvent = (Map<String, String>) event(-1, EventType.CHALLENGE).data;
		var challengeID	   = challengeEvent.get("challengenumber");
		send("challenge", "accept", challengeID);

		return new Match(this);
	}

	public static void main(String[] args) throws IOException {
		var name   = "testclient" + new Random().nextInt(100);
		var client = new GameClient("localhost", 7789);	   // public: 145.33.225.170

		client.login(name);
		System.out.println("connected as " + name);

		var current = client.match(GameType.TICTACTOE4);
		for (;;) {
			while (current.update()) {
				int move;
				do {
					move = new Random().nextInt(current.getGame().maxMoves);
				} while (current.getMove(move) != 0);
				current.move(move);
			}
			System.out.printf("[%4s] %02d:%02d\n", current.getOutcome().type, current.getPointsSelf(), current.getPointsOther());
			if (current.getPointsSelf() >= 10 || current.getPointsOther() >= 10)
				break;

			current.rematch();
		}

		client.close();
	}
}
