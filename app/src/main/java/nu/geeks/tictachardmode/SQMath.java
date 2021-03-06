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
            if (activeSquares[0][1].getState() == c && activeSquares[1][1].getState() == c && activeSquares[2][1].getState() == c)
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

    public static boolean isSquareWinnable(MainSquare m, char player, int moveX, int moveY){

        //Create a char-matrix, representing the mainsquare.
        char[][] c = new char[3][3];
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                c[x][y] = m.getSubSquares()[x][y].getValue();
            }
        }
        //add the move in question
        c[moveX][moveY] = player;

        //Check if the row, column or diagonal the move is part of is winnable.
        if(     (c[moveX][0] == player || c[moveX][0] == ' ')
                &&
                (c[moveX][1] == player || c[moveX][1] == ' ')
                &&
                (c[moveX][2] == player || c[moveX][2] == ' ')){
            return true;
        }

        if(     (c[0][moveY] == player || c[0][moveY] == ' ')
                &&
                (c[1][moveY] == player || c[1][moveY] == ' ')
                &&
                (c[2][moveY] == player || c[2][moveY] == ' ')){
            return true;
        }
        if(     (moveX == 0 && moveY == 0)
                ||
                (moveX == 1 && moveY == 1)
                ||
                (moveX == 2 && moveY == 2)
                ){
            if(     (c[0][0] == player || c[0][0] == ' ')
                    &&
                    (c[1][1] == player || c[1][1] == ' ')
                    &&
                    (c[2][2] == player || c[2][2] == ' ')){
                return true;

            }

        }
        if(     (moveX == 2 && moveY == 0)
                ||
                (moveX == 1 && moveY == 1)
                ||
                (moveX == 2 && moveY == 0)
                ){
            if(     (c[2][0] == player || c[2][0] == ' ')
                    &&
                    (c[1][1] == player || c[1][1] == ' ')
                    &&
                    (c[0][2] == player || c[0][2] == ' ')){
                return true;

            }

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
        if(twos.size() > 0){
            ArrayList<int[]> ret = removeDuplicates(twos);
            Collections.shuffle(ret);

            ret.add(new int[] {2,-1});

            return ret;
        }
        if(ones.size() > 0){
            ArrayList<int[]> ret = removeDuplicates(ones);
            Collections.shuffle(ret);
            ret.add(new int[] {1,-1});
            return ret;
        }
        if(zeros.size() > 0){
            ArrayList<int[]> ret = removeDuplicates(zeros);
            Collections.shuffle(ret);
            ret.add(new int[] {0,-1});
            return ret;
        }

        //no moves available. Return empty ArrayList;
        return new ArrayList<int[]>();

    }

    private static ArrayList<int[]> removeDuplicates(ArrayList<int[]> list) {

        ArrayList<int[]> ret = new ArrayList<int[]>();
        for(int[] e1 : list){
            boolean existsInArray = false;
            for(int[] e2 : ret) if (e1[0] == e2[0] && e1[1] == e2[1]) existsInArray = true;
            if(!existsInArray) ret.add(e1);
        }
        return ret;
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



    public static int[] toWorldCoordinate(int aX, int aY, int sX, int sY){
        int[] ret = new int[2];
        ret[0] = (aX*3) + sX;
        ret[1] = (aY*3) + sY;
        return ret;
    }

}
