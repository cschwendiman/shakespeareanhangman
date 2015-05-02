package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.io.IOException;
import java.util.ArrayList;


public class AddNewProfileActivity extends Activity {

    String TAG = "Add New Profile Activity";
    EditText editText;
    Profile newProfile;
    private SharedPreferences prefs;
    ArrayList<Profile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newProfile = new Profile();

        if (null == profiles)
            profiles = new ArrayList<Profile>();





        if (profiles == null)
            Log.d(TAG, "Profile array returned null in onCreate");


        printProfiles();

        setContentView(R.layout.activity_add_new_profile);
        editText = (EditText) findViewById(R.id.profile_title);
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //set profile title
                    Log.d(TAG, v.getText().toString());
                    newProfile.setProfName(v.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
    }


    public void printProfiles()
    {
        Log.d(TAG, "printing Profile names" + profiles.size());
        for( int i = 0; i < profiles.size(); i++)
        {
            Log.d(TAG, profiles.get(i).getProfName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_profile, menu);
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

    public void buttonClick(View view) {
        Log.d(TAG, " Button Clicked");
        Intent intent;
        switch (view.getId()) {
            case R.id.cancel_add_new_profile_button:
                Log.d(TAG, "cancelling adding new profile");
                intent = new Intent(this, PlayerProfilesActivity.class);
                startActivity(intent);
                break;

            case R.id.commit_add_new_profile_button:
                Log.d(TAG, "adding new profile");
                profiles.add(newProfile);
                printProfiles();
                intent = new Intent(this, PlayerProfilesActivity.class);
                startActivity(intent);
                break;

        }
    }








}
