package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class TwoPlayerResultsActivity extends Activity {
    private TextView resultView;
    private TextView phraseView;
    private TextView playView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_results);

        SharedPreferences prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        boolean soundOn = prefs.getBoolean("soundToggle",false);

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
        }
        else {
            resultView.setText(R.string.result_loss);
            if (soundOn) {
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.crickets);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        }

        phraseView = (TextView) findViewById(R.id.phrase);
        phraseView.setText(extras.getString("phrase"));

        playView = (TextView) findViewById(R.id.play);
        playView.setText(extras.getString("play"));
    }

    public void buttonPress(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.continue_button:
                intent = new Intent(this, TwoPlayerGameActivity.class);
                startActivity(intent);
                break;
            /* In future we want to go to the final results activity if the tournament is over
            case R.id.main_menu_result_button:
                intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
                break;*/
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}