package nu.geeks.tictachardmode;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    final int ACTIVECOLOR = Color.argb(255, 155, 228, 102);
    private final String INTENTTAG = "GAMETYPE";


    Button[][] buttons = new Button[9][9];

    TextView playerInfo;

    Board board;

    boolean ai = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras();
        String game = "";
        if(extras != null){
           game = extras.getString(INTENTTAG);
        }
        if(game.equals("Player vs Player")){
            //TODO - game type is player vs player.
            Toast.makeText(getApplicationContext(), "Player vs Player", Toast.LENGTH_LONG).show();
        }else if(game.equals("Player vs AI")){
            //TODO - game type is Player vs AI.
            Toast.makeText(getApplicationContext(), "Player vs AI", Toast.LENGTH_LONG).show();
            ai = true;
        }


        playerInfo = (TextView) findViewById(R.id.playerInfo);
        playerInfo.setText("Player X's turn");

        board = new Board('X');

        initializeButton();


    }

    private void initializeButton() {
        GridLayout hv = (GridLayout) findViewById(R.id.view);
        int x = 0; //this is a stupid way to keep track of the iteration below
        int y = 0;

        for (int i = 0; i < hv.getChildCount(); i++) {

            //Initialize the corresponding button on the board.
            //Pass this mainActivity and the view id to the subsquare.
            buttons[x][y] = board.getMainSquares()[x / 3 ][ y / 3 ]
                    .getSubSquares()[ x % 3][ y % 3 ]
                    .initializeButton(hv.getChildAt(i).getId(), this);

            buttons[x][y].setBackgroundColor(ACTIVECOLOR);
            buttons[x][y].setText(" ");

            //Set onclick listener.
            buttons[x][y].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doMove(v);
                    //If game is against ai, make the AI move as soon as the player has made a move.
                    if(board.getActivePlayer() == 'O' && ai){
                        int[] aiMove = AI.makeMove(board);
                        makeMove(aiMove[0], aiMove[1]);
                    }
                }
            });

            x++;
            if (x == 9) {
                y++;
                x = 0;
            }
        }
    }

    public void doMove(View v){
        String name = getResources().getResourceEntryName(v.getId());


        if (name.length() == 3) {

            int x = Integer.parseInt("" + name.charAt(1));
            int y = Integer.parseInt("" + name.charAt(2));


            makeMove(y, x);


        }
    }


    private void makeMove(int x, int y) {

            //Check if move is in active square.
            if((board.getActiveMainSquare()[0] == x / 3 && board.getActiveMainSquare()[1] == y / 3) || board.getActiveMainSquare()[0] == -1){

                //check that the state of the selected main square is valid
                if(board.getMainSquares()[ x / 3][ y / 3 ].getState() == 'G') {

                    //Check if sub-square is empty
                    if (board.getMainSquares()[x / 3][y / 3].getSubSquares()[x % 3][y % 3].getValue() == ' ') {

                        //Move was valid.
                        //Update the board. Returns the winner if someone won the game. Else it returns 'N'
                        char winner = board.update(x, y);


                        //Update the graphics for the board.
                        board.updateGraphics();



                        //Finally, change the infotext.
                        playerInfo.setText("Player " + board.getActivePlayer() + "'s turn");

                        if(winner != 'N'){
                            Toast.makeText(getApplicationContext(), "" + winner + " has won the game!!", Toast.LENGTH_LONG).show();
                        }

                    }

                }

            }
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
            Toast.makeText(this.getApplication(), "It's " + board.getActivePlayer() + " turn", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
