package com.nelson.weather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nelson.weather.R;
import com.nelson.weather.WeatherApplication;
import com.nelson.weather.utils.DisplayUtil;

/**
 * @author yating
 */
public class SuperCircleView extends View {

    private final String TAG = "SuperCircleView";

    /**view宽的中心点*/
    private int mViewCenterX;
    /**view高的中心点*/
    private int mViewCenterY;
    /**最里面白色圆的半径*/
    private int mMinRadio;

    /**底层默认圆环的进度条宽度*/
    private final float mFirstNormalRingWidth;
    /**第二层默认圆环进度条的宽度*/
    private float mSecondNormalRingWidth;
    /**进度圆环的进度条宽度*/
    private float mColorRingWidth;
    /**圆环的宽度*/
    private final float mRingWidth;

    /**最外面圆的颜色*/
    private int mMaxCircleColor;
    /**默认圆环的颜色*/
    private int mRingFirstNormalColor;
    /**第二层默认圆环的颜色*/
    private int mRingSecondNormalColor;
    /**进度圆环颜色*/
    private final int mProgressCircleColor;

    private final int mOffsetY1;
    private final int mOffsetY2;
    private final int mOffsetY3;
    private final Paint mPaint;

    /**圆环的矩形区域*/
    private RectF mRectF;
    /**要显示几段彩色*/
    private int mSelectRing = 0;
    /**第一行文本的大小*/
    private final float mFirstTextSize;
    /**第一行文本的颜色*/
    private final int mFirstTextColor;
    /**第二行文本*/
    private String mSecondText = "0";
    /**第二行文本的大小*/
    private final float mSecondTextSize;
    /**第三行文本*/
    private String mThirdText = "0";

    private String frontFamily;

    private final Context context = WeatherApplication.getContext();

    public SuperCircleView(Context context) {
        this(context, null);
    }

    public SuperCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperCircleView);

        /**最里面白色圆的半径*/
        mMinRadio = a.getInteger(R.styleable.SuperCircleView_min_circle_radio, DisplayUtil.dpToPx(context, 162)) / 2;

        /**底层默认圆环的进度条宽度*/
        mFirstNormalRingWidth = a.getFloat(R.styleable.SuperCircleView_first_nomal_ring_width, DisplayUtil.dpToPx(context, 8)) / 2;
        /**第二层默认圆环的进度条宽度*/
        mSecondNormalRingWidth = a.getFloat(R.styleable.SuperCircleView_second_nomal_ring_width, DisplayUtil.dpToPx(context, 15));
        /**进度圆环的进度条宽度*/
        mColorRingWidth = a.getFloat(R.styleable.SuperCircleView_color_ring_width, DisplayUtil.dpToPx(context, 15));
        /**圆环的宽度*/
        mRingWidth = a.getFloat(R.styleable.SuperCircleView_ring_width, DisplayUtil.dpToPx(context, 15)) / 2;

        /**最外面圆的颜色*/
        mMaxCircleColor = a.getColor(R.styleable.SuperCircleView_max_circle_color, context.getResources().getColor(R.color.white_6));
//        mRingFirstNormalColor = a.getColor(R.styleable.SuperCircleView_ring_first_normal_color, context.getResources().getColor(R.color.transparent_bg_3)); //底层默认圆环的颜色
        /**第二层默认圆环的颜色*/
        mRingSecondNormalColor = a.getColor(R.styleable.SuperCircleView_ring_sencond_normal_color, context.getResources().getColor(R.color.white_6));
        /**进度圆环的颜色*/
        mProgressCircleColor = a.getColor(R.styleable.SuperCircleView_progress_circle_color, context.getResources().getColor(R.color.white));
        /**要显示几段彩色*/
        mSelectRing = a.getInt(R.styleable.SuperCircleView_ring_color_select, 0);

//        mFirstText = a.getString(R.styleable.SuperCircleView_first_text);
        mFirstTextSize = a.getFloat(R.styleable.SuperCircleView_first_text_size, DisplayUtil.spToPx(context, 14));
        mFirstTextColor = a.getColor(R.styleable.SuperCircleView_first_text_color, context.getResources().getColor(R.color.white));

//        mSecondtText = a.getString(R.styleable.SuperCircleView_second_text);
        mSecondTextSize = a.getFloat(R.styleable.SuperCircleView_second_text_size, DisplayUtil.spToPx(context, 64));
        /**第二行文本的颜色*/
        int mSecondTextColor = a.getColor(R.styleable.SuperCircleView_second_text_color, context.getResources().getColor(R.color.white));

//        mThirdText = a.getString(R.styleable.SuperCircleView_first_text);
        /**第三行文本的大小*/
        float mThirdTextSize = a.getFloat(R.styleable.SuperCircleView_color_ring_width, DisplayUtil.spToPx(context, 14));
        /**第三行文本的颜色*/
        int mThirdTextColor = a.getColor(R.styleable.SuperCircleView_third_text_color, context.getResources().getColor(R.color.white));

