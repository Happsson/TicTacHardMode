package nu.geeks.tictachardmode;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *
 *
 * Created by hannespa on 15-05-30.
 */

/* new take

A take on a non-brute force attempt. Think more like a human.

Give all squares a value array, based on how attractive it is for the AI and the Player.
Values are in an array, [AI points, player points, AI moves made to win square, player moves made to win square].

    * A square gets 4 in [0] || [1] if winning this is the ONLY way to win the game. (SKIPPED)

    * A square gets 3 if [0] || [1] winning this would win the game.
    * A square gets 2 if [0] || [1] winning this would get two squares in a row/column/diagonal.
    * A square gets 1 if [0] || [1] winning this square in some way could be part of a win.
    * A square gets 0 if [0] || [1] winning this could in no way help the player.

    * A square gets -1 in [2]|| [3] if square is full or not winnable
    * A square gets 0 in [2] || [3] if three moves are needed in a square to win
    * A square gets 1 in [2] || [3] if two moves are needed in a square to win
    * A square gets 2 in [2] || [3] if one moves are needed in a square to win
    * A square gets 3 in [2] || [3] if square is won.

Calculate the values for all main squares on the board.

First, check if all squares has value 1 for [0] and [1], and a value of 1 or less for [2] and [3].
This will be the case the first few rounds of the game. If so, just make a random move.

    1.  If AI-points for this square gets a 4:
        1.1 Check if AI can win this square with this move. If so, make this move, disregard
            what happens after this move (game is over)
        1.2 Check if AI can prevent the player from winning this square.

    2.  If AI-points for this square gets a positive value (the AI can win the game by winning this square).

        2.2 Check if the AI can win the square with this move.
        2.3 Check if the AI can prevent the player from winning this square.

    3.  If AI points is 0 and player points is positive (the Player can win the game by winning this
        square).

        3.1 Check if AI can prevent the player from winning this square.

 */
public class AI {

    private final String TAG = "AIMOVE";


    public static int[] makeMove(Board board) {

        int x = board.getActiveMainSquare()[0];
        int y = board.getActiveMainSquare()[1];

        board.updateAnalysisArrays();

        String st = ":\n\n";
        for(int a = 0; a < 3; a ++){
            for(int b = 0; b < 3; b++){
                st += " {" + a + "," + b +"}: ";

                st += "AI Game " +board.getMainSquares()[a][b].getAnalysisArray()[0] + ",";
                st += "P1 Game " +board.getMainSquares()[a][b].getAnalysisArray()[1] + ",";
                st += "AI Sqre " +board.getMainSquares()[a][b].getAnalysisArray()[2] + ",";
                st += "P1 Sqre " +board.getMainSquares()[a][b].getAnalysisArray()[3] + ",";
                st += " \t";
            }
            st += " \n";
        }
        Log.d("AI-ERROR", st);

        //Based on the analysisArray, make a play for the AI.
        /*
        aa[0]
            Can be:
                0   -   The AI cannot win the game by winning this square
                1   -   The AI can win the game with this square, but needs two more squares
                2   -   The AI can win the game with this square, but needs one more square.
                3   -   The AI would win the game if it wins this square.

        aa[1]
            Same as above, but for the player.
        aa[2]
            can be:
                -1  -   The AI cannot win this square.
                0   -   The AI can win this square with three moves.
                        valid moves stored in arrayList in the active mainsquare.
                1   -   The AI can win this square with two moves.
                        valid moves stored in arrayList in the active mainsquare.
                2   -   The AI can win this square with one move.
                        Valid moves stored in arrayList in the active mainsquare.
                3   -   The AI has already won this square.
        aa[3]
            Same as above, but for the player.

         */
        //If there is an active square.
        if (board.getActiveMainSquare()[0] != -1) {
            return decisionTree(board, x, y, true);
        } else

        {
            return decisionTreeNoActiveSquare(board, x, y);

        }

    }



    private static int[] decisionTreeNoActiveSquare(Board board, int x, int y) {
        //No active square.
        //Iterate through all the squares and find one that works for this step and the next.
        for(int xx = 0; xx < 3; xx++){
            for(int yy = 0; yy < 3; yy++){
                char state = board.getMainSquares()[xx][yy].getState();
                if(state == 'G' || state == 'D'){
                    //Square is playable.
                    int[] move = decisionTree(board, xx, yy, false);
                    if(move != null){
                        return move;
                    }

                }
            }
        }
        //There is no active square, but every single move the AI can make will be a losing move.
        //Just pick the first one, and let the player win.
        for(int xx = 0; xx < 9; xx++){
            for(int yy = 0; yy < 9; yy++){
                if(board.getMainSquares()[xx / 3][yy / 3].getSubSquares()[xx % 3][yy % 3].getValue() == ' '){
                    return new int[] {xx,yy};
                }
            }
        }
        Log.d("AI-ERROR", "Nothing move was done. Don't know why. :D");
        return null;

    }

