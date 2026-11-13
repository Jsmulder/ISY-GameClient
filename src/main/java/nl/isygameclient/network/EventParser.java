package nl.isygameclient.network;

import java.util.HashMap;
import java.util.LinkedList;

public class EventParser {
	private final String str;

	private int index = 0;

	private void stripLeft() {
		while (Character.isSpaceChar(str.charAt(index)))
			index++;
	}

	public EventParser(String str) {
		this.str = str;
	}

	public Object parseData() throws GameClientException {
		try {
			stripLeft();

			if (index >= str.length())
				return null;

			switch (str.charAt(index)) {
				case '"':
					index++;
					var string = str.substring(index, str.indexOf("\"", index));
					index	   = str.indexOf("\"", index) + 1;
					return string;
				case '[':
					index++;
					var array = new LinkedList<Object>();
					for (;;) {
						array.add(parseData());
						stripLeft();
						if (str.charAt(index) == ']') {
							index++;
							return array;
						} else if (str.charAt(index) == ',') {
							index++;
						} else {
							throw new GameClientException(String.format("invalid server response: unexpected '%c' at %d in '%s'", str.charAt(index), index, str));
						}
					}
				case '{':
					index++;
					var map = new HashMap<String, Object>();
					for (;;) {
						stripLeft();
						var key = str.substring(index, str.indexOf(":", index)).toLowerCase();

						index = str.indexOf(":", index) + 1;
						map.put(key, parseData());
						stripLeft();
						if (str.charAt(index) == '}') {
							index++;
							return map;
						} else if (str.charAt(index) == ',') {
							index++;
						} else {
							throw new GameClientException(String.format("invalid server response: unexpected '%c' at %d in '%s'", str.charAt(index), index, str));
						}
					}
				default:
					if (index != 0)
						throw new GameClientException(String.format("invalid server response: unexpected '%c' at %d in '%s'", str.charAt(index), index, str));
					index = str.length();
					return str;
			}
		} catch (Exception ext) {
			throw new GameClientException(String.format("invalid server response: unexpected '%c' at %d in '%s'", str.charAt(index), index, str));
		}
	}

	public void reset() {
		index = 0;
	}
}
