package nu.geeks.tictachardmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class LaunchActivity extends Activity {

    private final String INTENTTAG = "GAMETYPE";

    Button pvsai, pvsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

    pvsai = (Button) findViewById(R.id.btnAI);
    pvsp = (Button) findViewById(R.id.btnpvsp);

    pvsai.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startGame("Player vs AI");
        }
    });

    pvsp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startGame("Player vs Player");
        }
    });

    }

    private void startGame(String extra){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(INTENTTAG, extra);
        startActivity(intent);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
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
