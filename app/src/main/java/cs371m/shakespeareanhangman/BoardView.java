package cs371m.shakespeareanhangman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by cassie on 3/29/15.
 */
public class BoardView extends View {
    public static final String TAG = "GameBoard";
    private HangmanGame game;

    private TextPaint textPaint;
    private String phrase;
    private DynamicLayout text;
    private SpannableStringBuilder stringBuilder;
    
    // game logic var

    public BoardView(Context context) {
        super(context);
        initialize();
    }


    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }


    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void initialize() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTypeface(Typeface.SERIF);

        stringBuilder = new SpannableStringBuilder("");
        text = new DynamicLayout(stringBuilder, textPaint, 0, Layout.Alignment.ALIGN_CENTER, 1.0f, 2.0f, false);
    }

    public void setGame(HangmanGame gameObject) {
        game = gameObject;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "Redrawing Game Board");
        phrase = game.getCurrentPhrase();
        Log.d(TAG, "Phrase " + phrase);

        // game logic get number of misses for drawing hangman
        // draw hangman
        int wrongGuesses = game.getWrongGuesses();
        text.increaseWidthTo(getWidth());
        stringBuilder.replace(0, stringBuilder.length(), Integer.toString(wrongGuesses));
        text.draw(canvas);
    }
}
