package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;


public class EditProfileActivity extends Activity {

    private long id;
    private Profile p;
    private EditText e;
    String TAG = "edit";
    DBHelper database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        database = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("profile_id");
        }
        Log.d(TAG, "PASSED ID " + id);
        p = database.getProfile(id);

        e = (EditText) findViewById(R.id.editProfileText);

        String name = p.getName();
        e.setHint(name);
        e.setText(name);

        List<Profile> list = database.getAllProfiles();
        if(list.size() == 1)
        {
            Button btn = (Button) findViewById(R.id.delete_profile);
            btn.setEnabled(false);
        }
    }


    public void buttonClick(View view) throws SQLException {
        Log.d(TAG, " Button Clicked");
        switch (view.getId()) {
            case R.id.cancel_edit_profile_button:
                Log.d(TAG, "cancelling adding new profile");
                finish();
                break;

            case R.id.commit_edit_profile_button:
                Log.d(TAG, "adding new profile");
                String name = e.getText().toString();
                if (name.equals("") || name.equals(null))
                {
                    Toast.makeText(getApplicationContext(), "must enter a name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                saveProfile();
                finish();
                break;
            case R.id.delete_profile:
                database.deleteProfile(database.getProfile(id));
                finish();
                break;
        }
    }

    private void saveProfile() throws SQLException {

        String name = e.getText().toString();
        p.setName(name);
        database.updateProfile(p);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
}
