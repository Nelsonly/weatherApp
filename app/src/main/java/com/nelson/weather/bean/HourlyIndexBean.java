package com.nelson.weather.bean;

import com.nelson.weather.adapter.IndexAdapter;
import com.nelson.mvplibrary.base.BaseBean;

import java.util.List;

public class HourlyIndexBean extends BaseBean {
    private List<HourlyResponse.HourlyBean> hourlyBeans;
    private HourlyResponse.HourlyBean nowBean;
    private String sunrise;
    private String sunset;
    private String air;

    public HourlyIndexBean() {
        this.setViewType(IndexAdapter.ITEM_HOURLY);
    }

    public List<HourlyResponse.HourlyBean> getHourlyBeans() {
        return hourlyBeans;
    }

    public void setHourlyBeans(List<HourlyResponse.HourlyBean> hourlyBeans) {
        this.hourlyBeans = hourlyBeans;
    }

    public HourlyResponse.HourlyBean getNowBean() {
        return nowBean;
    }

    public void setNowBean(HourlyResponse.HourlyBean nowBean) {
        this.nowBean = nowBean;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }
}
