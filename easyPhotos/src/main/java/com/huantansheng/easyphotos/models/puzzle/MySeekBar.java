package com.huantansheng.easyphotos.models.puzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

import com.huantansheng.easyphotos.utils.DisplayUtil;

import java.io.File;

public class MySeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
    private static final String TAG ="zhangyuhong" ;

    public MySeekBar(Context context) {
        super(context);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private float height;
    private float width;
    private Paint mPaint = new Paint();
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = this.getHeight();
        width  = this.getWidth();


    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float)getWidth()/2.0f,(float) getHeight()/2.0f, DisplayUtil.dip2px(getContext(),4),mPaint );
        super.onDraw(canvas);
    }
}
