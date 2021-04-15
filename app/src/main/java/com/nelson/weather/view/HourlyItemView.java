package com.nelson.weather.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nelson.weather.R;
import com.nelson.weather.utils.WeatherUtil;


public class HourlyItemView extends LinearLayout {

    private View rootView;
    private TextView tvTime;
    private TemperatureView ttvTemp;
    private ImageView ivWeather;
    private TextView tvWeather;
    private TextView tvTemp;
    private TextView tvWindOri;
    private TextView tvWindLevel;
    private ImageView ivAir;

    public HourlyItemView(Context context) {
        this(context, null);
    }

    public HourlyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HourlyItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rootView = LayoutInflater.from(context).inflate(R.layout.item_hourly_index, null);
        tvTime = (TextView) rootView.findViewById(R.id.tv_time);
        ttvTemp = ((TemperatureView) rootView.findViewById(R.id.ttv_hourly)).setPointNum(1);
        ivWeather = (ImageView) rootView.findViewById(R.id.iv_weather);
        tvWeather = (TextView) rootView.findViewById(R.id.tv_weather);
        tvTemp = (TextView) rootView.findViewById(R.id.tv_temp);
        tvWindOri = (TextView) rootView.findViewById(R.id.tv_wind_ori);
        tvWindLevel = (TextView) rootView.findViewById(R.id.tv_wind_level);
        ivAir = (ImageView) rootView.findViewById(R.id.iv_air);
        rootView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(rootView);
    }

    public void setTime(String time) {
        if (tvTime != null)
            tvTime.setText(time);
    }

    /**
     * The visual x position of TemperatureView, in pixels
     * @return
     */
    public int getTempX() {
        if (ttvTemp != null)
            return (int) ttvTemp.getX();
        return 0;
    }

    public int getTempY() {
        if (ttvTemp != null)
            return (int) ttvTemp.getY();
        return 0;
    }

    public void setWeather(String weather) {
        if (tvWeather != null)
            tvWeather.setText(weather);
    }

    public void setWindOri(String windOri) {
        if (tvWindOri != null)
            tvWindOri.setText(windOri);
    }

    public void setWindLevel(String windLevel) {
        if (tvWindLevel != null)
            tvWindLevel.setText(windLevel + "级");
    }

    public void setAirLevel(String airLevel) {
        if (ivAir != null) {
            switch (airLevel) {
                case "优":
                    ivAir.setImageResource(R.drawable.block_1);
                    break;
                case "良":
                    ivAir.setImageResource(R.drawable.block_2);
                    break;
                case "轻度污染":
                    ivAir.setImageResource(R.drawable.block_3);
                    break;
                case "中度污染":
                    ivAir.setImageResource(R.drawable.block_4);
                    break;
                case "重度污染":
                    ivAir.setImageResource(R.drawable.block_5);
                    break;
                case "严重污染":
                    ivAir.setImageResource(R.drawable.block_6);
                    break;
            }
        }
    }

    public void setTemp(int temp) {
        if (ttvTemp != null)
            ttvTemp.setTemperatureDay(temp);
        if (tvTemp != null)
            tvTemp.setText(temp + "°");
    }

    public void setIcon(int code) {
        if (ivWeather != null) {
            WeatherUtil.changeIcon(ivWeather, code);
        }
    }

    public void setMaxTemp(int max) {
        if (ttvTemp != null)
            ttvTemp.setMaxTemp(max);
    }

    public void setMinTemp(int min) {
        if (ttvTemp != null) {
            ttvTemp.setMinTemp(min);
        }
    }

    public void setTimeGray() {
        if (tvTime != null) {
            tvTime.setTextColor(0xffa7a7a7);
        }
    }

    public void setPointColor(int color) {
        if (ttvTemp != null) {
            ttvTemp.setPointColorDay(color);
        }
    }

    public void setHollow(int hollow) {
        ttvTemp.setHollow(hollow);
    }
}
