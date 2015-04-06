package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class OptionsActivity extends Activity {
    private boolean soundToggle;
    private int difficulty;
    private SharedPreferences prefs;
    private String TAG = "Options Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //create shared preferences object
        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);

        soundToggle = prefs.getBoolean("soundToggle",false);
        ToggleButton sw = (ToggleButton) findViewById(R.id.toggle_sound_button);
        if(soundToggle)//then set the switch to show on
            sw.toggle();

        Button b = (Button) findViewById(R.id.changeProfile);
        b.setEnabled(false);

        difficulty = prefs.getInt("difficulty",0);
        Button db = (Button) findViewById(R.id.diff_button);
        if(difficulty == 0)
            db.setText("Difficulty: Easy");
        else if(difficulty == 1)
            db.setText("Difficulty: Medium");
        else if(difficulty == 2)
            db.setText("Difficulty: Hard");
        else
            db.setText("Error");
        Log.d(TAG, "Here in onCreate the difficulty is " + difficulty + "and the sound is " + soundToggle);

    }

    protected void onStop()
    {
        Log.d(TAG, "leaving options menu");
        super.onStop();
        SharedPreferences.Editor ed = prefs.edit();

        if (prefs.getInt("difficulty", 0) != difficulty) {
            // Reset phrase queue on difficulty change
            ed.putString("phraseQueue", "");
        }
        ed.putBoolean("soundToggle", soundToggle);
        ed.putInt("difficulty", difficulty);
        ed.apply();
    }

    protected Dialog onCreateDialog(int id) {
        Log.d(TAG,"diff button selected 1");
        //inflate menu
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Difficulty Select");
        final CharSequence[] levels = {
                "Easy",
                "Medium",
                "Hard"
        };
        Log.d(TAG,"diff button selected 2");


        final int selected = difficulty;

        Log.d(TAG, "selected difficulty value: " + selected);

        builder.setSingleChoiceItems(levels, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();   // Close dialog

                        //mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.values()[item]);
                        difficulty = item;
                        Log.d(TAG, "Difficulty level: " + difficulty);
                        Button b = (Button) findViewById(R.id.diff_button);
                        if(difficulty == 0)
                            b.setText("Difficulty: Easy");
                        else if(difficulty == 1)
                            b.setText("Difficulty: Medium");
                        else if(difficulty == 2)
                            b.setText("Difficulty: Hard");
                        else
                            b.setText("Error");

                        // Display the selected difficulty level
                        Toast.makeText(getApplicationContext(), levels[item],
                                Toast.LENGTH_SHORT).show();
                    }
                });
        Log.d(TAG,"diff button selected 3");

        dialog = builder.create();
        Log.d(TAG,"diff button selected 4");
        return dialog;
    }

    public void buttonClick(View view) {
        Log.d(TAG, " Button Clicked");
        switch (view.getId()) {
            case R.id.toggle_sound_button:
                //change the volume set in the preferences
                if(soundToggle) {
                    soundToggle = false;
                    //give some indication that sound is off now
                    Log.d(TAG,"sound is off: " + soundToggle);
                }
                else
                {
                    soundToggle = true;
                    //give some indication sound is on
                    Log.d(TAG,"sound is on:" + soundToggle);
                    //do we want to make the sound go on immediately or wait to activate it once the game is back on?
                    //is there constant background noise or is sound only a thing in a game?
                }
                break;
            case R.id.diff_button:
                showDialog(0);
                break;
        }
    }
}
