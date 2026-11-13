package nl.isygameclient.models.games;

import nl.isygameclient.models.Game;
import nl.isygameclient.models.Player;
import nl.isygameclient.models.PlayerManager;
import nl.isygameclient.models.board.HistoryBoard;
import nl.isygameclient.util.Vector2D;

import java.util.*;

public class TicTacToe extends Game {

    public TicTacToe(PlayerManager playerManager) {
        super(playerManager, new HistoryBoard<>(3,3){});
    }

    @Override
    public void restart() {
        playerManager.restart();
        board = new HistoryBoard<>(3,3){};
    }

    @Override
    public boolean move(Player player, Vector2D<Integer, Integer> pos) {
        if (!isMoveValid(player, pos)) {
            return false;
        }

        Map<Vector2D<Integer, Integer>, Player> changes = new HashMap<>();
        changes.put(pos, player);
        board.add(player, changes);

        playerManager.nextPlayer();
        return true;
    }

    @Override
    public boolean isMoveValid(Player player, Vector2D<Integer, Integer> pos) {
        boolean positionOnBoard = (pos.getX() >= 0 && pos.getX() < board.getWidth()) && (pos.getY() >= 0 && pos.getY() < board.getHeight());
        boolean positionEmpty = (board.get(pos) == null);
        return positionOnBoard && positionEmpty;
    }

    @Override
    public boolean isDraw() {
        for (Player player : playerManager.getPlayers()) {
            if (getValidMoves(player).size() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isGameOver() {
        return getWinners().size() > 0 || isDraw();
    }

    
    private boolean isPlayer(Player player, Vector2D<Integer, Integer> pos) {
        return Objects.equals(player, board.get(pos));
    }
    @Override
    public boolean isWinner(Player player) {
        boolean topRow = isPlayer(player, new Vector2D<>(0,0)) && isPlayer(player, new Vector2D<>(0,1)) && isPlayer(player, new Vector2D<>(0,2));
        boolean middleRow = isPlayer(player, new Vector2D<>(1,0)) && isPlayer(player, new Vector2D<>(1,1)) && isPlayer(player, new Vector2D<>(1,2));
        boolean bottomRow = isPlayer(player, new Vector2D<>(2,0)) && isPlayer(player, new Vector2D<>(2,1)) && isPlayer(player, new Vector2D<>(2,2));

        boolean leftColumn = isPlayer(player, new Vector2D<>(0,0)) && isPlayer(player, new Vector2D<>(1,0)) && isPlayer(player, new Vector2D<>(2,0));
        boolean midColumn = isPlayer(player, new Vector2D<>(0,1)) && isPlayer(player, new Vector2D<>(1,1)) && isPlayer(player, new Vector2D<>(2,1));
        boolean rightColumn = isPlayer(player, new Vector2D<>(0,2)) && isPlayer(player, new Vector2D<>(1,2)) && isPlayer(player, new Vector2D<>(2,2));

        boolean lrCross = isPlayer(player, new Vector2D<>(0,0)) && isPlayer(player, new Vector2D<>(1,1)) && isPlayer(player, new Vector2D<>(2,2));
        boolean rlCross = isPlayer(player, new Vector2D<>(0,2)) && isPlayer(player, new Vector2D<>(1,1)) && isPlayer(player, new Vector2D<>(2,0));

        return topRow || middleRow || bottomRow || leftColumn || midColumn || rightColumn || lrCross || rlCross;
    }

    @Override
    public List<Vector2D<Integer, Integer>> getValidMoves(Player player) {
        List<Vector2D<Integer, Integer>> valid = new ArrayList<>();
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (isMoveValid(player, new Vector2D<>(x, y))) valid.add(new Vector2D<>(x,y));
            }
        }
        return valid;
    }

    public List<Player> getWinners(){
        List<Player> players  = new ArrayList<>();
        for (Player player: playerManager.getPlayers()) {
            if (isWinner(player)) {
                players.add(player);
            }
        }
        return players;
    }

    @Override
    public int getPlayerScore(Player player){
        if (isGameOver() && isWinner(player)) {
            return 1;
        }
        return 0;
    }
}