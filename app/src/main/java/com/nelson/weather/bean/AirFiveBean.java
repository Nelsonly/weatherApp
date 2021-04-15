package com.nelson.weather.bean;

/**
 * @author yating
 */
public class AirFiveBean {
    private String aqi = "0";
    private String date;
    private String category = "-";

    public AirFiveBean() {
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
