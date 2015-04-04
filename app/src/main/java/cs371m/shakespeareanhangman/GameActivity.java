package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.RelativeLayout.LayoutParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GameActivity extends Activity {
    private static final String TAG = "Game Activity";

    private HangmanGame game;
    private BoardView board;
    private TextView phraseView;

    private SharedPreferences prefs;
    private boolean soundOn;
    private int difficultyLevel;

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

        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        soundOn = prefs.getBoolean("soundToggle",false);
        difficultyLevel = prefs.getInt("difficulty",0);

        // Choose the secret phrase and store as a Phrase object for later
        this.secretPhrase = getSecretPhrase(difficultyLevel);

        // Kickoff the game logic
        game = new HangmanGame(secretPhrase.getQuote());

        // Init views
        phraseView = (TextView) findViewById(R.id.phrase);
        phraseView.setText(game.getCurrentPhrase());
        board = (BoardView) findViewById(R.id.board);
        board.setGame(game);
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

    //TODO: Remove menu stuff??

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
        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        String[] keyboard = getResources().getStringArray(R.array.keyboard);
        LayoutParams rowParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams keyParams = new LayoutParams(42, 42);
        for (String row : keyboard) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowParams);
            for (char key : row.toCharArray()) {
                // TODO maybe use charAt instead
                Log.d(TAG, "Create Button: " + key);
                Button button = new Button(this);
                button.setText(String.valueOf(key));
                button.setTag(Character.toLowerCase(key));
                button.setOnClickListener(keyboardClickListener);
                button.setLayoutParams(keyParams);
                button.setPadding(0, 0, 0, 0);
                button.setTextSize(15);
                button.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                rowLayout.addView(button);
            }
            bottomLayout.addView(rowLayout);
        }
    }

    private void startNewGame() {
        board.invalidate();

    }

    private void makeGuess(char letter) {
        boolean guessCorrect = game.makeGuess(letter);

        if (soundOn) {
            if (guessCorrect) {
                sounds.play(correctSoundID, 1, 1, 1, 0, 1);
            } else {
                sounds.play(incorrectSoundID, 1, 1, 1, 0, 1);
            }
        }
        phraseView.setText(game.getCurrentPhrase());
        board.invalidate();

        switch (game.checkGameStatus()) {
            case PLAYER_WIN:
                endGame(true);
                return;
            case PLAYER_LOSS:
                endGame(false);
                return;
            case ONGOING:
                //do nothing
                return;
        }

    }

    private void endGame(boolean status) {
        Log.d(TAG, "Game Ended");
        Log.d(TAG, status ? "WIN" : "LOSE");
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("phrase", secretPhrase.getQuote());
        intent.putExtra("play", secretPhrase.getPlay());
        startActivity(intent);
        // Call finish to remove activity from stack
        finish();
    }

    private View.OnClickListener keyboardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            char move = (char) v.getTag();
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
