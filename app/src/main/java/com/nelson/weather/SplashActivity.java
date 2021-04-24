package com.nelson.weather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.AllDatas;
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
import com.nelson.weather.contract.SplashContract;
import com.nelson.weather.utils.ActivityUtils;
import com.nelson.weather.utils.AppStartUpUtils;
import com.nelson.weather.utils.CodeToStringUtils;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.DateUtils;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.StatusBarUtil;
import com.nelson.weather.utils.ToastUtils;
import com.nelson.weather.view.privacyDialog.BaseDialogFragment;
import com.nelson.weather.view.privacyDialog.PrivacyProtocolDialog;
import com.nelson.mvplibrary.bean.Country;
import com.nelson.mvplibrary.mvp.MvpActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Response;

//import com.nelson.weather.utils.StatusBarUtil;
//import com.nelson.mvplibrary.bean.AppVersion;

/**
 * 欢迎页
 *
 * @author nelson
 */
public class SplashActivity extends MvpActivity<SplashContract.SplashPresenter> implements SplashContract.ISplashView{

    private boolean shouldShowMainActivity = true;
    private static final String PREF_FILE_ENTER_ACTIVITY = "optimizer_enter_app";
    private static final String PREF_KEY_IS_FIRST_ENTER = "PREF_KEY_IS_FIRST_ENTER";
    /**定位器*/
    public LocationClient mLocationClient = null;
    private final MyLocationListener myListener = new MyLocationListener();
    /**城市id，用于查询城市数据  V7版本 中 才有*/
    private String locationId = null;

    private ViewGroup viewGroup;
    boolean needDismiss = false;
    boolean isForeground = false;

    @Override
    public void initData(Bundle savedInstanceState) {
        viewGroup = findViewById(R.id.ad_splash_container);

        //透明状态栏
        StatusBarUtil.transparencyBar(context);

        /**第一次进入标志*/
        boolean isFirstEntry = AppStartUpUtils.isFirstStartApp(this);
        if (isFirstEntry) {
            showPrivacyAlertDialog();
            SPUtils.putString(Constant.allLocation,"暂无定位",context);
        }else{
            permissionVersion();//权限判断
        }

    }

