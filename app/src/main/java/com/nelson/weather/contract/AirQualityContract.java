package com.nelson.weather.contract;

import com.nelson.weather.api.ApiService;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.NewSearchCityResponse;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.net.NetCallBack;
import com.nelson.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class AirQualityContract {

    public static class AirQualityPresenter extends BasePresenter<IAirQualityView> {

        /**
         * 搜索城市  搜索站点的城市id，用于查询空气质量
         *
         * @param location 城市名
         */
        public void searchCityId(String location) {//注意这里的4表示新的搜索城市地址接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.newSearchCity(location, "exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getSearchCityIdResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 空气质量  V7
         *
         * @param location 城市id
         */
        public void air(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
                @Override
                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
                    if (getView() != null) {
                        getView().getMoreAirResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }
/*
        *//**
         * 生活方式
         *
         * @param location 城市id
         *//*
        public void LifeStyleRes(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.lifestyle("0", location).enqueue(new NetCallBack<LifestyleResponse>() {
                @Override
                public void onSuccess(Call<LifestyleResponse> call, Response<LifestyleResponse> response) {
                    if (getView() != null) {
                        getView().getMoreLifestyleResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        *//**
         * 5天空气质量
         *
         * @param location 城市id
         *//*
        public void MoreAirFiveRes(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.airFiveWeather(location).enqueue(new NetCallBack<MoreAirFiveResponse>() {
                @Override
                public void onSuccess(Call<MoreAirFiveResponse> call, Response<MoreAirFiveResponse> response) {
                    if (getView() != null) {
                        getView().getMoreAirFiveResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        *//**
         * 历史空气质量
         *
         * @param location
         * @param date
         * *//*
        public void HistoryAirRes(String location,String date) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.historyAirData(location,date).enqueue(new NetCallBack<HistoryAirResponse>() {
                @Override
                public void onSuccess(Call<HistoryAirResponse> call, Response<HistoryAirResponse> response) {
                    if (getView() != null) {
                        getView().getHistoryAirResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }*/

    }

    public interface IAirQualityView extends BaseView{

        //搜索城市Id
        void getSearchCityIdResult(Response<NewSearchCityResponse> response);

        //空气质量返回数据 V7
        void getMoreAirResult(Response<AirNowResponse> response);
/*
        //空气质量预测
        void getMoreAirFiveResult(Response<MoreAirFiveResponse> response);

        //生活指数
        void getMoreLifestyleResult(Response<LifestyleResponse> response);

        //历史空气质量
        void getHistoryAirResult(Response<HistoryAirResponse> response);*/

        //错误返回
        void getDataFailed();

    }

}
