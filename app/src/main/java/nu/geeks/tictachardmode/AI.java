package nu.geeks.tictachardmode;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by hannespa on 15-05-30.
 */
public class AI {

    private final String TAG = "AIMOVE";

    /**
     * This will calculate the best possible move for the AI at the given board.
     * It will check all 9x9 moves, since the activeSquare might be -1. (All playable moves acceptable).
     * For this reason, most of what happens in here is based on world coordinates.
     *
     * If AI can't make any winning move, this returns {-1,-1}. i.e, AI gives up.
     *
     * @param board
     * @return the AI move in world coordinates.
     */
    public static int[] makeMove(Board board) {
        int[] returnValue = {-1,-1};

        //Will hold the amount of wins every move would generate.
        int[][] bestMove = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9 ; j++){
                bestMove[i][j] = 0;
            }
        }
        loop:
        for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                   bestMove[x][y] = checkMove(board, 0);
                   if(bestMove[x][y] < 0){
                       break loop;
                   }
                }
            }

        int highestValue = 0;
        //Find the best value
        for(int i = 0; i < 9; i++){
            for( int j = 0; j < 9; j++){
                if(bestMove[i][j] > highestValue){
                    highestValue = bestMove[i][j];

                    returnValue[0] = i;
                    returnValue[1] = j;

                }
            }
        }
        return returnValue;

    }


    public static int checkMove(Board board, int counter) {

       // Log.d("AIMOVE","counter = " + counter);
        mainloop:
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {

                //Check if move is in active square.
                if ((board.getActiveMainSquare()[0] == x / 3 && board.getActiveMainSquare()[1] == y / 3) || board.getActiveMainSquare()[0] == -1) {

                    //check that the state of the selected main square is valid
                    if (board.getMainSquares()[x / 3][y / 3].getState() == 'G') {

                        //Check if sub-square is empty
                        if (board.getMainSquares()[x / 3][y / 3].getSubSquares()[x % 3][y % 3].getValue() == ' ') {

                            //Move was valid.
                            //Before we do anything else, lets save the current gamestate so we can revert back to it.
                            char gameState = board.getGameState();

                            //Update the board. Returns the winner if someone won the game. Else it returns 'N'
                            char winner = board.update(x, y);
                            if(winner == 'N') {
                                if (SquareMath.isGameWinnableForAI(board.getMainSquares())) {

                                   // Log.d("AIMOVE", "x = " + x + ", y = " + y + " counter = " + counter);

                                    //Recurse.
                                    return checkMove(board, counter);
                                }
                            }else if(winner == 'O'){
                                //AI won
                                counter++;
                                board.revert(x,y, gameState);
                                return counter;

                            }

                            //Revert
                            board.revert(x,y, gameState);


                        }

                    }

                }

            }
        }
        return 0;
    }
}

