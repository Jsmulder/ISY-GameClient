package nl.isygameclient;

import nl.isygameclient.models.Player;
import nl.isygameclient.models.PlayerManager;
import nl.isygameclient.models.games.Othello;
import nl.isygameclient.network.Event;
import nl.isygameclient.network.GameClient;
import nl.isygameclient.util.Vector2D;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tournament {
    public static final int[][] HEURISTIC = {
            {10, 2, 5, 5, 5, 5, 2, 10},
            {2, 2, 1, 1, 1, 1, 2, 2},
            {5, 1, 1, 1, 1, 1, 1, 5},
            {5, 1, 1, 1, 1, 1, 1, 5},
            {5, 1, 1, 1, 1, 1, 1, 5},
            {5, 1, 1, 1, 1, 1, 1, 5},
            {2, 2, 1, 1, 1, 1, 2, 2},
            {10, 2, 5, 5, 5, 5, 2, 10}
    };

    public static void main(String[] args) throws IOException {
        //		GameClient client = new GameClient("145.33.225.170", 7789);
        GameClient client = new GameClient("localhost", 7789);

        client.login("ITV2D1" + new Random().nextInt(9));

        Player dummyPlayer = new Player("network", "black") {
            public Vector2D<Integer, Integer> onPlayerTurn() {
                return null;
            }
        };

        Player randomPlayer = new Player("network", "black") {
            public Vector2D<Integer, Integer> onPlayerTurn() {
                return null;
            }
        };

        PlayerManager playerManager = new PlayerManager(0, List.of(randomPlayer, dummyPlayer));
        Othello othello = new Othello(playerManager);
        Random random = new Random();

        String firstPlayer = null;

        while (true) {
            Event event = client.event(-1);

            switch (event.type) {
                case MATCH:
                    @SuppressWarnings("unchecked")
                    var data = (Map<String, String>) event.data;

                    String opponent = data.get("opponent");
                    System.out.println("playing against " + opponent);

                    firstPlayer = null;
                    playerManager.restart();
                    othello.getBoard().clear();

                    break;
                case YOURTURN:
                    if (firstPlayer == null) {
                        firstPlayer = "self";
                        othello.initializeBoard();
                    }
                    var turns = othello.getValidMoves(randomPlayer);
                    System.out.println(turns);
                    var turn = turns.get(random.nextInt(turns.size()));

                    System.out.println(turn);
                    client.send("move " + (turn.getY() * 8 + turn.getX()));
                    //					othello.move(ai, turn);

                    break;
                case MOVE:
                    if (firstPlayer == null) {
                        firstPlayer = "other";
                        playerManager.nextPlayer();
                        othello.initializeBoard();
                    }
                    @SuppressWarnings("unchecked")
                    var moveMap = (Map<String, String>) event.data;

                    int move = Integer.parseInt(moveMap.get("move"));
                    System.out.printf("%s did move %dx%d\n", moveMap.get("player"), move % 8, move / 8);
                    othello.move(moveMap.get("player").equals(client.getName()) ? randomPlayer : dummyPlayer, new Vector2D<>(move % 8, move / 8));

                    break;
                case WIN:
                case LOSS:
                default:
                    System.out.printf("%s -> %s\n", client.getName(), event.type);
                    for (int y = 0; y < 8; y++) {
                        System.out.print("|");
                        for (int x = 0; x < 8; x++) {
                            Player p = othello.getBoard().get(new Vector2D<Integer, Integer>(x, y));
                            if (p == dummyPlayer) {
                                System.out.print("N|");
                            } else if (p == randomPlayer) {
                                System.out.print("A|");
                            } else {
                                System.out.print(" |");
                            }
                        }
                        System.out.println();
                    }     //	outcome = event;
                    //	return false;
            }
        }
        //		client.close();
    }
}