    /**
     * 手机是否开启位置服务，如果没有开启那么App将不能使用定位功能
     */
    private boolean isOpenLocationServiceEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
    /**
     *定位
     * */
    private void startLocation() {
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false
        option.setNeedNewVersionRgc(true);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);
        //启动定位
        mLocationClient.start();
        //ifShowDialog = false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needDismiss) {
            startActivity(new Intent(com.nelson.weather.SplashActivity.this, WeatherMainActivity.class));
            finish();
        }
        isForeground = true;
    }

    /**
     * 权限判断
     */
    private void permissionVersion() {
        //6.0或6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //动态权限申请
            permissionsRequest();
        } else {//6.0以下
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted  提示一下即可
            ToastUtils.showShortToast(this, "你的版本在Android6.0以下，不需要动态申请权限。");
        }
    }

    /**
     * 动态权限申请  使用这个框架需要制定JDK版本，建议用1.8
     */
    private void permissionsRequest() {
        //实例化这个权限请求框架，否则会报错
        /**
         * 权限请求框架
         */
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {//申请成功
                        //得到权限可以进入APP
                        //加载世界国家数据到本地数据库,已有则不加载
                        //initCountryData();
                        //请求版本更新
                        //mPresent.getAppInfo();
                        if (isOpenLocationServiceEnable()) {
                            startLocation();//开始定位
                        } else {
                            ToastUtils.showShortToast(context, "(((φ(◎ロ◎;)φ)))，你好像忘记打开定位功能了");
                        }
                    } else {//申请失败
                        finish();
                        ToastUtils.showShortToast(this, "权限未开启");
                    }

                });

    }
    private void showPrivacyAlertDialog(){
        PrivacyProtocolDialog privacyProtocolDialog = new PrivacyProtocolDialog();
        privacyProtocolDialog.setOnDismissOrCancelListener(new BaseDialogFragment.OnDismissOrCancelListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!shouldShowMainActivity) {
                    return;
                }
                permissionVersion();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                shouldShowMainActivity = false;
                finish();
            }
        });
        ActivityUtils.showDialogFragment(this, privacyProtocolDialog);
    }
    private List<Country> list;

    /**
     * 初始化世界国家及地区数据
     */
    private void initCountryData() {
        list = LitePal.findAll(Country.class);
        //有数据了
        if (list.size() > 0) {
            return;
            //第一次加载
        } else {
            InputStreamReader is = null;
            try {
                is = new InputStreamReader(getAssets().open("world_country.csv"), "UTF-8");
                BufferedReader reader = new BufferedReader(is);
                reader.readLine();
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] result = line.split(",");
                    Country country = new Country();
                    country.setName(result[0]);
                    country.setCode(result[1]);
                    country.save();
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
//        finish();
    }

    /**
     * 进入主页面
     */
    private void goToMain() {
       startActivity(new Intent(SplashActivity.this,WeatherMainActivity.class));
       finish();
    }

    @Override
    protected SplashContract.SplashPresenter createPresent() {
        return new SplashContract.SplashPresenter();
    }

//    /**
//     * 获取APP最新版本信息返回
//     *
//     * @param response
//     */
//    @Override
//    public void getAppInfoResult(Response<AppVersion> response) {
//        if (response.body() != null) {
//            AppVersion appVersion = new AppVersion();
//            //应用名称
//            appVersion.setName(response.body().getName());
//            //应用版本 对应code
//            appVersion.setVersion(response.body().getVersion());
//            //应用版本名
//            appVersion.setVersionShort(response.body().getVersionShort());
//            //更新日志
//            appVersion.setChangelog(response.body().getChangelog());
//            //更新地址
//            appVersion.setUpdate_url(response.body().getUpdate_url());
//            //安装地址
//            appVersion.setInstall_url(response.body().getInstall_url());
//            //APK大小
//            appVersion.setAppSize(String.valueOf(response.body().getBinary().getFsize()));
//
//            //添加数据前先判断是否已经有数据了
//            if (LitePal.find(AppVersion.class, 1) != null) {
//                appVersion.update(1);
//            } else {
//                //保存添加数据
//                appVersion.save();
//            }
//
//        }
//    }

    /**
     * 获取AllataD里的数据。
     * 通过getinstance得到。
     *
     * @return
     */

    @Override
    public void getAirNowResult(Response<AirNowResponse> response) {
        AllDatas.getInstance().setAirNowResponse(response.body());
    }

    @Override
    public void getMoreDailyResult(Response<DailyResponse> response) {
        AllDatas.getInstance().setDailyResponse(response.body());
    }

    @Override
    public void getMoreAirFiveResult(Response<MoreAirFiveResponse> response) {
        AllDatas.getInstance().setMoreAirFiveResponse(response.body());

    }

    @Override
    public void getMoreLifestyleResult(Response<LifestyleResponse> response) {
        AllDatas.getInstance().setLifestyleResponse(response.body());
    }

    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
//        refresh.finishRefresh();//关闭刷新
        mLocationClient.stop();//数据返回后关闭定位
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            if (response.body().getLocation() != null && response.body().getLocation().size() > 0) {
                //城市Id
                locationId = response.body().getLocation().get(0).getId();
                AllDatas.getInstance().setNewSearchCityResponse(response.body());
                AllDatas.getInstance().setLocationId(locationId);
                mPresent.nowWeather(locationId);
                mPresent.hourlyWeather(locationId);
                mPresent.DailyRes(locationId);
                mPresent.AirNowRes(locationId);
                mPresent.LifeStyleRes(locationId);
                mPresent.MoreAirFiveRes(locationId);
                mPresent.nowWarn(locationId);
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {

            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    @Override
    public void getHistoryResult(Response<HistoryResponse> response) {
        AllDatas.getInstance().setHistoryResponse(response.body());
    }

    @Override
    public void getHistoryAirResult(Response<HistoryAirResponse> response) {
        AllDatas.getInstance().setHistoryAirResponse(response.body());
    }

    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        AllDatas.getInstance().setHourlyResponse(response.body());
    }

    @Override
    public void getNowWarnResult(Response<WarningResponse> response) {
        AllDatas.getInstance().setWarningResponse(response.body());
    }

    @Override
    public void getNowResult(Response<NowResponse> response) {
        if(response.body()!=null) {
            AllDatas.getInstance().setNowResponse(response.body());
            mPresent.HistoryRes(locationId, DateUtils.updateTime_month(response.body().getUpdateTime()));
            mPresent.HistoryAirRes(locationId, DateUtils.updateTime_month(response.body().getUpdateTime()));
            goToMain();
        }
    }

    @Override
    public void getBiYingResult(Response<BiYingImgResponse> response) {
        if (response.body().getImages() != null) {
            //得到的图片地址是没有前缀的，所以加上前缀否则显示不出来
            String  biyingUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
            SPUtils.putString(Constant.BiYingURL,biyingUrl,context);
            Log.d("type-->", biyingUrl);
        } else {
            ToastUtils.showShortToast(context, "未获取到必应的图片");
        }

    }

    @Override
    public void getDataFailed() {
        ToastUtils.showLongToast(context,"数据请求失败，请连接网络后重试。");
        goToMain();
    }
    /**
     * 定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            AllDatas.getInstance().setBdLocation(location);
            Log.d("TAG", location.toString());
            /**区/县改为全局的静态变量,方便更换城市之后也能进行下拉刷新*/
            String district = location.getDistrict();//获取区/县
            /**市 国控站点数据  用于请求空气质量*/
            String city = location.getCity();//获取市
            if(city !=null&& district !=null) {
                SPUtils.putString("location", district + " " + location.getStreet(), context);
                SPUtils.putString(Constant.allLocation, city + district, context);
                SPUtils.putString(Constant.DISTRICT, district, context);
                //在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
                //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
                mPresent.newSearchCity(district);//定位返回时
            }
            else {
                //未获取到定位信息，请重新定位
                ToastUtils.showShortToast(context, "未获取到定位信息，请重新定位");
                goToMain();
                //页面处理
            }
        }
    }

}
