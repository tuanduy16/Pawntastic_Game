// Minimax with alpha-beta pruning
public class MinimaxAlphaBeta {
    /**
     * maxValueAlphaBeta method for the MAX player with alpha beta pruning
     * @param game the game configuration
     * @param b the current player
     * @param minimaxPlayer the minimax player
     * @param alpha the lower bound alpha for Alpha-Beta pruning
     * @param beta the upper bound beta for Alpha-Beta pruning
     * @return a pair of the best value and the best action
     */
    private static Pair<Double, Action> maxValueAlphaBeta(PawntasticGame game, int b, int minimaxPlayer, Double alpha,
            Double beta) {
        if (game.isTerminalState()) {
            Pair<Double, Action> ans = new Pair<Double, Action>(null, null);
            ans.first = 1.0d * game.getUtility(minimaxPlayer);
            return ans;
        }
        Pair<Double, Action> optimalMove = new Pair<Double, Action>(-999999999.0, null);
        for (Action a : game.getAllLegalMoves(b)) {
            PawntasticGame newGame = game.cloneGame();
            newGame.makeMoves(a);
            Pair<Double, Action> newBoard = minValueAlphaBeta(newGame, 0 - b, minimaxPlayer, alpha, beta);
            if (newBoard.first > optimalMove.first) {
                optimalMove.first = newBoard.first;
                optimalMove.second = a;
            }
            if (optimalMove.first >= beta) {
                return optimalMove;
            }
        }
        return optimalMove;
    }

    /**
     * minValueAlphaBeta method for the MIN player with alpha beta pruning
     * @param game the game configuration
     * @param b the current player
     * @param minimaxPlayer the minimax player
     * @param alpha the lower bound alpha for Alpha-Beta pruning
     * @param beta the upper bound beta for Alpha-Beta pruning
     * @return a pair of the best value and the best action
     */
    private static Pair<Double, Action> minValueAlphaBeta(PawntasticGame game, int b, int minimaxPlayer, Double alpha,
            Double beta) {
        if (game.isTerminalState()) {
            Pair<Double, Action> ans = new Pair<Double, Action>(null, null);
            ans.first = 1.0d * game.getUtility(minimaxPlayer);
            return ans;
        }
        Pair<Double, Action> optimalMove = new Pair<Double, Action>(9999999.0, null);
        for (Action a : game.getAllLegalMoves(b)) {
            PawntasticGame newGame = game.cloneGame();
            newGame.makeMoves(a);
            Pair<Double, Action> newBoard = maxValueAlphaBeta(newGame, 0 - b, minimaxPlayer, alpha, beta);
            if (newBoard.first < optimalMove.first) {
                optimalMove.first = newBoard.first;
                optimalMove.second = a;
            }
            if (optimalMove.first <= alpha) {
                return optimalMove;
            }
        }
        return optimalMove;
    }

    /**
     * minimaxAlphaBeta method for the minimax player with alpha beta pruning
     * @param game the game configuration
     * @param player the minimax player
     * @return the best action
     */
    public static Action minimaxAlphaBeta(PawntasticGame game, int player) {
        return maxValueAlphaBeta(game, player, player, -999999999.0, 9999999.0).second;
    }
}