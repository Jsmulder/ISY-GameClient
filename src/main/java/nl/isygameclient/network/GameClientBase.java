package nl.isygameclient.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class GameClientBase extends Socket {
	private final OutputStream	 outputStream;
	private final BufferedReader inputBuffer;

	private final LinkedList<Event>  eventQueue	  = new LinkedList<>();
	private final LinkedList<String> eventLineQueue = new LinkedList<>();

	public GameClientBase(String host, int port) throws IOException {
		super(host, port);
		outputStream = getOutputStream();
		inputBuffer	 = new BufferedReader(new InputStreamReader(getInputStream()));

		try {
			Thread.sleep(100);
			while (inputBuffer.ready()) {
				inputBuffer.readLine();
				Thread.sleep(100);
			}
		} catch (InterruptedException exc) {
			throw new GameClientException("socket initiation failed: " + exc.getMessage());
		}
	}

	public void send(String command, String... arguments) throws IOException {
		var requestBuilder = new StringBuilder(command);
		for (var argument : arguments) {
			requestBuilder.append(' ');
			requestBuilder.append(argument);
		}

		requestBuilder.append('\n');
		outputStream.write(requestBuilder.toString().getBytes());

		String response;
		for (;;) {
			response = inputBuffer.readLine();
			if (response.startsWith("ERR "))
				throw new GameClientException(response.substring(4));
			else if (response.startsWith("SVR ")) {
				eventLineQueue.add(response);
				continue;
			} else if (response.equals("OK"))
				break;
			throw new GameClientException("invalid server response: '" + response + "'");
		}
	}

	public Event event(EventType... target) throws IOException {
		return event(1, true, target);
	}

	public Event event(double timeout, EventType... target) throws IOException {
		return event(timeout, true, target);
	}

	public Event event(double timeout, boolean consume, EventType... target) throws IOException {
		var targetList = Arrays.asList(target);
		if (!eventQueue.isEmpty()) {
			if (target == null)
				return eventQueue.poll();

			for (int i = 0; i < eventQueue.size(); i++) {
				if (targetList.contains(eventQueue.get(i).type))
					return consume ? eventQueue.remove(i) : eventQueue.get(i);
			}
		}

		long   start = System.currentTimeMillis();
		Event  result;
		String line;

		do {
			if (eventLineQueue.isEmpty()) {
				try {
					Thread.sleep(100);
					if (!inputBuffer.ready())
						continue;
					line = inputBuffer.readLine();
				} catch (InterruptedException exc) {
					throw new GameClientException("event-pulling interrupted");
				}
			} else {
				line = eventLineQueue.poll();
			}

			if (!line.startsWith("SVR "))
				throw new GameClientException("invalid server response: '" + line + "'");


			result = null;
			for (var evt : EventType.values()) {
				if (line.startsWith(evt.identifier, 4)) {
					result = new Event(evt, new EventParser(line.substring(evt.identifier.length() + 4)).parseData());
					break;
				}
			}

			if (result == null)
				throw new GameClientException("invalid server response: unknown event '" + line + "'");


			if (target.length == 0 || target.length > 0 && targetList.contains(result.type)) {
				if (!consume)
					eventQueue.push(result);
				return result;
			}

			eventQueue.push(result);
		} while (timeout == -1 || System.currentTimeMillis() - start < timeout * 1000);

		return null;
	}

	@Override
	public void close() throws IOException {
		outputStream.write("logout\n".getBytes());
		super.close();
	}
}
