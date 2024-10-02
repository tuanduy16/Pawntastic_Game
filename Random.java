// Random class to allow bot to make random moves (Agent 1)
import java.util.*;

public class Random {
    /**
     * randomMove method to allow bot to make random moves
     * @param game the game configuration
     * @param player the current player
     * @return the Action that the bot will take after moving a random legal move
     */
    public static Action randomMove(PawntasticGame game, int player) {
        List<Action> allMoves = game.getAllLegalMoves(player);
        int randomNum = (int) Math.random() * allMoves.size();
        return allMoves.get(randomNum);
    }
}
