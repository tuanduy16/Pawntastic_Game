import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PawntasticGame {
    public static final int PLAYER1 = 1; // black pawn
    public static final int PLAYER2 = -1; // white pawn
    public int boardSize; // the size of the board
    public int[][] board; // the chess board 
    public int agent; // the bot agent
    public int cutOffDepth; // the cut off depth that player enter
    public String userColor; // the side that player choose (Black pawns or White pawns)

    // constructor for the game board and set the pawns to their initial cell
    public PawntasticGame(int size) {
        boardSize = size;
        board = new int[boardSize][boardSize];

        for (int col = 0; col < boardSize; col++) {
            board[1][col] = PLAYER1;
        }
        for (int col = 0; col < boardSize; col++) {
            board[boardSize - 2][col] = PLAYER2;
        }
    }

    /**
     * Clone the game
     * @return the cloned game
     */
    public PawntasticGame cloneGame() {
        PawntasticGame newGame = new PawntasticGame(boardSize);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                newGame.board[i][j] = board[i][j];
            }
        }
        return newGame;
    }

    /**
     * Check if a move is legal
     * @param action the action that player want to make
     * @param player the current player
     * @return true if the move is legal, false otherwise
     */
    public boolean isMoveLegal(Action action, int player) {
        int fromRow = action.getFromRow();
        int fromCol = action.getFromCol();
        int toRow = action.getToRow();
        int toCol = action.getToCol();

        if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol))
            return false;

        // check if the starting position contains player's pawn
        if (board[fromRow][fromCol] != player)
            return false;

        // check if it's a valid pawn move (one square forward)
        if (player == PLAYER1) {
            if (fromRow + 1 == toRow && fromCol == toCol && board[toRow][toCol] == 0) { // nothing above and nothing
                                                                                        // block the pawn
                return true;
            }
            if (fromRow + 2 == toRow && fromCol == toCol && board[toRow][toCol] == 0 &&
                    board[fromRow + 1][fromCol] == 0 && fromRow == 1) { // pawn can move 2 squares from starting point
                return true;
            }
        } else if (player == PLAYER2) {
            if (fromRow - 1 == toRow && fromCol == toCol && board[toRow][toCol] == 0) { // nothing below and nothing
                                                                                        // block the pawn
                return true;
            }
            if (fromRow - 2 == toRow && fromCol == toCol && board[toRow][toCol] == 0 &&
                    board[fromRow - 1][fromCol] == 0 && fromRow == -1) { // pawn can move 2 squares from starting point
                return true;
            }
        }

        // check if it's a valid pawn capture
        if (Math.abs(fromCol - toCol) == 1) {
            if (player == PLAYER1) {
                if (fromRow + 1 == toRow && board[toRow][toCol] == PLAYER2) {
                    return true;
                }
            }
            if (player == PLAYER2) {
                if (fromRow - 1 == toRow && board[toRow][toCol] == PLAYER1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get and store all the possible & legal moves
     * 
     * @param player the current player
     * @return the list of legal moves for a player
     */
    public List<Action> getAllLegalMoves(int player) {
        List<Action> legalMoves = new ArrayList<>();
        int opponent = -player;
        int direction = (player == PLAYER1) ? 1 : -1;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == player) {
                    int newRow = row + direction;

                    // case when the pawn can MOVE UPward/DOWNward(no opponent's pawn block)
                    if (isValidPosition(newRow, col) && board[newRow][col] == 0) {
                        // promotion
                        if (newRow == 0 || newRow == boardSize - 1) {
                            legalMoves.add(new Action(row, col, newRow, col, true));
                        } else
                            legalMoves.add(new Action(row, col, newRow, col));
                    }

                    // case when the pawn can move 2 squares
                    if (boardSize >= 6) {
                        if (row == (player == PLAYER1 ? 1 : boardSize - 2) && isValidPosition(newRow + direction, col)
                                &&
                                board[newRow][col] == 0 && board[newRow + direction][col] == 0) {
                            legalMoves.add(new Action(row, col, newRow + direction, col));
                        }
                    }

                    // case when pawn can CAPTURE opponent's pawn
                    int[] captureCols = { col - 1, col + 1 };
                    for (int captureCol : captureCols) {
                        if (isValidPosition(newRow, captureCol) && board[newRow][captureCol] == opponent) {
                            legalMoves.add(new Action(row, col, newRow, captureCol));
                        }
                    }
                }

            }
        }

        return legalMoves;
    }

    /**
     * Make moves (apply an action to current state)
     * @param action the action that player want to make
     */
    public void makeMoves(Action action) {
        int fromRow = action.getFromRow();
        int fromCol = action.getFromCol();
        int toRow = action.getToRow();
        int toCol = action.getToCol();
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = 0;
    }

    /**
     * Define whether a state is a terminal state
     * @return true if the state is terminal, false otherwise
     */
    public boolean isTerminalState() {
        for (int col = 0; col < boardSize; col++) {
            if (board[0][col] == PLAYER2 || board[boardSize - 1][col] == PLAYER1) {
                return true;
            }
        }

        if (getAllLegalMoves(PLAYER1).size() == 0 || getAllLegalMoves(PLAYER2).size() == 0) {
            return true;
        }

        return false;
    }

    /**
     * Get utility when at terminal state
     * @param player the current player
     * @return the utility value: 1 if current player win, 0 if tie, -1 if lose
     */
    public int getUtility(int player) {
        for (int col = 0; col < boardSize; col++) {
            if (board[0][col] == PLAYER2) {
                return PLAYER2 == player ? 1 : -1;
            }
            if (board[boardSize - 1][col] == PLAYER1) {
                return PLAYER1 == player ? 1 : -1;
            }
        }
        return 0;
    }

    /**
     * The heuristic function for the game, at current player
     * @param player the current player
     * @return the heuristic value
     */
    public double eval(int player) {
        int move1 = 0;
        int move2 = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == PLAYER1)
                    move1 += Math.pow(row-1,3);
                if (board[row][col] == PLAYER2)
                    move2 += Math.pow(boardSize-row-2,3);
            }
        }
        if (move1 > move2){
            return PLAYER1 == player ? 1.0 * (move1-move2) / (move1+move2) : -1.0 * (move1-move2) / (move1+move2);
        }
        else if (move1 < move2)
            return PLAYER2 == player ? 1.0 * (move2-move1) / (move1+move2) : -1.0 * (move2-move1) / (move1+move2);

        return 0.0;
    }

    /**
     * Print the board
     */
    public void printBoard() {
        char[] files = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H','I','K'};
        
        System.out.print("  ");
        for (int i = 0; i < boardSize; i++) {
            System.out.print(" " + files[i] + "  ");
        }
        System.out.println();

        System.out.print("  ");
        for (int j = 0; j < boardSize; j++) {
                System.out.print("+---");
            }
            System.out.println("+");
        
        for (int i = 0; i < boardSize; i++) {
            System.out.print(boardSize - i + " ");
            
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 1)
                    System.out.print("| B ");
                else if (board[i][j] == -1)
                    System.out.print("| W ");
                else
                    System.out.print("|   ");
            }
            System.out.print("| " + (boardSize - i)); 
            System.out.println();
            
            System.out.print("  ");
            for (int j = 0; j < boardSize; j++) {
                System.out.print("+---");
            }
            System.out.println("+");
        }
        
        System.out.print("  ");
        for (int i = 0; i < boardSize; i++) {
            System.out.print(" " + files[i] + "  ");
        }
        System.out.println();
    }

    /**
     * The gameplay method, to simulate the whole game
     * @param currentBoard the current game situation
     */
    public void gamePlay(PawntasticGame currentBoard) {
        Scanner scanner = new Scanner(System.in);
        int moveCount = 0;

        while (!isTerminalState()) {
            printBoard();
            System.out.println("");

            int currentPlayer;
            currentPlayer = (moveCount % 2 == 0) ? PLAYER2 : PLAYER1;
            String currentPlayerName = (currentPlayer == PLAYER1) ? "BLACK" : "WHITE";

            // check all legal moves
            List<Action> list = currentBoard.getAllLegalMoves(currentPlayer);
            System.out.println("List of all possible moves ");
            for (Action a : list) {
                System.out.print("From "+ convertColToCoordinator(a.getFromCol()) + convertRowToCoordinator(a.getFromRow()) + " to " 
                + convertColToCoordinator(a.getToCol()) + convertRowToCoordinator(a.getToRow()));
                System.out.println();
            }
            System.out.println("-----------------");

            if (userColor.equals("B")) {
                if (currentPlayer == PLAYER1) {
                    System.out.println(
                            currentPlayerName + ", enter your move in a form 'fromCol fromRow toCol toRow' (Ex: A 1 A 2 means make a pawn moves from A1 to A2)");
                    String input = scanner.nextLine();
                    Action userAction = processUserMove(input, currentPlayer);
                    if (userAction == null) {
                        System.out.println("Invalid move!");
                        System.out.println(" ");
                    } else {
                        currentBoard.makeMoves(userAction);
                        moveCount++;
                    }
                    System.out.println(" ");
                }
                if (currentPlayer == PLAYER2) {
                    System.out.println("Processing...");
                    System.out.println();

                    Action move;
                    if (agent == 1) {
                        move = Random.randomMove(currentBoard, currentPlayer);
                    } else if (agent == 2) {
                        move = Minimax.minimax(currentBoard, currentPlayer);
                    } else if (agent == 3) {
                        move = MinimaxAlphaBeta.minimaxAlphaBeta(currentBoard, currentPlayer);
                    } else if (agent == 4) {
                        move = H_Minimax.minimax(currentBoard, currentPlayer);
                    } else {
                        move = H_MinimaxAlphaBeta.hMinimaxAlphaBeta(currentBoard, currentPlayer);
                    }
                    currentBoard.makeMoves(move);
                    System.out.println("The bot just played " + convertColToCoordinator(move.getFromCol()) + convertRowToCoordinator(move.getFromRow()) + " to "
                            + convertColToCoordinator(move.getToCol()) + convertRowToCoordinator(move.getToRow()));
                    System.out.println();
                    moveCount++;
                }
            } else if (userColor.equals("W")) {
                if (currentPlayer == PLAYER2) {
                    System.out.println(
                            currentPlayerName + ", enter your move in a form 'fromCol fromRow toCol toRow' (Ex: A 1 A 2 means make a pawn moves from A1 to A2)");
                    String input = scanner.nextLine();
                    Action userAction = processUserMove(input, currentPlayer);
                    if (userAction == null) {
                        System.out.println("Invalid move!");
                        System.out.println(" ");
                    } else {
                        currentBoard.makeMoves(userAction);
                        moveCount++;
                    }
                    System.out.println(" ");
                }
                if (currentPlayer == PLAYER1) {
                    System.out.println("Processing...");
                    System.out.println();
                    Action move;
                    if (agent == 1) {
                        move = Random.randomMove(currentBoard, currentPlayer);
                    } else if (agent == 2) {
                        move = Minimax.minimax(currentBoard, currentPlayer);
                    } else if (agent == 3) {
                        move = MinimaxAlphaBeta.minimaxAlphaBeta(currentBoard, currentPlayer);
                    } else if (agent == 4) {
                        move = H_Minimax.minimax(currentBoard, currentPlayer);
                    } else {
                        move = H_MinimaxAlphaBeta.hMinimaxAlphaBeta(currentBoard, currentPlayer);
                    }
                    currentBoard.makeMoves(move);

                    

                    System.out.println("The bot just played " + convertColToCoordinator(move.getFromCol()) + convertRowToCoordinator(move.getFromRow()) + " to "
                            + convertColToCoordinator(move.getToCol()) + convertRowToCoordinator(move.getToRow()));
                    System.out.println();
                    moveCount++;
                }
            }
        }

        printBoard();
        int winner = getWinner();
        if (winner == PLAYER1)
            System.out.println("PLAYER1 win!");
        else if (winner == PLAYER2)
            System.out.println("PLAYER2 win!");
        else
            System.out.println("It's a draw");

        scanner.close();
    }

    /**
     * Helper method to process the user's move through the input
     * @param input the input that user enter
     * @param currentPlayer the current player
     * @return the action that user want to make after processing the input
     */
    private Action processUserMove(String input, int currentPlayer) {
        String[] parts = input.split(" ");
        if (parts.length != 4) {
            return null;
        }
        int fromCol = convertCoordinatorToCol(parts[0]);
        int fromRow = covertCoordinatorToRow(parts[1]);
        int toCol = convertCoordinatorToCol(parts[2]);
        int toRow = covertCoordinatorToRow(parts[3]);
        Action move = new Action(fromRow, fromCol, toRow, toCol);
        return isValidMove(move, currentPlayer) ? new Action(fromRow, fromCol, toRow, toCol) : null;
    }

    /**
     * Check if the move is valid for the current player
     * @param action the action that player want to make
     * @param currentPlayer the current player
     * @return true if the move is valid, false otherwise
     */
    private boolean isValidMove(Action action, int currentPlayer) {
        List<Action> legalMoves = getAllLegalMoves(currentPlayer);
        for (Action legal : legalMoves) {
            if (legal.getFromRow() == action.getFromRow() && legal.getFromCol() == action.getFromCol() &&
                    legal.getToRow() == action.getToRow() && legal.getToCol() == action.getToCol()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the winner of the game
     * @return the winner of the game, 0 if tie
     */
    private int getWinner() {
        for (int col = 0; col < boardSize; col++) {
            if (board[0][col] == PLAYER2) {
                return PLAYER2;
            }
            if (board[boardSize - 1][col] == PLAYER1) {
                return PLAYER1;
            }
        }
        return 0;
    }

    /**
     * Check if the position is valid (not out of bound)
     * @param row the row of the position
     * @param col the column of the position
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    private int convertCoordinatorToCol(String c){
        switch (c) {
            case "A": return 0;
            case "B": return 1;
            case "C": return 2;
            case "D": return 3;
            case "E": return 4;
            case "F": return 5;
            case "G": return 6;
            case "H": return 7;
            case "I": return 8;
            case "K": return 9;
            default: throw new IllegalArgumentException("Invalid column: " + c);
            }
        }

    private int covertCoordinatorToRow(String num){
        int row = Integer.parseInt(num);
        return Math.abs(row-boardSize);
    }

    private int convertRowToCoordinator(int row){
        return Math.abs(row - boardSize);
    }

    private String convertColToCoordinator(int col){
        switch (col) {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
            case 8: return "I";
            case 9: return "K";
            default: throw new IllegalArgumentException("Invalid column: " + col);
            }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose your game:");
        System.out.println(
                "4. Tiny 4x4 Pawntastic\n5. Very small 5x5 Pawntastic\n6. Small 6x6 Pawntastic\n8. Standard 8x8 Pawntastic\n10. Jumbo 10x10 Pawntastic");
        System.out.print("Your choice: ");
        int userGame = scanner.nextInt();
        scanner.nextLine();
        PawntasticGame example = new PawntasticGame(userGame);

        System.out.println(
                "Choose your opponent:\n1. An agent that plays randomly\n2. An agent that uses MINIMAX\n3. An agent that uses MINIMAX with alpha-beta pruning\n4. An agent that uses H-MINIMAX with a fixed depth cutoff\n5. An agent that uses H-MINIMAX with a fixed depth cutoff and alpha-beta pruning");
        System.out.print("Your choice: ");
        int userAgent = scanner.nextInt();
        scanner.nextLine();
        example.agent = userAgent;

        if(userAgent == 4 || userAgent == 5){
            System.out.print("Please specify the depth cutoff (recommended at approximately 5): ");
            int userDepth = scanner.nextInt();
            scanner.nextLine();
            example.cutOffDepth = userDepth;
        }

        System.out.println("Do you want to play BLACK (B) or WHITE (W)? (WHITE plays first)");
        System.out.print("Your choice: ");
        String userPlayer = scanner.nextLine();
        example.userColor = userPlayer;

        example.gamePlay(example);

        scanner.close();
    }
}
