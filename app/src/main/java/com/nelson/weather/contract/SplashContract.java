package com.nelson.weather.contract;

import com.nelson.weather.api.ApiService;
import com.nelson.weather.bean.BiYingImgResponse;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.bean.AppVersion;
import com.nelson.mvplibrary.net.NetCallBack;
import com.nelson.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 欢迎页订阅器
 *
 * @author llw
 */
public class SplashContract {

    public static class SplashPresenter extends BasePresenter<ISplashView> {

        /**
         * 获取最新的APP版本信息
         */
        public void getAppInfo() {//注意这里的4表示新的搜索城市地址接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 5);
            service.getAppInfo().enqueue(new NetCallBack<AppVersion>() {
                @Override
                public void onSuccess(Call<AppVersion> call, Response<AppVersion> response) {
                    if (getView() != null) {
                        getView().getAppInfoResult(response);
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
    }

    public interface ISplashView extends BaseView {
        //APP信息返回
        void getAppInfoResult(Response<AppVersion> response);

        /**
         * 获取必应每日一图返回
         * @param response BiYingImgResponse
         */
        void getBiYingResult(Response<BiYingImgResponse> response);

        //错误返回
        void getDataFailed();


    }
}