    private static int[] decisionTree(Board board, int x, int y, boolean acceptBadMove) {
        int[] aa = board.getMainSquares()[x][y].getAnalysisArray();

        if (aa[0] == 3) {
            //Winning this would win the game.
            return decisionTreeForAI3(aa, board, x, y, acceptBadMove);
        }
        else if (aa[0] == 2) {
            //This situation is handled almost exactly as aa[0] == 3, but with one key difference,
            //since winning this square would not mean winning this game, no moves are safe.
            return decisionTreeForAI2(aa, board, x, y, acceptBadMove);
        }
        else if (aa[0] == 1) {
            //This situation is handled exactly the same as aa[0] == 2.
            return decisionTreeForAI2(aa, board, x, y, acceptBadMove);

        }
        else if (aa[0] == 0) {
            //For now this is also handled the same way as aa[0] == 1 and aa[0] == 2.
            return decisionTreeForAI2(aa, board, x, y, acceptBadMove);
        }
        else{
            //Now something has happened. If we get here, I have done something wrong.
            Log.d("AI-ERROR", "analysisArray invalid for all aa[0]");
            return new int[] {-1,-1};
        }

    }

    private static int[] decisionTreeForAI2(int[] aa, Board board, int x, int y, boolean acceptBadMove) {
        if (aa[2] == 2 || aa[2] == 1 || aa[2] == 0) {
            //For now, lets handle these situations the same way.
            int[] ret = decisionTreeForAI31(aa, board, x, y, board.getMainSquares()[x][y].getPlayerOMoves());
            if (ret == null) {
                //If ret == null, all the available moves are kind of bad. Read the info in this method.
                if(acceptBadMove) return pickABadMove(aa, board, x, y, board.getMainSquares()[x][y].getPlayerOMoves());
                return null;
            } else {
                return ret;
            }

        }
        else if (aa[2] == -1) {
            //The AI cannot win this square.
            //There are no moves in the getPlayerOMoves-array, since no move would be a winning move.
            //Instead, lets just check all the available moves by checking if it is at all possible
            //to play the move, and let the decisionTreeForAI31 make a choice.
            ArrayList<int[]> playerOMoves = new ArrayList<int[]>();
            int[] active = board.getActiveMainSquare();
            for (int xx = 0; xx < 3; xx++) {
                for (int yy = 0; yy < 3; yy++) {
                    if (board.getMainSquares()[active[0]][active[1]].getSubSquares()[xx][yy].getValue() == ' ') {
                        playerOMoves.add(new int[]{xx, yy});
                    }
                }
            }

            int[] ret = decisionTreeForAI31(aa, board, x, y, playerOMoves);
            if (ret == null) {
                //If ret == null, all the available moves are kind of bad. Read the info in this method.
                if(acceptBadMove) return pickABadMove(aa, board, x, y, board.getMainSquares()[x][y].getPlayerOMoves());
                return null;
            } else {
                return ret;
            }
        }
        else{
            //Now something has happened. If we get here, I have done something wrong.
            Log.d("AI-ERROR", "analysisArray invalid for aa[0] = 3");
            return new int[] {-1,-1};
        }

    }

