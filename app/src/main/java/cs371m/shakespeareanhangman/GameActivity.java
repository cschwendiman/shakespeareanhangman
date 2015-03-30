package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class GameActivity extends Activity {
    private static final String TAG = "Game Activity";

    //private HangmanGame game;
    private GameBoard gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createKeyboard();

        //Prefs prefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
        //game = new HangmanGame();

        gameBoard = (GameBoard) findViewById(R.id.game_board);
        //gameBoard.setGame(game);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    private void createKeyboard() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_game);
        LinearLayout keyboardLayout = new LinearLayout(this);
        keyboardLayout.setOrientation(LinearLayout.VERTICAL);
        keyboardLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutParams keyboardLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        keyboardLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        keyboardLayout.setLayoutParams(keyboardLayoutParams);

        String[] keyboard = getResources().getStringArray(R.array.keyboard);
        LayoutParams rowParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        for (String row : keyboard) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowParams);
            for (char key : row.toCharArray()) {
                // TODO maybe use charAt instead
                Log.d(TAG, "Create Button: " + key);
                Button button = new Button(this);
                button.setText(String.valueOf(key));
                button.setOnClickListener(keyboardClickListener);
                rowLayout.addView(button);
            }
            keyboardLayout.addView(rowLayout);
        }
        layout.addView(keyboardLayout);
    }

    private void startNewGame() {
        gameBoard.invalidate();

    }

    private void chooseLetter(String letter) {
        assert(letter.length() == 1);
        //game.chooseLetter(letter);
        gameBoard.invalidate();
        //if(mSoundOn) {
        //    mSounds.play(mSoundIDMap.get(R.raw.human_move), 1, 1, 1, 0, 1);
        //}

        boolean isGameOver = false;
        //if (game.isGameOver()) {
        //  endGame();
        //}
    }

    private void endGame() {
        Log.d(TAG, "Game Ended");
        // TODO: Redirect to status activity
        // Call finish to remove activity from stack
        finish();
    }

    private View.OnClickListener keyboardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String move = ((Button) v).getText().toString();
            Log.d(TAG, " Button Clicked: " + move);
            chooseLetter(move);
            v.setVisibility(View.INVISIBLE);
        }
    };
}
