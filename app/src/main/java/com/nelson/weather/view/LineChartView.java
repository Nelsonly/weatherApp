package com.nelson.weather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.nelson.weather.R;
import com.nelson.weather.WeatherApplication;
import com.nelson.weather.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
    /**原点的Y坐标*/
    private int yPoint = 0;
    /**X的刻度长度，默认值，后面会改写。*/
    private int xScale = 55;
    /**Y的刻度长度，默认值，后面会改写。*/
    private int yScale = 40;
    /**X轴的默认长度*/
    private int xLength = 300;
    /**Y的刻度*/
    private List<String> yLabel =new ArrayList<>();
    /**数据*/
    private List<String> data =new ArrayList<>();
    /**折线的颜色*/
    private int lineColor;
    /**圆点的半径*/
    private int radius = 0;
    private int dataSize =0;

    private final Context context = WeatherApplication.getContext();

    public LineChartView(Context context) {
        super(context);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
//        YLableSize=a.getInt(YLabel.size(),0);
//        DataSize=a.getInt(Data.size(),0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 传入折线Y轴的值,传入X轴的各点的数据
     *
     * @param yLabels Y轴原点值和最大值
     * @param allData 数据
     */
    public void setInfo(List<String> yLabels, List<String> allData) {
        //初始化数据
        //外部传入的Y轴刻度数据
        yLabel = yLabels;
        //外部传入的折线点数据
        data = allData;
        int yLableSize = yLabels.size();
        dataSize =allData.size();
        Log.d("tag", yLabel.toString());

        invalidate();
    }

    /**
     * 设置折线的颜色
     *
     * @param lineColor
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * 设置折线的线宽
     *
     * @param lineWidth
     */
    public void setLineWidth(int lineWidth) {
        /**折线线宽*/
    }

    /**
     * 设置折线圆点的半径
     *
     * @param radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        yPoint = getHeight() - DisplayUtil.dpToPx(context, 5);
        xLength = right;
        /**Y轴的默认长度*/
        int yLength = yPoint;
        if(data.size()>0){
            //实际X轴的刻度长度,改写后的真实值
            xScale = xLength / dataSize;
        }else {
            //实际X轴的刻度长度,改写后的真实值
            xScale = xLength;
        }
        //实际Y轴的刻度长度,改写后的真实值
        yScale = yLength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6f);
        paint.setAntiAlias(true);
        //颜色
        paint.setColor(lineColor);
        paint.setShadowLayer(10, 0, 30, 0xff4287ed);
        for (int i = 0; i * xScale < xLength; i++) {
            try {
                /**原点的X坐标*/
                int xPoint = 50;
                canvas.drawCircle(xPoint + i * xScale, yCoord(data.get(i)), DisplayUtil.dpToPx(context, radius), paint);
                // 数据值,保证有效数据
                if (i >= 0 && yCoord(data.get(i - 1)) != -999 && yCoord(data.get(i)) != -999) {
                    canvas.drawLine(xPoint + (i - 1) * xScale + radius + 4,
                            yCoord(data.get(i - 1)), xPoint + i * xScale - radius - 4,
                            yCoord(data.get(i)), paint);
                }
            } catch (Exception e) {
            }
        }

    }

    private int yCoord(String y0)  //计算绘制时的Y坐标，无数据时返回-999
    {
        int y;
        try {
            y = Integer.parseInt(y0);
        } catch (Exception e) {
            //出错则返回-999
            return -999;
        }
        try {
            return yPoint - y * yScale / Integer.parseInt(yLabel.get(1));
        } catch (Exception e) {
        }
        return y;
    }
}
