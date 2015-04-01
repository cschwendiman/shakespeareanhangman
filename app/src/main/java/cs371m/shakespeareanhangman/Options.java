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
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Button;
import android.content.Context;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.widget.Toast;

public class Options extends Activity {


    private boolean soundToggle;
    private int difficulty;

    private SharedPreferences mPrefs;

    private String TAG = "Options Menu Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options2);

        //create shared preferences object
        mPrefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);

        soundToggle = mPrefs.getBoolean("soundToggle",false);
        difficulty = mPrefs.getInt("difficulty",0);
        Log.d(TAG, "Here in onCreate the difficulty is " + difficulty);

    }


    protected void onStop()
    {
        Log.d(TAG, "leaving options menu");

        super.onStop();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean("soundToggle", soundToggle);
        ed.putInt("difficulty", difficulty);

        ed.apply();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
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
        Intent intent;
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


                break;    // this case


                /*
                Button b = (Button) findViewById(R.id.diff_button);

                switch (difficulty)
                {
                    case 0:
                        b.setText("1");
                        difficulty = 1;
                        break;
                    case 1:
                        b.setText("2");
                        difficulty = 2;
                        break;
                    case 2:
                        b.setText("0");
                        difficulty = 0;
                        break;
                }

                System.out.println("difficulty is set to " + difficulty);

                Log.d(TAG,"difficulty is set to " + difficulty);
                break;
                //*/
        }
    }
}
