package com.nelson.weather.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.nelson.weather.R;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.HistoryAirResponse;
import com.nelson.weather.bean.HistoryResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.bean.SunMoonResponse;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.WeatherUtil;
import com.nelson.weather.view.SunView;


import static com.nelson.weather.utils.DateUtils.updateTime;

/**
 * Created by Administrator on 2017/11/10.
 */

public class CusFragment extends Fragment {
    private View view;
    private SunMoonResponse sunMoonResponse = new SunMoonResponse();
    private MoreAirFiveResponse.DailyBean moreAirFiveResponse = new MoreAirFiveResponse.DailyBean();
    private DailyResponse.DailyBean dailyResponse = new DailyResponse.DailyBean();
    private HistoryResponse historyResponse = new HistoryResponse();
    private HistoryAirResponse historyAirResponse = new HistoryAirResponse();
    private AirNowResponse.NowBean nowBean = new AirNowResponse.NowBean();
    private boolean istoday = false;
    private ImageView daily_img;
    private TextView daily_weather;
    private TextView daily_temperature;
    private TextView daily_wind;
    private TextView daily_air;
    private TextView daily_sun;
    private TextView daily_humidity;
    private TextView daily_pressure;
    private TextView daily_uv;
    private ImageView air_image;
    private SunView sunView;
    private TextView daily_air_mun;
    private TextView daily_air_suggest;
    private CardView air_inf;
    private ToAirQuality toAirQuality;
    private TextView sunrise_time;
    private TextView sunset_time;
    private boolean isvisiable;
    private String currentTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_daily_weather, container, false);
        initView();
        Log.e("yytTest", "onCreateView: +++++++++?????????,?????????" + dailyResponse.getFxDate().substring(5, 10));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("yytTest", "onStart: +++++++++Cus?????????");
    }

    @Override
    public void onResume() {
        super.onResume();

        Refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void initView() {
        daily_weather = view.findViewById(R.id.daily_weather);
        daily_temperature = view.findViewById(R.id.daily_temperature);
        daily_wind = view.findViewById(R.id.daily_wind);
        daily_img = view.findViewById(R.id.daily_img);
        daily_air = view.findViewById(R.id.daily_air);
        daily_air_suggest = view.findViewById(R.id.daily_air_suggest);
        daily_uv = view.findViewById(R.id.daily_uv);
        daily_pressure = view.findViewById(R.id.daily_pressure);
        daily_humidity = view.findViewById(R.id.daily_humidity);
        sunView = view.findViewById(R.id.sun_view);
        daily_air_mun = view.findViewById(R.id.daily_air_num);
        air_image = view.findViewById(R.id.air_background);
        air_inf = view.findViewById(R.id.air_inf);
        sunrise_time = view.findViewById(R.id.sunrise_time);
        sunset_time = view.findViewById(R.id.sunset_time);
        Refresh();
    }


    public void Refresh() {
        //??????????????????
        WeatherUtil.changeIcon(daily_img, Integer.parseInt(dailyResponse.getIconDay()));
        //????????????
        daily_weather.setText(dailyResponse.getTextDay());
        //???
        daily_wind.setText(getwind(dailyResponse));
        //????????????
        daily_air.setText(getAir(moreAirFiveResponse));
        daily_air_mun.setText(moreAirFiveResponse.getAqi());
        WeatherUtil.changeAirIcon(air_image, Integer.parseInt(moreAirFiveResponse.getAqi()));
        //??????
        daily_temperature.setText(getTemperature(dailyResponse));
        //??????
        daily_humidity.setText(dailyResponse.getHumidity() + "%");
        //??????
        daily_pressure.setText(dailyResponse.getPressure() + "hPa");
        //?????????
        daily_uv.setText(WeatherUtil.uvIndexInfo(dailyResponse.getUvIndex()));
        //????????????
        String sunRise = dailyResponse.getSunrise();
        String sunSet = dailyResponse.getSunset();
        sunrise_time.setText(sunRise);
        sunset_time.setText(sunSet);

        if (istoday) {
            currentTime = updateTime(null);
            Log.d("timeii", currentTime);
        } else {
            currentTime = sunRise;
        }
        sunView.setTimes(sunRise, sunSet, currentTime);

        air_inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAirQuality.isonClick();
            }
        });
        if (isvisiable) {
            air_inf.setVisibility(View.VISIBLE);
        }
    }

    private String getwind(DailyResponse.DailyBean dailyData) {
        String WindText = "";
        //?????????
        WindText += dailyData.getWindDirDay();
        //??????
        WindText += " " + dailyData.getWindScaleDay() + "???";
        return WindText;
    }

    private String getAir(MoreAirFiveResponse.DailyBean dailyData) {
        String str = dailyData.getCategory().replace("??????", "");
        daily_air_suggest.setText(WeatherUtil.daily_suggest(str));
        //????????????
        return "?????? " + str;
    }

    private String getTemperature(DailyResponse.DailyBean dailyData) {
        String TempText = "";
        TempText += dailyData.getTempMax() + "/" + dailyData.getTempMin() + "??";
        return TempText;
    }

    public void setIstoday(boolean istoday) {
        this.istoday = istoday;
    }

    public void setvisiableair(boolean isgone) {
        this.isvisiable = isgone;
    }

    public void setSunMoonResponse(SunMoonResponse sunMoonResponse) {
        this.sunMoonResponse = sunMoonResponse;
    }

    public void setDailyResponse(DailyResponse.DailyBean dailyResponse) {
        this.dailyResponse = dailyResponse;
    }

    public void setMoreAirFiveResponse(MoreAirFiveResponse.DailyBean moreAirFiveResponse) {
        this.moreAirFiveResponse = moreAirFiveResponse;
    }

    public void setAirNowResponse(AirNowResponse.NowBean moreAirFiveResponse) {
        this.nowBean = moreAirFiveResponse;
    }
    /*public void set_ad_visibility() {
        daily_ad_line.setVisibility(View.VISIBLE);
    }*/

    /**
     * ???????????????????????????????????? ????????????
     *
     * @param
     * @return
     */
    public static CusFragment newInStance(ToAirQuality toAirQuality) {
        return new CusFragment(toAirQuality);
    }

    public CusFragment(ToAirQuality toAirQuality) {
        this.toAirQuality = toAirQuality;
    }

    public CusFragment() {
    }

    /**
     * ????????????????????????
     */
    public interface ToAirQuality {
        void isonClick();
    }

}
