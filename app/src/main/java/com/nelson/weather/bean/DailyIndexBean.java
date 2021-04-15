package com.nelson.weather.bean;

import com.nelson.weather.adapter.IndexAdapter;
import com.nelson.mvplibrary.base.BaseBean;

import java.util.List;

public class DailyIndexBean extends BaseBean {
    private List<DailyResponse.DailyBean> dailyBeans;
    private List<MoreAirFiveResponse.DailyBean> airBeans;
    private DailyResponse.DailyBean hisDailyBean;
    private MoreAirFiveResponse.DailyBean hisAirBean;

    public DailyIndexBean() {
        this.setViewType(IndexAdapter.ITEM_DAILY);
    }

    public List<DailyResponse.DailyBean> getDailyBeans() {
        return dailyBeans;
    }

    public void setDailyBeans(List<DailyResponse.DailyBean> dailyBeans) {
        this.dailyBeans = dailyBeans;
    }

    public List<MoreAirFiveResponse.DailyBean> getAirBeans() {
        return airBeans;
    }

    public void setAirBeans(List<MoreAirFiveResponse.DailyBean> airBeans) {
        this.airBeans = airBeans;
    }

    public DailyResponse.DailyBean getHisDailyBean() {
        return hisDailyBean;
    }

    public void setHisDailyBean(DailyResponse.DailyBean hisDailyBean) {
        this.hisDailyBean = hisDailyBean;
    }

    public MoreAirFiveResponse.DailyBean getHisAirBean() {
        return hisAirBean;
    }

    public void setHisAirBean(MoreAirFiveResponse.DailyBean hisAirBean) {
        this.hisAirBean = hisAirBean;
    }
}
