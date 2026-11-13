package nl.isygameclient.network;

public class Event {
	public final EventType type;
	public final Object	   data;

	public Event(EventType type, Object data) {
		this.type = type;
		this.data = data;
	}

	public String toString() {
		return String.format("Event.%s(%s)", type, data);
	}
}