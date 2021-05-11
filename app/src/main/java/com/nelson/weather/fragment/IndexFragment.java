package com.nelson.weather.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nelson.mvplibrary.bean.Country;
import com.nelson.mvplibrary.bean.CountryScore;
import com.nelson.mvplibrary.bean.KeyScore;
import com.nelson.weather.R;
import com.nelson.weather.WeatherMainActivity;
import com.nelson.weather.adapter.AreaAdapter;
import com.nelson.weather.adapter.CityAdapter;
import com.nelson.weather.adapter.PanoramaAdapter;
import com.nelson.weather.adapter.ProvinceAdapter;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.AllDatas;
import com.nelson.weather.bean.CityResponse;
import com.nelson.weather.bean.DailyIndexBean;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.HistoryAirResponse;
import com.nelson.weather.bean.HistoryResponse;
import com.nelson.weather.bean.HourlyIndexBean;
import com.nelson.weather.bean.HourlyResponse;
import com.nelson.weather.bean.LifeIndexBean;
import com.nelson.weather.bean.LifestyleResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.bean.NowResponse;
import com.nelson.weather.bean.WarningResponse;
import com.nelson.weather.contract.WeatherContract;
import com.nelson.weather.eventbus.SearchCityEvent;
import com.nelson.weather.ui.CommonlyUsedCityActivity;
import com.nelson.weather.ui.MapWeatherActivity;
import com.nelson.weather.ui.PanoramaActivity;
import com.nelson.weather.ui.SearchCityActivity;
import com.nelson.weather.ui.WarnActivity;
import com.nelson.weather.ui.WorldCityActivity;
import com.nelson.weather.utils.CodeToStringUtils;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.DateUtils;
import com.nelson.weather.utils.LiWindow;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.SizeUtils;
import com.nelson.weather.utils.ToastUtils;
import com.nelson.weather.utils.WeatherUtil;
import com.nelson.mvplibrary.base.BaseBean;
import com.nelson.mvplibrary.mvp.MvpFragment;
import com.nelson.mvplibrary.utils.AnimationUtil;
import com.nelson.mvplibrary.view.AlwaysMarqueeTextView;
import com.google.gson.Gson;
import com.nelson.weather.view.DailyItemView;
import com.nelson.weather.view.DailyView;
import com.nelson.weather.view.HourlyItemView;
import com.nelson.weather.view.HourlyView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

import static com.nelson.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimationRight;

//import com.nelson.weather.ad.adadaptermanager.AcbExpressAdapterManager;

public class IndexFragment extends MvpFragment<WeatherContract.WeatherPresenter> implements OnGetSuggestionResultListener, WeatherContract.IWeatherView , PanoramaAdapter.OnItemClickListener{

    public static final String location = "101010100";
    public static final int ITEM_HOURLY = 0;
    public static final int ITEM_DAILY = 1;
    public static final int ITEM_LIFE = 2;

    SmartRefreshLayout refreshLayout;
    ImageView ivIllu;
    ImageView ivBig;
    AlwaysMarqueeTextView tv_scroll;
    TextView tv_temp;
    TextView tv_weather_info;
    TextView tv_air;
    TextView tv_wind_direction;
    TextView tv_wind_power;
    TextView tv_humidity;
    TextView tv_temp_today;
    TextView tv_air_today;
    ImageView iv_air_today;
    TextView tv_temp_tomorrow;
    TextView tv_air_tomorrow;
    ImageView iv_air_tomorrow;
    RecyclerView rv;
    GoToAirPage goToAirPage;

    NewCitySearch newCitySearch;

    TextView index_location;
    ImageView tv_addlocation;
    List<BaseBean> list;
    BDLocation bdLocation;
    ImageView iv_add;
    AlwaysMarqueeTextView tvWarn;
    Toolbar toolbar;
    ImageView location_btn;
    LinearLayout index_nolocation;
    HourlyView hourlyView;
    TextView tvSunrise;
    TextView tvSunset;
    FrameLayout frameLayout;
    DailyView dailyView;
    ViewPager vp;
    LinearLayout llLife;

    //定位器
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private boolean ifShowDialog;
    private String warnBodyString = null;//灾害预警数据字符串
    private int iconCode = 0;
    private GoToDaily goToDaily ;

    //
    private boolean isScroll;
    private long oldTime = 0;
    private long nowTime = 0;

    private Handler handler = new Handler();
    private Runnable task;
    private Runnable task_2;
    private int delay = 10000;
    public static final int LIFE_PAGE_NUM = 2;
    private PanoramaAdapter panoramaAdapter;
    private SuggestionSearch mSuggestionSearch = null;
    private RecyclerView mSugListView;

    @Override
    protected WeatherContract.WeatherPresenter createPresent() {
        return new WeatherContract.WeatherPresenter();
    }
    public IndexFragment(){

    }

    public IndexFragment(GoToDaily goToDaily) {
        this.goToDaily = goToDaily;
    }

