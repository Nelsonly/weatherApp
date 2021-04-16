package com.huantansheng.easyphotos.models.puzzle;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.utils.DisplayUtil;

/**
 * @author nelson
 * @类名: NumberSeekBar
 * @作者: wang.fb
 * @日期: 2014-8-11 下午2:01:14
 * @修改人:
 * @修改时间: 2014-8-11 下午2:01:14
 * @修改内容:
 * @版本: V1.0
 */
public class NumberSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    public static final int ONEPOINT = 0;
    public static final int NOPOINT = 1;
    public static final int CENTER_TYPE = 2;
    public static final int NORMAL = 3;
    public static final int RGB = 4;
    private static final String TAG = "zhangyuhong";
    private int oldPaddingTop;

    private int oldPaddingLeft;

    private int oldPaddingRight;

    private int oldPaddingBottom;

    private boolean isMysetPadding = true;

    private String mText;

    private float mTextWidth;

    private float mImgWidth;

    private float mImgHei;

    private Paint mPaint;

    private Resources res;

    private Bitmap bm;


    private Paint circlePaint = new Paint();

    private int textsize = DisplayUtil.sp2px(getContext(), 12);

    private float sliderWidth = 1;

    private float sliderHeight = 1;

    private int textpaddingleft;

    private int textpaddingtop;

    private int imagepaddingleft;

    private int imagepaddingtop;
    private int maxDegree = 100;
    private int minDegree = 0;
    private Paint outCircle = new Paint();
    private Paint linePaint = new Paint();
    private Boolean isTextShow = true;
    private int type = 0;
    TypedArray ta;
    private boolean isTextOrange;
    private float centerDegree = 0;

    public NumberSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public NumberSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    // 修改setpadding 使其在外部调用的时候无效
    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (isMysetPadding) {
            super.setPadding(left, top, right, bottom);
        }
    }

    // 初始化
    private void init(Context context, AttributeSet attr) {
        res = getResources();
        ta = context.obtainStyledAttributes(attr, R.styleable.NumberSeekBar);
//        initBitmap();
        initDraw();
//        setPadding();
    }

    private void initDraw() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(textsize);
        mPaint.setColor(Color.WHITE);
        outCircle.setColor(getResources().getColor(R.color.out_ring_color, null));
        outCircle.setStyle(Paint.Style.STROKE);
        outCircle.setAntiAlias(true);
        outCircle.setStrokeWidth(4);
        circlePaint.setColor(ta.getColor(R.styleable.NumberSeekBar_centerCircleColor, android.R.color.white));
        circlePaint.setStyle(Paint.Style.FILL);
        sliderWidth = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_puzzle_seek_slider_style, null).getIntrinsicWidth();
        sliderHeight = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_puzzle_seek_slider_style, null).getIntrinsicHeight();
        linePaint.setColor(ta.getColor(R.styleable.NumberSeekBar_progressColor, android.R.color.white));
        linePaint.setStrokeWidth(6);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        Rect bounds = this.getProgressDrawable().getBounds();
        float yImg = imagepaddingtop + oldPaddingTop;
        float xText =
                bounds.width() * getProgress() / (maxDegree - minDegree) + mImgWidth / 2
                        - mTextWidth / 2 + textpaddingleft + oldPaddingLeft + sliderWidth;
        float yText =
                yImg + textpaddingtop + mImgHei / 2 + getTextHei() / 2;
        float widthLine = bounds.width() + mImgWidth / 2.0f - mTextWidth / 2.0f + textpaddingleft + oldPaddingLeft + sliderWidth;
        if (isTextShow) {
            try {
                mText = String.valueOf(getProgress() + minDegree);
                mTextWidth = mPaint.measureText(mText);
                if (isTextOrange) {
                    mPaint.setColor(getResources().getColor(R.color.out_ring_color));
                }
                canvas.drawText(mText, xText - 5, yText + 2, mPaint);
                mPaint.setColor(Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // sliderWidth/2.0f  + (float) widthLine*((float)minDegree/ ((float)minDegree - (float)maxDegree))

        // 环的宽度一半
//        canvas.drawLine((sliderWidth/2.0f-(float) widthLine*((float)centerDegree/((float)minDegree - (float)maxDegree)))-2,getHeight()/2.0f,sliderWidth/2.0f+xText,getHeight()/2.0f,linePaint);

        float startX = (sliderWidth / 2.0f - (float) widthLine * ((float) centerDegree / ((float) minDegree - (float) maxDegree)));
        float startY = getHeight() / 2.0f;
        float stopX = sliderWidth / 2.0f + xText;
        float stopY = getHeight() / 2.0f;

        super.onDraw(canvas);

        switch (type) {
            case CENTER_TYPE:
                canvas.drawCircle((float) getWidth() / 2.0f, (float) getHeight() / 2.0f, DisplayUtil.dip2px(getContext(), 4), circlePaint);

                if (getProgress() >= 45 && getProgress() <= 55) {

                } else if (getProgress() > 55) {
                    canvas.drawLine(startX, startY, stopX - 44 - 6, stopY, linePaint);
                } else {
                    canvas.drawLine(startX, startY, stopX - 6, stopY, linePaint);
                }

                break;

            case ONEPOINT:
                //一个中心点
                canvas.drawLine(sliderWidth / 2.0f + (float) widthLine * (minDegree / ((float) minDegree - (float) maxDegree)), getHeight() / 2.0f, sliderWidth / 2.0f + xText, getHeight() / 2.0f, linePaint);
                canvas.drawCircle((float) getWidth() / 2.0f, (float) getHeight() / 2.0f, DisplayUtil.dip2px(getContext(), 4), circlePaint);
                break;

            case NOPOINT:
                if (getProgress() <= 4) {

                } else if (getProgress() > 4 && getProgress() <= 9) {
                    canvas.drawLine(sliderWidth + (float) widthLine * (minDegree / ((float) minDegree - (float) maxDegree)), getHeight() / 2.0f, xText - 13, getHeight() / 2.0f, linePaint);
                } else if (getProgress() > 9) {
                    canvas.drawLine(sliderWidth + (float) widthLine * (minDegree / ((float) minDegree - (float) maxDegree)), getHeight() / 2.0f, xText - 4, getHeight() / 2.0f, linePaint);

                }
//                canvas.drawLine(sliderWidth/2.0f+(float) widthLine*(minDegree/((float)minDegree - (float)maxDegree)),getHeight()/2.0f,/*sliderWidth/2.0f+*/xText,getHeight()/2.0f,linePaint);
//                canvas.drawLine(sliderWidth/2.0f+(float) widthLine*(minDegree/((float)minDegree - (float)maxDegree)),getHeight()/2.0f,xText-sliderWidth/2,getHeight()/2.0f,linePaint);


                break;

            case NORMAL:
                if (getProgress() > 4) {
                    canvas.drawLine(startX + sliderWidth / 2.0f + 1, startY, stopX - sliderWidth / 2 - 2 - 6, stopY, linePaint);
                }

                canvas.drawLine(startX + sliderWidth / 2.0f, startY, stopX - 6 - 30, stopY, linePaint);

                break;

            case RGB:
                canvas.drawLine(sliderWidth / 2.0f + (float) widthLine * (minDegree / ((float) minDegree - (float) maxDegree)), getHeight() / 2.0f, xText - sliderWidth / 2, getHeight() / 2.0f, linePaint);

            default:
                break;
        }

    }

    // 初始化padding 使其左右上 留下位置用于展示进度图片
    private void setPadding() {
        int top = getBitmapHeigh() + oldPaddingTop;
        int left = getBitmapWidth() / 2 + oldPaddingLeft;
        int right = getBitmapWidth() / 2 + oldPaddingRight;
        int bottom = oldPaddingBottom;
        isMysetPadding = true;
        setPadding(left, top, right, bottom);
        isMysetPadding = false;
    }

    /**
     * 设置展示进度背景图片
     *
     * @param resid
     */
    public void setBitmap(int resid) {
        bm = BitmapFactory.decodeResource(res, resid);
        if (bm != null) {
            mImgWidth = bm.getWidth();
            mImgHei = bm.getHeight();
        } else {
            mImgWidth = 0;
            mImgHei = 0;
        }
        setPadding();
    }

    /**
     * 替代setpadding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setMyPadding(int left, int top, int right, int bottom) {
        oldPaddingTop = top;
        oldPaddingLeft = left;
        oldPaddingRight = right;
        oldPaddingBottom = bottom;
        isMysetPadding = true;
        setPadding(left + getBitmapWidth() / 2, top + getBitmapHeigh(), right
                + getBitmapWidth() / 2, bottom);
        isMysetPadding = false;
    }

    /**
     * 设置进度字体大小
     *
     * @param textsize
     */
    public void setTextSize(int textsize) {
        this.textsize = textsize;
        mPaint.setTextSize(textsize);
    }

    /**
     * 设置进度字体颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * 调整进度字体的位置 初始位置为图片的正中央
     *
     * @param top
     * @param left
     */
    public void setTextPadding(int top, int left) {
        this.textpaddingleft = left;
        this.textpaddingtop = top;
    }

    /**
     * 调整进图背景图的位置 初始位置为进度条正上方、偏左一半
     *
     * @param top
     * @param left
     */
    public void setImagePadding(int top, int left) {
        this.imagepaddingleft = left;
        this.imagepaddingtop = top;
    }

    private int getBitmapWidth() {
        return (int) Math.ceil(mImgWidth);
    }

    private int getBitmapHeigh() {
        return (int) Math.ceil(mImgHei);
    }

    private float getTextHei() {
        FontMetrics fm = mPaint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.top) + 2;
    }

    public int getTextpaddingleft() {
        return textpaddingleft;
    }

    public int getTextpaddingtop() {
        return textpaddingtop;
    }

    public int getImagepaddingleft() {
        return imagepaddingleft;
    }

    public int getImagepaddingtop() {
        return imagepaddingtop;
    }

    public int getTextsize() {
        return textsize;
    }

    public int getMaxDegree() {
        return maxDegree;
    }

    public void setMaxDegree(int maxDegree) {
        this.maxDegree = maxDegree;
    }

    public int getMinDegree() {
        return minDegree;
    }

    public void setMinDegree(int minDegree) {
        this.minDegree = minDegree;
    }

    public void setDegree(int maxDegree, int minDegree) {
        this.maxDegree = maxDegree;
        this.minDegree = minDegree;
        centerDegree = minDegree;
        Log.d(TAG, "setDegree: " + (maxDegree - minDegree));
        setMax(maxDegree - minDegree);
        invalidate();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Boolean getTextShow() {
        return isTextShow;
    }

    public void setTextShow(Boolean textShow) {
        isTextShow = textShow;
    }

    public void setTextColorOrange() {
        isTextOrange = true;
    }

    public void setCenterValue() {
        minDegree = minDegree - 50;
        maxDegree = maxDegree - 50;
        invalidate();
    }

    public void setCenterDegree(float centerDegree) {
        this.centerDegree = centerDegree;
    }


}