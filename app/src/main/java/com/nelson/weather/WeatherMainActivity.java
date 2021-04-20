package com.nelson.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.huantansheng.easyphotos.constant.Code;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;
import com.nelson.weather.adapter.IndexAdapter;
import com.nelson.weather.adapter.TabAdapter;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.AllDatas;
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
import com.nelson.weather.ui.PuzzleImgActivity;
import com.nelson.weather.utils.CodeToStringUtils;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.DateUtils;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.ToastUtils;
import com.nelson.weather.view.ShareView;
import com.nelson.mvplibrary.mvp.MvpActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.huantansheng.easyphotos.ui.PuzzleSelectorActivitySecond;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import retrofit2.Response;

/**
 * @author nelson
 */
public class WeatherMainActivity extends MvpActivity<AllDataContract.AllDataPresenter> implements PuzzleSelectorActivitySecond.OnSelectPhotosForPuzzleDoneListener, AllDataContract.IAllDataView, IndexAdapter.OnRecyclerViewItemClick, CusFragment.ToAirQuality,
        IndexFragment.GoToAirPage, IndexFragment.NewCitySearch,IndexFragment.GoToDaily {

    /**定位器*/
    public LocationClient mLocationClient = null;
    private final MyLocationListener myListener = new MyLocationListener();
    private ViewPager2 viewPager2;
    private final List<Integer> icons = new ArrayList<>();
    private final List<Integer> iconsNotChoose = new ArrayList<>();
    private final List<Integer> iconsStart = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    private String locationId = null;//城市id，用于查询城市数据  V7版本 中 才有
    private boolean ifShowDialog;
    private IndexFragment indexFragment;
    private final Boolean flagOther = false;
    private final Stack<Integer> fragQueue = new Stack<>();
    private final int pos = 0;

    private ShareView shareView;

    private boolean isAnimatorEnd;
    private final String[] tabEventIds = {
            Constant.APP_HOMETAB_CLICK,
            Constant.APP_7DAYSTAB_CLICK,
            Constant.APP_AIRCONDTIONTAB_CLICK
    };

    private final Handler handler = new Handler();
    private Runnable task;
    private final int delay = 2000;




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("ad_debug", "onKeyDown: this id BACK AD");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (fragQueue.empty()) {
                fragQueue.add(0);
            }
            fragQueue.pop();
            if (fragQueue.empty()) {
               return true;
            } else if (fragQueue.size() == 1) {
                viewPager2.setCurrentItem(0);
                return true;
            } else {
                viewPager2.setCurrentItem(fragQueue.pop());
                int temp = fragQueue.pop();
                fragQueue.clear();
                fragQueue.push(0);
                fragQueue.push(temp);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AcbExpressManager.getInstance().preloadAdView(context, "SHEEP01", "indexSheep01Chance");

        Log.d("yytTest", "onCreate: ++++++++++++++++++MainActivity");
        fragQueue.push(0);
        TabLayout bottomTabLayout = findViewById(R.id.bottom_tab);
        viewPager2 = findViewById(R.id.view_pager2);
        viewPager2.setUserInputEnabled(false);
        titles.add("综合预报");
        titles.add("15日天气");
        titles.add("空气质量");
        titles.add("壁纸");
        icons.add(R.drawable.choose_comprehensive);
        icons.add(R.drawable.choose_15_days);
        icons.add(R.drawable.choose_air_quality);
        icons.add(R.drawable.choose_air_quality);

        iconsNotChoose.add(R.drawable.not_choose_comprehensive);
        iconsNotChoose.add(R.drawable.not_choose_15_days);
        iconsNotChoose.add(R.drawable.not_choose_air_quality);
        iconsNotChoose.add(R.drawable.not_choose_air_quality);

        iconsStart.add(R.drawable.choose_comprehensive);
        iconsStart.add(R.drawable.not_choose_15_days);
        iconsStart.add(R.drawable.not_choose_air_quality);
        iconsStart.add(R.drawable.not_choose_air_quality);

        DailyFragment dailyFragment = new DailyFragment();
        indexFragment = new IndexFragment(this, this, this);
        AriQualityFragment ariQualityFragment = new AriQualityFragment();

        fragments.add(indexFragment);
        fragments.add(dailyFragment);
        fragments.add(ariQualityFragment);
        fragments.add(new WallpaperFragment());
        //实例化适配器
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        //设置适配器
        viewPager2.setAdapter(tabAdapter);
        //TabLayout和Viewpager2进行关联
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
                } else if (fragQueue.peek() != position) {
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

    }

    /**
     * 获取AllData里的数据。
     * 通过getinstance得到。
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
//        indexFragment.Refresh();

    }

    @Override
    public void getMoreDailyResult(Response<DailyResponse> response) {
        AllDatas.getInstance().setDailyResponse(response.body());
//        indexFragment.Refresh();
    }

    @Override
    public void getMoreAirFiveResult(Response<MoreAirFiveResponse> response) {
        AllDatas.getInstance().setMoreAirFiveResponse(response.body());
//        indexFragment.Refresh();
    }

    @Override
    public void getMoreLifestyleResult(Response<LifestyleResponse> response) {
        AllDatas.getInstance().setLifestyleResponse(response.body());
//        indexFragment.Refresh();
    }

    @Override
    public void getHistoryResult(Response<HistoryResponse> response) {
        AllDatas.getInstance().setHistoryResponse(response.body());
//        indexFragment.Refresh();
    }

    @Override
    public void getHistoryAirResult(Response<HistoryAirResponse> response) {
        AllDatas.getInstance().setHistoryAirResponse(response.body());
//        indexFragment.Refresh();
    }

    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        AllDatas.getInstance().setHourlyResponse(response.body());
//        indexFragment.Refresh();
    }

    @Override
    public void getNowWarnResult(Response<WarningResponse> response) {
        AllDatas.getInstance().setWarningResponse(response.body());
        indexFragment.Refresh();
        dismissLoadingDialog();
    }

    @Override
    public void getNowResult(Response<NowResponse> response) {
        if (response.body() != null) {
            AllDatas.getInstance().setNowResponse(response.body());
            mPresent.HistoryRes(locationId, DateUtils.updateTime_month(response.body().getUpdateTime()));
            mPresent.HistoryAirRes(locationId, DateUtils.updateTime_month(response.body().getUpdateTime()));
        }
        indexFragment.Refresh();
    }

    @Override
    public void getDataFailed() {
        dismissLoadingDialog();//关闭弹窗
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lambTest", "onResume: ------->MainActivity");

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
     * 通过点击首页15天预报跳转到15日天气
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
     * 通过点击空气质量卡片跳转到空气质量
     */
    @Override
    public void isonClick() {
        fragQueue.push(2);
        viewPager2.setCurrentItem(2);

    }

    /**
     * 通过点击index的空气质量跳转
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
     * 定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            AllDatas.getInstance().setBdLocation(location);
            Log.d("TAG", location.toString());
            //区/县  改为全局的静态变量,方便更换城市之后也能进行下拉刷新
            String district = location.getDistrict();//获取区/县
            /**市 国控站点数据  用于请求空气质量*/
            String city = location.getCity();//获取市
            SPUtils.putString("location", district + " " + location.getStreet(), context);
            SPUtils.putString("AllLocation", city + district, context);
            SPUtils.putString(Constant.DISTRICT, district, context);
            if (district == null) {//未获取到定位信息，请重新定位
                ToastUtils.showShortToast(context, "未获取到定位信息，请重新定位");
                //页面处理
            } else {
                //在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
                showLoadingDialog();

                //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
                mPresent.newSearchCity(district);//定位返回时
            }
        }
    }

    /**
     * 和风天气  V7  API
     * 通过定位到的地址 /  城市切换得到的地址  都需要查询出对应的城市id才行，所以在V7版本中，这是第一步接口
     *
     * @param response
     */
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            if (response.body().getLocation() != null && response.body().getLocation().size() > 0) {
                Log.d("newsearch", "----------------------------123------------------------------->");
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
//                mPresent.HistoryRes(locationId,DateUtils.getYesterday_1(new Date()));
//                mPresent.HistoryAirRes(locationId, DateUtils.getYesterday_1(new Date()));
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 切换城市接收切换信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchCityEvent event) {
        showLoadingDialog();
        //相应事件时
        mPresent.newSearchCity(event.mLocation);
        Log.d("event22", "onEvent");
    }

    /**
     * 页面销毁时
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


}