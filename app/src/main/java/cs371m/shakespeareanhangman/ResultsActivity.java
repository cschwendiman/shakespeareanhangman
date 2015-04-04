package cs371m.shakespeareanhangman;

import android.app.Activity;
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

        Bundle extras = getIntent().getExtras();
        resultView = (TextView) findViewById(R.id.result);
        if (extras.getBoolean("status")) {
        resultView.setText(R.string.result_win);
        }
        else {
            resultView.setText(R.string.result_loss);
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