//        frontFamily=a.getFont(R.font.source_han_sans_cn_medium)；

        mOffsetY1 = DisplayUtil.dpToPx(context, 41);
        mOffsetY2 = DisplayUtil.dpToPx(context, 30);
        mOffsetY3 = DisplayUtil.dpToPx(context, 69);

        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        /**如果需要重写onDraw方法，则需要调用此方法 by Zero*/
        this.setWillNotDraw(false);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /**view的宽*/
        int mViewWidth = getMeasuredWidth();
        /**view的高*/
        int mViewHeight = getMeasuredHeight();
        mViewCenterX = mViewWidth / 2;
        mViewCenterY = mViewHeight / 2;
        mRectF = new RectF(mViewCenterX - mMinRadio - mRingWidth / 2, mViewCenterY - mMinRadio - mRingWidth / 2,
                mViewCenterX + mMinRadio + mRingWidth / 2, mViewCenterY + mMinRadio + mRingWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //最外层透明环
        mPaint.setColor(mMaxCircleColor);
        canvas.drawCircle(mViewCenterX, mViewCenterY, mMinRadio + mRingWidth + DisplayUtil.dpToPx(context, 20), mPaint);
        /*//画底层默认圆环
        drawFirstNormalRing(canvas);*/
        //画第二层默认圆环
        drawSecondNormalRing(canvas);
        //画进度圆环
        drawColorRing(canvas);
        //画第一级文本
        drawFirstText(canvas);
        //画第二级文本
        drawSecondText(canvas);
        //画第三级文本
        drawThirdText(canvas);

        Log.i("super", "onDraw: 已执行");
    }

    /**
     * 画进度圆环
     *
     * @param canvas
     */
    private void drawColorRing(Canvas canvas) {
        Paint ringColorPaint = new Paint(mPaint);
        /**设置画笔绘制模式"只描边，不填充"*/
        ringColorPaint.setStyle(Paint.Style.STROKE);
        /**线条宽度*/
        ringColorPaint.setStrokeWidth(mColorRingWidth);
        /**设置线帽*/
        ringColorPaint.setStrokeCap(Paint.Cap.ROUND);
        /**设置颜色*/
        ringColorPaint.setColor(mProgressCircleColor);

        /**drawArc——绘制圆弧*/
        canvas.drawArc(mRectF, 135, mSelectRing, false, ringColorPaint);
    }

    /**
     * 画底层默认圆环
     *
     * @param canvas
     */
    private void drawFirstNormalRing(Canvas canvas) {
        Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mFirstNormalRingWidth);
        ringNormalPaint.setColor(mRingFirstNormalColor);
        canvas.drawArc(mRectF, 270, 360, false, ringNormalPaint);
    }

    /**
     * 画第二层默认圆环
     *
     * @param canvas
     */
    private void drawSecondNormalRing(Canvas canvas) {
        Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mSecondNormalRingWidth);
        ringNormalPaint.setColor(mRingSecondNormalColor);
        /**设置线帽*/
        ringNormalPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(mRectF, 135, 270, false, ringNormalPaint);
    }

    /**
     * 显示几段
     *
     * @param i
     */
    public void setSelect(Float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress value can not be less than 0");
        }
        float mMaxProgress = 500f;
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        float mCurrentProgress = progress;
        float size = mCurrentProgress / mMaxProgress;
        mSelectRing = (int) (270 * size);
    }

    /**
     * 画第一行文本
     *
     * @param canvas
     * @param rectF
     */
    public void drawFirstText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mFirstTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mFirstTextSize);
        paint.setAntiAlias(true);
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        paint.setTypeface(typeface);
        canvas.drawText("全市平均AQI", mViewCenterX, mViewCenterY - mOffsetY1, paint);
    }

    /**
     * 画第二行文本
     *
     * @param canvas
     * @param rectF
     */
    public void drawSecondText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mFirstTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mSecondTextSize);
        paint.setAntiAlias(true);
//        Typeface typeface=Typeface.create(Typeface.MONOSPACE ,Typeface.NORMAL);
//        paint.setTypeface(typeface);
        paint.setFakeBoldText(false);
        canvas.drawText(mSecondText, mViewCenterX, mViewCenterY + mOffsetY2, paint);
        Log.i("super", "drawSecondText: 已执行");
    }

    /**
     * 画第三行文本
     *
     * @param canvas
     * @param rectF
     */
    public void drawThirdText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mFirstTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mFirstTextSize);
        paint.setAntiAlias(true);
        canvas.drawText(mThirdText, mViewCenterX, mViewCenterY + mOffsetY3, paint);
    }

    public void setMinRadio(int minRadio) {
        mMinRadio = minRadio;
    }

    public void setFirstText(String mFirstText) {
        /**第一行文本*/
    }

    public void setSecondText(String mSecondText) {
        this.mSecondText = mSecondText;
        Log.i("super", "setSecondText: 已执行");
    }

    public void setThirdText(String mThirdText) {
        this.mThirdText = mThirdText;
    }

    public void setSecondNormalRingWidth(float mSecondNormalRingWidth) {
        this.mSecondNormalRingWidth = mSecondNormalRingWidth;
    }

    public void setColorRingWidth(float mColorRingWidth) {
        this.mColorRingWidth = mColorRingWidth;
    }

    public void setMaxCircleColor(int mMaxCircleColor) {
        this.mMaxCircleColor = mMaxCircleColor;
    }

    public void setRingSecondNormalColor(int mRingSecondNormalColor) {
        this.mRingSecondNormalColor = mRingSecondNormalColor;
    }
}
