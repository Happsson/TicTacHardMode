package nu.geeks.tictachardmode;

import android.util.Log;

/**
 * Created by hannespa on 15-06-01.
 */
public class MainSquare {

    static final String TAG = "MainSquareTAG";

    SubSquare[][] subSquares;
    char state;

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
        state = SquareMath.updateState(subSquares);
        return state;
    }
}
