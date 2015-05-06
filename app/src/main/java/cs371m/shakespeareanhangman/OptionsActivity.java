package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class OptionsActivity extends Activity {
    private String TAG = "Options Activity";
    private SharedPreferences prefs;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_CHOOSE_PROFILE_ID = 1;

    final CharSequence[] difficultyLevels = {
            "Easy",
            "Medium",
            "Hard"
    };

    private boolean soundToggle;
    private int difficulty;
    int selectedProfileIndex;
    Profile selectedProfile;
    List<Profile> profiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);

        DBHelper database = new DBHelper(this);
        profiles = database.getAllProfiles();
        long currentPlayerId = prefs.getLong("currentPlayerId", 1);
        selectedProfile = database.getProfile(currentPlayerId);

        soundToggle = prefs.getBoolean("soundToggle",false);
        ToggleButton soundToggleButton = (ToggleButton) findViewById(R.id.toggle_sound_button);
        if(soundToggle) //then set the switch to show on
            soundToggleButton.toggle();

        difficulty = prefs.getInt("difficulty",0);
        Button difficultyButton = (Button) findViewById(R.id.diff_button);
        difficultyButton.setText("Difficulty: " + difficultyLevels[difficulty]);
    }

    protected void onPause()
    {
        Log.d(TAG, "leaving options menu");
        super.onPause();
        SharedPreferences.Editor ed = prefs.edit();

        if (prefs.getInt("difficulty", 0) != difficulty || prefs.getLong("currentPlayerId", 1) != selectedProfile.getId()) {
            // Reset phrase queue on difficulty or user change
            ed.putString("phraseQueue", "");
        }
        ed.putBoolean("soundToggle", soundToggle);
        ed.putInt("difficulty", difficulty);
        ed.putLong("currentPlayerId", selectedProfile.getId());
        ed.apply();
    }

    protected Dialog onCreateDialog(int id) {
        Log.d(TAG, "In onCreateDialog");
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case DIALOG_DIFFICULTY_ID:
                Log.d(TAG, "Difficulty Button selected");
                builder.setTitle("Difficulty Select");
                builder.setSingleChoiceItems(difficultyLevels, difficulty,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   //Close dialog
                                difficulty = item;
                                Log.d(TAG, "Difficulty: " + difficultyLevels[difficulty]);
                                Button b = (Button) findViewById(R.id.diff_button);
                                b.setText("Difficulty: " + difficultyLevels[difficulty]);
                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), difficultyLevels[difficulty],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_CHOOSE_PROFILE_ID:
                Log.d(TAG, "Change Profile Button selected");
                builder.setTitle("Choose Profile");

                CharSequence[] names = getNamesFromList(profiles);
                builder.setSingleChoiceItems(names, selectedProfileIndex,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   //Close dialog
                                selectedProfileIndex = item;
                                selectedProfile = profiles.get(item);
                            }
                        });
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    private CharSequence[] getNamesFromList(List<Profile> profiles)
    {
        CharSequence[] names = new CharSequence[profiles.size()];
        Profile p;
        for(int i = 0; i < names.length; i++)
        {
            p = profiles.get(i);
            names[i] = p.getName();
        }
        return names;
    }

    public void buttonClick(View view) {
        Log.d(TAG, " Button Clicked");
        switch (view.getId()) {
            case R.id.changeProfile:
                showDialog(DIALOG_CHOOSE_PROFILE_ID);
                break;
            case R.id.toggle_sound_button:
                if(soundToggle) {
                    soundToggle = false;
                    //give some indication that sound is off now
                    Log.d(TAG,"sound is off");
                    Toast.makeText(getApplicationContext(), "Sound turned off",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    soundToggle = true;
                    //give some indication sound is on
                    Log.d(TAG,"sound is on");
                    Toast.makeText(getApplicationContext(), "Sound turned on",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.diff_button:
                showDialog(DIALOG_DIFFICULTY_ID);
                break;
        }
    }
}