    public IndexFragment(GoToAirPage goToAirPage, NewCitySearch newCitySearch, GoToDaily goToDaily){
        this.goToAirPage = goToAirPage;
        this.newCitySearch = newCitySearch;
        this.goToDaily = goToDaily;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        refreshLayout = getActivity().findViewById(R.id.refresh);
        ivIllu = getActivity().findViewById(R.id.iv_illustration);
        ivBig = getActivity().findViewById(R.id.iv_weather_big);
        tv_scroll = getActivity().findViewById(R.id.tv_scroll);
        tv_temp = getActivity().findViewById(R.id.tv_temperature);
        tv_weather_info = getActivity().findViewById(R.id.tv_weather_info);
        tv_air = getActivity().findViewById(R.id.tv_air);
        tv_wind_direction = getActivity().findViewById(R.id.tv_wind_direction);
        tv_wind_power = getActivity().findViewById(R.id.tv_wind_power);
        tv_humidity = getActivity().findViewById(R.id.tv_humidity);
        tv_temp_today = getActivity().findViewById(R.id.tv_temp_today);
        tv_air_today = getActivity().findViewById(R.id.tv_air_today);
        iv_air_today = getActivity().findViewById(R.id.iv_air_today);
        tv_temp_tomorrow = getActivity().findViewById(R.id.tv_temp_tomorrow);
        tv_air_tomorrow = getActivity().findViewById(R.id.tv_air_tomorrow);
        iv_air_tomorrow = getActivity().findViewById(R.id.iv_air_tomorrow);
        index_location = getActivity().findViewById(R.id.index_location);
        tv_addlocation = getActivity().findViewById(R.id.tv_addLocation);
        tvWarn = getActivity().findViewById(R.id.tv_warn);
        iv_add = getActivity().findViewById(R.id.iv_add);
        tv_scroll.setHorizontallyScrolling(true);
        tvWarn.setHorizontallyScrolling(true);
        toolbar = getActivity().findViewById(R.id.toolbar);
        location_btn = getActivity().findViewById(R.id.location_btn);
        index_nolocation = getActivity().findViewById(R.id.index_nolocation);
        hourlyView = (HourlyView) getActivity().findViewById(R.id.hourly_view);
        tvSunrise = (TextView) getActivity().findViewById(R.id.tv_sunrise);
        tvSunset = (TextView) getActivity().findViewById(R.id.tv_sunset);
        dailyView = (DailyView) getActivity().findViewById(R.id.daily_view);
        frameLayout = (FrameLayout) getActivity().findViewById(R.id.item_15day);
        mSugListView = (RecyclerView)getActivity().findViewById(R.id.rv_panorma);
        vp = (ViewPager) getActivity().findViewById(R.id.vp_life);
        llLife = (LinearLayout) getActivity().findViewById(R.id.ll_dots);
        tv_addlocation.post(new Runnable() {
            @Override
            public void run() {
                int left = tv_addlocation.getLeft();
                int top = tv_addlocation.getTop();
                int right = tv_addlocation.getRight();
                int bottom = tv_addlocation.getBottom();
                int diff = 50;
                ((View)tv_addlocation.getParent()).setTouchDelegate(new TouchDelegate(
                        new Rect(left-diff, top-diff, right+diff, bottom+diff), tv_addlocation));
            }
        });

        //由于这个刷新框架默认是有下拉和上拉，但是上拉没有用到，为了不造成误会，我这里禁用上拉
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshData();
        });
        //初始化弹窗
        mPopupWindow = new PopupWindow(getActivity());
        animUtil = new AnimationUtil();

        list = new ArrayList<>(5);
        list.clear();
        list.add(new HourlyIndexBean());
        list.add(new DailyIndexBean());
        list.add(new LifeIndexBean());

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddWindow();//更多功能弹窗
                toggleBright();//计算动画时间
            }
        });

