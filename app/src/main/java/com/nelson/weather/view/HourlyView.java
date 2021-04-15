package com.nelson.weather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nelson.weather.R;
import com.nelson.weather.bean.HourlyResponse;
import com.nelson.weather.utils.DateUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HourlyView extends HorizontalScrollView {
    private Context context;
    LinearLayout llRoot;

    private List<HourlyResponse.HourlyBean> list;
    private String air = "优";
    private Paint dayPaint;
    private Paint shadePaint;

    protected Path pathDay;
    private Path pathShade;

    private float lineWidth = 6f;
    private int pointRadius = 10;
    private int hollow = 0;

    private int dayLineColor = 0xff78ad23;
    private int dayLineShadowColor = 0xff78ad23;

    private int columnNumber = 5;

    private int selected = -1;
    float eventX;
    float eventY;
    float moveX;
    float moveY;

    private OnWeatherItemClickListener weatherItemClickListener;

    public HourlyView(Context context) {
        this(context, null);
    }

    public HourlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HourlyView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        dayPaint = new Paint();
        dayPaint.setColor(dayLineColor);
        dayPaint.setAntiAlias(true);
        dayPaint.setStrokeWidth(lineWidth);
        dayPaint.setStyle(Paint.Style.STROKE);
        dayPaint.setShadowLayer(10,0,30, dayLineShadowColor);

        shadePaint = getShadePaint();

        pathDay = new Path();
        pathShade = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getChildCount() > 0) {  //=1，水平的LinearLayout
            ViewGroup root = (ViewGroup) getChildAt(0);

            if (root.getChildCount() > 0) { //column数量
                HourlyItemView c = (HourlyItemView) root.getChildAt(0);
                int dX = c.getTempX();
                int dY = c.getTempY();

                TemperatureView tv = (TemperatureView) c.findViewById(R.id.ttv_hourly);

                tv.setRadius(pointRadius);

                int x0 = (int) (dX + tv.getxPointDay());
                int y0 = (int) (dY + tv.getyPointDay());

                pathDay.reset();

                pathDay.moveTo(x0, y0);

                //折线
                for (int i = 0; i < root.getChildCount() - 1; i++) {
                    HourlyItemView child = (HourlyItemView) root.getChildAt(i);//当前天
                    HourlyItemView child1 = (HourlyItemView) root.getChildAt(i + 1);//下一天
                    int dayX = child.getTempX() + child.getWidth() * i;
                    int dayY = child.getTempY();
                    int dayX1 = child1.getTempX() + child1.getWidth() * (i + 1);
                    int dayY1 = child1.getTempY();

                    TemperatureView tempV = (TemperatureView) child.findViewById(R.id.ttv_hourly);
                    TemperatureView tempV1 = (TemperatureView) child1.findViewById(R.id.ttv_hourly);

                    tempV.setRadius(pointRadius);
                    tempV1.setRadius(pointRadius);

                    int x1 = (int) (dayX + tempV.getxPointDay());
                    int y1 = (int) (dayY + tempV.getyPointDay());

                    int x11 = (int) (dayX1 + tempV1.getxPointDay());
                    int y11 = (int) (dayY1 + tempV1.getyPointDay());
//                    Log.e("draw", "i=" + i + "x1=" + x1 + ",y1=" + y1 + ",x11=" + x11 + ",y11=" + y11);

                    canvas.drawLine(x1, y1, x11, y11, dayPaint);
                }

                canvas.drawPath(pathShade, shadePaint);

            }
        }
    }


    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        dayPaint.setStrokeWidth(lineWidth);
        invalidate();
    }

    public void setLineColor(int color, int shadowColor) {
        this.dayLineColor = color;
        dayPaint.setColor(color);
        this.dayLineShadowColor = shadowColor;
        dayPaint.setShadowLayer(10,0,30, shadowColor);
        invalidate();
    }

    public List<HourlyResponse.HourlyBean> getList() {
        return list;
    }

    public void setOnWeatherItemClickListener(OnWeatherItemClickListener weatherItemClickListener) {
        this.weatherItemClickListener = weatherItemClickListener;
    }

    public void setList(final List<HourlyResponse.HourlyBean> list) {
        this.list = list;
//        找到折线图中的最高/最低点
        int screenWidth = getCardWidth()+200;
//        Log.e("gqc", "setList: screenWidth = "+screenWidth );
        int max = getMaxTemp(list);
        int min = getMinTemp(list);

        removeAllViews();
//        创建水平LinearLayout
        llRoot = new LinearLayout(getContext());
        llRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llRoot.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < list.size(); i++) {
            HourlyResponse.HourlyBean bean = list.get(i);
            final HourlyItemView itemView = new HourlyItemView(getContext());
            itemView.setPointColor(dayLineColor);
            itemView.setMaxTemp(max);
            itemView.setMinTemp(min);
            if (i==0) {
                itemView.setTime("现在");
            }
            else {
                itemView.setTime(DateUtils.updateTime(bean.getFxTime()));
                itemView.setTimeGray();
                itemView.setHollow(hollow);
            }
            itemView.setIcon(Integer.parseInt(bean.getIcon()));
            itemView.setWeather(bean.getText());
            itemView.setTemp(Integer.parseInt(bean.getTemp()));
            itemView.setWindOri(bean.getWindDir());
            itemView.setWindLevel(bean.getWindScale());
            itemView.setAirLevel(air);

            itemView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / columnNumber, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemView.setClickable(true);
            final int finalI = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (weatherItemClickListener != null) {
                        weatherItemClickListener.onItemClick(itemView, finalI, list.get(finalI));
                    }
                }
            });
            itemView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            clearSelected();
                            eventX = event.getX();
                            eventY = event.getY();
                            selected = finalI;
                            drawShade(v, finalI);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            moveX = event.getX();
                            moveY = event.getY();
                            if (Math.abs(moveY - eventY) > Math.abs(moveX - eventX)) {  //竖直滑动
                                clearSelected();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                    return false;
                }
            });
            llRoot.addView(itemView);
        }
        addView(llRoot);
        invalidate();
    }

    public void setAir(String air) {
        this.air = air;
        if (getChildCount() > 0) {
            ViewGroup root = (ViewGroup) getChildAt(0);
            if (root.getChildCount() > 0) {
                for (int i = 0; i < root.getChildCount(); i++) {
                    HourlyItemView child = (HourlyItemView) root.getChildAt(i);
                    child.setAirLevel(air);
                }
            }
        }
    }

    public void setColumnNumber(int num) throws Exception {
        if (num > 2) {
            this.columnNumber = num;
            if (list != null) setList(this.list);
            if (air != null) setAir(this.air);
        } else {
            throw new Exception("ColumnNumber should lager than 2");
        }
    }

    public void setPointRadius (int r) {
        this.pointRadius = r;
        invalidate();
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private int getCardWidth() {
        View fl = LayoutInflater.from(context).inflate(R.layout.item_forecast_24hr, null);
        View view = fl.findViewById(R.id.card_hourly);
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        fl.measure(w,h);
        return fl.getMeasuredWidth();
    }

    private int getMinTemp(List<HourlyResponse.HourlyBean> list) {
        if (list != null) {
            return Integer.parseInt(Collections.min(list, new TempComparator()).getTemp());
        }
        return 0;
    }

    private int getMaxTemp(List<HourlyResponse.HourlyBean> list) {
        if (list != null) {
            return Integer.parseInt(Collections.max(list, new TempComparator()).getTemp());
        }
        return 0;
    }

    private static class TempComparator implements Comparator<HourlyResponse.HourlyBean> {
        @Override
        public int compare(HourlyResponse.HourlyBean o1, HourlyResponse.HourlyBean o2) {
            if (o1.getTemp() == o2.getTemp()) {
                return 0;
            } else if (Integer.parseInt(o1.getTemp()) > Integer.parseInt(o2.getTemp())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public interface OnWeatherItemClickListener {
        void onItemClick(HourlyItemView itemView, int position, HourlyResponse.HourlyBean bean);
    }

    private void drawShade(View v, int i) {

        shadePaint = getShadePaint();
        pathShade.reset();

        int[] now = getPointLocation(i);

        int[] pre, post;
        if (i==0) {
            post = getPointLocation(i+1);
            pre = new int[]{v.getLeft(), 2*now[1] - post[1]};
        }
        else if (i == list.size()-1) {
            pre = getPointLocation(i-1);
            post = new int[]{v.getRight(), 2*now[1] - pre[1]};
        } else {
            pre = getPointLocation(i-1);
            post = getPointLocation(i+1);
        }

        int[] left = new int[] { (pre[0] + now[0])/2, (pre[1] + now[1])/2 };
        int[] right = new int[] { (post[0] + now[0])/2, (post[1] + now[1])/2 };

        pathShade.moveTo(left[0], left[1]);
        pathShade.lineTo(now[0], now[1]);
        pathShade.lineTo(right[0], right[1]);
        pathShade.lineTo(right[0], 500);
        pathShade.lineTo(left[0], 500);
        pathShade.lineTo(left[0], left[1]);

        postInvalidate();

    }

    private int[] getPointLocation(int i) {
        if (i<0 || i>=list.size()) return new int[]{0, 0};

        ViewGroup root = (ViewGroup) getChildAt(0);
        HourlyItemView child = (HourlyItemView) root.getChildAt(i);
        TemperatureView tempV = (TemperatureView) child.findViewById(R.id.ttv_hourly);
        int dayX = child.getTempX() + child.getWidth() * i;
        int dayY = child.getTempY();
        int x1 = (int) (dayX + tempV.getxPointDay());
        int y1 = (int) (dayY + tempV.getyPointDay());

        return new int[]{x1, y1};
    }

    private Paint getShadePaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        Shader mShader = new LinearGradient(300, 0, 300, 500,
                new int[] { dayLineColor, Color.TRANSPARENT }, null, Shader.TileMode.CLAMP);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        paint.setShader(mShader);
        paint.setAlpha(70);

        return paint;
    }


    public void clearSelected() {
        shadePaint.reset();
        shadePaint.setColor(Color.TRANSPARENT);
        invalidate();

        selected = -1;
        eventX = 0f;
        eventY = 0f;
        moveX = 0f;
        moveY = 0f;
    }

    public void setHollow(int hollow) {
        this.hollow = hollow;
    }
}
