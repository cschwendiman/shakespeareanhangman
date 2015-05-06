package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ResultsActivity extends Activity {
    private TextView resultView;
    private TextView phraseView;
    private TextView playView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        SharedPreferences prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        boolean soundOn = prefs.getBoolean("soundToggle",false);
        long currentProfileId = prefs.getInt("currentProfileId", 1);

        DBHelper dbHelper = new DBHelper(this);
        Profile profile = dbHelper.getProfile(currentProfileId);

        Bundle extras = getIntent().getExtras();
        resultView = (TextView) findViewById(R.id.result);
        if (extras.getBoolean("status")) {
            resultView.setText(R.string.result_win);
            if (soundOn) {
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cheering);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
            profile.setWins(profile.getWins()+1);
        }
        else {
            resultView.setText(R.string.result_loss);
            if (soundOn) {
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.crickets);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
            profile.setLosses(profile.getLosses()+1);
        }
        dbHelper.updateProfile(profile);

        phraseView = (TextView) findViewById(R.id.phrase);
        phraseView.setText(extras.getString("phrase"));

        playView = (TextView) findViewById(R.id.play);
        playView.setText(extras.getString("play"));
    }

    public void buttonPress(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.new_game_result_button:
                intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                // Call finish to remove activity from stack
                finish();
                break;
            case R.id.main_menu_result_button:
                intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
                // Call finish to remove activity from stack
                finish();
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}
