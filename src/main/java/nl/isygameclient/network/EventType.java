package nl.isygameclient.network;

public enum EventType {
	PLAYERLIST("PLAYERLIST"),
	GAMELIST("GAMELIST"),
	MATCH("GAME MATCH"),
	YOURTURN("GAME YOURTURN"),
	MOVE("GAME MOVE"),
	CHALLENGE_ACCEPTED("GAME CALLENGE ACCEPT"),
	CHALLENGE("GAME CHALLENGE"),
	WIN("GAME WIN"),
	LOSS("GAME LOSS"),
	DRAW("GAME DRAW");

	public final String identifier;

	EventType(String identifier) {
		this.identifier = identifier;
	}
}