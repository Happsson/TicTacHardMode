package nu.geeks.tictachardmode;

import android.view.View;
import android.widget.Button;

/**
 * Created by hannespa on 15-06-01.
 */
public class SubSquare {

    private Button thisButton;


    //Can be 'X', 'O' or ' '
    private char value;

    public SubSquare() {
        value = ' ';
    }

    public char getValue() {
        return value;
    }

    public Button initializeButton(int id, final MainActivity mainActivity){
        thisButton = ( Button ) mainActivity.findViewById(id);
        return thisButton;
    }

    public void setColor(int color){
        thisButton.setBackgroundColor(color);
    }

    public void setValue(char value) {
        this.value = value;
    }


    public void setButtonText(char activePlayer) {
        thisButton.setText("" + activePlayer);

    }
}
