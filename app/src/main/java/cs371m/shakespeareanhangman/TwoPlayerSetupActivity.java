package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class TwoPlayerSetupActivity extends Activity {
    private static final String TAG = "Two player setup activity";

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_setup);

        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_two_player_setup, menu);
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

    public void buttonPress(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.start_game_button:
                Log.d(TAG, "Start game button pressed");

                /* Change this part to actually get the user's selections instead of dummy values*/
                String playerName1 = "Player 1";
                String playerName2 = "Player 2";
                int difficultyPlayer1 = prefs.getInt("difficulty", 0);

                Log.d(TAG, "player 1 difficulty set");

                int difficultyPlayer2 = prefs.getInt("difficulty", 0);

                Log.d(TAG, "player 2 difficulty set");

                /* Save the game setup info in shared preferences */
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("playerName1", playerName1);
                ed.putString("playerName2", playerName2);
                ed.putInt("difficultyPlayer1", difficultyPlayer1);
                ed.putInt("difficultyPlayer2", difficultyPlayer2);

                Log.d(TAG, "setup game info saved in shared prefs");

                // Initialize round number and player wins
                ed.putInt("roundNumber", 1);
                ed.putInt("playerWins1", 0);
                ed.putInt("playerWins2", 0);
                ed.apply();

                intent = new Intent(this, TwoPlayerGameActivity.class);
                startActivity(intent);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}
