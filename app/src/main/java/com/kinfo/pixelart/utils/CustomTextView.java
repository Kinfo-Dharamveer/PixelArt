package com.kinfo.pixelart.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Zoptal.101 on 16/01/17.
 */
public class CustomTextView extends TextView {

    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
     */
    private static Typeface mTypeface;

    public CustomTextView(final Context context) {
        this(context, null);
    }

    public CustomTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "NexaBold.otf");
        }
        setTypeface(mTypeface);
    }

}
