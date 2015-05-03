package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class PlayerProfilesActivity extends Activity {

    private SharedPreferences prefs;
    private String TAG = "Player Profiles Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profiles);

        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_profiles, menu);
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
            case R.id.addNewProfile:
                Log.d(TAG, "Adding new Profile");
                intent = new Intent(this, AddNewProfileActivity.class);
                startActivity(intent);
                break;

            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}
