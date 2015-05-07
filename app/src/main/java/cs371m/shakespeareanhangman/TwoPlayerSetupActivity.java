package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class TwoPlayerSetupActivity extends Activity {
    private static final String TAG = "2P Setup Activity";

    private SharedPreferences prefs;
    private int playerOneDifficulty;
    private int playerTwoDifficulty;

    private List<Profile> profiles;
    private int playerOneProfileIndex;
    private int playerTwoProfileIndex;

    private boolean playerOneSet = false;
    private boolean playerTwoSet = false;

    final CharSequence[] difficultyLevels = {
            "Easy",
            "Medium",
            "Hard"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_setup);

        // Get the player1 and player2 difficulty preferences from the previous game, if any
        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        playerOneDifficulty = prefs.getInt("difficultyPlayer1", 0);
        playerTwoDifficulty = prefs.getInt("difficultyPlayer2", 0);

        // Start with the previous player1 difficulty filled in as the default
        Button p1db = (Button) findViewById(R.id.player_one_difficulty);
        p1db.setText(difficultyLevels[playerOneDifficulty]);

        // Start with the previous player2 difficulty filled in as the default
        Button p2db = (Button) findViewById(R.id.player_two_difficulty);
        p2db.setText(difficultyLevels[playerTwoDifficulty]);

        DBHelper database = DBHelper.getInstance(this);
        profiles = database.getAllProfiles();
        long playerOneProfileId = prefs.getLong("playerId1", prefs.getLong("currentPlayerId", 0));
        long playerTwoProfileId = prefs.getLong("playerId2", 0);
        int i = 0;
        for (Profile p : profiles) {
            if (p.getId() == playerOneProfileId) {
                playerOneProfileIndex = i;
                byte[] byteArray = p.getImage();
                if (byteArray.length > 0) {
                    ImageView iv = (ImageView) findViewById(R.id.player_one_image);
                    Bitmap profileImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    iv.setImageBitmap(profileImage);
                }
                TextView tv = (TextView) findViewById(R.id.player_one_name);
                tv.setText("Profile: " + p.getName());
                playerOneSet = true;
            }
            if (p.getId() == playerTwoProfileId) {
                playerTwoProfileIndex = i;
                byte[] byteArray = p.getImage();
                if (byteArray.length > 0) {
                    ImageView iv = (ImageView) findViewById(R.id.player_two_image);
                    Bitmap profileImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    iv.setImageBitmap(profileImage);
                }
                TextView tv = (TextView) findViewById(R.id.player_two_name);
                tv.setText("Profile: " + p.getName());
                playerTwoSet = true;
            }
            i++;
        }

        if (playerTwoProfileId == 0) {
            if (playerOneProfileIndex == 0 && profiles.size() > 1) {
                playerTwoProfileIndex = 1;
            } else {
                playerTwoProfileIndex = 0;
            }
            Profile p = profiles.get(playerTwoProfileIndex);
            byte[] byteArray = p.getImage();
            if (byteArray.length > 0) {
                ImageView iv = (ImageView) findViewById(R.id.player_two_image);
                Bitmap profileImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                iv.setImageBitmap(profileImage);
            }
            TextView tv = (TextView) findViewById(R.id.player_two_name);
            tv.setText("Profile: " + p.getName());
        }
    }

    // Handle button presses from the user
    public void buttonClick(View view) {
        Intent intent;
        switch (view.getId()) {
            // User wishes to select the difficulty level for player1 or player2
            case R.id.player_one_difficulty:
                showDialog(0);
                break;
            case R.id.player_two_difficulty:
                showDialog(1);
                break;
            // User wishes to select a profile for player1 or player2
            case R.id.choose_player_one:
                showDialog(2);
                break;
            case R.id.choose_player_two:
                showDialog(3);
                break;

            // User is happy with the selections and wishes to start the tournament
            case R.id.start_game_button:
                Log.d(TAG, "Start game button pressed");

                if (!playerOneSet || !playerTwoSet) {
                    Toast.makeText(getApplicationContext(), "You must select a profile for both players before beginning.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Profile player1 = profiles.get(playerOneProfileIndex);
                Profile player2 = profiles.get(playerTwoProfileIndex);
                int difficultyPlayer1 = playerOneDifficulty;

                Log.d(TAG, "player 1 difficulty set to " + difficultyPlayer1);

                int difficultyPlayer2 = playerTwoDifficulty;

                Log.d(TAG, "player 2 difficulty set to " + difficultyPlayer2);

                /* Save the game setup info in shared preferences */
                SharedPreferences.Editor ed = prefs.edit();
                ed.putLong("playerId1", player1.getId());
                ed.putLong("playerId2", player2.getId());
                ed.putString("playerName1", player1.getName());
                ed.putString("playerName2", player2.getName());
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
                // Call finish to remove activity from stack
                finish();
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }

    // Dialog to choose a difficulty level for either player1 or player2
    protected Dialog difficultyHelper(final int viewid, final boolean playerOne) {
        // Set up the dialog with the three difficulty level options
        Log.d(TAG, "Difficulty Open");
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Difficulty Select");

        // Handle choice selection
        final int selected = playerOne ? playerOneDifficulty : playerTwoDifficulty;
        builder.setSingleChoiceItems(difficultyLevels, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                        // Update the chosen difficulty to what was selected
                        if (playerOne) {
                            playerOneDifficulty = item;
                        } else {
                            playerTwoDifficulty = item;
                        }
                        // Update the button text to what was selected
                        Button b = (Button) findViewById(viewid);
                        b.setText(difficultyLevels[item]);
                        Toast.makeText(getApplicationContext(), difficultyLevels[item],
                                Toast.LENGTH_SHORT).show();
                    }
                });
        dialog = builder.create();
        return dialog;
    }

    // Dialog to choose a profile for player1 or player2
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
                        // Set player profile to the profile selected
                        Profile p = profiles.get(item);
                        if (playerOne) {
                            playerOneProfileIndex = item;
                            byte[] byteArray = p.getImage();
                            ImageView iv = (ImageView) findViewById(R.id.player_one_image);
                            if (byteArray.length > 0) {
                                Bitmap profileImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                iv.setImageBitmap(profileImage);
                            }
                            else {
                                iv.setImageDrawable(getResources().getDrawable(R.drawable.defaultprofileimage));
                            }
                            TextView tv = (TextView) findViewById(R.id.player_one_name);
                            tv.setText("Profile: " + p.getName());
                            playerOneSet = true;
                        } else {
                            playerTwoProfileIndex = item;
                            byte[] byteArray = p.getImage();
                            ImageView iv = (ImageView) findViewById(R.id.player_two_image);
                            if (byteArray.length > 0) {
                                Bitmap profileImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                iv.setImageBitmap(profileImage);
                            }
                            else {
                                iv.setImageDrawable(getResources().getDrawable(R.drawable.defaultprofileimage));
                            }
                            TextView tv = (TextView) findViewById(R.id.player_two_name);
                            tv.setText("Profile: " + p.getName());
                            playerTwoSet = true;
                        }
                    }
                });

        dialog = builder.create();
        return dialog;
    }

    // Show dialogs to select the difficulty level or profile for a player
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

    private CharSequence[] getNamesFromList(List<Profile> profiles)
    {
        CharSequence[] names = new CharSequence[profiles.size()];
        Profile p;
        for(int i = 0; i < names.length; i++) {
            p = profiles.get(i);
            names[i] = p.getName();
        }
        return names;
    }
}
