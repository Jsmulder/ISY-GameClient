package nl.isygameclient.models.board;


import java.util.*;
import nl.isygameclient.util.Vector2D;

public class HistoryBoard<T> extends Board<T> {
	public record Move<T>(T current, T previous) {
	}

	public record Change<T>(T player, Map<Vector2D<Integer, Integer>, Move<T>> moves) {
	}

	private int	  index;
	private final Stack<Change<T>> stack;

	public HistoryBoard(int width, int height) {
		super(width, height);
		this.stack = new Stack<>();
		this.index = 0;
	}

	public List<Change<T>> getHistory() {
		return stack.subList(0, index);
	}

	public List<Change<T>> getFuture() {
		return stack.subList(index, stack.size());
	}

	public void add(T player, Map<Vector2D<Integer, Integer>, T> changes) {
		for (int i = stack.size(); i > index; i--)
			stack.pop();

		Map<Vector2D<Integer, Integer>, Move<T>> moves = new HashMap<>();
		for (Map.Entry<Vector2D<Integer, Integer>, T> entry : changes.entrySet()) {
			var vector = entry.getKey();
			moves.put(entry.getKey(), new Move<>(entry.getValue(), get(vector)));
		}

		stack.push(new Change<>(player, moves));
		index++;

		for (var move : changes.entrySet()) {
			var vector = move.getKey();
			set(move.getValue(), vector);
		}
	}

	public boolean canUndo() {
		return index > 0;
	}

	public boolean canRedo() {
		return index < stack.size();
	}

	public boolean undo() {
		if (!canUndo())
			return false;

		index--;

		for (var move : stack.get(index).moves.entrySet()) {
			var vector = move.getKey();
			set(move.getValue().previous, vector);
		}

		return true;
	}

	public boolean redo() {
		if (!canRedo())
			return false;

		for (var move : stack.get(index).moves.entrySet()) {
			var vector = move.getKey();
			set(move.getValue().current, vector);
		}

		index++;
		return true;
	}

	public void clear() {
		super.clear();
		stack.clear();
		index = 0;
	}
}
