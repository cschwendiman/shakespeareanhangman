package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class AddNewProfileActivity extends Activity {

    String TAG = "Add New Profile Activity";
    EditText editText;
    Profile newProfile;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_profile);
        newProfile = new Profile();
        editText = (EditText) findViewById(R.id.profile_title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_profile, menu);
        return true;
    }

    public void buttonClick(View view) throws SQLException {
        Log.d(TAG, " Button Clicked");
        switch (view.getId()) {
            case R.id.cancel_add_new_profile_button:
                Log.d(TAG, "cancelling adding new profile");
                finish();
                break;

            case R.id.commit_add_new_profile_button:
                Log.d(TAG, "adding new profile");
                String name = editText.getText().toString();
                if (name.equals("") || name.equals(null))
                {
                    Toast.makeText(getApplicationContext(), "must enter a name",
                    Toast.LENGTH_SHORT).show();
                    return;
                }
                saveProfile();
                finish();
                break;
        }
    }

    private void saveProfile() throws SQLException {

        String name = editText.getText().toString();
        newProfile.setName(name);
        DBHelper database = new DBHelper(this);
        database.createProfile(newProfile.getName(), new byte[0]);
    }



}
