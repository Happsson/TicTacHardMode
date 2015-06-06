package nu.geeks.tictachardmode;

import android.graphics.Color;
import android.widget.Button;

/**
 * Created by hannespa on 15-06-01.
 */
public class Board {

    final int ACTIVECOLOR = Color.argb(255, 155, 228, 102);
    final int INACTIVECOLOR = Color.argb(255, 244, 142, 102);
    final int XWONCOLOR = Color.argb(255, 85, 68, 30);
    final int OWONCOLOR = Color.argb(255, 217,204,30);


    //9 main squares
    private MainSquare[][] mainSquares;

    private char activePlayer;

    /**
     * Game states are:
     * X = X won
     * O = O won
     * D = Games is draw / no one can win
     * G = Games is go.
     */
    private char gameState;


    private int[] activeMainSquare;



    public Board(char activePlayer) {

        this.activePlayer = activePlayer;

        mainSquares = new MainSquare[3][3];

        for(int x = 0; x < 3; x++){
            for (int y = 0; y < 3; y++){
                mainSquares[x][y] = new MainSquare();
            }
        }

        activeMainSquare = new int[2];
        activeMainSquare[0] = -1;
        activeMainSquare[1] = -1;

        gameState = 'G';
    }


    public MainSquare[][] getMainSquares() {
        return mainSquares;
    }

    //Getters and setters
    public void setMainSquares(MainSquare[][] mainSquares) {
        this.mainSquares = mainSquares;
    }

    public char getActivePlayer() {
        return activePlayer;
    }

    public void changeActivePlayer() {

        if(activePlayer == 'X') activePlayer = 'O';
        else if(activePlayer == 'O') activePlayer = 'X';
    }

    public void setSubSquareValue(int x, int y, int subX, int subY){
        getMainSquares()[x][y].getSubSquares()[subX][subY].setValue(activePlayer);
        getMainSquares()[x][y].getSubSquares()[subX][subY].setButtonText(activePlayer);
    }

    public char getGameState() {
        return gameState;
    }

    public void setGameState(char gameState) {
        this.gameState = gameState;
    }

    public int[] getActiveMainSquare() {
        return activeMainSquare;
    }

    public void setActiveMainSquare(int x, int y) {

        this.activeMainSquare[0] = x;
        this.activeMainSquare[1] = y;
    }


    public char update(int x, int y) {


        //Set the value of the square
        setSubSquareValue(x / 3, y / 3, x % 3, y % 3);



        //Update the played square
        mainSquares[ x / 3 ][ y / 3].updateState();

        // Set active square.
        setActiveMainSquare( x % 3 , y % 3);

        //Check if the game is over
        char winner = SquareMath.checkIfGameIsWon(mainSquares);

        //Change player
        changeActivePlayer();


        return winner;

    }

    public void updateGraphics() {

        if (gameState == 'G' || gameState == 'D') {

            //Iterate through all subsquares, and set them to the correct color.
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {

                    //Get current state for the mainsquare
                    MainSquare square = getMainSquares()[x][y];
                    for (int subX = 0; subX < 3; subX++) {
                        for (int subY = 0; subY < 3; subY++) {

                            setSquareColor(square, subX, subY, x, y);


                        }
                    }

                }
            }

        }else{
           //Game is over. The current player has won.
           //TODO - update the graphics here.
        }
    }

    private void setSquareColor(MainSquare square, int subX, int subY, int x, int y) {
        //If activeMainSquare is -1, all playable squares is active.
        if (activeMainSquare[0] == -1) {
            if (square.getState() == 'G') {
                //All squares that are playable are green
                square.getSubSquares()[subX][subY].setColor(ACTIVECOLOR);
            } else {
                setPlayerColor(square, subX,subY);
            }

        } else {
            if (activeMainSquare[0] == x && activeMainSquare[1] == y) {
                //This is the only active main square.
                square.getSubSquares()[subX][subY].setColor(ACTIVECOLOR);

            } else {
                setPlayerColor(square, subX,subY);
            }
        }
    }

    private void setPlayerColor(MainSquare square, int subX, int subY) {
        if(square.getState() == 'X') {
            //X has won this square
            square.getSubSquares()[subX][subY].setColor(XWONCOLOR);
        } else if(square.getState() == 'O') {
            //O has won this square
            square.getSubSquares()[subX][subY].setColor(OWONCOLOR);
            //this is NOT the active square
        }else{
            //No one has won this square, it's just inactive.
            square.getSubSquares()[subX][subY].setColor(INACTIVECOLOR);
        }
    }
}
