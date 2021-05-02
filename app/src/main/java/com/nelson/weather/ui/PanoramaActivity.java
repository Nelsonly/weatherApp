package com.nelson.weather.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.nelson.mvplibrary.base.BasePresenter;
import com.nelson.mvplibrary.base.BaseView;
import com.nelson.mvplibrary.mvp.MvpActivity;

import com.baidu.lbsapi.panoramaview.*;
import com.baidu.lbsapi.BMapManager;
import com.nelson.weather.R;
import com.nelson.weather.WeatherApplication;


import butterknife.BindView;

/**
 * @author nelson
 */
public class PanoramaActivity extends MvpActivity<BasePresenter> {


    @BindView(R.id.panorama)
    PanoramaView mPanoView;

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        super.initBeforeView(savedInstanceState);
        WeatherApplication app = (WeatherApplication) this.getApplication();
        if (app.bMapManager == null) {
            app.bMapManager = new BMapManager(app);
            app.bMapManager.init(new WeatherApplication.MyGeneralListener());
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPanoView.setPanorama("0100220000130817164838355J5");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_panorama;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    protected void onPause() {
        super.onPause();
        mPanoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoView.onResume();
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }
    @Override
    protected BasePresenter createPresent() {
        return new BasePresenter();
    }
}