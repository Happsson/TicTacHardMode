package nu.geeks.tictachardmode;

import android.util.Log;

/**
 * Created by hannespa on 15-06-07.
 */
public class SQMath{

    static final String TAG = "MATHTAG:";

    public static char updateState(SubSquare[][] square) {
        for(int i = 0 ; i < 3; i++) {
            if (square[i][0].getValue() == 'X' && square[i][1].getValue() == 'X' && square[i][2].getValue() == 'X'){
                Log.d(TAG, "X won on row " + i);
                return 'X';
            }
            else if (square[i][0].getValue() == 'O' && square[i][1].getValue() == 'O' && square[i][2].getValue() == 'O'){
                Log.d(TAG, "O won on row "+i);
                return 'O';
            }

            else if (square[0][i].getValue() == 'X' && square[1][i].getValue() == 'X' && square[2][i].getValue() == 'X'){
                Log.d(TAG, "X won on col "+i);
                return 'X';
            }
            else if (square[0][i].getValue() == 'O' && square[1][i].getValue() == 'O' && square[2][i].getValue() == 'O'){
                Log.d(TAG, "O won on col "+i);
                return 'O';
            }
        }
        if(square[0][0].getValue() == 'X' && square[1][1].getValue() == 'X' && square[2][2].getValue() == 'X'){
            Log.d(TAG, "X won on dia1");
            return 'X';
        }
        else if(square[0][0].getValue() == 'O' && square[1][1].getValue() == 'O' && square[2][2].getValue() == 'O'){
            Log.d(TAG, "O won on dia1");
            return 'O';
        }

        else if(square[0][2].getValue() == 'X' && square[1][1].getValue() == 'X' && square[2][0].getValue() == 'X'){
            Log.d(TAG, "X won on dia2");
            return 'X';
        }
        else if(square[0][2].getValue() == 'O' && square[1][1].getValue() == 'O' && square[2][0].getValue() == 'O'){
            Log.d(TAG, "O won on dia2");
            return 'O';
        }

        //Square is not won, check if it is a draw or if it is full
        //TODO - check if game is draw, but not full

        else{
            char c = drawOrFull(square);
            Log.d(TAG, "returning with " + c);
            return c;
        }

    }

    private static char drawOrFull(SubSquare[][] square) {

        int movesLeft = 9;

        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                if(square[x][y].getValue() != ' ') movesLeft--;
            }
        }

        if(movesLeft == 0) return 'F';
        else return 'G';

    }

    public static char checkIfGameIsWon(MainSquare[][] activeSquares) {
        for (int i = 0; i < 2; i++) {
            char c = 'N';
            if (i == 0) c = 'X';
            if (i == 1) c = 'O';

            //row
            if (activeSquares[0][0].getState() == c && activeSquares[0][1].getState() == c && activeSquares[0][2].getState() == c)
                return c;
            if (activeSquares[1][0].getState() == c && activeSquares[1][1].getState() == c && activeSquares[1][2].getState() == c)
                return c;
            if (activeSquares[2][0].getState() == c && activeSquares[2][1].getState() == c && activeSquares[2][2].getState() == c)
                return c;


            //column
            if (activeSquares[0][0].getState() == c && activeSquares[1][0].getState() == c && activeSquares[2][0].getState() == c)
                return c;
            if (activeSquares[0][1].getState() == c && activeSquares[2][1].getState() == c && activeSquares[2][1].getState() == c)
                return c;
            if (activeSquares[0][2].getState() == c && activeSquares[2][2].getState() == c && activeSquares[2][2].getState() == c)
                return c;

            //Diagonal
            if (activeSquares[0][0].getState() == c && activeSquares[1][1].getState() == c && activeSquares[2][2].getState() == c)
                return c;
            if (activeSquares[2][0].getState() == c && activeSquares[1][1].getState() == c && activeSquares[0][2].getState() == c)
                return c;


        }
        return 'N';
    }

    /**
     * Checks wether or not the AI can win the game at this moment.
     * Returns false if it can't win, true otherwise.
     *
     * @param s the mainsquares of the board.
     * @return true if game is still winnable.
     */
    public static boolean isGameWinnableForAI(MainSquare[][] s ){

        boolean[] rows = {true, true, true};
        boolean[] cols = {true, true, true};
        boolean[] dias = {true, true};



        for(int i = 0; i < 3; i++) {
            if (!s[0][i].getPlayableFor('O') || !s[1][i].getPlayableFor('O') || !s[2][i].getPlayableFor('O')) cols[i] = false;
            if (!s[i][0].getPlayableFor('O') || !s[i][1].getPlayableFor('O') || !s[i][2].getPlayableFor('O')) rows[i] = false;
        }
        if (!s[0][0].getPlayableFor('O') || !s[1][1].getPlayableFor('O') || !s[2][2].getPlayableFor('O')) dias[0] = false;
        if (!s[2][0].getPlayableFor('O') || !s[1][1].getPlayableFor('O') || !s[0][2].getPlayableFor('O')) dias[0] = false;

        //iterate through all, if anything is true, game is still winnable for AI.
        for(int i = 0; i < 3; i++){
            if(i < 2){
                if(dias[i]) return true;
            }
            if(cols[i]) return true;
            if(rows[i]) return true;
        }
        return false;
    }
}
