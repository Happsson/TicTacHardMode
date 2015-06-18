package nu.geeks.tictachardmode;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    /**
     * This method finds the moves that makes sense for the player p1 to play.
     * It will return an ArrayList.
     *
     * If two squares is already filled in a winnable situation, and only one is needed to win this square,
     * the returned ArrayList will end with {2,2}. This could hold more than one other move,
     * if there are more than one move that would win the square.
     *
     * If only one square is filled in a winnable situation, and two moves are needed to win this
     * square, the returned ArrayList will end with {1,1}. This will always hold at least two moves needed
     * to win, but will in most cases hold more moves.
     *
     * If zero squares are filled in a winnable situation (note that this does NOT mean that there
     * are no squares filled by the player. It can be a situation where the squares that are filled
     * by the player can not be part of a win.), the returned ArrayList will end with {0,0}.
     *
     * If no winnable moves exists, it will return an empty ArrayList.
     *
     * @param p1 the player that should be analyzed.
     * @param s the subsquare[][] in question.
     * @return ArrayList with the possible moves, if any.
     */
    public static ArrayList<int[]> findGoodMoves(char p1, SubSquare[][] s){
        char p2 = ' ';
        if(p1 == 'X') p2 = 'O';
        if(p1 == 'O') p2 = 'X';

        //three ArrayLists are made. One for each state. Only one will be returned,
        //by the same prioritazion as they are declared below.
        ArrayList<int[]> twos = new ArrayList<int[]>();
        ArrayList<int[]> ones = new ArrayList<int[]>();
        ArrayList<int[]> zeros = new ArrayList<int[]>();

        //First, check all rows.
        for(int row = 0; row < 3; row++){
            //Check if the first row is winnable for p1 (no squares contains p2)
            if(s[row][0].getValue() != p2 && s[row][1].getValue() != p2 && s[row][2].getValue() != p2){

                //Create a temp ArrayList to populate. The values will be added to the correct list
                //when when we know how many possible choices there are.
                ArrayList<int[]> temp = new ArrayList<int[]>();

                for(int pos = 0; pos < 3; pos++){
                    //Check all three positions on this row. If they are not already occupied by p1,
                    //add to list.
                    if(s[row][pos].getValue() != p1){
                        int[] add = {row,pos};
                        temp.add(add);
                    }
                }

                //Add from the temp list to the correct ArrayList.
                if(temp.size() == 1){
                    twos.add(temp.get(0));
                }
                if(temp.size() == 2){
                    ones.add(temp.get(0));
                    ones.add(temp.get(1));
                }
                if(temp.size() == 3){
                    zeros.add(temp.get(0));
                    zeros.add(temp.get(1));
                    zeros.add(temp.get(2));
                }
            }

        }

        //Do the same thing, but for the columns (Not very DRY)
        for(int col = 0; col < 3; col++){
            //Check if the first col is winnable for p1 (no squares contains p2)
            if(s[0][col].getValue() != p2 && s[1][col].getValue() != p2 && s[2][col].getValue() != p2){

                //Create a temp ArrayList to populate. The values will be added to the correct list
                //when when we know how many possible choices there are.
                ArrayList<int[]> temp = new ArrayList<int[]>();

                for(int pos = 0; pos < 3; pos++){
                    //Check all three positions on this col. If they are not already occupied by p1,
                    //add to list.
                    if(s[pos][col].getValue() != p1){
                        int[] add = {pos,col};
                        temp.add(add);
                    }
                }

                //Add from the temp list to the correct ArrayList.
                if(temp.size() == 1){
                    twos.add(temp.get(0));
                }
                if(temp.size() == 2){
                    ones.add(temp.get(0));
                    ones.add(temp.get(1));
                }
                if(temp.size() == 3){
                    zeros.add(temp.get(0));
                    zeros.add(temp.get(1));
                    zeros.add(temp.get(2));
                }
            }
        }

        //One last time for the diagonals.
        if(s[0][0].getValue() != p2 && s[1][1].getValue() != p2 && s[2][2].getValue() != p2){

            //Create a temp ArrayList to populate. The values will be added to the correct list
            //when when we know how many possible choices there are.
            ArrayList<int[]> temp = new ArrayList<int[]>();
            for(int i = 0; i < 3; i++){
                if(s[i][i].getValue() != p1){
                    int[] add = {i,i};
                    temp.add(add);
                }
            }

            //Add from the temp list to the correct ArrayList.
            if(temp.size() == 1){
                twos.add(temp.get(0));
            }
            if(temp.size() == 2){
                ones.add(temp.get(0));
                ones.add(temp.get(1));
            }
            if(temp.size() == 3){
                zeros.add(temp.get(0));
                zeros.add(temp.get(1));
                zeros.add(temp.get(2));
            }

        }

        //The other diagonal.
        if(s[0][2].getValue() != p2 && s[1][1].getValue() != p2 && s[2][0].getValue() != p2){

            //Create a temp ArrayList to populate. The values will be added to the correct list
            //when when we know how many possible choices there are.
            ArrayList<int[]> temp = new ArrayList<int[]>();

            if(s[0][2].getValue() != p1){
                int[] add = {0,2};
                temp.add(add);
            }

            if(s[1][1].getValue() != p1){
                int[] add = {1,1};
                temp.add(add);
            }
            if(s[2][0].getValue() != p1){
                int[] add = {2,0};
                temp.add(add);
            }


            //Add from the temp list to the correct ArrayList.
            if(temp.size() == 1){
                twos.add(temp.get(0));
            }
            if(temp.size() == 2){
                ones.add(temp.get(0));
                ones.add(temp.get(1));
            }
            if(temp.size() == 3){
                zeros.add(temp.get(0));
                zeros.add(temp.get(1));
                zeros.add(temp.get(2));
            }

        }

        //If any of the list is populated, return it.
        if(twos.size() > 1){
            twos.add(new int[] {2,2});
            return twos;
        }
        if(ones.size() > 1){
            ones.add(new int[] {1,1});
            return ones;
        }
        if(zeros.size() > 1){
            zeros.add(new int[] {0,0});
            return zeros;
        }

        //no moves avalibe. Return empty ArrayList;
        return new ArrayList<int[]>();

    }

    /**
     *
     *
     */
    public static ArrayList<int[]> checkBoardMoves(char p1, MainSquare[][] m, int x, int y){
        char p2 = ' ';
        if(p1 == 'X') p2 = 'O';
        if(p1 == 'O') p2 = 'X';

        //Start by checking if p1 can win by winning this main square, of if it can be
        //a part of a win.
        boolean canWinOnRow = false;
        if(
                //If the rest of the row is either p1 or 'G'
                (m[x][0].getState() == p1 || m[x][0].getState() == 'G')
                &&
                (m[x][1].getState() == p1 || m[x][1].getState() == 'G')
                &&
                (m[x][2].getState() == p1 || m[x][2].getState() == 'G')
                ){

            canWinOnRow = true;
        }

        boolean canWinOnCol = false;
        if(
                //If the rest of the column is either p1 or 'G'
                (m[0][y].getState() == p1 || m[0][y].getState() == 'G')
                        &&
                        (m[1][y].getState() == p1 || m[1][y].getState() == 'G')
                        &&
                        (m[2][y].getState() == p1 || m[2][y].getState() == 'G')
                ){

            canWinOnRow = true;
        }

        int isSquarePartOfDiagonal = isSquarePartOfDiagonal(x,y);

        boolean canWinOnDia1 = false;
        boolean canWinOnDia2 = false;

        //Check if diagonal is winnable, if it is on a square.
        if(isSquarePartOfDiagonal != 0){


            if(isSquarePartOfDiagonal == 3){
            //square is at {1,1}, both diagonals are possible.
                if(     (m[0][0].getState() == p1 || m[0][0].getState() == 'G')
                        &&
                        (m[2][2].getState() == p1 || m[2][2].getState() == 'G')) {
                    //diagonal 0,0  1,1 2,2 can be won.
                    //TODO - kolla om ett eller två drag krävs.

                }
                if(     (m[0][2].getState() == p1 || m[0][2].getState() == 'G')
                        &&
                        (m[2][0].getState() == p1 || m[2][0].getState() == 'G')) {
                    //Diagonal 0,2  1,1  2,0  can be won.
                    //TODO - kolla om ett eller två drag krävs.
                }

            }

            if(isSquarePartOfDiagonal == 1){
                //Square is part of diagonal 1.
                //TODO - kolla om diagonal 1 går att vinna. Kolla om ett eller två drag krävs.
            }
            if(isSquarePartOfDiagonal == 2){
                //TODO - kolla om diagonal 2 går att vinna. Kolla om ett eller två drag krävs.
            }

        }


        //Find out how many moves are needed.
        int movesNeededOnRow = -1; //Set as -1, in case this row couldn't be won.
        if(canWinOnRow){
            movesNeededOnRow = 3; //assume three moves are needed to win
            for(int i = 0; i < 3; i++){
                if(m[x][i].getState() == p1) movesNeededOnRow--; //one less move is needed.
            }
        }





    }

    private static int isSquarePartOfDiagonal(int x, int y) {
        if(x == 0 && y == 0) return 1;
        if(x == 1 && y == 1) return 3;
        if(x == 2 && y == 2) return 1;
        if(x == 0 && y == 2) return 2;
        if(x == 2 && y == 0) return 2;
        return 0;

    }

}
