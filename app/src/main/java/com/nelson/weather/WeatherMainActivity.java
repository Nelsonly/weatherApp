package com.nelson.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DownloadManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huantansheng.easyphotos.constant.Code;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;
import com.nelson.mvplibrary.bean.AppVersion;
import com.nelson.mvplibrary.view.dialog.AlertDialog;
import com.nelson.weather.adapter.IndexAdapter;
import com.nelson.weather.adapter.TabAdapter;
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
import com.nelson.weather.contract.AllDataContract;
import com.nelson.weather.eventbus.SearchCityEvent;
import com.nelson.weather.fragment.CusFragment;
import com.nelson.weather.fragment.DailyFragment;
import com.nelson.weather.fragment.IndexFragment;
import com.nelson.weather.fragment.AriQualityFragment;
import com.nelson.weather.fragment.WallpaperFragment;
import com.nelson.weather.sharelibrary.util.ToastUtil;
import com.nelson.weather.ui.PuzzleImgActivity;
import com.nelson.weather.utils.APKVersionInfoUtils;
import com.nelson.weather.utils.AppStartUpUtils;
import com.nelson.weather.utils.CodeToStringUtils;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.DateUtils;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.SizeUtils;
import com.nelson.weather.utils.ToastUtils;
import com.nelson.weather.view.ShareView;
import com.nelson.mvplibrary.mvp.MvpActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.huantansheng.easyphotos.ui.PuzzleSelectorActivitySecond;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.nelson.weather.R;
import butterknife.BindView;
import retrofit2.Response;

/**
 * @author nelson
 */
public class WeatherMainActivity extends MvpActivity<AllDataContract.AllDataPresenter> implements PuzzleSelectorActivitySecond.OnSelectPhotosForPuzzleDoneListener, AllDataContract.IAllDataView, IndexAdapter.OnRecyclerViewItemClick, CusFragment.ToAirQuality,
        IndexFragment.GoToAirPage, IndexFragment.NewCitySearch,IndexFragment.GoToDaily {

    /**?????????*/
    public LocationClient mLocationClient = null;
    private final MyLocationListener myListener = new MyLocationListener();
    private ViewPager2 viewPager2;
    private final List<Integer> icons = new ArrayList<>();
    private final List<Integer> iconsNotChoose = new ArrayList<>();
    private final List<Integer> iconsStart = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    private String locationId = null;//??????id???????????????????????????  V7?????? ??? ??????
    private boolean ifShowDialog;
    private IndexFragment indexFragment;
    private final Boolean flagOther = false;
    private final Stack<Integer> fragQueue = new Stack<>();
    private final int pos = 0;

    private ShareView shareView;

    private boolean isAnimatorEnd;
    private int loading = 9;

    private final Handler handler = new Handler();
    private Runnable task;
    private final int delay = 2000;
    @BindView(R.id.main_background)
    ImageView mainBackground;
    private Boolean isFirst = true;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("ad_debug", "onKeyDown: this id BACK AD");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (fragQueue.empty()) {
                if(isFirst) {
                    ToastUtil.showToast(this, "??????????????????", true);
                    isFirst = false;
                }else {
                    finish();
                }
                return true;
            } else if (fragQueue.size() == 1) {
                viewPager2.setCurrentItem(0);
                return true;
            } else {
                fragQueue.pop();
                viewPager2.setCurrentItem(fragQueue.pop());
                if (fragQueue.size()>=2 ) {
                    int temp = fragQueue.pop();
                    fragQueue.clear();
                    fragQueue.push(0);
                    fragQueue.push(temp);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabLayout bottomTabLayout = findViewById(R.id.bottom_tab);
        viewPager2 = findViewById(R.id.view_pager2);
        viewPager2.setUserInputEnabled(false);
        titles.add("????????????");
        titles.add("15?????????");
        titles.add("????????????");
        titles.add("??????");
        icons.add(R.drawable.choose_comprehensive);
        icons.add(R.drawable.choose_15_days);
        icons.add(R.drawable.choose_air_quality);
        icons.add(R.drawable.choose_wallpaper);

        iconsNotChoose.add(R.drawable.not_choose_comprehensive);
        iconsNotChoose.add(R.drawable.not_choose_15_days);
        iconsNotChoose.add(R.drawable.not_choose_air_quality);
        iconsNotChoose.add(R.drawable.not_choose_wallpaper);

        iconsStart.add(R.drawable.choose_comprehensive);
        iconsStart.add(R.drawable.not_choose_15_days);
        iconsStart.add(R.drawable.not_choose_air_quality);
        iconsStart.add(R.drawable.not_choose_wallpaper);

        DailyFragment dailyFragment = new DailyFragment();
        indexFragment = new IndexFragment(this, this, this);
        AriQualityFragment ariQualityFragment = new AriQualityFragment();

        fragments.add(indexFragment);
        fragments.add(dailyFragment);
        fragments.add(ariQualityFragment);
        fragments.add(new WallpaperFragment());
        //??????????????????
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        //???????????????
        viewPager2.setAdapter(tabAdapter);
        //TabLayout???Viewpager2????????????
        new TabLayoutMediator(bottomTabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
                tab.setIcon(iconsStart.get(position));
            }
        }).attach();
        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               tab.setIcon(icons.get(tab.getPosition()));
                int position = tab.getPosition();
                if (position == 0) {
                    fragQueue.clear();
                    fragQueue.push(0);
                }else if (fragQueue.empty()){
                    fragQueue.push(position);
                }
                else if (fragQueue.peek() != position) {
                    fragQueue.push(position);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(iconsNotChoose.get(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Log.d("lambTest", "initData: -------------->MainActivity");
        EventBus.getDefault().register(this);
        mPresent.biying();
        initBackground();
        checkAppVersion();
    }
    /**
     * ????????????  1  ????????????  2  ????????????  3  ????????????  4  ????????????
     */
    private void initBackground(){
        switch (SPUtils.getInt(Constant.WALLPAPER_TYPE,4,context)){
            case 1:
            case 3:
                Glide.with(this).load(SPUtils.getString(Constant.WALLPAPER_URL, "", context)).into(mainBackground);
            case 2:
                break;
            case 4:
                Glide.with(this).load(R.mipmap.defult_background).into(mainBackground);
                break;
        }
    }
    /**
     * ??????AllData???????????????
     * ??????getinstance?????????
     *
     * @return
     */
    @Override
    protected AllDataContract.AllDataPresenter createPresent() {
        return new AllDataContract.AllDataPresenter();
    }

    @Override
    public void getAirNowResult(Response<AirNowResponse> response) {
        AllDatas.getInstance().setAirNowResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
            loading = 9;
        }

    }

    @Override
    public void getMoreDailyResult(Response<DailyResponse> response) {
        AllDatas.getInstance().setDailyResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
            loading = 9;
        }
//        indexFragment.Refresh();
    }

    @Override
    public void getMoreAirFiveResult(Response<MoreAirFiveResponse> response) {
        AllDatas.getInstance().setMoreAirFiveResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
            loading = 9;
        }
//        indexFragment.Refresh();
    }

    @Override
    public void getMoreLifestyleResult(Response<LifestyleResponse> response) {
        AllDatas.getInstance().setLifestyleResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
        }
