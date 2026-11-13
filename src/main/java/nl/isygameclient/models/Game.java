package nl.isygameclient.models;


import nl.isygameclient.models.board.Board;
import nl.isygameclient.models.board.HistoryBoard;
import nl.isygameclient.util.Vector2D;

import java.util.List;

public abstract class Game {

    protected PlayerManager playerManager;
    protected HistoryBoard<Player> board;

    public Game(PlayerManager playerManager, HistoryBoard<Player> board) {
        this.playerManager = playerManager;
        this.board = board;

        for (Player player : playerManager.getPlayers()) {
            player.setGame(this);
        }
    }

    public abstract void restart();

    public abstract boolean move(Player player, Vector2D<Integer, Integer> pos);

    public abstract boolean isMoveValid(Player player, Vector2D<Integer, Integer> pos);

    public abstract boolean isDraw();

    public abstract boolean isGameOver();

    public abstract boolean isWinner(Player player);

    public void undo() {
        board.undo();
        playerManager.previousPlayer();
    }

    public abstract List<Vector2D<Integer, Integer>> getValidMoves(Player player);

    public abstract List<Player> getWinners();

    public abstract int getPlayerScore(Player player);

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public HistoryBoard<Player> getBoard() {
        return board;
    }
}
