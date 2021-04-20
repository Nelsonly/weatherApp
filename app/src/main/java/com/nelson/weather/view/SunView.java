package com.nelson.weather.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.nelson.weather.R;
import com.nelson.weather.utils.DisplayUtil;

import java.text.DecimalFormat;

/**
 * 太阳、月亮自定义View
 *
 * @author hefeng
 */

public class SunView extends View {

    private static final String TAG = "sunview";
    private int mWidth; //屏幕宽度
    private int marginTop = 0;//离顶部的高度
    private int mCircleColor;  //圆弧颜色
    private int mFontColor;  //字体颜色
    private int mRadius;  //圆的半径

    private float mCurrentAngle = 0 ; //当前旋转的角度
    private float mTotalMinute; //总时间(日落时间减去日出时间的总分钟数)
    private float mNeedMinute; //当前时间减去日出时间后的总分钟数
    private float mPercentage; //根据所给的时间算出来的百分占比
    private float positionX, positionY; //太阳图片的x、y坐标
    private float mFontSize;  //字体大小

    private String mStartTime; //开始时间(日出时间)
    private String mEndTime; //结束时间（日落时间）
    private String mCurrentTime; //当前时间

    private Paint mTextPaint; //画笔
    private Paint mLinePaint; //画笔
    private Paint mTimePaint; //画笔
    private RectF mRectF; //半圆弧所在的矩形
    private Bitmap mSunIcon; //太阳图片
    private WindowManager wm;
    private Paint mShadePaint;
    private Paint mPathPaint;
    private Context mContext;
    private boolean isSun = true;
    private float endHour;
    private Paint shadePaint;
    private Paint pathPaint;
    private Bitmap bitmap;
    public SunView(Context context) {
        this(context, null);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        marginTop = DisplayUtil.dip2px(context, 30);
        @SuppressLint("CustomViewStyleable") TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.SunAnimationView);
        mCircleColor = type.getColor(R.styleable.SunAnimationView_sun_circle_color, getContext().getResources().getColor(R.color.dark_text_color));
        mFontColor = type.getColor(R.styleable.SunAnimationView_sun_font_color, getContext().getResources().getColor(R.color.colorAccent));
       // mRadius = type.getInteger(R.styleable.SunAnimationView_sun_circle_radius, DisplayUtil.dp2px(getContext(), 130));
//        mRadius = mWidth;
//        Log.d("radius",String.valueOf(mRadius));
        mFontSize = type.getDimension(R.styleable.SunAnimationView_sun_font_size, DisplayUtil.dp2px(getContext(), 10));
        mFontSize = DisplayUtil.dp2px(getContext(), mFontSize);

        isSun = type.getBoolean(R.styleable.SunAnimationView_type, true);
        type.recycle();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        shadePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSunIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_7_days_sunrise_sunset);
        mSunIcon = DisplayUtil.bitmapResize(mSunIcon, DisplayUtil.dp2px(mContext, 18), DisplayUtil.dp2px(mContext, 18));

        positionX = mWidth - mRadius-DisplayUtil.dpToPx(mContext,9); // 太阳图片的初始x坐标
        positionY = mRadius*0.43f; // 太阳图片的初始y坐标
    }

    public void setType(boolean isSun, int circleColor, int fontColor) {
        this.isSun = isSun;
        mCircleColor = circleColor;
        mFontColor = fontColor;
    }

    public void setTimes(String startTime, String endTime, String currentTime) {
        mStartTime = startTime;
        mEndTime = endTime;
        mCurrentTime = currentTime;

        String currentTimes[] = currentTime.split(":");
        String startTimes[] = startTime.split(":");
        String endTimes[] = endTime.split(":");
        float currentHour = Float.parseFloat(currentTimes[0]);
        float currentMinute = Float.parseFloat(currentTimes[1]);

        float startHour = Float.parseFloat(startTimes[0]);
        endHour = Float.parseFloat(endTimes[0]);
        if (!isSun && endHour < startHour) {
            endHour += 24;
        }
        float endMinute = Float.parseFloat(endTimes[1]);

        if (isSun) {
            if (currentHour > endHour) {
                mCurrentTime = endTime;
            } else if (currentHour == endHour && currentMinute >= endMinute) {
                mCurrentTime = endTime;
            }
        }

        mTotalMinute = calculateTime(mStartTime, mEndTime, false);//计算总时间，单位：分钟
        mNeedMinute = calculateTime(mStartTime, mCurrentTime, true);//计算当前所给的时间 单位：分钟
        mPercentage = Float.parseFloat(formatTime(mTotalMinute, mNeedMinute));//当前时间的总分钟数占日出日落总分钟数的百分比
        mCurrentAngle = 110 * mPercentage + 35;

        setAnimation(35, mCurrentAngle, 2000);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        positionX = mWidth - (float) (mRadius * Math.cos((mCurrentAngle) * Math.PI / 180)) - DisplayUtil.dp2px(mContext, 10);
        positionY = mRadius - (float) (mRadius * Math.sin((mCurrentAngle) * Math.PI / 180)) + DisplayUtil.dip2px(mContext, 18);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, mWidth / 2 -(int)Math.ceil(mRadius*0.6), marginTop, mWidth / 2 + (int)Math.ceil(mRadius*0.6), mRadius * 2 + marginTop);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        mWidth= getWidth()/2;
        mRadius =(int)(mWidth/0.9);
        //positionX = mWidth - mRadius-DisplayUtil.dpToPx(mContext,9); // 太阳图片的初始x坐标