//        indexFragment.Refresh();
    }

    @Override
    public void getHistoryResult(Response<HistoryResponse> response) {
        AllDatas.getInstance().setHistoryResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
        }
//        indexFragment.Refresh();
    }

    @Override
    public void getHistoryAirResult(Response<HistoryAirResponse> response) {
        AllDatas.getInstance().setHistoryAirResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
        }
//        indexFragment.Refresh();
    }

    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        AllDatas.getInstance().setHourlyResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
        }
//        indexFragment.Refresh();
    }

    @Override
    public void getNowWarnResult(Response<WarningResponse> response) {
        AllDatas.getInstance().setWarningResponse(response.body());
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
            loading = 9;
        }
//        indexFragment.Refresh();
        dismissLoadingDialog();
    }

    @Override
    public void getBiYingResult(Response<BiYingImgResponse> response) {
        if(response!=null) {
            String biyingUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
            SPUtils.putString(Constant.BiYingURL, biyingUrl, context);
            if(SPUtils.getInt(Constant.WALLPAPER_TYPE,4,context) == 2){
                Glide.with(this).load(biyingUrl).into(mainBackground);
            }
        }

    }

    @Override
    public void getNowResult(Response<NowResponse> response) {
        if (response.body() != null) {
            AllDatas.getInstance().setNowResponse(response.body());
            mPresent.HistoryRes(locationId, DateUtils.updateTime_month(response.body().getUpdateTime()));
            mPresent.HistoryAirRes(locationId, DateUtils.updateTime_month(response.body().getUpdateTime()));
        }
        loading--;
        if (loading == 0) {
            indexFragment.Refresh();
            loading = 9;
        }
//        indexFragment.Refresh();
    }

    @Override
    public void getDataFailed() {
        dismissLoadingDialog();//????????????
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lambTest", "onResume: ------->MainActivity");
        initBackground();
    }

    /**
     * ?????????????????????????????????????????????????????????App???????????????????????????
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
     * ??????????????????15??????????????????15?????????
     */
    @Override
    public void onItemClick(int item) {
        SPUtils.putBoolean(Constant.isindexfragment, true, context);
        SPUtils.putInt("select_item", item, context);
        fragQueue.push(1);
        viewPager2.setCurrentItem(1);

    }
    @Override
    public void onRvItemClick(int item) {
        SPUtils.putBoolean(Constant.isindexfragment, true, context);
        SPUtils.putInt("select_item", item, context);
        fragQueue.push(1);
        viewPager2.setCurrentItem(1);

    }

    /**
     * ???????????????????????????????????????????????????
     */
    @Override
    public void isonClick() {
        fragQueue.push(2);
        viewPager2.setCurrentItem(2);

    }

    /**
     * ????????????index?????????????????????
     */
    @Override
    public void isonclick() {
        fragQueue.push(2);
        viewPager2.setCurrentItem(2);

    }

    @Override
    public void getNewCity(String newcity, boolean ifShowDialog) {
        if (ifShowDialog) {
            showLoadingDialog();
        }
        mPresent.newSearchCity(newcity);
    }


    /**
     * ??????????????????
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            AllDatas.getInstance().setBdLocation(location);
            Log.d("TAG", location.toString());
            //???/???  ???????????????????????????,????????????????????????????????????????????????
            String district = location.getDistrict();//?????????/???
            /**??? ??????????????????  ????????????????????????*/
            String city = location.getCity();//?????????
            SPUtils.putString("location", district + " " + location.getStreet(), context);
            SPUtils.putString("AllLocation", city + district, context);
            SPUtils.putString(Constant.DISTRICT, district, context);
            if (district == null) {//??????????????????????????????????????????
                ToastUtils.showShortToast(context, "??????????????????????????????????????????");
                //????????????
            } else {
                //???????????????????????????????????????????????????????????????????????????
                showLoadingDialog();

                //V7?????????????????????????????????ID ,??????????????????????????????????????????????????????
                mPresent.newSearchCity(district);//???????????????
            }
        }
    }

    /**
     * ????????????  V7  API
     * ???????????????????????? /  ???????????????????????????  ?????????????????????????????????id??????????????????V7?????????????????????????????????
     *
     * @param response
     */
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            if (response.body().getLocation() != null && response.body().getLocation().size() > 0) {
                Log.d("newsearch", "----------------------------123------------------------------->");
                //??????Id
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
//                mPresent.HistoryRes(locationId,DateUtils.getYesterday_1(new Date()));
//                mPresent.HistoryAirRes(locationId, DateUtils.getYesterday_1(new Date()));
            } else {
                ToastUtils.showShortToast(context, "????????????");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchCityEvent event) {
        showLoadingDialog();
        //???????????????
        mPresent.newSearchCity(event.mLocation);
        Log.d("event22", "onEvent");
    }

    /**
     * ???????????????
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onSelectPhotosForPuzzleDoneListener(ArrayList<Photo> selectedPhotos) {
        PuzzleImgActivity.startWithPhotos(WeatherMainActivity.this, selectedPhotos, Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+Environment.DIRECTORY_PICTURES+"/"+getString(R.string.app_name),"IMG", Code.REQUEST_PUZZLE_SELECTOR,false, Setting.imageEngine);
    }
    /**
     * ??????APP??????
     */
    private void checkAppVersion() {
        AppVersion appVersion = LitePal.find(AppVersion.class, 1);//?????????????????????
        Log.d("appVersion", new Gson().toJson(appVersion.getVersionShort()));

        if (AppStartUpUtils.isTodayFirstStartApp(context)) {//?????????????????????APP
            if (!appVersion.getVersionShort().equals(APKVersionInfoUtils.getVerName(context))) {//????????????
                //??????????????????
                showUpdateAppDialog(appVersion.getInstall_url(), appVersion.getChangelog());
            }
        }

    }
    /**
     * ????????????????????????
     *
     * @param downloadUrl ????????????
     * @param updateLog   ????????????
     */
    private AlertDialog updateAppDialog = null;//????????????????????????

    private void showUpdateAppDialog(String downloadUrl, String updateLog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .addDefaultAnimation()//??????????????????
                .setCancelable(true)
                .setText(R.id.tv_update_info, updateLog)
                .setContentView(R.layout.dialog_update_app_tip)//??????????????????
                .setWidthAndHeight(SizeUtils.dp2px(context, 270), ViewGroup.LayoutParams.WRAP_CONTENT)//??????????????????
                .setOnClickListener(R.id.tv_cancel, v -> {//??????
                    updateAppDialog.dismiss();
                }).setOnClickListener(R.id.tv_fast_update, v -> {//????????????
                    //??????Apk
                    ToastUtils.showShortToast(context, "?????????????????????????????????????????????");
                    downloadApk(downloadUrl);
                    updateAppDialog.dismiss();
                });
        updateAppDialog = builder.create();
        updateAppDialog.show();
    }
    /**
     * ??????APK
     *
     * @param downloadUrl ????????????
     */
    private void downloadApk(String downloadUrl) {
        clearApk("nelson_weather.apk");
        //??????????????? ????????????????????????
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        //??????????????????????????????????????????????????????Wifi?????????
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //????????????????????????
        request.setAllowedOverRoaming(true);
        //??????????????????
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
        request.setMimeType(mimeString);
        //????????????????????????????????????????????????????????????
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("???????????????");
        request.setVisibleInDownloadsUi(true);//??????UI
        //sdcard????????????download?????????
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "GoodWeather.apk");
        //???????????????????????????
        downloadManager.enqueue(request);
    }
    /**
     * ??????APK
     *
     * @param apkName
     * @return
     */
    public static File clearApk(String apkName) {
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);
        if (apkFile.exists()) {
            apkFile.delete();
        }
        return apkFile;
    }

}