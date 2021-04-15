package com.nelson.weather.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by lixiaojian on 16/12/22.
 */

public class RobotoMediumTextView extends AppCompatTextView {

    public RobotoMediumTextView(Context context) {
        super(context);

        initTypeFace();
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initTypeFace();
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initTypeFace();
    }

    private void initTypeFace() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        } else {
            setTypeface(Typeface.SANS_SERIF);
        }
    }
}
