package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.os.Bundle;


public class AboutActivity extends Activity {
    public static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
