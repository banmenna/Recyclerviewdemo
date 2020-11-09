package com.example.music;



        import android.content.Context;
        import android.util.AttributeSet;


/**
 * Created by CW3479 on 2015/4/2.
 */
public class AlwaysMarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}