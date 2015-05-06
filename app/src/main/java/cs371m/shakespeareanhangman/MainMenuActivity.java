package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainMenuActivity extends Activity {
    private static final String TAG = "Main Menu Activity";
    private MediaPlayer mediaPlayer;
    private boolean soundOn;
    private SharedPreferences prefs;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dbHelper = new DBHelper(this);
        if (null == dbHelper.getProfile(1)) {
            dbHelper.createProfile("Default", new byte[0]);
        }

        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
    }

     @Override
    protected void onResume() {
         super.onResume();

         soundOn = prefs.getBoolean("soundToggle",false);
         if (soundOn) {
             mediaPlayer = MediaPlayer.create(this, R.raw.intro);
             mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             mediaPlayer.setLooping(false);
             mediaPlayer.start();
         }
     }

    @Override
    protected void onPause() {
        super.onPause();

        if (soundOn) {
            mediaPlayer.stop();
        }
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
                Log.d(TAG, "Two Player Game Button pressed");
                intent = new Intent(this, TwoPlayerSetupActivity.class);
                startActivity(intent);
                break;
            case R.id.player_profiles_button:
                intent = new Intent(this, PlayerProfilesActivity.class);
                startActivity(intent);
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
