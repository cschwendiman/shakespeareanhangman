package cs371m.shakespeareanhangman;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cassie on 4/5/15.
 */
public class ScriptView extends TextView {
    public ScriptView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Meddon.ttf"));
    }
}
