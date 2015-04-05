package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ResultsActivity extends Activity {
    private TextView resultView;
    private TextView phraseView;
    private TextView playView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        SharedPreferences prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        boolean soundOn = prefs.getBoolean("soundToggle",false);
        SoundPool sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        sounds.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundID, int status) {
                soundPool.play(soundID, 1, 1, 1, 0, 1);
            }
        });

        Bundle extras = getIntent().getExtras();
        resultView = (TextView) findViewById(R.id.result);
        if (extras.getBoolean("status")) {
            resultView.setText(R.string.result_win);
            if (soundOn) {
                sounds.load(this, R.raw.cheering, 1);
            }
        }
        else {
            resultView.setText(R.string.result_loss);
            if (soundOn) {
                //sounds.load(this, R.raw.loss, 1);
            }
        }

        phraseView = (TextView) findViewById(R.id.phrase);
        phraseView.setText(extras.getString("phrase"));

        playView = (TextView) findViewById(R.id.play);
        playView.setText(extras.getString("play"));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