//        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.icon_more));
//        toolbar.inflateMenu(R.menu.index_menu);
        /**
         *跳转到空气质量
         */
        tv_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAirPage.isonclick();
            }
        });
        /**
         * 重新定位
         */
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpenLocationServiceEnable()) {
                    startLocation();//开始定位
                    location_btn.setVisibility(View.GONE);
                    index_nolocation.setVisibility(View.GONE);
                    //showLoadingDialog();
                } else {
                    ToastUtils.showShortToast(context, "(((φ(◎ロ◎;)φ)))，你好像忘记打开定位功能了");
                }
            }
        });
        index_nolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showLongToast(context,"开启定位后，下拉刷新或点击定位即可开始使用啦～");
            }
        });
        tv_addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MapWeatherActivity.class));
            }
        });
        tvWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//缓存标识
                Intent intent = new Intent(context, WarnActivity.class);
                intent.putExtra("warnBodyString", warnBodyString);
                startActivity(intent);
            }
        });

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        Refresh();
    }

    /**
     * 实况天气数据返回  V7
     *
     * @param response
     */
    @Override
    public void getNowResult (Response< NowResponse > response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {//200则成功返回数据
            //根据V7版本的原则，只要是200就一定有数据，我们可以不用做判空处理，但是，为了使程序不ANR，还是要做的，信自己得永生
            NowResponse data = response.body();
            if (data != null) {
                NowRefresh(data);
            } else {
                ToastUtils.showShortToast(context, "暂无实况天气数据");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }
    private void NowRefresh (NowResponse data){
        tv_temp.setText(data.getNow().getTemp());
        tv_weather_info.setText(data.getNow().getText());
        tv_wind_direction.setText(data.getNow().getWindDir());
        tv_wind_power.setText(data.getNow().getWindScale() + "级");
        tv_humidity.setText(data.getNow().getHumidity() + "%");

        //顶部背景图
        iconCode = Integer.parseInt(data.getNow().getIcon());
        WeatherUtil.changeIllustration(ivIllu, iconCode, DateUtils.getNowTime(), context);
        //滚动文案
        WeatherUtil.changeDescription(tv_scroll, data.getNow().getText());
        int code = Integer.parseInt(data.getNow().getIcon());
        if(!WeatherUtil.isSunrise(DateUtils.getNowTime(), context)) {
            code = WeatherUtil.iconCode2Night(code);
        }
        WeatherUtil.changeIcon(ivBig, code);

        //实况天气中的现在
        HourlyResponse.HourlyBean hourlyBean = now2hourly(data);
        HourlyIndexBean hourlyIndexBean = (HourlyIndexBean) list.get(ITEM_HOURLY);
        hourlyIndexBean.setNowBean(hourlyBean);
        list.set(ITEM_HOURLY, hourlyIndexBean);
    }

    /**
     * 天气预报数据返回  V7
     *
     * @param response
     */
    @Override
    public void getDailyResult(Response<DailyResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyBean> dailyBeans = response.body().getDaily();
            if (dailyBeans != null && dailyBeans.size() > 0) {
               DailyRefresh(response.body());
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }
    private void DailyRefresh(DailyResponse dailyResponse){
        List<DailyResponse.DailyBean> dailyBeans = dailyResponse.getDaily();
        DailyIndexBean dailyIndexBean = (DailyIndexBean) list.get(ITEM_DAILY);
        dailyIndexBean.setDailyBeans(dailyBeans);
        list.set(ITEM_DAILY, dailyIndexBean);

//                15天预报
        tv_temp_today.setText(dailyBeans.get(0).getTempMax() + "/" + dailyBeans.get(0).getTempMin() + "℃");
        tv_temp_tomorrow.setText(dailyBeans.get(1).getTempMax() + "/" + dailyBeans.get(1).getTempMin() + "℃");


//                24小时预报中的日出日落
        HourlyIndexBean hourlyIndexBean;
        hourlyIndexBean = (HourlyIndexBean) list.get(ITEM_HOURLY);
        hourlyIndexBean.setSunrise(dailyBeans.get(0).getSunrise());
        hourlyIndexBean.setSunset(dailyBeans.get(0).getSunset());
        hourlyIndexBean.setViewType(ITEM_HOURLY);
        list.set(ITEM_HOURLY, hourlyIndexBean);

        SPUtils.putString(Constant.SUNRISE,dailyBeans.get(0).getSunrise(),context);
        SPUtils.putString(Constant.SUNSET,dailyBeans.get(0).getSunset(),context);
        WeatherUtil.changeIllustration(ivIllu, iconCode, DateUtils.getNowTime(), context);
        initDaily(dailyIndexBean);
    }

    /**
     * 逐小时天气数据返回  V7
     *
     * @param response
     */
    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            HourlyResponse data = response.body();
            HourlyRefresh(data);
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    private void  HourlyRefresh(HourlyResponse hourlyResponse){
        List<HourlyResponse.HourlyBean> hourlyBeans = hourlyResponse.getHourly();
        HourlyIndexBean hourlyIndexBean = (HourlyIndexBean) list.get(ITEM_HOURLY);
        hourlyIndexBean.setHourlyBeans(hourlyBeans);
        list.set(ITEM_HOURLY, hourlyIndexBean);
        initHourly(hourlyIndexBean);
    }

    /**
     * 空气质量返回  V7
     *
     * @param response
     */
    @Override
    public void getMoreAirFiveResult(Response<MoreAirFiveResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            //List<MoreAirFiveResponse.DailyBean> data = response.body().getDaily();
            if (response.body() != null) {
                MoreAirFiveRefresh(response.body());
            } else {
                ToastUtils.showShortToast(context, "空气质量数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }
    private void MoreAirFiveRefresh(MoreAirFiveResponse moreAirFiveResponse){
        List<MoreAirFiveResponse.DailyBean> data = moreAirFiveResponse.getDaily();


//                今明空气质量
        tv_air_today.setText(WeatherUtil.getAirText(data.get(0).getCategory()));
        iv_air_today.setImageResource(getAirBlock(data.get(0).getCategory()));
        tv_air_tomorrow.setText(WeatherUtil.getAirText(data.get(1).getCategory()));
        iv_air_tomorrow.setImageResource(getAirBlock(data.get(1).getCategory()));

        //24小时预报中的空气质量
        HourlyIndexBean hourlyIndexBean = (HourlyIndexBean) list.get(ITEM_HOURLY);
        hourlyIndexBean.setAir(data.get(0).getCategory());
        hourlyIndexBean.setViewType(ITEM_HOURLY);
        list.set(ITEM_HOURLY, hourlyIndexBean);

        //15天预报中的空气质量
        DailyIndexBean dailyIndexBean = (DailyIndexBean) list.get(ITEM_DAILY);
        dailyIndexBean.setAirBeans(moreAirFiveResponse.getDaily());
        list.set(ITEM_DAILY, dailyIndexBean);
    }


    @Override
    public void getLifestyleResult(Response<LifestyleResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            LifestyleResponse data = response.body();
            LifestyleRefresh(data);
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }
    private void LifestyleRefresh(LifestyleResponse data){
        if(list.size()==5){
            LifeIndexBean lifeIndexBean = (LifeIndexBean) list.get(ITEM_LIFE);
            lifeIndexBean.setViewType(ITEM_LIFE);
            lifeIndexBean.setLifestyleResponse(data);
            initLifeStyle(lifeIndexBean);
            list.set(ITEM_LIFE, lifeIndexBean);
        }else if (list.size()==4){
            LifeIndexBean lifeIndexBean = (LifeIndexBean) list.get(3);
            lifeIndexBean.setViewType(3);
            lifeIndexBean.setLifestyleResponse(data);
            initLifeStyle(lifeIndexBean);
            list.set(3, lifeIndexBean);
        }else if(list.size()==3){
            LifeIndexBean lifeIndexBean = (LifeIndexBean) list.get(2);
            lifeIndexBean.setViewType(2);
            lifeIndexBean.setLifestyleResponse(data);
            initLifeStyle(lifeIndexBean);
            list.set(2, lifeIndexBean);
        }

    }

    /**
     * 灾害预警返回
     *
     * @param response
     */
    @Override
    public void getNowWarnResult(Response<WarningResponse> response) {
//        dismissLoadingDialog();//关闭加载弹窗
//        checkAppVersion();//检查版本信息
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            NowWarnRefresh(response.body());
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }
    public void NowWarnRefresh(WarningResponse response){
        List<WarningResponse.WarningBean> data = response.getWarning();
        if (data != null && data.size() > 0) {
            warnBodyString = new Gson().toJson(response);
            tvWarn.setText(data.get(0).getTitle() + "   " + data.get(0).getText());//设置滚动标题和内容
        } else {//没有该城市预警有隐藏掉这个TextView
            tvWarn.setVisibility(View.GONE);
        }
    }

    @Override
    public void getHistoryResult(Response<HistoryResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            historyRefresh(response.body());
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    private void historyRefresh(HistoryResponse response) {
        if (response.getWeatherDaily() == null || response.getWeatherHourly().size()==0) {
            return;
        }
        DailyResponse.DailyBean dailyBean = history2daily(response);

        DailyIndexBean dailyIndexBean = (DailyIndexBean) list.get(ITEM_DAILY);
        dailyIndexBean.setHisDailyBean(dailyBean);

        if(list.size()==5){
            dailyIndexBean.setViewType(ITEM_DAILY);
            list.set(ITEM_DAILY, dailyIndexBean);
        }else if(list.size()==4){
            dailyIndexBean.setViewType(1);
            list.set(1, dailyIndexBean);
        }else if(list.size()==3){
            dailyIndexBean.setViewType(0);
            list.set(0, dailyIndexBean);
        }
    }

    @Override
    public void getHistoryAirResult(Response<HistoryAirResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            Log.e("gqc", "getHistoryAirResult: " );
            historyAirRefresh(response.body());
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    private void historyAirRefresh(HistoryAirResponse response) {
        if (response.getAirHourly() == null || response.getAirHourly().size()==0) {
            return;
        }
        MoreAirFiveResponse.DailyBean airBean = history2air(response);

        DailyIndexBean dailyIndexBean = (DailyIndexBean) list.get(ITEM_DAILY);
        dailyIndexBean.setHisAirBean(airBean);
        if(list.size()==5){

        }else if(list.size()==4){

        }else if(list.size()==3){

        }
        list.set(ITEM_DAILY, dailyIndexBean);
    }

    private void airNowRefresh(AirNowResponse.NowBean nowBean){
        tv_air.setText(nowBean.getAqi() + " " + nowBean.getCategory());
//                空气质量树叶图标
        Drawable icon = getAirIcon(nowBean.getCategory());
        icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
        tv_air.setCompoundDrawables(icon, null, null, null);
    }


    @Override
    public void getWeatherDataFailed() {
        refreshLayout.finishRefresh();//关闭刷新
        dismissLoadingDialog();//关闭弹窗
        ToastUtils.showShortToast(context, "天气数据获取异常");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    private HourlyResponse.HourlyBean now2hourly(NowResponse nowResponse) {
        NowResponse.NowBean nowBean = nowResponse.getNow();
        HourlyResponse.HourlyBean hourlyBean = new HourlyResponse.HourlyBean();
        hourlyBean.setFxTime(nowBean.getObsTime());
        hourlyBean.setTemp(nowBean.getTemp());
        hourlyBean.setText(nowBean.getText());
        hourlyBean.setIcon(nowBean.getIcon());
        hourlyBean.setWind360(nowBean.getWind360());
        hourlyBean.setWindDir(nowBean.getWindDir());
        hourlyBean.setWindScale(nowBean.getWindScale());
        hourlyBean.setWindSpeed(nowBean.getWindSpeed());
        hourlyBean.setHumidity(nowBean.getHumidity());
        hourlyBean.setPrecip(nowBean.getPrecip());
        hourlyBean.setPressure(nowBean.getPressure());
        hourlyBean.setDew(nowBean.getDew());
        hourlyBean.setCloud(nowBean.getCloud());
        hourlyBean.setPop("-");

        return hourlyBean;
    }

    /**
     * 历史天气数据转换
     *
     * @param historyResponse
     * @return
     */
    private DailyResponse.DailyBean history2daily(HistoryResponse historyResponse) {
        HistoryResponse.WeatherDailyDTO dailyDTO = historyResponse.getWeatherDaily();
        HistoryResponse.WeatherHourlyDTO hourlyDay = historyResponse.getWeatherHourly().get(13); //14：00
        HistoryResponse.WeatherHourlyDTO hourlyNight = historyResponse.getWeatherHourly().get(20); //21：00
        DailyResponse.DailyBean dailyBean = new DailyResponse.DailyBean();
        dailyBean.setFxDate(dailyDTO.getDate());
        dailyBean.setTempMax(dailyDTO.getTempMax());
        dailyBean.setIconDay(hourlyDay.getIcon());
        dailyBean.setIconNight(hourlyNight.getIcon());
        dailyBean.setTempMin(dailyDTO.getTempMin());
        dailyBean.setTextDay(hourlyDay.getText());
        dailyBean.setTextNight(hourlyNight.getText());
        dailyBean.setWindDirDay(hourlyDay.getWindDir());
        dailyBean.setWindScaleDay(hourlyDay.getWindScale());

        return dailyBean;
    }

    private MoreAirFiveResponse.DailyBean history2air(HistoryAirResponse historyAirResponse) {
        HistoryAirResponse.AirHourlyDTO airHourlyDTO = historyAirResponse.getAirHourly().get(13);
        MoreAirFiveResponse.DailyBean dailyBean = new MoreAirFiveResponse.DailyBean();
        dailyBean.setFxDate(airHourlyDTO.getPubTime().substring(0, 10));
        dailyBean.setAqi(airHourlyDTO.getAqi());
        dailyBean.setCategory(airHourlyDTO.getCategory());
        dailyBean.setLevel(airHourlyDTO.getLevel());
        dailyBean.setPrimary(airHourlyDTO.getPrimary());

        return dailyBean;
    }

    private Drawable getAirIcon(String air) {
        switch (air) {
            case "优":
                return getResources().getDrawable(R.drawable.icon_air_1);
            case "良":
                return getResources().getDrawable(R.drawable.icon_air_2);
            case "轻度污染":
                return getResources().getDrawable(R.drawable.icon_air_3);
            case "中度污染":
                return getResources().getDrawable(R.drawable.icon_air_4);
            case "重度污染":
                return getResources().getDrawable(R.drawable.icon_air_5);
            case "严重污染":
                return getResources().getDrawable(R.drawable.icon_air_6);
        }
        return getResources().getDrawable(R.drawable.icon_air_1);
    }

    private int getAirBlock(String air) {
        switch (air) {
            case "优":
                return R.drawable.air_1_1;
            case "良":
                return R.drawable.air_1_2;
            case "轻度污染":
                return R.drawable.air_1_3;
            case "中度污染":
                return R.drawable.air_1_4;
            case "重度污染":
                return R.drawable.air_1_5;
            case "严重污染":
                return R.drawable.air_1_6;
        }
        return R.drawable.air_1_1;
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        List<HashMap<String, String>> suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.getKey() != null && info.getDistrict() != null && info.getCity() != null) {
                HashMap<String, String> map = new HashMap<>();
                map.put("key",info.getKey());
                map.put("tag",info.getTag());
                map.put("city",info.getCity());
                map.put("dis",info.getDistrict());
                map.put("uid",info.getUid());
                suggest.add(map);
            }
        }
        panoramaAdapter = new PanoramaAdapter(getActivity());
        panoramaAdapter.addData(suggest);
        panoramaAdapter.setOnItemClickListener(this::onCitysClick);
        mSugListView.setAdapter(panoramaAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        mSugListView.setLayoutManager(llm);
    }

    @Override
    public void onCitysClick(String key,String path, String city) {
        Intent intent = new Intent(context,PanoramaActivity.class);
        intent.putExtra("tag",key);
        intent.putExtra("city",city);
        intent.putExtra("path",path);
        startActivity(intent);
    }

    public interface GoToAirPage{
        void isonclick();
    }

    public interface NewCitySearch{
        void getNewCity(String newcity, boolean ifShowDialog);
    }

    @Override
    public void onResume() {

        index_location.setText(SPUtils.getString("AllLocation","暂无定位",context));
        if(!isOpenLocationServiceEnable()){
            location_btn.setVisibility(View.VISIBLE);
            index_nolocation.setVisibility(View.VISIBLE);
        }else {
            location_btn.setVisibility(View.GONE);
            index_nolocation.setVisibility(View.GONE);
        }


        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mSuggestionSearch.destroy();
        super.onDestroyView();
    }

    public void refreshData() {
        if(System.currentTimeMillis()-SPUtils.getLong(Constant.refreshTime,0,context) <60000){
            showLoadingDialog();
            Refresh();
            for(int i =0;i<1000000;i++) {
            }
            dismissLoadingDialog();
        }else {
            if(district == null) {
                district = SPUtils.getString(Constant.DISTRICT, "暂无定位", context);
            }
            if(district.equals("暂无定位")) {
                if (!isOpenLocationServiceEnable()) {
                    ToastUtils.showLongToast(context, "开启定位后，下拉刷新或点击定位即可开始使用啦～");
                } else {
                    startLocation();//开始定位
                }
            }
            else {
                EventBus.getDefault().post(new SearchCityEvent(district, city));
                SPUtils.putLong(Constant.refreshTime, System.currentTimeMillis(), context);
            }
        }
        if(!isOpenLocationServiceEnable()){
            location_btn.setVisibility(View.VISIBLE);
            index_nolocation.setVisibility(View.VISIBLE);
        }else {
            location_btn.setVisibility(View.GONE);
            index_nolocation.setVisibility(View.GONE);
        }
        refreshLayout.finishRefresh();
    }

    public void Refresh(){
        if(AllDatas.getInstance().getBdLocation()!=null){
            bdLocation = AllDatas.getInstance().getBdLocation();
            if(index_location!=null) {
                index_location.setText(SPUtils.getString(Constant.allLocation,"暂无定位",context));
            }
        }
        if(AllDatas.getInstance().getAirNowResponse()!=null) {
            airNowRefresh(AllDatas.getInstance().getAirNowResponse().getNow());
        }
        if(AllDatas.getInstance().getLifestyleResponse()!=null) {
            LifestyleRefresh(AllDatas.getInstance().getLifestyleResponse());
        }else {
            mPresent.lifestyle(AllDatas.getInstance().getLocationId());
        }
        if(AllDatas.getInstance().getMoreAirFiveResponse()!=null) {
            MoreAirFiveRefresh(AllDatas.getInstance().getMoreAirFiveResponse());
        }else {
            mPresent.airFive(AllDatas.getInstance().getLocationId());
        }
        if(AllDatas.getInstance().getHourlyResponse()!=null) {
            HourlyRefresh(AllDatas.getInstance().getHourlyResponse());
        }else {
            mPresent.hourlyWeather(AllDatas.getInstance().getLocationId());
        }
        if(AllDatas.getInstance().getDailyResponse()!=null) {
            DailyRefresh(AllDatas.getInstance().getDailyResponse());
        }else {
            mPresent.dailyWeather(AllDatas.getInstance().getLocationId());
        }
        if(AllDatas.getInstance().getNowResponse()!=null) {
            NowRefresh(AllDatas.getInstance().getNowResponse());
        }else {
            mPresent.nowWeather(AllDatas.getInstance().getLocationId());
        }
        if(AllDatas.getInstance().getWarningResponse()!=null) {
            NowWarnRefresh(AllDatas.getInstance().getWarningResponse());
        }else {
            mPresent.nowWarn(AllDatas.getInstance().getLocationId());
        }
        if(AllDatas.getInstance().getHistoryAirResponse()!=null) {
            historyAirRefresh(AllDatas.getInstance().getHistoryAirResponse());
        }
        if(AllDatas.getInstance().getHistoryResponse()!=null) {
            historyRefresh(AllDatas.getInstance().getHistoryResponse());
        }

        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        CountryScore countryScore = LitePal.order(Constant.countryScore+" desc").findFirst(CountryScore.class);
        KeyScore keyScore = LitePal.order(Constant.keyScore+" desc").findFirst(KeyScore.class);
        if(countryScore!=null||keyScore!=null) {
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                     .keyword(keyScore.getKeyName()) // 关键字
                     .city(countryScore.getCountryName())); // 城市
            }
        }
    //右上角的弹窗
    private PopupWindow mPopupWindow;
    private AnimationUtil animUtil;
    /**
     * 更多功能弹窗，因为区别于我原先写的弹窗
     */
    private void showAddWindow() {
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.window_add, null));// 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));// 设置pop透明效果
        mPopupWindow.setAnimationStyle(R.style.pop_add);// 设置pop出入动画
        mPopupWindow.setFocusable(true);// 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setTouchable(true);// 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setOutsideTouchable(true);// 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.showAsDropDown(iv_add, -100, 0);// 相对于 + 号正下面，同时可以设置偏移量
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//关闭弹窗
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });
        //绑定布局中的控件
        TextView changeCity = mPopupWindow.getContentView().findViewById(R.id.tv_change_city);//切换城市
        TextView wallpaper = mPopupWindow.getContentView().findViewById(R.id.tv_wallpaper);//全景地图  我才是这条Gai最靓的仔
        TextView searchCity = mPopupWindow.getContentView().findViewById(R.id.tv_search_city);//城市搜索
        TextView worldCity = mPopupWindow.getContentView().findViewById(R.id.tv_world_city);//世界城市  V7
        TextView residentCity = mPopupWindow.getContentView().findViewById(R.id.tv_resident_city);//常用城市
//        TextView aboutUs = mPopupWindow.getContentView().findViewById(R.id.tv_about_us);//关于我们
        TextView setting = mPopupWindow.getContentView().findViewById(R.id.tv_setting);//隐私政策
        changeCity.setOnClickListener(view -> {//切换城市
            showCityWindow();
            mPopupWindow.dismiss();
        });
        wallpaper.setOnClickListener(view -> {//壁纸管理
            startActivity(new Intent(context, PanoramaActivity.class));
            mPopupWindow.dismiss();
        });
        searchCity.setOnClickListener(view -> {//城市搜索
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//缓存标识
            startActivity(new Intent(context, SearchCityActivity.class));
            mPopupWindow.dismiss();
        });
        worldCity.setOnClickListener(view -> {//世界城市
            startActivity(new Intent(context, WorldCityActivity.class));
            mPopupWindow.dismiss();
        });
        residentCity.setOnClickListener(view -> {//常用城市
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//缓存标识
            startActivity(new Intent(context, CommonlyUsedCityActivity.class));
            mPopupWindow.dismiss();
        });
//        aboutUs.setOnClickListener(view -> {//关于我们
////            startActivity(new Intent(context, AboutUsActivity.class));
//            mPopupWindow.dismiss();
//        });
        setting.setOnClickListener(view -> {//隐私政策
//            startActivity(new Intent(context, SettingActivity.class));
            jumpToPolicyDetail();
            mPopupWindow.dismiss();
        });
    }
    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;//0.5s
    private static final float START_ALPHA = 0.7f;//开始透明度
    private static final float END_ALPHA = 1f;//结束透明度
    /**
     * 计算动画时间
     */
    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimationUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimationUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }
    private List<String> slist;//字符串列表
    private List<CityResponse> provinceList;//省列表数据
    private List<CityResponse.CityBean> citylist;//市列表数据
    private List<CityResponse.CityBean.AreaBean> arealist;//区/县列表数据
    private ProvinceAdapter provinceAdapter;//省数据适配器
    private CityAdapter cityAdapter;//市数据适配器
    private AreaAdapter areaAdapter;//县/区数据适配器
    private String provinceTitle;//标题
    private LiWindow liWindow;//自定义弹窗

    private String district = null;//区/县  改为全局的静态变量,方便更换城市之后也能进行下拉刷新
    private String city;//市 国控站点数据  用于请求空气质量

    /**
     * 城市弹窗
     */
    private void showCityWindow() {
        provinceList = new ArrayList<>();
        citylist = new ArrayList<>();
        arealist = new ArrayList<>();
        slist = new ArrayList<>();
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_city_list, null);
        ImageView areaBack = (ImageView) view.findViewById(R.id.iv_back_area);
        ImageView cityBack = (ImageView) view.findViewById(R.id.iv_back_city);
        TextView windowTitle = (TextView) view.findViewById(R.id.tv_title);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        liWindow.showRightPopupWindow(view);//显示弹窗
        initCityData(recyclerView, areaBack, cityBack, windowTitle);//加载城市列表数据
    }
    /**
     * 省市县数据渲染
     *
     * @param recyclerView 列表
     * @param areaBack     区县返回
     * @param cityBack     市返回
     * @param windowTitle  窗口标题
     */
    private void initCityData(RecyclerView recyclerView, ImageView areaBack, ImageView cityBack, TextView windowTitle) {
        //初始化省数据 读取省数据并显示到列表中
        try {
            //读取城市数据
            InputStream inputStream = getResources().getAssets().open("City.txt");//安卓读取csv
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String lines = bufferedReader.readLine();

            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }

            final JSONArray Data = new JSONArray(stringBuffer.toString());
            //循环这个文件数组、获取数组中每个省对象的名字
            for (int i = 0; i < Data.length(); i++) {
                JSONObject provinceJsonObject = Data.getJSONObject(i);
                String provinceName = provinceJsonObject.getString("name");
                CityResponse response = new CityResponse();
                response.setName(provinceName);
                provinceList.add(response);
            }

            //定义省份显示适配器
            provinceAdapter = new ProvinceAdapter(R.layout.item_city_list, provinceList);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(provinceAdapter);
            provinceAdapter.notifyDataSetChanged();
            //runLayoutAnimationRight(recyclerView);//动画展示
            provinceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    try {
                        //返回上一级数据
                        cityBack.setVisibility(View.VISIBLE);
                        cityBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setAdapter(provinceAdapter);
                                provinceAdapter.notifyDataSetChanged();
                                cityBack.setVisibility(View.GONE);
                                windowTitle.setText("中国");
                            }
                        });

                        //根据当前位置的省份所在的数组位置、获取城市的数组
                        JSONObject provinceObject = Data.getJSONObject(position);
                        windowTitle.setText(provinceList.get(position).getName());
                        provinceTitle = provinceList.get(position).getName();
                        final JSONArray cityArray = provinceObject.getJSONArray("city");

                        //更新列表数据
                        if (citylist != null) {
                            citylist.clear();
                        }

                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject cityObj = cityArray.getJSONObject(i);
                            String cityName = cityObj.getString("name");
                            CityResponse.CityBean response = new CityResponse.CityBean();
                            response.setName(cityName);
                            citylist.add(response);
                        }

                        cityAdapter = new CityAdapter(R.layout.item_city_list, citylist);
                        LinearLayoutManager manager1 = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(manager1);
                        recyclerView.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();
                        //runLayoutAnimationRight(recyclerView);

                        cityAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                try {
                                    //返回上一级数据
                                    areaBack.setVisibility(View.VISIBLE);
                                    areaBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            recyclerView.setAdapter(cityAdapter);
                                            cityAdapter.notifyDataSetChanged();
                                            areaBack.setVisibility(View.GONE);
                                            windowTitle.setText(provinceTitle);
                                            arealist.clear();
                                        }
                                    });
                                    //获取区/县的上级 市，用于请求空气质量数据API接口
                                    city = citylist.get(position).getName();
                                    //根据当前城市数组位置 获取地区数据
                                    windowTitle.setText(citylist.get(position).getName());
                                    JSONObject cityJsonObj = cityArray.getJSONObject(position);
                                    JSONArray areaJsonArray = cityJsonObj.getJSONArray("area");
                                    if (arealist != null) {
                                        arealist.clear();
                                    }
                                    if (slist != null) {
                                        slist.clear();
                                    }
                                    for (int i = 0; i < areaJsonArray.length(); i++) {
                                        slist.add(areaJsonArray.getString(i));
                                    }
                                    Log.i("slist", slist.toString());
                                    for (int j = 0; j < slist.size(); j++) {
                                        CityResponse.CityBean.AreaBean response = new CityResponse.CityBean.AreaBean();
                                        response.setName(slist.get(j).toString());
                                        arealist.add(response);
                                    }
                                    areaAdapter = new AreaAdapter(R.layout.item_city_list, arealist);
                                    LinearLayoutManager manager2 = new LinearLayoutManager(context);

                                    recyclerView.setLayoutManager(manager2);
                                    recyclerView.setAdapter(areaAdapter);
                                    areaAdapter.notifyDataSetChanged();
                                    runLayoutAnimationRight(recyclerView);

                                    areaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                        @Override
                                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                                            district = arealist.get(position).getName();//选中的区/县赋值给这个全局变量
                                            SPUtils.putString("AllLocation",city+district,context);
                                            newCitySearch.getNewCity(district, true);
                                            //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
