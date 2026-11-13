package nl.isygameclient.network;

import java.io.IOException;
import java.util.Map;

public class Match {
	private final GameClient client;

	private GameType game;
	private Event	 outcome;
	private String	 opponent;
	private int[] moves;
	private int		pointsSelf;
	private int		pointsOther;
	private boolean yourTurn;

	public Match(GameClient client) throws IOException {
		this.client = client;
	}

	public GameType getGame() {
		return game;
	}

	public boolean isYourTurn() {
		return yourTurn;
	}

	public boolean update() throws IOException {
		if (outcome != null)
			return false;

		Event event = client.event(EventType.MATCH, EventType.MOVE, EventType.YOURTURN, EventType.WIN, EventType.DRAW, EventType.LOSS);
		if (event == null)
			return true;

		switch (event.type) {
			case MATCH:
				@SuppressWarnings("unchecked")
				var data = (Map<String, String>) event.data;

				game	 = GameType.byName(data.get("gametype").toLowerCase());
				moves	 = new int[game.maxMoves];
				opponent = data.get("opponent");
				return true;
			case YOURTURN:
				yourTurn = true;
				return true;
			case MOVE:
				@SuppressWarnings("unchecked")
				var moveMap = (Map<String, String>) event.data;

				moves[Integer.parseInt(moveMap.get("move"))] = moveMap.get("player").equals(client.getName()) ? 1 : -1;
				return true;
			case WIN:
				pointsSelf++;
				outcome = event;
				return false;
			case LOSS:
				pointsOther++;
				outcome = event;
				return false;
			default:	// loss
				outcome = event;
				return false;
		}
	}

	public boolean isStarted() {
		return game != null;
	}

	public int getMove(int move) {
		return moves[move];
	}

	public void abort() throws IOException {
		if (outcome == null && !isStarted())
			client.send("forfeit");
	}

	public void rematch() throws IOException {
		if (outcome == null && !isStarted())
			return;

		Event evt;
		if ((evt = client.event(0.5, EventType.CHALLENGE)) != null) {
			@SuppressWarnings("unchecked")
			var id = ((Map<String, String>) evt.data).get("challengenumber");
			client.send("challenge", "accept", id);
		} else {
			client.send("challenge", opponent, game.name);
			client.event(-1, EventType.MATCH);
		}

		for (int i = 0; i < moves.length; i++)
			moves[i] = 0;
		outcome = null;
	}

	public Event getOutcome() {
		return outcome;
	}

	public int getPointsSelf() {
		return pointsSelf;
	}

	public int getPointsOther() {
		return pointsOther;
	}

	public String getOpponent() {
		return opponent;
	}

	public void move(int move) throws IOException {
		if (outcome != null && !isStarted())
			return;
		client.send("move " + move);
		yourTurn = false;
	}
}