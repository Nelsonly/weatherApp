package com.nelson.weather.contract;

import com.nelson.weather.api.ApiService;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.net.NetCallBack;
import com.nelson.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 更多天气预报订阅器
 *
 * @author llw
 */
public class MoreDailyContract {

    public static class MoreDailyPresenter extends BasePresenter<IMoreDailyView> {

        /**
         * 更多天气预报  V7
         *
         * @param location 城市id
         */
        public void worldCity(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.dailyWeather("15d", location).enqueue(new NetCallBack<DailyResponse>() {
                @Override
                public void onSuccess(Call<DailyResponse> call, Response<DailyResponse> response) {
                    if (getView() != null) {
                        //Log.d("TAG",response.body().toString());
                        getView().getMoreDailyResult(response);
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
        public void airFive(String location) {
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
//        public void sunMoon(String location,String date,int item) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
//            service.getSunMoon(location,date).enqueue(new NetCallBack<SunMoonResponse>() {
//                @Override
//                public void onSuccess(Call<SunMoonResponse> call, Response<SunMoonResponse> response) {
//                    if (getView() != null) {
//                        getView().getSunMoonResponseResult(response,item);
//                    }
//                }
//
//                @Override
//                public void onFailed() {
//                    if (getView() != null) {
//                        getView().getDataFailed();
//                    }
//                }
//            });
//        }
    }

    public interface IMoreDailyView extends BaseView {

        //更多天气预报返回数据 V7
        void getMoreDailyResult(Response<DailyResponse> response);
        //空气质量预测
        void getMoreAirFiveResult(Response<MoreAirFiveResponse> response);
        //日出日落
//        void getSunMoonResponseResult(Response<SunMoonResponse> responseResponse,int item);

        //错误返回
        void getDataFailed();

    }
}
