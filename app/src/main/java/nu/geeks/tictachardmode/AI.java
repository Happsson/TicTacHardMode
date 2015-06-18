package nu.geeks.tictachardmode;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 *
 *
 * Created by hannespa on 15-05-30.
 */

/* new take

A take on a non-brute force attempt. Think more like a human.

Give all squares a value array, based on how attractive it is for the AI and the Player.
Values are in an array, [AI points, player points, AI moves made to win square, player moves made to win square].

    * A square gets 4 in [0] || [1] if winning this is the ONLY way to win the game.
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


    public static int[] makeMove(Board board){

        return null;
    }

}

