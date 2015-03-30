package cs371m.shakespeareanhangman;

import android.util.Log;

import junit.framework.Assert;

/**
 * Created by kat25 on 3/25/15.
 */
public class HangmanGame {

    private static final String TAG = "HangmanGame";
    private static final char HIDDEN_LETTER = '_';
    private static final int MAX_WRONG_GUESSES = 6;

    // Game difficulty level
    private int difficultyLevel;

    // Game secret phrase
    private String secretPhrase;

    // Game phrase as currently revealed
    private String currentPhrase;

    // Number of wrong guesses the player has made
    private int wrongGuesses;

    // Constructor
    public HangmanGame() {
        // Set difficulty level
        difficultyLevel = 1;  // TODO get level from options
        // Set number of wrong guesses so far
        wrongGuesses = 0;
        // Get secret phrase for this game
        secretPhrase = generateSecretPhrase(difficultyLevel);
        // Get the phrase with unrevealed characters blanked out
        currentPhrase = generateHiddenPhrase(secretPhrase);
    }

    // Chooses a secret phrase for the game based on the difficulty level
    private String generateSecretPhrase(int difficultyLevel) {
        return "To be or not to be, that is the question.";  // TODO
    }

    // Replaces all letters in the secret phrase with the "hidden letter" character
    private String generateHiddenPhrase(String secretPhrase) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < secretPhrase.length(); i++) {
            char current = secretPhrase.charAt(i);
            if(Character.isLetter(current)) {
                sb.append(HIDDEN_LETTER);
            } else {
                sb.append(current);
            }
        }
        return sb.toString();
    }

    /*
     * Resets the game by resetting the difficulty level, number of wrong guesses,
     * secret phrase, and current phrase.
     */
    public void resetGame() {
        difficultyLevel = 1; //TODO
        wrongGuesses = 0;
        secretPhrase = generateSecretPhrase(difficultyLevel);
        currentPhrase = generateHiddenPhrase(secretPhrase);
    }

    public String getCurrentPhrase() {
        return currentPhrase;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    /* Processes a user's guess
     * Input: letter the user guessed
     * Output: true if the guess was correct, false otherwise
     */
    public boolean makeGuess(char guess) {
        if(!Character.isLetter(guess)) {
            Log.d(TAG, "ERROR: Sent a non-letter as a guess");
        }

        boolean success;

        if(secretPhrase.indexOf(guess) != -1) {
            success = true;

            // Update the current phrase with the correctly guessed letter revealed
            StringBuilder newCurrentPhrase = new StringBuilder();
            for(int i = 0; i < currentPhrase.length(); i++) {
                if(secretPhrase.charAt(i) == guess) {
                    newCurrentPhrase.append(guess);
                } else {
                    newCurrentPhrase.append(currentPhrase.charAt(i));
                }
            }

            currentPhrase = newCurrentPhrase.toString();

        } else {
            wrongGuesses++;
            success = false;
        }
        return success;

    }

    public boolean checkForWin() {
        return secretPhrase.equals(currentPhrase);
    }

    public boolean checkForLoss() {
        return wrongGuesses == MAX_WRONG_GUESSES;
    }

}
