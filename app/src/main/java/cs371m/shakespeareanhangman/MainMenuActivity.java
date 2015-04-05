package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainMenuActivity extends Activity {
    private static final String TAG = "Main Menu Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        boolean soundOn = prefs.getBoolean("soundToggle",false);
        if (soundOn) {
            SoundPool sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            sounds.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
            {
                @Override
                public void onLoadComplete(SoundPool soundPool, int soundID, int status) {
                    soundPool.play(soundID, 1, 1, 1, 0, 1);
                }
            });
            sounds.load(this, R.raw.welcome, 1);
        }

        Button twoPlayerGameButton = (Button) findViewById(R.id.two_player_game_button);
        twoPlayerGameButton.setEnabled(false);
        Button playerProfilesButton = (Button) findViewById(R.id.player_profiles_button);
        playerProfilesButton.setEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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

    public void buttonClick(View view) {
        Log.d(TAG, " Button Clicked");
        Intent intent;
        switch (view.getId()) {
            case R.id.new_game_button:
                Log.d(TAG, "New Game Button");
                intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            case R.id.two_player_game_button:
                //intent = new Intent(this, GameActivity.class);
                //startActivity(intent);
                break;
            case R.id.player_profiles_button:
                //intent = new Intent(this, GameActivity.class);
                //startActivity(intent);
                break;
            case R.id.options_button:
                Log.d(TAG, "Options Button pressed");
                intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                break;
            case R.id.about_button:
                Log.d(TAG, "About Button was pressed");
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}
