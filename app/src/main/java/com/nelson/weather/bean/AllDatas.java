package com.nelson.weather.bean;

import com.baidu.location.BDLocation;

public class AllDatas {
    private AirNowResponse airNowResponse;
    private DailyResponse dailyResponse;
    private LifestyleResponse lifestyleResponse;
    private MoreAirFiveResponse moreAirFiveResponse;
    private NowResponse nowResponse;
    private String Location_id;
    private NewSearchCityResponse newSearchCityResponse;
    private BDLocation bdLocation;
    private HistoryAirResponse historyAirResponse;
    private HistoryResponse historyResponse;
    private HourlyResponse hourlyResponse;
    private WarningResponse warningResponse;
    private EpidemicDataResponse epidemicDataResponse;

    public EpidemicDataResponse getEpidemicDataResponse() {
        return epidemicDataResponse;
    }

    public void setEpidemicDataResponse(EpidemicDataResponse epidemicDataResponse) {
        this.epidemicDataResponse = epidemicDataResponse;
    }

    public HourlyResponse getHourlyResponse() {
        return hourlyResponse;
    }

    public void setHourlyResponse(HourlyResponse hourlyResponse) {
        this.hourlyResponse = hourlyResponse;
    }

    public HistoryAirResponse getHistoryAirResponse() {
        return historyAirResponse;
    }

    public void setHistoryAirResponse(HistoryAirResponse historyAirResponse) {
        this.historyAirResponse = historyAirResponse;
    }

    public HistoryResponse getHistoryResponse() {
        return historyResponse;
    }

    public void setHistoryResponse(HistoryResponse historyResponse) {
        this.historyResponse = historyResponse;
    }

    public BDLocation getBdLocation() {
        return bdLocation;
    }

    public void setBdLocation(BDLocation bdLocation) {
        this.bdLocation = bdLocation;
    }

    public void setNewSearchCityResponse(NewSearchCityResponse newSearchCityResponse) {
        this.newSearchCityResponse = newSearchCityResponse;
    }

    public NewSearchCityResponse getNewSearchCityResponse() {
        return newSearchCityResponse;
    }

    public String getLocationId() {
        return Location_id;
    }

    public void setLocationId(String locationId) {
        Location_id = locationId;
    }


    public AirNowResponse getAirNowResponse() {
        return airNowResponse;
    }

    public void setAirNowResponse(AirNowResponse airNowResponse) {
        this.airNowResponse = airNowResponse;
    }

    public DailyResponse getDailyResponse() {
        return dailyResponse;
    }

    public void setDailyResponse(DailyResponse dailyResponse) {
        this.dailyResponse = dailyResponse;
    }

    public LifestyleResponse getLifestyleResponse() {
        return lifestyleResponse;
    }

    public void setLifestyleResponse(LifestyleResponse lifestyleResponse) {
        this.lifestyleResponse = lifestyleResponse;
    }

    public MoreAirFiveResponse getMoreAirFiveResponse() {
        return moreAirFiveResponse;
    }

    public void setMoreAirFiveResponse(MoreAirFiveResponse moreAirFiveResponse) {
        this.moreAirFiveResponse = moreAirFiveResponse;
    }

    public NowResponse getNowResponse() {
        return nowResponse;
    }

    public void setNowResponse(NowResponse nowResponse) {
        this.nowResponse = nowResponse;
    }

    private static class AllDatasInstance{
        private static final AllDatas instance = new AllDatas();
    }

    private AllDatas(){}

    public static AllDatas getInstance(){
        return AllDatasInstance.instance;
    }

    public WarningResponse getWarningResponse() {
        return warningResponse;
    }

    public void setWarningResponse(WarningResponse warningResponse) {
        this.warningResponse = warningResponse;
    }
}
