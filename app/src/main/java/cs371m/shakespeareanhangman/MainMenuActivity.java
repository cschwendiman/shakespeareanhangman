package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainMenuActivity extends Activity {
    private String TAG = "Main Menu Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
            case R.id.new_game_button:
                //intent = new Intent(this, GameActivity.class);
                //startActivity(intent);
                break;
            case R.id.two_player_game_button:
                //intent = new Intent(this, GameActivity.class);
                //startActivity(intent);
                break;
            case R.id.player_profiles_button:
                //intent = new Intent(this, GameActivity.class);
                //startActivity(intent);
                break;
            case R.id.options_button:
                Log.d(TAG, "Options Button");
                //intent = new Intent(this, Options.class);
                //startActivity(intent);
                break;
            case R.id.about_button:
                Log.d(TAG, "About Button");
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}
