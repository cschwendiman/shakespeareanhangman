package cs371m.shakespeareanhangman;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cassie on 3/29/15.
 */
public class GameBoard extends View {
    public static final String TAG = "GameBoard";
    private HangmanGame game;
    
    // game logic var

    public GameBoard(Context context) {
        super(context);
        initialize();
    }


    public GameBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }


    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void initialize() {

    }

    public void setGame(HangmanGame gameObject) {
        game = gameObject;
    }

    public void onDraw() {
        // game logic get phrase
        // draw phrase

        // game logic get number of misses for drawing hangman
        // draw hangman
    }
}