//                                            mPresent.newSearchCity(district);//切换城市时
//                                            flag = false;//切换城市得到的城市不属于定位，因此这里隐藏定位图标
                                            liWindow.closePopupWindow();//关闭弹窗
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
//            outRect.left = mSpace;
//            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }

        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

    }
    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
    private void jumpToPolicyDetail() {
        String defaultUrl = "https://cdn.irigel.com/durian/Durian_privacy.html";
        String policyUrl = null;
//        if (TextUtils.equals(ChannelInfoUtil.getCurrentApkStore(WeatherApplication.getContext()), "HW")) {
//            policyUrl = IRGConfig.optString(defaultUrl, "Application", "ChannelInfo", "Store", "HW", "ServiceURL");
//        } else {
//            policyUrl = IRGConfig.optString(defaultUrl, "Application", "Modules", "ServiceURL");
//        }

        if (TextUtils.isEmpty(policyUrl)) {
            policyUrl = defaultUrl;
        }
    }
    /**
     * 手机是否开启位置服务，如果没有开启那么App将不能使用定位功能
     */
    private boolean isOpenLocationServiceEnable() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
    //定位
    private void startLocation() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getActivity());
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
        ifShowDialog = false;
    }
    /**
     * 定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            AllDatas.getInstance().setBdLocation(location);
            Log.d("TAG", location.toString());
            district = location.getDistrict();//获取区/县
            city = location.getCity();//获取市

            if (district == null) {//未获取到定位信息，请重新定位
                ToastUtils.showShortToast(context, "未获取到定位信息，请重新定位");
                //页面处理
            } else {
                //在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
                SPUtils.putString("location", district + " " + location.getStreet(), context);
                SPUtils.putString("AllLocation",city+district,context);
                SPUtils.putString(Constant.DISTRICT,district,context);
                //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
                EventBus.getDefault().post(new SearchCityEvent(district,city));//Adm2 代表市
            }
        }
    }
    private void initHourly(HourlyIndexBean hourlyIndexBean) {
//        if (list.get(position).getViewType() != ITEM_HOURLY) return;
        HourlyIndexBean bean = hourlyIndexBean;
        if (bean.getHourlyBeans() != null && bean.getHourlyBeans().size() > 0) {
            List<HourlyResponse.HourlyBean> hourlyBeans = new ArrayList<>();
            if (bean.getNowBean() != null) {
                hourlyBeans.add(bean.getNowBean());
            }
            hourlyBeans.addAll(bean.getHourlyBeans());

            hourlyView.setLineColor(Color.parseColor("#4287ed"), Color.parseColor("#e4eefc"));
            hourlyView.setLineWidth(8f);
            hourlyView.setPointRadius(12);
            hourlyView.setHollow(4);
            hourlyView.setList(hourlyBeans);
            //设置一屏幕显示几列(最少3列)
            try {
                hourlyView.setColumnNumber(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //点击某一列
            hourlyView.setOnWeatherItemClickListener(new HourlyView.OnWeatherItemClickListener() {
                @Override
                public void onItemClick(HourlyItemView itemView, int position, HourlyResponse.HourlyBean bean) {
                    HourlyResponse.HourlyBean clickData = hourlyBeans.get(position);
                    showHourlyWindow(clickData);
                }
            });
            hourlyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            hourlyView.clearSelected();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }

        if (bean.getSunrise() != null) {
            tvSunrise.setText(bean.getSunrise());
        }
        if (bean.getSunset() != null) {
            tvSunset.setText(bean.getSunset());
        }

        if (bean.getAir() != null) {
            hourlyView.setAir(bean.getAir());
        }
    }
    /**
     * 显示小时详情天气信息弹窗
     */
    private void showHourlyWindow(HourlyResponse.HourlyBean data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_hourly_detail, null);
        TextView tvTime = view.findViewById(R.id.tv_time);
        TextView tvTem = view.findViewById(R.id.tv_tem);
        TextView tvCondTxt = view.findViewById(R.id.tv_cond_txt);
        TextView tvWindDeg = view.findViewById(R.id.tv_wind_deg);
        TextView tvWindDir = view.findViewById(R.id.tv_wind_dir);
        TextView tvWindSc = view.findViewById(R.id.tv_wind_sc);
        TextView tvWindSpd = view.findViewById(R.id.tv_wind_spd);
        TextView tvHum = view.findViewById(R.id.tv_hum);
        TextView tvPres = view.findViewById(R.id.tv_pres);
        TextView tvPop = view.findViewById(R.id.tv_pop);
        TextView tvDew = view.findViewById(R.id.tv_dew);
        TextView tvCloud = view.findViewById(R.id.tv_cloud);

        String time = DateUtils.updateTime(data.getFxTime());

        tvTime.setText(String.format(getActivity().getResources().getString(R.string.index_tv_time), WeatherUtil.showTimeInfo(time), time));
        tvTem.setText(String.format(getActivity().getResources().getString(R.string.index_tv_temp), data.getTemp()));
        tvCondTxt.setText(data.getText());
        tvWindDeg.setText(String.format(getActivity().getResources().getString(R.string.index_tv_wind_deg), data.getWind360()));
        tvWindDir.setText(data.getWindDir());
        tvWindSc.setText(String.format(getActivity().getResources().getString(R.string.index_tv_wind_sc), data.getWindScale()));
        tvWindSpd.setText(String.format(getActivity().getResources().getString(R.string.index_tv_wind_spd), data.getWindSpeed()));
        tvHum.setText(data.getHumidity() + "%");
        tvPres.setText(String.format(getActivity().getResources().getString(R.string.index_tv_pres), data.getPressure()));
        tvPop.setText(data.getPop() + "%");
        tvDew.setText(String.format(getActivity().getResources().getString(R.string.index_tv_dew), data.getDew()));
        tvCloud.setText(data.getCloud() + "%");
        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 400), true);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initDaily(DailyIndexBean dailyIndexBean) {
        if (dailyIndexBean.getDailyBeans() != null && dailyIndexBean.getDailyBeans().size() > 0) {
            List<DailyResponse.DailyBean> dailyBeans = new ArrayList<>();
            if (dailyIndexBean.getHisDailyBean() != null) {
                dailyBeans.add(dailyIndexBean.getHisDailyBean());
            }
            dailyBeans.addAll(dailyIndexBean.getDailyBeans());
            setDailyView(dailyBeans);
            //点击某一列
            dailyView.setOnWeatherItemClickListener(new DailyView.OnWeatherItemClickListener() {
                @Override
                public void onItemClick(DailyItemView itemView, int position, DailyResponse.DailyBean bean) {
                    goToDaily.onRvItemClick(position);
                }
            });
            dailyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            dailyView.clearSelected();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }

        if (dailyIndexBean.getAirBeans() != null) {
            List<MoreAirFiveResponse.DailyBean> airBeans = new ArrayList<>();
            if (dailyIndexBean.getHisDailyBean() != null) {
                airBeans.add(dailyIndexBean.getHisAirBean());
            }
            airBeans.addAll(dailyIndexBean.getAirBeans());
            dailyView.setAir(airBeans);
        }
    }

    /**
     * 15日天气item的基本设置
     */
    private void setDailyView(List<DailyResponse.DailyBean> dailyBeans) {
        //设置白天和晚上线条的颜色
        dailyView.setDayAndNightLineColor(Color.parseColor("#E4AE47"), Color.parseColor("#4287ed"),
                Color.parseColor("#fdf3ea"), Color.parseColor("#edf4fd"));

        //设置线宽
        dailyView.setLineWidth(8);
        dailyView.setPointRadius(12);
        dailyView.setHollow(4);
        dailyView.setList(dailyBeans);
        //设置一屏幕显示几列(最少3列)
        try {
            dailyView.setColumnNumber(6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public interface GoToDaily {
        void onRvItemClick(int item);
    }
    private void initLifeStyle(LifeIndexBean lifeIndexBean) {
        for (BaseBean i : list){
            if (i instanceof LifeIndexBean){
                lifeIndexBean = (LifeIndexBean) i;
            }
        }
        if (lifeIndexBean.getLifestyleResponse() != null) {
            List<LifestyleResponse.DailyBean> data = lifeIndexBean.getLifestyleResponse().getDaily();
            //下方圆点
            ImageView[] dotArray = new ImageView[LIFE_PAGE_NUM];
            if (llLife.getChildCount() == 0) {
                for (int i = 0; i < LIFE_PAGE_NUM; i++) {
                    dotArray[i] = new ImageView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                    if (i != 0) {
                        params.leftMargin = 20;
                        dotArray[i].setImageResource(R.drawable.point_next);
                    } else {
                        dotArray[i].setImageResource(R.drawable.point_current);
                    }
                    dotArray[i].setLayoutParams(params);
                    llLife.addView(dotArray[i]);
                }
            }

            vp.setAdapter(new com.nelson.weather.adapter.LifeViewPagerAdapter(context, data));
            vp.setCurrentItem(com.nelson.weather.adapter.LifeViewPagerAdapter.maxLoop / 2);
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < LIFE_PAGE_NUM; i++) {
                        ImageView dot = (ImageView) (llLife.getChildAt(i));
                        if (position % 2 == i) {
                            dot.setImageResource(R.drawable.point_next);
                        } else {
                            dot.setImageResource(R.drawable.point_current);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }
}