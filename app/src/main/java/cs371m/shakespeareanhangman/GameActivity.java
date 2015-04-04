package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GameActivity extends Activity {
    private static final String TAG = "Game Activity";

    private HangmanGame game;
    private GameBoard gameBoard;

    // Control the sounds
    private SoundPool sounds;
    private int correctSoundID;
    private int incorrectSoundID;

    // Storing the chosen secret phrase for later
    // Perhaps we will want to put it in SharedPreferences so we can display the correct answer on the game results screen?
    private Phrase secretPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createKeyboard();

        //Prefs prefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);

        // TODO: get difficulty level from options
        int difficultyLevel = 1;

        // Choose the secret phrase and store as a Phrase object for later
        this.secretPhrase = getSecretPhrase(difficultyLevel);

        // Kickoff the game logic
        game = new HangmanGame(secretPhrase.getQuote());

        gameBoard = (GameBoard) findViewById(R.id.game_board);
        gameBoard.setGame(game);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        correctSoundID = sounds.load(this, R.raw.clapping, 1);
        incorrectSoundID = sounds.load(this, R.raw.no, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(sounds != null) {
            sounds.release();
            sounds = null;
        }
    }

    /*
     * This method uses the text files in res/raw to choose a secret phrase
     * Input: difficulty level (determines which file to use to get a phrase)
     * Output: Phrase object (stores the quote and the name of the play it's from)
     */
    private Phrase getSecretPhrase(int difficultyLevel) {
        Resources r = getResources();
        Phrase chosenPhrase = new Phrase("ERROR", "ERROR");

        try {
            InputStream quotes = null;

            // Get the appropriate file based on the difficulty level
            if(difficultyLevel == 0) {
                quotes = r.openRawResource(R.raw.easyquotes);
            } else if(difficultyLevel == 1) {
                quotes = r.openRawResource(R.raw.mediumquotes);
            } else if(difficultyLevel == 2) {
                quotes = r.openRawResource(R.raw.hardquotes);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(quotes);
            BufferedReader br = new BufferedReader(inputStreamReader);

            /* Note on the file format:
             * First line has a single int with the total number of quotes in the file
             * Then each quote is represented in the following three-line format:
             * First line is the quote
             * Second line is the name of the play or sonnet it was from
             */
            // TODO: add better logic to avoid repeating phrases. Perhaps store record of used phrases in SharedPreferences?

            // Get the total number of quotes
            int numQuotes = Integer.parseInt(br.readLine());

            // Choose a random quote number
            Random rand = new Random();
            int targetQuoteNum = rand.nextInt() % numQuotes;

            // Go to the correct quote
            int quoteNum = 0;
            while (quoteNum < targetQuoteNum) {
                // Skip the next quote
                br.readLine();
                br.readLine();
                quoteNum++;
            }

            // Now we're at the correct quote, so parse it and store as a Phrase
            String quote = br.readLine();
            String play = br.readLine();
            chosenPhrase = new Phrase(quote, play);

            br.close();

        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
        return chosenPhrase;
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

    private void makeGuess(char letter) {
        boolean guessCorrect = game.makeGuess(letter);

        if(guessCorrect) {
            sounds.play(correctSoundID, 1, 1, 1, 0, 1);
        } else {
            sounds.play(incorrectSoundID, 1, 1, 1, 0, 1);
        }

        gameBoard.invalidate();

        //if(mSoundOn) {
        //    mSounds.play(mSoundIDMap.get(R.raw.human_move), 1, 1, 1, 0, 1);
        //}


        switch (game.checkGameStatus()) {
            case PLAYER_WIN:
                return;
            case PLAYER_LOSS:
                endGame();
                return;
            case ONGOING:
                //do nothing
                return;
        }

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
            char move = ((Button) v).getText().charAt(0);
            Log.d(TAG, " Button Clicked: " + move);
            makeGuess(move);
            v.setVisibility(View.INVISIBLE);
        }
    };

    /*
     * A little class to store information about the secret phrase
     * Inputs: the secret phrase, the name of the play or sonnet it's from
     */
    private class Phrase {
        private String quote;
        private String play;

        public Phrase(String quote, String play) {
            this.quote = quote;
            this.play = play;
        }

        public String getQuote() {
            return quote;
        }

        public String getPlay() {
            return play;
        }
    }
}
