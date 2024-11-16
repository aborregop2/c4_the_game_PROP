package edu.epsevg.prop.lab.c4;
import java.util.HashSet;


public class MyJugador implements Jugador, IAuto {
    private int maxDepth;
    private int heristicsCalculations = 0;


    public MyJugador(int depth) {
        this.maxDepth = depth;
    }

    @Override
    public int moviment(Tauler t, int color) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        heristicsCalculations = 0;

        for (int col = 0; col < t.getMida(); col++) {
            if (t.movpossible(col)) {
                Tauler newBoard = new Tauler(t);
                newBoard.afegeix(col, color);

                //int score = minimaxNoPoda(newBoard, maxDepth - 1, false, color); // , Integer.MIN_VALUE, Integer.MAX_VALUE
                int score = minimax(newBoard, maxDepth - 1, false, color, Integer.MIN_VALUE, Integer.MAX_VALUE);
                
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }

        System.out.println("Heuristics calculations: " + heristicsCalculations);
        
        return bestMove;
    }

    private int minimax(Tauler board, int depth, boolean isMaximizing, int color, int alpha, int beta) {
        if (depth == 0 || !board.espotmoure()) {
            return heuristic(board, color);
        }
    
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < board.getMida(); col++) {
                if (board.movpossible(col)) {
                    Tauler newBoard = new Tauler(board);
                    newBoard.afegeix(col, color);
    
                    int eval = minimax(newBoard, depth - 1, false, color, alpha, beta);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < board.getMida(); col++) {
                if (board.movpossible(col)) {
                    Tauler newBoard = new Tauler(board);
                    newBoard.afegeix(col, -color);
    
                    int eval = minimax(newBoard, depth - 1, true, color, alpha, beta);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }
    
    private int minimaxNoPoda(Tauler board, int depth, boolean isMaximizing, int color) {

        if (depth == 0 || !board.espotmoure()) {
            return heuristic(board, color);
        }
    
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < board.getMida(); col++) {
                if (board.movpossible(col)) {
                    Tauler newBoard = new Tauler(board);
                    newBoard.afegeix(col, color);
    
                    int eval = minimaxNoPoda(newBoard, depth - 1, false, color);
                    maxEval = Math.max(maxEval, eval);
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < board.getMida(); col++) {
                if (board.movpossible(col)) {
                    Tauler newBoard = new Tauler(board);
                    newBoard.afegeix(col, -color);
    
                    int eval = minimaxNoPoda(newBoard, depth - 1, true, color);
                    minEval = Math.min(minEval, eval);
                }
            }
            return minEval;
        }
    }
    
    

    private int heuristic(Tauler board, int color) {
        heristicsCalculations++;
        int opponentColor = -color;
        int score = 0;
    
        for (int row = 0; row < board.getMida(); row++) {
            for (int col = 0; col < board.getMida(); col++) {
                int currentColor = board.getColor(row, col);
    
                if (currentColor == color) {
                    score += evaluatePosition(board, row, col, color);
                } 
                else if (currentColor == opponentColor) {
                    score -= evaluatePosition(board, row, col, opponentColor);
                }
            }
        }
    
        return score;
    }
    
    private int evaluatePosition(Tauler board, int row, int col, int color) {
        int score = 0;
    
        score += evaluateDirection(board, row, col, color, 1, 0);
        score += evaluateDirection(board, row, col, color, 0, 1);
        score += evaluateDirection(board, row, col, color, 1, 1);
        score += evaluateDirection(board, row, col, color, 1, -1);
    
        return score;
    }
    
    private int evaluateDirection(Tauler board, int row, int col, int color, int dRow, int dCol) {
        int size = board.getMida();
        int count = 0;
    
        for (int i = 0; i < 4; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;
    
            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size && board.getColor(newRow, newCol) == color) {
                count++;
            } else {
                break;
            }
        }
    
        if (count == 4) return 1000;
        if (count == 3) return 100;
        if (count == 2) return 10;
        if (count == 1) return 1;

        return 0;
    }
    

    @Override
    public String nom() {
        return "MyJugador";
    }
}
