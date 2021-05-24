package com.nelson.weather.bean;

import com.google.gson.annotations.SerializedName;


public class EpidemicDataResponse {

    /**
     * confirm : 0
     * isUpdated : true
     * nowConfirm : 2
     */

    @SerializedName("confirm")
    private Integer confirm;
    @SerializedName("isUpdated")
    private Boolean isUpdated;
    @SerializedName("nowConfirm")
    private Integer nowConfirm;

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    public Boolean getUpdated() {
        return isUpdated;
    }

    public void setUpdated(Boolean updated) {
        isUpdated = updated;
    }

    public Integer getNowConfirm() {
        return nowConfirm;
    }

    public void setNowConfirm(Integer nowConfirm) {
        this.nowConfirm = nowConfirm;
    }
}
