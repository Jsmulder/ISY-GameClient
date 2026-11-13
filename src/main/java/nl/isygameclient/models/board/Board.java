package nl.isygameclient.models.board;

import java.util.Arrays;
import nl.isygameclient.util.Vector2D;

public class Board<T> {
	protected final int width;
	protected final int height;

	protected final T[][] field;

	@SuppressWarnings("unchecked")
	public Board(int width, int height) {
		this.width	= width;
		this.height = height;
		this.field	= (T[][]) new Object[height][width];
	}

	public void set(T value, Vector2D<Integer, Integer> pos) {
		field[pos.getY()][pos.getX()] = value;
	}

	public T get(Vector2D<Integer, Integer> pos) {
		return field[pos.getY()][pos.getX()];
	}

	public int size() {
		return width * height;
	}

	public void clear() {
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				field[y][x] = null;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public T[][] getField() {
		return field;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (T[] row : field) {
			builder.append(Arrays.toString(row))
				.append(", \n");
		}

		return "Board{"
	  +
			"width=" + width +
			", height=" + height +
			", field=\n" + builder +
			'}';
	}
}
