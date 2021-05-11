package com.nelson.weather;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.nelson.mvplibrary.BaseApplication;
import com.nelson.mvplibrary.utils.ActivityManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
//import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

/**
 * 项目管理
 *
 * @author nelson
 */
public class WeatherApplication extends BaseApplication {

    /**
     * 应用实例
     */
    public static com.nelson.weather.WeatherApplication weatherApplication;
    private static Context context;
    private static ActivityManager activityManager;

    private static Activity sActivity;

    public BMapManager bMapManager = null;
    public static Context getMyContext() {
        return weatherApplication == null ? null : weatherApplication.getApplicationContext();
    }

    private Handler myHandler;

    public Handler getMyHandler() {
        return myHandler;
    }

    public void setMyHandler(Handler handler) {
        myHandler = handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        activityManager = new ActivityManager();
        context = getApplicationContext();
        weatherApplication = this;
        initEngineManager(this);

        this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                sActivity = activity;

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        //初始化数据库
        LitePal.initialize(this);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);

        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

//        CrashReport.initCrashReport(getApplicationContext(), "d3637c0f25", true);
    }


    public static ActivityManager getActivityManager() {
        return activityManager;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.darker_gray, android.R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    public void initEngineManager(Context context) {
        if (bMapManager == null) {
            bMapManager = new BMapManager(context);
        }

        if (!bMapManager.init(new MyGeneralListener())) {
            Toast.makeText(
                    this,
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
        Log.d("ljx", "initEngineManager");
    }
    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
            if (iError != 0) {
                // 授权Key错误：
                Toast.makeText(
                        WeatherApplication.getMyContext(),
                        "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: "
                                + iError, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(
                        WeatherApplication.getMyContext(), "key认证成功",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