//        positionX = mWidth;
//        positionY = mRadius*0.43f; // 太阳图片的初始y坐标

        // 渐变遮罩的画笔
        shadePaint.setColor(mContext.getResources().getColor(R.color.back_white));
        shadePaint.setStyle(Paint.Style.FILL);
        mShadePaint = shadePaint;
//        mSunIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_7_days_sunrise_sunset);
//        mSunIcon = DisplayUtil.bitmapResize(mSunIcon, DisplayUtil.dp2px(mContext, 18), DisplayUtil.dp2px(mContext, 18));
        mPathPaint = pathPaint;

        //第一步：画半圆
        drawSemicircle(canvas);
        //canvas.save();

        //第二步：绘制太阳的初始位置 以及 后面在动画中不断的更新太阳的X，Y坐标来改变太阳图片在视图中的显示
        drawSunPosition(canvas);
        super.onDraw(canvas);
    }

    /**
     * 绘制半圆
     */
    private void drawSemicircle(Canvas canvas) {

        mRectF = new RectF(mWidth-mRadius, marginTop, mWidth+mRadius, 2*mRadius +marginTop );
        mLinePaint.setColor(getResources().getColor(R.color.transparent_bg_0));
//        RectF mfect = new RectF(0,marginTop,mWidth*2,mRadius);
//        canvas.drawRect(mfect,mLinePaint);
        //canvas.drawRect(mRectF,mLinePaint);
//        mRectF = new RectF(mWidth  - mRadius, marginTop, mWidth  + mRadius, mRadius +marginTop );
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setDither(true);//防止抖动
        mLinePaint.setColor(getResources().getColor(R.color.sunccolor));
        mLinePaint.setPathEffect ( new DashPathEffect( new float [ ] { 10, 10 }, 0 ) ) ;
        mLinePaint.setStrokeWidth(3);
        canvas.drawArc(mRectF, -145, 110, false, mLinePaint);
       // canvas.drawLine(0,marginTop,mWidth*2,marginTop,mLinePaint);
    }

    /**
     * 绘制太阳的位置
     */
    private void drawSunPosition(Canvas canvas) {
//        canvas.drawRect(positionX + DisplayUtil.dp2px(mContext, 10), marginTop, mWidth / 2 + mRadius, mRadius * 2 + marginTop, mShadePaint);
//        canvas.drawArc(mRectF, 180+32, 116, false, mPathPaint);
        if(positionX==0 ||positionY == 0){
            positionX = mWidth - (float) (mRadius) - DisplayUtil.dp2px(mContext, 10);
            positionY = mRadius + DisplayUtil.dip2px(mContext, 18);
        }
        Log.d(TAG, "drawSunPosition: "+String.valueOf(positionX)+" "+String.valueOf(positionY));
        canvas.drawBitmap(mSunIcon, positionX, positionY, null);
    }

    /**
     * 绘制底部左右边的日出时间和日落时间
     *
     * @param canvas
     */
    private void drawFont(Canvas canvas) {
        mFontSize = DisplayUtil.sp2px(getContext(), 14);
        mFontColor = getResources().getColor(R.color.suntext);
        mTextPaint.setColor(mFontColor);
        mTextPaint.setTextSize(mFontSize);
        mTimePaint.setColor(getResources().getColor(R.color.black_4));
        mTimePaint.setTextSize(mFontSize);
        String startTime = TextUtils.isEmpty(mStartTime) ? "" : mStartTime;
        String endTime = TextUtils.isEmpty(mEndTime) ? "" : mEndTime;
        String sunrise = "日出";
        String sunset = "日落";

        mTimePaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(sunrise, DisplayUtil.dpToPx(mContext,30) ,   DisplayUtil.dip2px(mContext, 38+12) , mTextPaint);
        canvas.drawText(startTime,   DisplayUtil.dpToPx(mContext,30), DisplayUtil.dip2px(mContext, 63+12), mTimePaint);
        canvas.drawText(sunset, 2*mWidth - DisplayUtil.spToPx(mContext,14) - DisplayUtil.dpToPx(mContext,30), DisplayUtil.dip2px(mContext, 38+12) , mTextPaint);
        canvas.drawText(endTime,mWidth + (int)(0.8*mRadius), DisplayUtil.dip2px(mContext, 63+12) , mTimePaint);
    }

    /**
     * 精确计算文字宽度
     *
     * @param paint 画笔
     * @param str   字符串文本
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 根据日出和日落时间计算出一天总共的时间:单位为分钟
     *
     * @param startTime 日出时间
     * @param endTime   日落时间
     * @return
     */
    private float calculateTime(String startTime, String endTime, boolean isCurrent) {
        String startTimes[] = startTime.split(":");
        String endTimes[] = endTime.split(":");
        float startHour = Float.parseFloat(startTimes[0]);
        float startMinute = Float.parseFloat(startTimes[1]);

        float endHour = Float.parseFloat(endTimes[0]);
        float endMinute = Float.parseFloat(endTimes[1]);

        if (!isCurrent && !isSun && endHour < startHour) {
            endHour += 24;
        }

        if (isSun) {
            if (startHour > endHour) {
                return 0;
            } else if (startHour == endHour && startMinute >= endMinute) {
                return 0;
            }
        } else {
            if (isCurrent) {
                if (startHour > endHour) {
                    return 0;
                } else if (startHour == endHour && startMinute >= endMinute) {
                    return 0;
                }
            } else {
                if (startHour >= endHour + 24) {
                    return 0;
                }
            }
        }

        if (checkTime(startTime, endTime)) {
            return (endHour - startHour - 1) * 60 + (60 - startMinute) + endMinute;
        }
        return 0;
    }

    /**
     * 对所给的时间做一下简单的数据校验
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private boolean checkTime(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)
                || !startTime.contains(":") || !endTime.contains(":")) {
            return false;
        }

        String startTimes[] = startTime.split(":");
        String endTimes[] = endTime.split(":");
        float startHour = Float.parseFloat(startTimes[0]);
        float startMinute = Float.parseFloat(startTimes[1]);

        float endHour = Float.parseFloat(endTimes[0]);
        float endMinute = Float.parseFloat(endTimes[1]);

        //如果所给的时间(hour)小于日出时间（hour）或者大于日落时间（hour）
        if ((startHour < Float.parseFloat(mStartTime.split(":")[0]))
                || (endHour > this.endHour)) {
            return false;
        }

        //如果所给时间与日出时间：hour相等，minute小于日出时间
        if ((startHour == Float.parseFloat(mStartTime.split(":")[0]))
                && (startMinute < Float.parseFloat(mStartTime.split(":")[1]))) {
            return false;
        }

        //如果所给时间与日落时间：hour相等，minute大于日落时间
        if ((startHour == this.endHour)
                && (endMinute > Float.parseFloat(mEndTime.split(":")[1]))) {
            return false;
        }

        if (startHour < 0 || endHour < 0
                || startHour > 23 || endHour > 23
                || startMinute < 0 || endMinute < 0
                || startMinute > 60 || endMinute > 60) {
            return false;
        }
        return true;
    }

    /**
     * 根据具体的时间、日出日落的时间差值 计算出所给时间的百分占比
     *
     * @param totalTime 日出日落的总时间差
     * @param needTime  当前时间与日出时间差
     * @return
     */
    private String formatTime(float totalTime, float needTime) {
        if (totalTime == 0)
            return "0.00";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//保留2位小数，构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(needTime / totalTime);//format 返回的是字符串
    }

    private void setAnimation(float startAngle, float currentAngle, int duration) {
        ValueAnimator sunAnimator = ValueAnimator.ofFloat(startAngle, currentAngle);
        sunAnimator.setDuration(duration);
        sunAnimator.setTarget(currentAngle);
        sunAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //每次要绘制的圆弧角度
                mCurrentAngle = (float) animation.getAnimatedValue();
                invalidateView();
            }

        });
        sunAnimator.start();
    }

    private void invalidateView() {
        //绘制太阳的x坐标和y坐标
        positionX = mWidth - (float) (mRadius * Math.cos((mCurrentAngle) * Math.PI / 180)) - DisplayUtil.dp2px(mContext, 10);
        positionY = mRadius - (float) (mRadius * Math.sin((mCurrentAngle) * Math.PI / 180)) + DisplayUtil.dip2px(mContext, 18);
        invalidate();
    }
}