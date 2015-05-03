package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainMenuActivity extends Activity {
    private static final String TAG = "Main Menu Activity";
    private MediaPlayer mediaPlayer;
    private boolean soundOn;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        soundOn = prefs.getBoolean("soundToggle",false);
        if (soundOn) {
            mediaPlayer = MediaPlayer.create(this, R.raw.intro);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }

    public void buttonClick(View view) {
        Log.d(TAG, " Button Clicked");
        Intent intent;
        if (soundOn) {
            mediaPlayer.stop();
        }
        switch (view.getId()) {
            case R.id.new_game_button:
                Log.d(TAG, "New Game Button");
                intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            case R.id.two_player_game_button:
                Log.d(TAG, "Two Player Game Button pressed");
                intent = new Intent(this, TwoPlayerGameActivity.class);
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
