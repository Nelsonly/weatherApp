package com.nelson.weather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * Created by lzm on 2017/9/18.
 */

public class ShareView extends FrameLayout {

    private Context context;
    private float startX;
    private float startY;
    private int screenHeight;
    private int top;
    private int bottom;
    private int start;
    private boolean isOnclick;


    public ShareView(Context context) {
        super(context);
    }

    public ShareView(Context context, @Nullable  AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        screenHeight = getScreenHeight(context);
        top = dp2px(context, 30);
        bottom = dp2px(context, 260);
        start = dp2px(context, 280);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            //layoutParams.leftMargin = getLeft() + offsetX;
            layoutParams.topMargin = screenHeight - start;
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int touchSlop = 2;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isOnclick = true;
                startX = event.getRawX();
                startY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                float Y = event.getRawY();
                // ?????????
                int offsetX = (int) (x - startX);
                int offsetY = (int) (Y - startY);
                if (Math.abs(offsetY) > touchSlop) {
                    isOnclick = false;
                }
                // ????????????
                int margin = getTop() + offsetY;
                if (margin > top && margin < (screenHeight - bottom)) {
                    //layout(getLeft(), getTop()+offsetY, getRight(),getBottom() + offsetY);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                    //layoutParams.leftMargin = getLeft() + offsetX;
                    layoutParams.topMargin = margin;
                    setLayoutParams(layoutParams);
                }
                // ???????????????
                startX = x;
                startY = Y;
                break;

            case MotionEvent.ACTION_UP:
                if (isOnclick){
                    performClick();
                }
                break;
        }
        return true;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int dp2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static float dp2px_f(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }
}
