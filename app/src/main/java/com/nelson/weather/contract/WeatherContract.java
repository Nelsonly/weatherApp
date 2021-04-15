package com.nelson.weather.contract;


import android.util.Log;

import com.nelson.weather.api.ApiService;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.HistoryAirResponse;
import com.nelson.weather.bean.HistoryResponse;
import com.nelson.weather.bean.HourlyResponse;
import com.nelson.weather.bean.LifestyleResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.bean.NowResponse;
import com.nelson.weather.bean.WarningResponse;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.net.NetCallBack;
import com.nelson.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 天气订阅器
 *
 * @author llw
 */
public class WeatherContract {

    public static class WeatherPresenter extends BasePresenter<IWeatherView> {


        /**
         * 实况天气  V7版本
         *
         * @param location 城市名
         */
        public void nowWeather(String location) {//这个3 表示使用新的V7API访问地址
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.nowWeather(location).enqueue(new NetCallBack<NowResponse>() {
                @Override
                public void onSuccess(Call<NowResponse> call, Response<NowResponse> response) {
                    if (getView() != null) {
                        getView().getNowResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }


        /**
         * 天气预报  V7版本   7d 表示天气的数据 为了和之前看上去差别小一些，这里先用七天的
         *
         * @param location 城市名
         */
        public void dailyWeather(String location) {//这个3 表示使用新的V7API访问地址
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.dailyWeather("15d", location).enqueue(new NetCallBack<DailyResponse>() {
                @Override
                public void onSuccess(Call<DailyResponse> call, Response<DailyResponse> response) {
                    if (getView() != null) {
                        getView().getDailyResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }


        /**
         * 逐小时预报（未来24小时）
         *
         * @param location 城市名
         */
        public void hourlyWeather(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.hourlyWeather(location).enqueue(new NetCallBack<HourlyResponse>() {
                @Override
                public void onSuccess(Call<HourlyResponse> call, Response<HourlyResponse> response) {
                    if (getView() != null) {
                        getView().getHourlyResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 五天空气质量数据  V7
         *
         * @param location 城市id
         */
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
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }


        /**
         * 生活指数
         *
         * @param location 城市名  type中的"1,2,3,5,6,8,9,10"，表示只获取这8种类型的指数信息，同样是为了对应之前的界面UI
         */
        public void lifestyle(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.lifestyle("0", location).enqueue(new NetCallBack<LifestyleResponse>() {
                @Override
                public void onSuccess(Call<LifestyleResponse> call, Response<LifestyleResponse> response) {
                    if (getView() != null) {
                        Log.e("11133", "onSuccess: ");
                        getView().getLifestyleResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }


        /**
         * 城市灾害预警
         *
         * @param location 城市id
         */
        public void nowWarn(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.nowWarn(location).enqueue(new NetCallBack<WarningResponse>() {
                @Override
                public void onSuccess(Call<WarningResponse> call, Response<WarningResponse> response) {
                    if (getView() != null) {
                        getView().getNowWarnResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }


        public void HistoryRes(String location,String date) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.historyWeather(location,date).enqueue(new NetCallBack<HistoryResponse>() {
                @Override
                public void onSuccess(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                    if (getView() != null) {
                        getView().getHistoryResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }
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
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

    }

    public interface IWeatherView extends BaseView {
        //天气数据获取错误返回
        void getWeatherDataFailed();

        //实况天气
        void getNowResult(Response<NowResponse> response);

        //天气预报  7天
        void getDailyResult(Response<DailyResponse> response);

        //逐小时天气预报
        void getHourlyResult(Response<HourlyResponse> response);

        //空气质量
        void getMoreAirFiveResult(Response<MoreAirFiveResponse> response);

        //生活指数
        void getLifestyleResult(Response<LifestyleResponse> response);

        //灾害预警
        void getNowWarnResult(Response<WarningResponse> response);


        void getHistoryResult(Response<HistoryResponse> response);

        void getHistoryAirResult(Response<HistoryAirResponse> response);
    }
}
