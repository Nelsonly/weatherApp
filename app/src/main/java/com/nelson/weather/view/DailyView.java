package com.nelson.weather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nelson.weather.R;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.utils.DateUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class DailyView extends HorizontalScrollView {

    LinearLayout llRoot;

    private List<DailyResponse.DailyBean> list;
    private List<MoreAirFiveResponse.DailyBean> airList;
    private Paint dayPaint;
    private Paint dayPaintDash;
    private Paint nightPaint;
    private Paint nightPaintDash;

    protected Path pathDay;
    protected Path pathNight;

    private float lineWidth = 6f;
    private int pointRadius = 10;
    private float shadowRadius = 6f;
    private int hollow = 0;

    private int dayLineColor = 0xff78ad23;
    private int dayLineShadowColor = 0xff78ad23;
    private int nightLineColor = 0xff23acb3;
    private int nightLineShadowColor = 0xff23acb3;

    private float[] interval = {20f, 20f};

    private int columnNumber = 6;

    private int selected = -1;
    float eventX;
    float eventY;
    float moveX;
    float moveY;

    private OnWeatherItemClickListener weatherItemClickListener;

    public DailyView(Context context) {
        this(context, null);
    }

    public DailyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DailyView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        dayPaint = new Paint();
        dayPaint.setColor(dayLineColor);
        dayPaint.setAntiAlias(true);
        dayPaint.setStrokeWidth(lineWidth);
        dayPaint.setStyle(Paint.Style.STROKE);
        dayPaint.setShadowLayer(10,0,30, dayLineShadowColor);

        dayPaintDash = new Paint();
        dayPaintDash.setColor(dayLineColor);
        dayPaintDash.setAntiAlias(true);
        dayPaintDash.setStrokeWidth(lineWidth);
        dayPaintDash.setStyle(Paint.Style.STROKE);
        dayPaintDash.setPathEffect(new DashPathEffect(interval, 0));

        nightPaint = new Paint();
        nightPaint.setColor(nightLineColor);
        nightPaint.setAntiAlias(true);
        nightPaint.setStrokeWidth(lineWidth);
        nightPaint.setStyle(Paint.Style.STROKE);
        nightPaint.setShadowLayer(10,0,30, nightLineShadowColor);

        nightPaintDash = new Paint();
        nightPaintDash.setColor(nightLineColor);
        nightPaintDash.setAntiAlias(true);
        nightPaintDash.setStrokeWidth(lineWidth);
        nightPaintDash.setStyle(Paint.Style.STROKE);
        nightPaintDash.setPathEffect(new DashPathEffect(interval, 0));

        pathDay = new Path();
        pathNight = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getChildCount() > 0) {  //=1，水平的LinearLayout
            ViewGroup root = (ViewGroup) getChildAt(0);

            if (root.getChildCount() > 0) { //column数量

                float prevDx = 0f;
                float prevDy = 0f;
                float curDx = 0f;
                float curDy = 0f;
                float prevDx1 = 0f;
                float prevDy1 = 0f;
                float curDx1 = 0f;
                float curDy1 = 0f;
                float intensity = 0.16f;

                DailyItemView c = (DailyItemView) root.getChildAt(0);;
                int dX = c.getTempX();
                int dY = c.getTempY();
                int nX = c.getTempX();
                int nY = c.getTempY();

                TemperatureView tv = (TemperatureView) c.findViewById(R.id.ttv_day);

                tv.setRadius(pointRadius);

                int x0 = (int) (dX + tv.getxPointDay());
                int y0 = (int) (dY + tv.getyPointDay());
                int x = (int) (nX + tv.getxPointNight());
                int y = (int) (nY + tv.getyPointNight());

                pathDay.reset();
                pathNight.reset();

                pathDay.moveTo(x0, y0);
                pathNight.moveTo(x, y);

                int i = 0;
                if (list.size()==16) {  //虚线
                    DailyItemView child1 = (DailyItemView) root.getChildAt(i + 1);//下一天
                    int dayX1 = child1.getTempX() + child1.getWidth() * (i + 1);
                    int dayY1 = child1.getTempY();
                    int nightX1 = child1.getTempX() + child1.getWidth() * (i + 1);
                    int nightY1 = child1.getTempY();

                    TemperatureView tempV1 = (TemperatureView) child1.findViewById(R.id.ttv_day);

                    tempV1.setRadius(pointRadius);

                    int x11 = (int) (dayX1 + tempV1.getxPointDay());
                    int y11 = (int) (dayY1 + tempV1.getyPointDay());
                    int x22 = (int) (nightX1 + tempV1.getxPointNight());
                    int y22 = (int) (nightY1 + tempV1.getyPointNight());

                    pathDay.lineTo(x11, y11);
                    pathNight.lineTo(x22, y22);

                    canvas.drawPath(pathDay, dayPaintDash);
                    canvas.drawPath(pathNight, nightPaintDash);

                    pathDay.reset();
                    pathNight.reset();
                    pathDay.moveTo(x11, y11);
                    pathNight.moveTo(x22, y22);

                    i++;
                }

                //折线
                for (; i < root.getChildCount() - 1; i++) {
                    DailyItemView child = (DailyItemView) root.getChildAt(i);//当前天
                    DailyItemView child1 = (DailyItemView) root.getChildAt(i + 1);//下一天
                    int dayX = child.getTempX() + child.getWidth() * i;
                    int dayY = child.getTempY();
                    int nightX = child.getTempX() + child.getWidth() * i;
                    int nightY = child.getTempY();
                    int dayX1 = child1.getTempX() + child1.getWidth() * (i + 1);
                    int dayY1 = child1.getTempY();
                    int nightX1 = child1.getTempX() + child1.getWidth() * (i + 1);
                    int nightY1 = child1.getTempY();

//                    Log.e("draw", "i=" + i + ", day x=" + dayX + ", day y=" + dayY + ", night x=" + nightX + ", night y=" + nightY);
//                    Log.e("draw", "i=" + i + ", day x1=" + dayX1 + ", day y1=" + dayY1 + ", night x1=" + nightX1 + ", night y1=" + nightY1);

                    TemperatureView tempV = (TemperatureView) child.findViewById(R.id.ttv_day);
                    TemperatureView tempV1 = (TemperatureView) child1.findViewById(R.id.ttv_day);

                    tempV.setRadius(pointRadius);
                    tempV1.setRadius(pointRadius);

                    int x1 = (int) (dayX + tempV.getxPointDay());
                    int y1 = (int) (dayY + tempV.getyPointDay());
                    int x2 = (int) (nightX + tempV.getxPointNight());
                    int y2 = (int) (nightY + tempV.getyPointNight());

                    int x11 = (int) (dayX1 + tempV1.getxPointDay());
                    int y11 = (int) (dayY1 + tempV1.getyPointDay());
                    int x22 = (int) (nightX1 + tempV1.getxPointNight());
                    int y22 = (int) (nightY1 + tempV1.getyPointNight());
//                    Log.e("draw", "x1=" + x1 + ",y1=" + y1 + ",x11=" + x11 + ",y11=" + y11);

                    pathDay.lineTo(x11, y11);
                    pathNight.lineTo(x22, y22);
                }

                canvas.drawPath(pathDay, dayPaint);
                canvas.drawPath(pathNight, nightPaint);
            }
        }
    }


    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        dayPaint.setStrokeWidth(lineWidth);
        dayPaintDash.setStrokeWidth(lineWidth);
        nightPaint.setStrokeWidth(lineWidth);
        nightPaintDash.setStrokeWidth(lineWidth);
        invalidate();
    }

    public void setDayAndNightLineColor(int dayColor, int nightColor, int dayShadowColor, int nightShadowColor) {
        this.dayLineColor = dayColor;
        this.nightLineColor = nightColor;
        this.dayLineShadowColor = dayShadowColor;
        this.nightLineShadowColor = nightShadowColor;
        dayPaint.setColor(dayLineColor);
        dayPaintDash.setColor(dayLineColor);
        nightPaint.setColor(nightLineColor);
        nightPaintDash.setColor(nightLineColor);
        dayPaint.setShadowLayer(shadowRadius,0,30, dayLineShadowColor);
        nightPaint.setShadowLayer(shadowRadius,0,30, nightLineShadowColor);
        dayPaintDash.setShadowLayer(shadowRadius,0,30, dayLineShadowColor);
        nightPaintDash.setShadowLayer(shadowRadius,0,30, nightLineShadowColor);
        invalidate();
    }

    public List<DailyResponse.DailyBean> getList() {
        return list;
    }

    public void setOnWeatherItemClickListener(OnWeatherItemClickListener weatherItemClickListener) {
        this.weatherItemClickListener = weatherItemClickListener;
    }

    public void setList(final List<DailyResponse.DailyBean> list) {
        this.list = list;
//        找到折线图中的最高/最低点
        int screenWidth = getScreenWidth();
        int maxDay = getMaxDayTemp(list);
        int maxNight = getMaxNightTemp(list);
        int minDay = getMinDayTemp(list);
        int minNight = getMinNightTemp(list);
        int max = Math.max(maxDay, maxNight);
        int min = Math.min(minDay, minNight);

        removeAllViews();
//        创建水平LinearLayout
        llRoot = new LinearLayout(getContext());
        llRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llRoot.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < list.size(); i++) {
            DailyResponse.DailyBean bean = list.get(i);
            final DailyItemView itemView = new DailyItemView(getContext());
            itemView.setDayPointColor(dayLineColor);
            itemView.setNightPointColor(nightLineColor);
            itemView.setMaxTemp(max);
            itemView.setMinTemp(min);
            itemView.setDate(DateUtils.dateSplit(bean.getFxDate()));
            itemView.setWeek(DateUtils.Week(bean.getFxDate()));
            itemView.setDayTemp(Integer.parseInt(bean.getTempMax()));
            itemView.setDayWeather(bean.getTextDay());
            itemView.setIcon(Integer.parseInt(bean.getIconDay()), true);
            itemView.setIcon(Integer.parseInt(bean.getIconNight()), false);
            itemView.setNightWeather(bean.getTextNight());
            itemView.setNightTemp(Integer.parseInt(bean.getTempMin()));
            itemView.setWindOri(bean.getWindDirDay());
            itemView.setWindLevel(bean.getWindScaleDay());
//            itemView.setAirLevel("优");
            itemView.setHollow(hollow);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / columnNumber, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemView.setClickable(true);

            final int finalI = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (weatherItemClickListener != null) {
                        clearSelected();
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
                            itemView.setBackgroundResource(R.drawable.bg_selected);
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

        if (list.size()==16) {
            ((DailyItemView) llRoot.getChildAt(0)).setGray();
        }

        addView(llRoot);
        invalidate();
    }

    public void setAir(List<MoreAirFiveResponse.DailyBean> air) {
        this.airList = air;
        if (getChildCount() > 0) {
            ViewGroup root = (ViewGroup) getChildAt(0);
            if (root.getChildCount() > 0) {
                if (list.size() != air.size()) {
                    int n = Math.min(list.size(), air.size());
                    for (int i=0; i < n; i++) {
                        DailyItemView child = (DailyItemView) root.getChildAt(i);
                        child.setAirLevel(airList.get(i).getCategory());
                    }
                } else {
                    for (int i=0; i < air.size(); i++) {
                        DailyItemView child = (DailyItemView) root.getChildAt(i);
                        child.setAirLevel(airList.get(i).getCategory());
                    }
                }
            }
        }
    }

    public void setColumnNumber(int num) throws Exception {
        if (num > 2) {
            this.columnNumber = num;
            setList(this.list);
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

    private int getMinDayTemp(List<DailyResponse.DailyBean> list) {
        if (list != null) {
            return Integer.parseInt(Collections.min(list, new DayTempComparator()).getTempMax());
        }
        return 0;
    }

    private int getMinNightTemp(List<DailyResponse.DailyBean> list) {
        if (list != null) {
            return Integer.parseInt(Collections.min(list, new NightTempComparator()).getTempMin());
        }
        return 0;
    }

    private int getMaxNightTemp(List<DailyResponse.DailyBean> list) {
        if (list != null) {
            return Integer.parseInt(Collections.max(list, new NightTempComparator()).getTempMin());
        }
        return 0;
    }

    private int getMaxDayTemp(List<DailyResponse.DailyBean> list) {
        if (list != null) {
            return Integer.parseInt(Collections.max(list, new DayTempComparator()).getTempMax());
        }
        return 0;
    }

    public void clearSelected() {
        for (int i=0; i<llRoot.getChildCount(); i++)
            llRoot.getChildAt(i).setBackgroundResource(0);
        selected = -1;
        eventX = 0f;
        eventY = 0f;
        moveX = 0f;
        moveY = 0f;
    }

    public void setHollow(int hollow) {
        this.hollow = hollow;
    }

    private static class DayTempComparator implements Comparator<DailyResponse.DailyBean> {
        @Override
        public int compare(DailyResponse.DailyBean o1, DailyResponse.DailyBean o2) {
            if (o1.getTempMax() == o2.getTempMax()) {
                return 0;
            } else if (Integer.parseInt(o1.getTempMax()) > Integer.parseInt(o2.getTempMax())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private static class NightTempComparator implements Comparator<DailyResponse.DailyBean> {
        @Override
        public int compare(DailyResponse.DailyBean o1, DailyResponse.DailyBean o2) {
            if (o1.getTempMin() == o2.getTempMin()) {
                return 0;
            } else if (Integer.parseInt(o1.getTempMin()) > Integer.parseInt(o2.getTempMin())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public interface OnWeatherItemClickListener {
        void onItemClick(DailyItemView itemView, int position, DailyResponse.DailyBean bean);
    }

}
