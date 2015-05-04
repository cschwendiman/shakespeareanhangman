package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class TwoPlayerSetupActivity extends Activity {
    private static final String TAG = "Two player setup activity";

    private SharedPreferences prefs;
    private int playerOneDifficulty;
    private int playerTwoDifficulty;

    private List<Profile> profiles;
    private int playerOneProfileIndex;
    private int playerTwoProfileIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_setup);

        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        playerOneDifficulty = prefs.getInt("difficultyPlayer1", 0);
        playerTwoDifficulty = prefs.getInt("difficultyPlayer2", 0);

        Button p1db = (Button) findViewById(R.id.player_one_difficulty);
        switch (playerOneDifficulty) {
            case 0:
                p1db.setText("Easy");
                break;
            case 1:
                p1db.setText("Medium");
                break;
            case 2:
                p1db.setText("Hard");
                break;
        }

        Button p2db = (Button) findViewById(R.id.player_two_difficulty);
        switch (playerOneDifficulty) {
            case 0:
                p2db.setText("Easy");
                break;
            case 1:
                p2db.setText("Medium");
                break;
            case 2:
                p2db.setText("Hard");
                break;
        }

        DBHelper database = new DBHelper(this);
        profiles = database.getAllProfiles();
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
            case R.id.player_one_difficulty:
                showDialog(0);
                break;
            case R.id.player_two_difficulty:
                showDialog(1);
                break;
            case R.id.choose_player_one:
                showDialog(2);
                break;
            case R.id.choose_player_two:
                showDialog(3);
                break;
            case R.id.start_game_button:
                Log.d(TAG, "Start game button pressed");

                String playerName1 = profiles.get(playerOneProfileIndex).getName();
                String playerName2 = profiles.get(playerTwoProfileIndex).getName();
                int difficultyPlayer1 = playerOneDifficulty;

                Log.d(TAG, "player 1 difficulty set");

                int difficultyPlayer2 = playerTwoDifficulty;

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

    protected Dialog difficultyHelper(final int viewid, final boolean playerOne) {
        Log.d(TAG, "Difficulty Open");
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Difficulty Select");
        final CharSequence[] levels = {
                "Easy",
                "Medium",
                "Hard"
        };
        final int selected = playerOne ? playerOneDifficulty : playerTwoDifficulty;
        builder.setSingleChoiceItems(levels, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                        if (playerOne) {
                            playerOneDifficulty = item;
                        } else {
                            playerTwoDifficulty = item;
                        }
                        Button b = (Button) findViewById(viewid);
                        if (item == 0)
                            b.setText("Easy");
                        else if (item == 1)
                            b.setText("Medium");
                        else if (item == 2)
                            b.setText("Hard");
                        else
                            b.setText("Error");
                        Toast.makeText(getApplicationContext(), levels[item],
                                Toast.LENGTH_SHORT).show();
                    }
                });
        dialog = builder.create();
        return dialog;
    }

    protected Dialog choosePlayerHelper(final int viewid, final boolean playerOne) {
        Log.d(TAG, "Change Profile Button selected");
        //inflate menu
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Profile");
        CharSequence[] names = getNamesFromList(profiles);
        final int selected = playerOne ? playerOneProfileIndex : playerTwoProfileIndex;
        builder.setSingleChoiceItems(names, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();   //Close dialog
                        Profile p = profiles.get(item);
                        if (playerOne) {
                            playerOneProfileIndex = item;
                        } else {
                            playerTwoProfileIndex = item;
                        }
                        Button b = (Button) findViewById(viewid);
                        b.setText(p.getName());
                    }
                });

        dialog = builder.create();
        return dialog;
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return difficultyHelper(R.id.player_one_difficulty, true);
            case 1:
                return difficultyHelper(R.id.player_two_difficulty, false);
            case 2:
                return choosePlayerHelper(R.id.choose_player_one, true);
            case 3:
                return choosePlayerHelper(R.id.choose_player_two, false);
        }
        return null;
    }

    private CharSequence[] getNamesFromList(List<Profile> arr)
    {
        CharSequence[] names = new CharSequence[arr.size()];
        Profile p;
        for(int i = 0; i < names.length; i++) {
            p = arr.get(i);
            names[i] = p.getName();
        }
        return names;
    }
}
