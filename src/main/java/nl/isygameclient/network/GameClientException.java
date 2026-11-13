package nl.isygameclient.network;

import java.io.IOException;

public class GameClientException extends IOException {
	public GameClientException(String reason) {
		super(reason);
	}
}