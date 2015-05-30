package nu.geeks.tictachardmode;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity{

    final int ACTIVECOLOR = Color.argb(255,155,228,102);
    final int INACTIVECOLOR = Color.argb(255,244,142,102);

    TextView tv;

    Button[][] bs = new Button[9][9];

    char[] activeSquares = new char[9];


    char currentTurn = 'X';
    int activeSquare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Player X's turn");
        activeSquare = -1; //no
        for(int i = 0; i < 9 ; i++){
            activeSquares[i] = 'C'; //C means square is still in game
        }

        GridLayout hv = (GridLayout) findViewById(R.id.view);
        int x = 0; //this is a stupid way to keep track of the iteration below
        int y = 0;
        for(int i = 0; i < hv.getChildCount(); i++){
            bs[x][y] = (Button) findViewById( hv.getChildAt(i).getId() );
            bs[x][y].setBackgroundColor(ACTIVECOLOR);
            bs[x][y].setText(" ");
            bs[x][y].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(clickOn(v)) changeTurn();

                }
            });
            x++;
            if(x == 9){
                y++;
                x = 0;
            }
        }
    }

    private void changeTurn() {
        if(currentTurn == 'X') currentTurn = 'O';
        else if(currentTurn == 'O') currentTurn = 'X';

        tv.setText("Player "+ currentTurn + "'s turn");
    }

    private boolean clickOn(View v){

        String name = getResources().getResourceEntryName(v.getId());


        if(name.length() == 3) {

                int y = Integer.parseInt("" + name.charAt(1));
                int x = Integer.parseInt("" + name.charAt(2));
                int thisSquare = getCurrentSquare(x, y);

                if (activeSquares[thisSquare] == 'C') {


                    if (thisSquare == activeSquare || activeSquare == -1) {

                        //Set the last square to inactive before changing this value.
                        setSquareColor(activeSquare, INACTIVECOLOR);

                        //Get active square.
                        activeSquare = getCorrespondingWorldSquare(x, y);

                        //bs[x][y].setBackgroundColor(INACTIVECOLOR);
                        bs[x][y].setText("" + currentTurn);


                        char wonChar = isSquareWon(thisSquare);
                        if (wonChar != 'C') {
                            Toast.makeText(this.getApplicationContext(), "" + wonChar + " won square " + thisSquare, Toast.LENGTH_LONG).show();
                            activeSquares[thisSquare] = wonChar; //Square no longer in play
                        }

                        char c = checkIfGameIsWon();
                        if (c == 'X' || c == 'O') {
                            Toast.makeText(this.getApplicationContext(), c + " wins!", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        //activeSquare = -1;
                    }

                    if( activeSquares[ activeSquare ] != 'C'){
                       activeSquare = -1;
                    }

                    if (activeSquare == -1) {
                        for (int i = 0; i < 9; i++) {
                            if (activeSquares[i] == 'C') setSquareColor(i, ACTIVECOLOR);
                        }
                    } else {
                        setSquareColor(activeSquare, ACTIVECOLOR);
                    }
                    return true;
                }
        }
        return false;
    }

    private char checkIfGameIsWon() {
        for(int i = 0; i < 2; i++) {
            char c = 'a';
            if( i == 0) c = 'X';
            if( i == 1) c = 'O';

            //row
            if (activeSquares[0] == c && activeSquares[1] == c && activeSquares[2] == c)
                return c;
            if (activeSquares[3] == c && activeSquares[4] == c && activeSquares[5] == c)
                return c;
            if (activeSquares[6] == c && activeSquares[7] == c && activeSquares[8] == c)
                return c;
            //column
            if (activeSquares[0] == c && activeSquares[3] == c && activeSquares[6] == c)
                return c;
            if (activeSquares[1] == c && activeSquares[4] == c && activeSquares[7] == c)
                return c;
            if (activeSquares[2] == c && activeSquares[5] == c && activeSquares[8] == c)
                return c;
            //diagonal
            if (activeSquares[0] == c && activeSquares[4] == c && activeSquares[8] == c)
                return c;
            if (activeSquares[2] == c && activeSquares[4] == c && activeSquares[6] == c)
                return c;


        }
        return 'c';
    }

    private int getCorrespondingWorldSquare(int x, int y){
        if(x % 3 == 0 && y % 3 == 0) return 0;
        if(x % 3 == 1 && y % 3 == 0) return 1;
        if(x % 3 == 2 && y % 3 == 0) return 2;
        if(x % 3 == 0 && y % 3 == 1) return 3;
        if(x % 3 == 1 && y % 3 == 1) return 4;
        if(x % 3 == 2 && y % 3 == 1) return 5;
        if(x % 3 == 0 && y % 3 == 2) return 6;
        if(x % 3 == 1 && y % 3 == 2) return 7;
        if(x % 3 == 2 && y % 3 == 2) return 8;
        else return -1;
    }

    private void setSquareColor(int square, int color){

        if(square != -1) {
            int[] xy = getsquareStartingPosition(square);
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    bs[xy[0] + x][xy[1] + y].setBackgroundColor(color);
                }
            }

        }else{
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9 ; j++){
                    bs[i][j].setBackgroundColor(color);
                }

            }
        }
    }

    private int getCurrentSquare(int x, int y){
        if(x < 3){
            if(y < 3) return 0;
            else if(y < 6) return 3;
            else return 6;
        }
        else if( x < 6){
            if(y < 3) return 1;
            else if(y < 6) return 4;
            else return 7;
        }
        else{
            if(y < 3) return 2;
            else if( y < 6) return 5;
            else return 8;
        }
    }

    private int[] getsquareStartingPosition(int square){

        int xy[] = new int[2];
        switch(square){
            case 0:
                xy[0] = 0;
                xy[1] = 0;
                break;
            case 1:
                xy[0] = 3;
                xy[1] = 0;
                break;
            case 2:
                xy[0] = 6;
                xy[1] = 0;
                break;
            case 3:
                xy[0] = 0;
                xy[1] = 3;
                break;
            case 4:
                xy[0] = 3;
                xy[1] = 3;
                break;
            case 5:
                xy[0] = 6;
                xy[1] = 3;
                break;
            case 6:
                xy[0] = 0;
                xy[1] = 6;
                break;
            case 7:
                xy[0] = 3;
                xy[1] = 6;
                break;
            case 8:
                xy[0] = 6;
                xy[1] = 6;
                break;

        }

        return xy;

    }

    private char isSquareWon(int square){
        char c = 'C';
        int[] xy = getsquareStartingPosition(square);
        for(int i = 0; i < 3; i++) {
            if (
                    bs[ xy[ 0 ] ] [ xy[ 1 ] + i].getText().toString().charAt(0) == 'X' &&
                            bs[ xy[ 0 ] + 1 ] [ xy[ 1 ] + i].getText().toString().charAt(0) == 'X' &&
                            bs[ xy[ 0 ] + 2 ] [ xy[ 1 ] + i].getText().toString().charAt(0) == 'X')

            {
                c = 'X';
            }

            if (
                    bs[ xy[ 0 ] + i] [ xy[ 1 ]].getText().toString().charAt(0) == 'X' &&
                            bs[ xy[ 0 ] + i ] [ xy[ 1 ] + 1].getText().toString().charAt(0) == 'X' &&
                            bs[ xy[ 0 ] + i ] [ xy[ 1 ] + 2].getText().toString().charAt(0) == 'X')

            {
                c = 'X';
            }

            if (
                    bs[ xy[ 0 ] ] [ xy[ 1 ] + i].getText().toString().charAt(0) == 'O' &&
                            bs[ xy[ 0 ] + 1 ] [ xy[ 1 ] + i].getText().toString().charAt(0) == 'O' &&
                            bs[ xy[ 0 ] + 2 ] [ xy[ 1 ] + i].getText().toString().charAt(0) == 'O')

            {
                c = 'O';
            }

            if (
                    bs[ xy[ 0 ] + i] [ xy[ 1 ]].getText().toString().charAt(0) == 'O' &&
                            bs[ xy[ 0 ] + i ] [ xy[ 1 ] + 1].getText().toString().charAt(0) == 'O' &&
                            bs[ xy[ 0 ] + i ] [ xy[ 1 ] + 2].getText().toString().charAt(0) == 'O')

            {
                c = 'O';
            }


        }

        if (
                bs[ xy[ 0 ]] [ xy[ 1 ]].getText().toString().charAt(0) == 'O' &&
                        bs[ xy[ 0 ]+1] [ xy[ 1 ] + 1].getText().toString().charAt(0) == 'O' &&
                        bs[ xy[ 0 ]+2] [ xy[ 1 ] + 2].getText().toString().charAt(0) == 'O')

        {
            c = 'O';
        }

        if (
                bs[ xy[ 0 ] +2 ] [ xy[ 1 ]].getText().toString().charAt(0) == 'O' &&
                        bs[ xy[ 0 ]+1] [ xy[ 1 ] + 1].getText().toString().charAt(0) == 'O' &&
                        bs[ xy[ 0 ]] [ xy[ 1 ] + 2].getText().toString().charAt(0) == 'O')

        {
            c = 'O';
        }

        if (
                bs[ xy[ 0 ]] [ xy[ 1 ]].getText().toString().charAt(0) == 'X' &&
                        bs[ xy[ 0 ]+1] [ xy[ 1 ] + 1].getText().toString().charAt(0) == 'X' &&
                        bs[ xy[ 0 ]+2] [ xy[ 1 ] + 2].getText().toString().charAt(0) == 'X')

        {
            c = 'X';
        }

        if (
                bs[ xy[ 0 ] +2 ] [ xy[ 1 ]].getText().toString().charAt(0) == 'X' &&
                        bs[ xy[ 0 ]+1] [ xy[ 1 ] + 1].getText().toString().charAt(0) == 'X' &&
                        bs[ xy[ 0 ]] [ xy[ 1 ] + 2].getText().toString().charAt(0) == 'X')

        {
            c = 'X';
        }

        return c;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this.getApplication(), "It's " + currentTurn + " turn", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
