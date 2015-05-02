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
    private TextView roundView;
    private TextView score1View;
    private TextView score2View;

    private int round;

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

        round = prefs.getInt("roundNumber", 1);
        roundView = (TextView) findViewById(R.id.round_info);
        roundView.setText("Round " + round + " of 6");

        score1View = (TextView) findViewById(R.id.standings1);
        score1View.setText(prefs.getString("playerName1", "Player 1") + "'s score: " + prefs.getInt("playerWins1", 0));

        score2View = (TextView) findViewById(R.id.standings2);
        score2View.setText(prefs.getString("playerName2", "Player 2") + "'s score: " + prefs.getInt("playerWins2", 0));

        // Update round number
        round++;
        SharedPreferences.Editor ed = prefs.edit();
        ed.putInt("roundNumber", round);
        ed.apply();
    }

    public void buttonPress(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.continue_button:
                // If the tournament isn't over yet, play another game
                if(round <= 6) {
                    intent = new Intent(this, TwoPlayerGameActivity.class);
                }
                // Otherwise show the final results
                else {
                    intent = new Intent(this, MainMenuActivity.class);
                }
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