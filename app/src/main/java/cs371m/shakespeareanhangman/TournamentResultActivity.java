package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class TournamentResultActivity extends Activity {
    private TextView resultView;
    private TextView score1View;
    private TextView score2View;
    private String playerName1;
    private String playerName2;
    private int playerScore1;
    private int playerScore2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_result);

        SharedPreferences prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        boolean soundOn = prefs.getBoolean("soundToggle",false);

        // Play cheering if sound is on
        if (soundOn) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cheering);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }

        DBHelper dbHelper = DBHelper.getInstance(this);

        // Get the players' names and scores
        playerName1 = prefs.getString("playerName1", "Player 1");
        playerName2 = prefs.getString("playerName2", "Player 2");
        playerScore1 = prefs.getInt("playerWins1", 0);
        playerScore2 = prefs.getInt("playerWins2", 0);
        long playerId1 = prefs.getLong("playerId1", 0);
        long playerId2 = prefs.getLong("playerId2", 0);
        Profile p1 = dbHelper.getProfile(playerId1);
        Profile p2 = dbHelper.getProfile(playerId2);

        resultView = (TextView) findViewById(R.id.tournament_result);
        if(playerScore1 > playerScore2) {
            resultView.setText(playerName1 + " Won!");
            p1.setWins(p1.getWins()+1);
            p2.setLosses(p2.getLosses()+1);
        } else if(playerScore2 > playerScore1) {
            resultView.setText(playerName2 + " Won!");
            p1.setLosses(p1.getLosses()+1);
            p2.setWins(p2.getWins()+1);
        } else {
            resultView.setText("You Tied!");
        }
        p1.setGamesPlayed(p1.getGamesPlayed()+1);
        p2.setGamesPlayed(p2.getGamesPlayed()+1);
        dbHelper.updateProfile(p1);
        dbHelper.updateProfile(p2);

        score1View = (TextView) findViewById(R.id.score1);
        score1View.setText(playerName1 + "'s score: " + playerScore1);

        score2View = (TextView) findViewById(R.id.score2);
        score2View.setText(playerName2 + "'s score: " + playerScore2);

    }


    public void buttonPress(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.new_tournament_button:
                intent = new Intent(this, TwoPlayerSetupActivity.class);
                startActivity(intent);
                break;

            case R.id.main_menu_tournament_button:
                intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}
