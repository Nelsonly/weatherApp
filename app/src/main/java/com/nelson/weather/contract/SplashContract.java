package com.nelson.weather.contract;

import com.nelson.weather.api.ApiService;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.BiYingImgResponse;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.HistoryAirResponse;
import com.nelson.weather.bean.HistoryResponse;
import com.nelson.weather.bean.HourlyResponse;
import com.nelson.weather.bean.LifestyleResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.bean.NewSearchCityResponse;
import com.nelson.weather.bean.NowResponse;
import com.nelson.weather.bean.WarningResponse;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.net.NetCallBack;
import com.nelson.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

//import com.nelson.mvplibrary.bean.AppVersion;

/**
 * 欢迎页订阅器
 *
 * @author nelson
 */
public class SplashContract {

    public static class SplashPresenter extends BasePresenter<ISplashView> {
        //AirNowResponse


        public void AirNowRes(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
                @Override
                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
                    if (getView() != null) {
                        getView().getAirNowResult(response);
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
        public void newSearchCity(String location) {//注意这里的4表示新的搜索城市地址接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.newSearchCity(location, "exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getNewSearchCityResult(response);
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
        public void DailyRes(String location) {
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
                        getView().getDataFailed();
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
                        getView().getDataFailed();
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
                        getView().getDataFailed();
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
                        getView().getDataFailed();
                    }
                }
            });
        }
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
                        getView().getDataFailed();
                    }
                }
            });
        }
        /**
         * 获取必应  每日一图
         */
        public void biying() {
            ApiService service = ServiceGenerator.createService(ApiService.class, 1);
            service.biying().enqueue(new NetCallBack<BiYingImgResponse>() {
                @Override
                public void onSuccess(Call<BiYingImgResponse> call, Response<BiYingImgResponse> response) {
                    if (getView() != null) {
                        getView().getBiYingResult(response);
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
         * 获取最新的APP版本信息
         */
//        public void getAppInfo() {//注意这里的4表示新的搜索城市地址接口
//            ApiService service = ServiceGenerator.createService(ApiService.class, 5);
//            service.getAppInfo().enqueue(new NetCallBack<AppVersion>() {
//                @Override
//                public void onSuccess(Call<AppVersion> call, Response<AppVersion> response) {
//                    if (getView() != null) {
//                        getView().getAppInfoResult(response);
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

    public interface ISplashView extends BaseView {
        //APP信息返回
//        void getAppInfoResult(Response<AppVersion> response);

        //当前天气
        void getAirNowResult(Response<AirNowResponse> response);
        //更多天气预报返回数据 V7
        void getMoreDailyResult(Response<DailyResponse> response);
        //空气质量预测
        void getMoreAirFiveResult(Response<MoreAirFiveResponse> response);
        //生活指数
        void getMoreLifestyleResult(Response<LifestyleResponse> response);

        void getNewSearchCityResult(Response<NewSearchCityResponse> response);

        void getHistoryResult(Response<HistoryResponse> response);

        void getHistoryAirResult(Response<HistoryAirResponse> response);
        //错误返回
        void getDataFailed();
        //逐小时天气预报
        void getHourlyResult(Response<HourlyResponse> response);

        void  getNowWarnResult(Response<WarningResponse> response);

        void getBiYingResult(Response<BiYingImgResponse> responseResponse);

        void   getNowResult(Response<NowResponse> response);
    }
}
