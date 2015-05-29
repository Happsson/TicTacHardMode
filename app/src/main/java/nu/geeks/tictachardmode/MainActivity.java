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
import android.widget.Toast;


public class MainActivity extends Activity{

    Button[][] bs = new Button[9][9];





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout hv = (GridLayout) findViewById(R.id.view);
        int x = 0; //this is a stupid way to keep track of the iteration below
        int y = 0;
        for(int i = 0; i < hv.getChildCount(); i++){
            bs[x][y] = (Button) findViewById( hv.getChildAt(i).getId() );
            bs[x][y].setBackgroundColor(Color.GREEN);
            bs[x][y].setText(" ");
            bs[x][y].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    clickOn(v);
                }
            });
            x++;
            if(x == 9){
                y++;
                x = 0;
            }


        }




    }

    private void clickOn(View v){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                if(v.getId() == bs[x][y].getId()){
                    bs[x][y].setBackgroundColor(Color.RED);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
