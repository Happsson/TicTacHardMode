package nu.geeks.tictachardmode;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

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


        char[][] c = new char[3][3];

        //Convert subsquares to char-matrix
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                c[x][y] = s[x][y].getValue();
            }
        }

        //three ArrayLists are made. One for each state. Only one will be returned.
        ArrayList<int[]> twos = new ArrayList<int[]>();
        ArrayList<int[]> ones = new ArrayList<int[]>();
        ArrayList<int[]> zeros = new ArrayList<int[]>();

        //Check rows, columns and the two diagonals for good moves, an add them to
        //the corresponding array.
        checkRowsForGoodMoves(p1, c, twos, ones, zeros);
        checkColumnsForGoodMoves(p1, c, twos, ones, zeros);
        checkDiagonalForGoodMoves(p1, c, twos, ones, zeros, 0, 0, 1, 1, 2, 2);
        checkDiagonalForGoodMoves(p1, c, twos, ones, zeros, 0, 2, 1, 1, 2, 0);


        //If any of the list is populated, return it.
        //Also shuffle the arrays. Otherwise the AI will play square 0,0 a lot in the beginning.
        if(twos.size() > 1){

            Collections.shuffle(twos);
            twos.add(new int[] {2,2});
            return twos;
        }
        if(ones.size() > 1){
            Collections.shuffle(ones);
            ones.add(new int[] {1,1});
            return ones;
        }
        if(zeros.size() > 1){
            Collections.shuffle(zeros);
            zeros.add(new int[] {0,0});
            return zeros;
        }

        //no moves available. Return empty ArrayList;
        return new ArrayList<int[]>();

    }

    private static void checkDiagonalForGoodMoves(char p1, char[][] s,
                                                  ArrayList<int[]> twos, ArrayList<int[]> ones, ArrayList<int[]> zeros,
                                                  int a, int b, int c, int d, int e, int f) {
        //The other diagonal.
        if(     (s[a][b] == p1 || s[a][b] == 'G' || s[a][b] == ' ')
                &&
                (s[c][d] == p1 || s[c][d] == 'G' || s[c][d] == ' ')
                &&
                (s[e][f] == p1 || s[e][f] == 'G' || s[e][f] == ' '))
        {

            //Create a temp ArrayList to populate. The values will be added to the correct list
            //when when we know how many possible choices there are.
            ArrayList<int[]> temp = new ArrayList<int[]>();

            if(s[a][b] != p1){
                int[] add = {a,b};
                temp.add(add);
            }

            if(s[c][d] != p1){
                int[] add = {c,d};
                temp.add(add);
            }
            if(s[e][f] != p1){
                int[] add = {e,f};
                temp.add(add);
            }


            //Add from the temp list to the correct ArrayList.
            addToCorrectArray(temp,twos,ones,zeros);

        }

    }

    private static void checkColumnsForGoodMoves(char p1, char[][] s, ArrayList<int[]> twos, ArrayList<int[]> ones,
                                                 ArrayList<int[]> zeros) {
        //Do the same thing, but for the columns (Not very DRY)
        for(int col = 0; col < 3; col++){
            //Check if the first col is winnable for p1 (no squares contains p2)
            if(     (s[0][col] == p1 || s[0][col] == 'G' || s[0][col] == ' ')
                    &&
                    (s[1][col] == p1 || s[1][col] == 'G' || s[1][col] == ' ')
                    &&
                    (s[2][col] == p1 || s[2][col] == 'G' || s[2][col] == ' '))
            {

                //Create a temp ArrayList to populate. The values will be added to the correct list
                //when when we know how many possible choices there are.
                ArrayList<int[]> temp = new ArrayList<int[]>();

                for(int pos = 0; pos < 3; pos++){
                    //Check all three positions on this col. If they are not already occupied by p1,
                    //add to list.
                    if(s[pos][col] != p1){
                        int[] add = {pos,col};
                        temp.add(add);
                    }
                }

                addToCorrectArray(temp,twos,ones,zeros);
            }
        }

    }

    private static void checkRowsForGoodMoves(char p1, char[][] s, ArrayList<int[]> twos, ArrayList<int[]> ones,
                                              ArrayList<int[]> zeros) {

        //First, check all rows.
        for(int row = 0; row < 3; row++){
            //Check if the first row is winnable for p1 (no squares contains p2)
            if(     (s[row][0] == p1 || s[row][0] == 'G' || s[row][0] == ' ')
                    &&
                    (s[row][1] == p1 || s[row][1] == 'G' || s[row][1] == ' ')
                    &&
                    (s[row][2] == p1 || s[row][2] == 'G' || s[row][2] == ' '))
            {

                //Create a temp ArrayList to populate. The values will be added to the correct list
                //when when we know how many possible choices there are.
                ArrayList<int[]> temp = new ArrayList<int[]>();

                for(int pos = 0; pos < 3; pos++){
                    //Check all three positions on this row. If they are not already occupied by p1,
                    //add to list.
                    if(s[row][pos]!= p1){
                        int[] add = {row,pos};
                        temp.add(add);
                    }
                }

                addToCorrectArray(temp, twos, ones, zeros);

            }

        }
    }

    private static void addToCorrectArray(ArrayList<int[]> temp, ArrayList<int[]> twos, ArrayList<int[]> ones,
                                          ArrayList<int[]> zeros) {
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



    public static int cbm(char p1, MainSquare[][] m, int x, int y){

        //Convert MainSquares to char-matrix.
        char[][] c = new char[3][3];
        for(int xx = 0; xx < 3; xx++){
            for(int yy = 0; yy < 3; yy++){
                c[xx][yy] = m[xx][yy].getState();
            }
        }

        //Create three ArrayLists
        ArrayList<int[]> twos = new ArrayList<int[]>();
        ArrayList<int[]> ones = new ArrayList<int[]>();
        ArrayList<int[]> zeros = new ArrayList<int[]>();

        checkRowsForGoodMoves(p1, c, twos, ones, zeros);
        checkColumnsForGoodMoves(p1, c, twos, ones, zeros);
        checkDiagonalForGoodMoves(p1, c, twos, ones, zeros, 0,0, 1,1 ,2,2);
        checkDiagonalForGoodMoves(p1, c, twos, ones, zeros, 0,2, 1,1, 2,0);

        //Check if the selected square, the x and y argument passed in to this method,
        //is part of a winning move. First check if this move alone would win the game.
        for(int[] moves : twos){
            if(moves[0] == x && moves[1] == y){
                return 3;
            }
        }
        for(int[] moves : ones){
            if(moves[0] == x && moves[1] == y){
                return 2;
            }
        }
        for(int[] moves : zeros){
            if(moves[0] == x && moves[1] == y){
                return 1;
            }
        }
        return 0; //None of the above means that this square can not be part of a win.

    }

    private static int isSquarePartOfDiagonal(int x, int y) {
        if(x == 0 && y == 0) return 1;
        if(x == 1 && y == 1) return 3;
        if(x == 2 && y == 2) return 1;
        if(x == 0 && y == 2) return 2;
        if(x == 2 && y == 0) return 2;
        return 0;

    }

    public static int[] toWorldCoordinate(int aX, int aY, int sX, int sY){
        int[] ret = new int[2];
        ret[0] = (aX*3) + sX;
        ret[1] = (aY*3) + sY;
        return ret;
    }

}
