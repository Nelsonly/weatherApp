package com.nelson.weather.contract;

import com.nelson.weather.api.ApiService;
import com.nelson.weather.bean.BiYingImgResponse;
import com.nelson.weather.bean.WallPaperResponse;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.net.NetCallBack;
import com.nelson.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 壁纸订阅器
 *
 * @author nelson
 */
public class WallPaperContract {

    public static class WallPaperPresenter extends BasePresenter<IWallPaperView> {


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
         * 获取壁纸数据
         */
        public void getWallPaper() {
            // 6 表示访问网络壁纸接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 6);
            service.getWallPaper().enqueue(new NetCallBack<WallPaperResponse>() {
                @Override
                public void onSuccess(Call<WallPaperResponse> call, Response<WallPaperResponse> response) {
                    if (getView() != null) {
                        getView().getWallPaperResult(response);
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

    public interface IWallPaperView extends BaseView {

        /**
         * 获取必应每日一图返回
         * @param response BiYingImgResponse
         */
        void getBiYingResult(Response<BiYingImgResponse> response);

        /**
         * 壁纸数据返回
         * @param response WallPaperResponse
         */
        void getWallPaperResult(Response<WallPaperResponse> response);

        /**
         * 错误返回
         */
        void getDataFailed();


    }
}
