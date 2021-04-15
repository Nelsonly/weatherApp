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


public class DailyItemView extends LinearLayout {

    private View rootView;
    private TextView tvWeek;
    private TextView tvDate;
    private TextView tvAir;
    private TextView tvDayWeather;
    private TextView tvNightWeather;
    private TextView tvDayTemp;
    private TextView tvNightTemp;
    private TemperatureView ttvTemp;
    private TextView tvWindOri;
    private TextView tvWindLevel;
    private ImageView ivDayWeather;
    private ImageView ivNightWeather;

    public DailyItemView(Context context) {
        this(context, null);
    }

    public DailyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DailyItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rootView = LayoutInflater.from(context).inflate(R.layout.item_daily_index, null);
        tvWeek = (TextView) rootView.findViewById(R.id.tv_week);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        tvDayWeather = (TextView) rootView.findViewById(R.id.tv_day_weather);
        tvDayTemp = (TextView) rootView.findViewById(R.id.tv_day_temp);
        tvNightTemp = (TextView) rootView.findViewById(R.id.tv_night_temp);
        tvNightWeather = (TextView) rootView.findViewById(R.id.tv_night_weather);
        ttvTemp = ((TemperatureView) rootView.findViewById(R.id.ttv_day)).setPointNum(2).setHollow(5);
        tvWindOri = (TextView) rootView.findViewById(R.id.tv_wind_ori);
        tvWindLevel = (TextView) rootView.findViewById(R.id.tv_wind_level);
        ivDayWeather = (ImageView) rootView.findViewById(R.id.iv_day_weather);
        ivNightWeather = (ImageView) rootView.findViewById(R.id.iv_night_weather);
        tvAir = (TextView) rootView.findViewById(R.id.tv_air);
        rootView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(rootView);
    }

    public void setWeek(String week) {
        if (tvWeek != null) {
            tvWeek.setText(week);
//            if (week == "今天") {
//                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/inter_medium.otf");
//                tvWeek.setTypeface(typeface);
//            }
        }
    }

    public void setDate(String date) {
        if (tvDate != null)
            tvDate.setText(date);
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

    public void setDayWeather(String dayWeather) {
        if (tvDayWeather != null)
            tvDayWeather.setText(dayWeather);
    }

    public void setNightWeather(String nightWeather) {
        if (tvNightWeather != null)
            tvNightWeather.setText(nightWeather);
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
        if (tvAir != null) {
            tvAir.setText(WeatherUtil.getAirText(airLevel));
            switch (airLevel) {
                case "优":
                    tvAir.setBackgroundResource(R.drawable.air_bg_1);
                    break;
                case "良":
                    tvAir.setBackgroundResource(R.drawable.air_bg_2);
                    break;
                case "轻度污染":
                    tvAir.setBackgroundResource(R.drawable.air_bg_3);
                    break;
                case "中度污染":
                    tvAir.setBackgroundResource(R.drawable.air_bg_4);
                    break;
                case "重度污染":
                    tvAir.setBackgroundResource(R.drawable.air_bg_5);
                    break;
                case "严重污染":
                    tvAir.setBackgroundResource(R.drawable.air_bg_6);
                    break;
            }
        }
    }

    public void setDayTemp(int dayTemp) {
        if (ttvTemp != null)
            ttvTemp.setTemperatureDay(dayTemp);
        if (tvDayTemp != null)
            tvDayTemp.setText(dayTemp + "°");
    }

    public void setNightTemp(int nightTemp) {
        if (ttvTemp != null)
            ttvTemp.setTemperatureNight(nightTemp);
        if (tvNightTemp != null)
            tvNightTemp.setText(nightTemp + "°");
    }

    public void setIcon(int code, boolean isDay) {
        if (isDay) {
            if (ivDayWeather != null) {
                WeatherUtil.changeIcon(ivDayWeather, code);
            }
        } else {
            if (ivNightWeather != null) {
                WeatherUtil.changeIcon(ivNightWeather, WeatherUtil.iconCode2Night(code));
            }
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

    public void setDayPointColor(int color) {
        if (ttvTemp != null) {
            ttvTemp.setPointColorDay(color);
        }
    }

    public void setNightPointColor(int color) {
        if (ttvTemp != null) {
            ttvTemp.setPointColorNight(color);
        }
    }

    public void setGray() {
        int gray = getResources().getColor(R.color.gray);
        tvWeek.setTextColor(gray);
        tvDate.setTextColor(gray);
        tvDayWeather.setTextColor(gray);
        tvDayTemp.setTextColor(gray);
        tvNightTemp.setTextColor(gray);
        tvNightWeather.setTextColor(gray);
        tvWindOri.setTextColor(gray);
        tvWindLevel.setTextColor(gray);
    }

    public void setHollow(int hollow) {
        ttvTemp.setHollow(hollow);
    }
}
