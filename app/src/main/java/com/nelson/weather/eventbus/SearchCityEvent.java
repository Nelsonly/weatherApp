package com.nelson.weather.eventbus;

/**
 * 搜索城市消息事件
 *
 * @author nelson
 */
public class SearchCityEvent {

    public final String mLocation;
    public final String mCity;

    public SearchCityEvent(String location, String city) {
        this.mLocation = location;
        this.mCity = city;
    }
}
