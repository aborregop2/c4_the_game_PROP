package edu.epsevg.prop.lab.c4;

public class myJugador implements Jugador, IAuto {
    private int maxDepth;

    public myJugador(int depth) {
        this.maxDepth = depth;
    }

    @Override
    public int moviment(Tauler t, int color) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int col = 0; col < t.getMida(); col++) {
            if (t.movpossible(col)) {
                Tauler newBoard = new Tauler(t);
                newBoard.afegeix(col, color);

                int score = minimax(newBoard, maxDepth - 1, true, color);
                
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }
        
        return bestMove;
    }

    private int minimax(Tauler board, int depth, boolean isMaximizing, int color) {
        if (depth == 0 || !board.espotmoure()) {
            return heuristic(board, color);
        }

        int opponentColor = -color;
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < board.getMida(); col++) {
                if (board.movpossible(col)) {
                    Tauler newBoard = new Tauler(board);
                    newBoard.afegeix(col, color);

                    int eval = minimax(newBoard, depth - 1, true, color);
                    maxEval = Math.max(maxEval, eval);
                }
            }
            return maxEval;
        } 
        else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < board.getMida(); col++) {
                if (board.movpossible(col)) {
                    Tauler newBoard = new Tauler(board);
                    newBoard.afegeix(col, opponentColor);

                    int eval = minimax(newBoard, depth - 1, false, color);
                    minEval = Math.min(minEval, eval);
                }
            }
            return minEval;
        }
    }

    private int heuristic(Tauler board, int color) {
        int opponentColor = -color;
        
        for (int col = 0; col < board.getMida(); col++) {
            if (board.solucio(col, color)) {
                return 1000;
            }
            if (board.solucio(col, opponentColor)) {
                return -1000;
            }
        }

        return 0;
    }

    @Override
    public String nom() {
        return "myJugador";
    }
}