    private static int[] decisionTreeForAI3(int[] aa, Board board, int x, int y, boolean acceptBadMove) {
        if (aa[2] == 2) {
            //This square is winnable with one move. This is a safe move, lets do it.
            int[] move = board.getMainSquares()[x][y].getPlayerOMoves().get(0);
            int[] worldCoords = SQMath.toWorldCoordinate(x, y, move[0], move[1]);
            return worldCoords;
        } else if (aa[2] == 1 || aa[2] == 0) {
            //For now, lets handle these situations the same way.
            int[] ret = decisionTreeForAI31(aa, board, x, y, board.getMainSquares()[x][y].getPlayerOMoves());
            if (ret == null) {
                if(acceptBadMove) return pickABadMove(aa, board, x, y, board.getMainSquares()[x][y].getPlayerOMoves());
                return null;
            }else{
                return ret;
            }
        } else if (aa[2] == -1) {
            //The AI cannot win this square.
            //There are no moves in the getPlayerOMoves-array, since no move would be a winning move.
            //Instead, lets just check all the available moves by checking if it is at all possible
            //to play the move, and let the decisionTreeForAI31 make a choice.
            ArrayList<int[]> playerOMoves = new ArrayList<int[]>();
            int[] active = board.getActiveMainSquare();
            for (int xx = 0; xx < 3; xx++) {
                for (int yy = 0; yy < 3; yy++) {
                    if (board.getMainSquares()[active[0]][active[1]].getSubSquares()[xx][yy].getValue() == ' ') {
                        playerOMoves.add(new int[]{xx, yy});
                    }
                }
            }
            int[] ret = decisionTreeForAI31(aa, board, x, y, playerOMoves);
            if (ret == null) {
                if(acceptBadMove) return pickABadMove(aa, board, x, y, playerOMoves);
                return null;
            }else{
                return ret;
            }
        } else {
                //Now something has happened. If we get here, I have done something wrong.
                Log.d("AI-ERROR", "analysisArray invalid for aa[0] = 3");
                return new int[]{-1, -1};
            }
        }


    private static int[] pickABadMove(int[] aa, Board board, int x, int y, ArrayList<int[]> playerOMoves) {
            //Either the move will make the player win, or will give the player a free for all.
            //Prefer free for all, if possible. Iterate through them all one more time and select the
            //first free for all.
            for(int[] possibleMove : playerOMoves){
                MainSquare mainSquare = board.getMainSquares()[possibleMove[0]][possibleMove[1]];
                if(mainSquare.getState() == 'X' || mainSquare.getState() == 'O'){
                    int[] worldCoords = SQMath.toWorldCoordinate(x, y, possibleMove[0], possibleMove[1]);
                    return worldCoords;
                }
            }
            //If we get to this point, the AI has lost the game. Just let the player win.
            int[] move = board.getMainSquares()[x][y].getPlayerOMoves().get(0);
            int[] worldCoords = SQMath.toWorldCoordinate(x, y, move[0], move[1]);
            return worldCoords;
    }



    private static int[] decisionTreeForAI31(int[] aa, Board board, int x, int y, ArrayList<int[]> playerOMoves) {

        //Check for all the available moves, if this move would not cause the player
        //to win, or give the player a free-for-all, go for it.
        for (int[] possibleMove : playerOMoves) {
            if (possibleMove[1] != -1) {
                //For each move, we are looking on step ahead, by getting the current state of the
                //Square we would send the player to.
                MainSquare mainSquare = board.getMainSquares()[possibleMove[0]][possibleMove[1]];
                if (mainSquare.getState() == 'D') {
                    //The square is not winnable for anyone, so lets play it.
                    int[] worldCoords = SQMath.toWorldCoordinate(x, y, possibleMove[0], possibleMove[1]);
                    return worldCoords;
                }
                if (mainSquare.getState() == 'G') {   //If the square is playable
                    int[] analysis = mainSquare.getAnalysisArray();
                    if (analysis[1] == 3) { //The player would win the game if it wins this square

                        if (analysis[3] == -1) {
                            //The player cannot win this square. Cool. Let the AI play this move.
                            int[] worldCoords = SQMath.toWorldCoordinate(x, y, possibleMove[0], possibleMove[1]);
                            return worldCoords;

                        }
                        if (analysis[3] == 0) {
                            //The player can win this square, but it would take three more moves.
                            //That's fine for now, let the AI play this move.
                            int[] worldCoords = SQMath.toWorldCoordinate(x, y, possibleMove[0], possibleMove[1]);
                            return worldCoords;
                        }
                        if (analysis[3] == 1) {
                            //Now we are getting kind of close. The player can win this square, but
                            //it would take one more move. Still fine though, let the AI play this move.
                            int[] worldCoords = SQMath.toWorldCoordinate(x, y, possibleMove[0], possibleMove[1]);
                            return worldCoords;
                        } else {
                            //NO! NO! This is NOT OK! If the AI makes this move, the player wins.
                        }
                    } else if (analysis[1] == 2) {
                        //The player could win the square
                    }

                }
            }
        }

        //This is a bad situation. All the moves are actually really bad. We return null from here,
        //and handle this in the method that called here.
        return null;


    }
}

