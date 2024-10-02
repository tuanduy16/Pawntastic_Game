// The Minimax Algorithm implementation
public class Minimax {
    /**
     * maxValue method for the MAX player
     * @param game the game configuration
     * @param b the current player
     * @param minimaxPlayer the minimax player
     * @return a pair of the best value and the best action
     */
    private static Pair<Double, Action> maxValue(PawntasticGame game, int b, int minimaxPlayer) {
        if (game.isTerminalState()) {
            Pair<Double, Action> ans = new Pair<Double, Action>(null, null);
            ans.first = 1.0d * game.getUtility(minimaxPlayer);
            return ans;
        }
        Pair<Double, Action> optimalMove = new Pair<Double, Action>(-999999999.0, null);
        for (Action a : game.getAllLegalMoves(b)) {
            PawntasticGame newGame = game.cloneGame();
            newGame.makeMoves(a);
            Pair<Double, Action> newBoard = minValue(newGame, 0 - b, minimaxPlayer);
            if (newBoard.first > optimalMove.first) {
                optimalMove.first = newBoard.first;
                optimalMove.second = a;
            }
        }
        return optimalMove;
    }

    /**
     * minValue method for the MIN player
     * @param game the game configuration
     * @param b the current player
     * @param minimaxPlayer the minimax player
     * @return a pair of the best value and the best action
     */
    private static Pair<Double, Action> minValue(PawntasticGame game, int b, int minimaxPlayer) {
        if (game.isTerminalState()) {
            Pair<Double, Action> ans = new Pair<Double, Action>(null, null);
            ans.first = 1.0d * game.getUtility(minimaxPlayer);
            return ans;
        }
        Pair<Double, Action> optimalMove = new Pair<Double, Action>(9999999.0, null);
        for (Action a : game.getAllLegalMoves(b)) {
            PawntasticGame newGame = game.cloneGame();
            newGame.makeMoves(a);
            Pair<Double, Action> newBoard = maxValue(newGame, 0 - b, minimaxPlayer);
            if (newBoard.first < optimalMove.first) {
                optimalMove.first = newBoard.first;
                optimalMove.second = a;
            }
        }
        return optimalMove;
    }

    /**
     * minimax method for the minimax player
     * @param game the game configuration
     * @param player the minimax player
     * @return the best action
     */
    public static Action minimax(PawntasticGame game, int player) {
        return maxValue(game, player, player).second;
    }
}
