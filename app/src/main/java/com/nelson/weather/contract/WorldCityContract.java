package com.nelson.weather.contract;

import com.nelson.weather.api.ApiService;
import com.nelson.weather.bean.WorldCityResponse;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.net.NetCallBack;
import com.nelson.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 世界城市订阅器
 *
 * @author llw
 */
public class WorldCityContract {

    public static class WorldCityPresenter extends BasePresenter<IWorldCityView> {

        /**
         * 世界城市  V7
         *
         * @param range 类型
         */
        public void worldCity(String range) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.worldCity(range).enqueue(new NetCallBack<WorldCityResponse>() {
                @Override
                public void onSuccess(Call<WorldCityResponse> call, Response<WorldCityResponse> response) {
                    if (getView() != null) {
                        getView().getWorldCityResult(response);
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

    public interface IWorldCityView extends BaseView {

        //热门城市返回数据 V7
        void getWorldCityResult(Response<WorldCityResponse> response);

        //错误返回
        void getDataFailed();
    }
}
