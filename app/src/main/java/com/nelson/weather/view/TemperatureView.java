package com.nelson.weather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class TemperatureView extends View {
    private int pointNum;
    public static final int DAY = 0;
    public static final int NIGHT = 1;

    private int maxTemp;    //整个折线图中的最高点
    private int minTemp;

    private int[] temperature = new int[2]; //温度

    private Paint[] pointPaint = new Paint[2];
    private Paint whitePaint;
    private Paint linePaint;
    private Paint textPaint;
    private int lineColor;
    private int[] pointColor = new int[2];
    private int textColor;

    private int radius = 10;
    private int rWhite = 0;
//    private int textSize = 40;
    private int padding = radius * 2;

    private int[] xPoint = new int[2];
    private int[] yPoint = new int[2];
    private int mWidth;

    public TemperatureView(Context context) {
        this(context, null);
    }

    public TemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        lineColor = 0xff93a122;
//        textColor = 0x22222222;
        pointColor[DAY] = lineColor;
        pointColor[NIGHT] = lineColor;
    }

    private void initPaint(Context context, AttributeSet attrs) {
        linePaint = new Paint();
        textPaint = new Paint();

        linePaint.setColor(lineColor);
        for (int i=0; i<2; i++) {
            pointPaint[i] = new Paint();
            pointPaint[i].setColor(pointColor[i]);
            pointPaint[i].setAntiAlias(true);
        }

        whitePaint = new Paint();
        whitePaint.setColor(Color.parseColor("#FFFFFF"));
        whitePaint.setAntiAlias(true);
//        textPaint.setColor(textColor);
//        textPaint.setTextSize(textSize);
//        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPoint(canvas);
//        drawText(canvas);
    }

    private void drawPoint(Canvas canvas) {
//        int height = getHeight() - textSize * 4;
        int height = getHeight() - padding * 2;
        mWidth = getWidth();
        for (int i=0; i<pointNum; i++) {
            int x = getWidth() / 2;
//        int y = (int) (height - height * (temperatureDay - minTemp) * 1.0f / (maxTemp - minTemp)) + textSize * 2;
            int y = (int) (height - height * (temperature[i] - minTemp) * 1.0f / (maxTemp - minTemp)) + padding;
            xPoint[i] = x;
            yPoint[i] = y;
            canvas.drawCircle(x, y, radius, pointPaint[i]);
            canvas.drawCircle(x, y, rWhite, whitePaint);
        }
    }

    private void drawText(Canvas canvas) {
//        int height = getHeight() - textSize * 4;
//        int yDay = (int) (height - height * (temperatureDay - minTemp) * 1.0f / (maxTemp - minTemp)) + textSize * 2;
//        int yNight = (int) (height - height * (temperatureNight - minTemp) * 1.0f / (maxTemp - minTemp)) + textSize * 2;
//        String dayTemp = temperatureDay + "°";
//        String nightTemp = temperatureNight + "°";
//        float widDay = textPaint.measureText(dayTemp);
//        float widNight = textPaint.measureText(nightTemp);
//        float hei = textPaint.descent() - textPaint.ascent();
//        canvas.drawText(dayTemp, getWidth() / 2 - widDay / 2, yDay - radius - hei / 2, textPaint);
//        canvas.drawText(nightTemp, getWidth() / 2 - widNight / 2, yNight + radius + hei, textPaint);
    }

    public int getPointNum() {
        return pointNum;
    }

    public TemperatureView setPointNum(int pointNum) {
        this.pointNum = pointNum;
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getxPointDay() {
        return xPoint[DAY];
    }

    public int getyPointDay() {
        return yPoint[DAY];
    }

    public void setxPointDay(int xPointDay) {
        this.xPoint[DAY] = xPointDay;
    }

    public void setyPointDay(int yPointDay) {
        this.yPoint[DAY] = yPointDay;
    }

    public int getxPointNight() {
        return xPoint[NIGHT];
    }

    public void setxPointNight(int xPointNight) {
        this.xPoint[NIGHT] = xPointNight;
    }

    public int getyPointNight() {
        return yPoint[NIGHT];
    }

    public void setyPointNight(int yPointNight) {
        this.yPoint[NIGHT] = yPointNight;
    }

    public int getmWidth() {
        return mWidth;
    }

    public int getTemperatureDay() {
        return temperature[DAY];
    }

    public void setTemperatureDay(int temperatureDay) {
        this.temperature[DAY] = temperatureDay;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getPointColorDay() {
        return pointColor[DAY];
    }

    public void setPointColorDay(int color) {
        this.pointColor[DAY] = color;
        pointPaint[DAY].setColor(color);
        invalidate();
    }

    public void setPointColorNight(int color) {
        this.pointColor[NIGHT] = color;
        pointPaint[NIGHT].setColor(color);
        invalidate();
    }

//    public int getTextColor() {
//        return textColor;
//    }
//
//    public void setTextColor(int textColor) {
//        this.textColor = textColor;
//    }

    public int getTemperatureNight() {
        return temperature[NIGHT];
    }

    public void setTemperatureNight(int temperatureNight) {
        this.temperature[NIGHT] = temperatureNight;
    }

    /**
     * 计算白色圆的半径
     * @param diff 空心圆的粗细
     * @return
     */
    public TemperatureView setHollow(int diff) {
        this.rWhite = radius - diff;
        invalidate();
        return this;
    }
}
