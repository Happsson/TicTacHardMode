package nu.geeks.tictachardmode;

/**
 * Created by hannespa on 15-06-01.
 */
public class MainSquare {

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

    public void setState(char state) {
        this.state = state;
    }

    public char updateState() {
        state = SquareMath.updateState(subSquares);
        return state;
    }
}
