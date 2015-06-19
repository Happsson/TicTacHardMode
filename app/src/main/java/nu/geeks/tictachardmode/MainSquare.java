package nu.geeks.tictachardmode;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hannespa on 15-06-01.
 */
public class MainSquare {

    static final String TAG = "MainSquareTAG";

    private int[] analysisArray = new int[4];

    private SubSquare[][] subSquares;
    private char state;

    private ArrayList<int[]> playerXMoves;
    private ArrayList<int[]> playerOMoves;

    /*

    States are:
    'X' - X has won
    'O' - O has won
    'D' - Square cannot be won, but is not yet full
    'F' - Square is full.
    'G' - Square is go (can be played, can be won)

     */


    public MainSquare() {

        subSquares = new SubSquare[3][3];
        playerXMoves = new ArrayList<int[]>();
        playerOMoves = new ArrayList<int[]>();

        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                subSquares[x][y] = new SubSquare();
            }
        }
        state = 'G';
    }


    public SubSquare[][] getSubSquares() {
        return subSquares;
    }

    public void setSubSquares(SubSquare[][] subSquares) {
        this.subSquares = subSquares;
    }

    public char getState() {
        return state;
    }

    /**
     * Returns whether or not this square is playable for @param player.
     * If square is won by the other player, or if it is full, this returns false.
     * Otherwise it returns true.
     *
     * @param player the player to be checked
     * @return false if that player can't play this square.
     */
    public boolean getPlayableFor(char player){
        char c;
        if(player == 'O') c = 'X';
        else if(player == 'X') c = 'O';
        else{
            Log.d(TAG, "Wrong type of user");
            c = 'W'; //WRONG TYPE OF USER;
        }
        if(state == c || state == 'F') return false;
        else return true;
    }

    public void setState(char state) {
        this.state = state;
    }

    public char updateState() {
        state = SQMath.updateState(subSquares);
        return state;
    }

    private void setAdvancedStatePlayerStatus(){
        playerXMoves = SQMath.findGoodMoves('X', subSquares);
        playerOMoves = SQMath.findGoodMoves('O', subSquares);
    }


    public int[] updateAnalysisArray(int index0, int index1){
        if(state == 'X') return new int[] {3,0};
        if(state == 'O') return new int[] {0,3};

        int x;
        int o;

        setAdvancedStatePlayerStatus();
        if(playerXMoves.size() > 0){
            x = playerXMoves.get(playerXMoves.size()-1)[0];
        }else{
            x = -1;
        }
        if(playerOMoves.size() > 0){
            o = playerOMoves.get(playerOMoves.size()-1)[0];
        }else{
            o = -1;
        }

        analysisArray[0] = index0;
        analysisArray[1] = index1;
        analysisArray[2] = x;
        analysisArray[3] = o;

        return analysisArray;

    }

    public int[] getAnalysisArray(){
        return analysisArray;
    }

    public ArrayList<int[]> getPlayerOMoves(){
        return playerOMoves;
    }
    public ArrayList<int[]> getPlayerXMoves(){
        return playerXMoves;
    }

}
