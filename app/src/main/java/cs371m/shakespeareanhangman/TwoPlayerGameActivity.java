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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoPlayerGameActivity extends Activity {
    private static final String TAG = "Two Player Game Activity";

    private HangmanGame game;
    private TextView phraseView;

    private float scale;

    private SharedPreferences prefs;
    private boolean soundOn;

    // Control the sounds
    private SoundPool sounds;
    private int correctSoundID;
    private int incorrectSoundID;

    // Storing the chosen secret phrase for later
    // Perhaps we will want to put it in SharedPreferences so we can display the correct answer on the game results screen?
    private Phrase secretPhrase;

    // For two-player game, must track player name
    private String playerName;
    private boolean isFirstPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_game);
        scale = getResources().getDisplayMetrics().density;
        createKeyboard();

        prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
        soundOn = prefs.getBoolean("soundToggle",false);

        // Choose the secret phrase and store as a Phrase object for later
        this.secretPhrase = getSecretPhrase();

        // Get player turn
        int roundNum = prefs.getInt("roundNumber", 0);

        // Get player name and display it
        if(roundNum % 2 == 1) {
            playerName = prefs.getString("playerName1", "Player 1");
            isFirstPlayer = true;
        } else {
            playerName = prefs.getString("playerName2", "Player 2");
            isFirstPlayer = false;
        }

        TextView nameView = (TextView) findViewById(R.id.player_name);
        nameView.setText(playerName + "'s turn");

        // Kickoff the game logic
        game = new HangmanGame(secretPhrase.getQuote());

        // Init views
        phraseView = (TextView) findViewById(R.id.phrase);
        phraseView.setText(game.getCurrentPhrase());
    }

    @Override
    protected void onResume() {
        super.onResume();
        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        correctSoundID = sounds.load(this, R.raw.yes, 1);
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
    private Phrase getSecretPhrase() {
        // TODO: Make this method less of a clusterfuck
        Resources r = getResources();
        Phrase chosenPhrase = new Phrase("ERROR", "ERROR");
        String newPhraseQueue = "";
        String phraseQueue = prefs.getString("phraseQueue","");
        int difficultyLevel = prefs.getInt("difficulty",0);
        try {
            InputStream quotes = null;

            // Get the appropriate file based on the difficulty level
            if (difficultyLevel == 0) {
                quotes = r.openRawResource(R.raw.easyquotes);
            } else if (difficultyLevel == 1) {
                quotes = r.openRawResource(R.raw.mediumquotes);
            } else if (difficultyLevel == 2) {
                quotes = r.openRawResource(R.raw.hardquotes);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(quotes);
            BufferedReader br = new BufferedReader(inputStreamReader);

            int chosenPhraseIndex = 0;
            if (phraseQueue.isEmpty()) {
                // If phraseQueue is empty, generate new queue and get phraseIndex
                // Get the total number of quotes
                int numQuotes = Integer.parseInt(br.readLine());
                // Build a list with the indices of all the quotes
                List<Integer> quoteIndices = new ArrayList<Integer>();
                for (int i = 0; i < numQuotes; i++) {
                    quoteIndices.add(i);
                }
                // Shuffle the list to randomize the quotes
                Collections.shuffle(quoteIndices);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < quoteIndices.size(); i++) {
                    int quoteIndex = quoteIndices.get(i);
                    if (i == 0){
                        // Save the first index for the current game's phrase
                        chosenPhraseIndex = quoteIndex;
                    }
                    else {
                        stringBuilder.append(quoteIndex);
                        stringBuilder.append(',');
                    }
                }
                newPhraseQueue = stringBuilder.toString();
            } else {
                // Otherwise, pop off next phrase index
                String[] pieces = phraseQueue.split(",", 2);
                chosenPhraseIndex = Integer.parseInt(pieces[0]);
                newPhraseQueue = pieces[1];
                // Skip the first line with the number of quotes
                br.readLine();
            }

            // Go to the correct quote
            Log.d(TAG, "Phrase Index: " + chosenPhraseIndex);
            int quoteNum = 0;
            while (quoteNum < chosenPhraseIndex) {
                // Skip the next quote
                br.readLine();
                br.readLine();
                quoteNum++;
            }

            // Now we're at the correct quote, so parse it and store as a Phrase
            String quote = br.readLine();
            String play = br.readLine();
            chosenPhrase = new Phrase(quote, play);
            Log.d(TAG, "Chosen Phrase: " + chosenPhrase.getQuote());
            br.close();
        }
        catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

        // Save new phrase queue to prefs
        Log.d(TAG, "New Phrase Queue: " + newPhraseQueue);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("phraseQueue", newPhraseQueue);
        ed.apply();

        // Return our phrase
        return chosenPhrase;
    }

    private void createKeyboard() {
        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        String[] keyboard = getResources().getStringArray(R.array.keyboard);
        LayoutParams rowParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int pixels = (int) (28.0f * scale + 0.5f);
        LayoutParams keyParams = new LayoutParams(pixels, pixels);
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

    private void makeGuess(char letter) {
        boolean guessCorrect = game.makeGuess(letter);

        if (soundOn) {
            if (guessCorrect) {
                sounds.play(correctSoundID, 1, 1, 1, 0, 1);
            } else {
                sounds.play(incorrectSoundID, 1, 1, 1, 0, 1);
            }
        }

        /* If the guess was wrong, update the picture of the hangman to give him another limb. */
        if(!guessCorrect) {
            ImageView hangmanPic = (ImageView) findViewById(R.id.hangmanImageView);
            switch(game.getWrongGuesses()) {
                case 0:
                    hangmanPic.setImageResource(R.drawable.hangman0);
                    break;
                case 1:
                    hangmanPic.setImageResource(R.drawable.hangman1);
                    break;
                case 2:
                    hangmanPic.setImageResource(R.drawable.hangman2);
                    break;
                case 3:
                    hangmanPic.setImageResource(R.drawable.hangman3);
                    break;
                case 4:
                    hangmanPic.setImageResource(R.drawable.hangman4);
                    break;
                case 5:
                    hangmanPic.setImageResource(R.drawable.hangman5);
                    break;
                case 6:
                    hangmanPic.setImageResource(R.drawable.hangman6);
                    break;
            }
        }
        phraseView.setText(game.getCurrentPhrase());

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

        SharedPreferences.Editor ed = prefs.edit();

        // Update number of wins for the player
        if(status) {
            if(isFirstPlayer) {
                int wins = prefs.getInt("playerWins1", 0);
                wins++;
                ed.putInt("playerWins1", wins);
            } else {
                int wins = prefs.getInt("playerWins2", 0);
                wins++;
                ed.putInt("playerWins2", wins);
            }
        }
        ed.apply();

        // Send quote and play to results screen
        Intent intent = new Intent(this, TwoPlayerResultsActivity.class);
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
