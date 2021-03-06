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

    //?????????
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private boolean ifShowDialog;
    private String warnBodyString = null;//???????????????????????????
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

        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshData();
        });
        //???????????????
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
                showAddWindow();//??????????????????
                toggleBright();//??????????????????
            }
        });

//        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.icon_more));
//        toolbar.inflateMenu(R.menu.index_menu);
        /**
         *?????????????????????
         */
        tv_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAirPage.isonclick();
            }
        });
        /**
         * ????????????
         */
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpenLocationServiceEnable()) {
                    startLocation();//????????????
                    location_btn.setVisibility(View.GONE);
                    index_nolocation.setVisibility(View.GONE);
                    //showLoadingDialog();
                } else {
                    ToastUtils.showShortToast(context, "(((??(?????????;)??)))???????????????????????????????????????");
                }
            }
        });
        index_nolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showLongToast(context,"?????????????????????????????????????????????????????????????????????");
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
                SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//????????????
                Intent intent = new Intent(context, WarnActivity.class);
                intent.putExtra("warnBodyString", warnBodyString);
                startActivity(intent);
            }
        });

        // ????????????????????????????????????????????????????????????
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        Refresh();
    }

    /**
     * ????????????????????????  V7
     *
     * @param response
     */
    @Override
    public void getNowResult (Response< NowResponse > response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {//200?????????????????????
            //??????V7???????????????????????????200????????????????????????????????????????????????????????????????????????????????????ANR???????????????????????????????????????
            NowResponse data = response.body();
            if (data != null) {
                NowRefresh(data);
            } else {
                ToastUtils.showShortToast(context, "????????????????????????");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }
    private void NowRefresh (NowResponse data){
        tv_temp.setText(data.getNow().getTemp());
        tv_weather_info.setText(data.getNow().getText());
        tv_wind_direction.setText(data.getNow().getWindDir());
        tv_wind_power.setText(data.getNow().getWindScale() + "???");
        tv_humidity.setText(data.getNow().getHumidity() + "%");

        //???????????????
        iconCode = Integer.parseInt(data.getNow().getIcon());
        WeatherUtil.changeIllustration(ivIllu, iconCode, DateUtils.getNowTime(), context);
        //????????????
        WeatherUtil.changeDescription(tv_scroll, data.getNow().getText());
        int code = Integer.parseInt(data.getNow().getIcon());
        if(!WeatherUtil.isSunrise(DateUtils.getNowTime(), context)) {
            code = WeatherUtil.iconCode2Night(code);
        }
        WeatherUtil.changeIcon(ivBig, code);

        //????????????????????????
        HourlyResponse.HourlyBean hourlyBean = now2hourly(data);
        HourlyIndexBean hourlyIndexBean = (HourlyIndexBean) list.get(ITEM_HOURLY);
        hourlyIndexBean.setNowBean(hourlyBean);
        list.set(ITEM_HOURLY, hourlyIndexBean);
    }

    /**
     * ????????????????????????  V7
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
                ToastUtils.showShortToast(context, "????????????????????????");
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

//                15?????????
        tv_temp_today.setText(dailyBeans.get(0).getTempMax() + "/" + dailyBeans.get(0).getTempMin() + "???");
        tv_temp_tomorrow.setText(dailyBeans.get(1).getTempMax() + "/" + dailyBeans.get(1).getTempMin() + "???");


//                24??????????????????????????????
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
     * ???????????????????????????  V7
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
     * ??????????????????  V7
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
                ToastUtils.showShortToast(context, "????????????????????????");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }
    private void MoreAirFiveRefresh(MoreAirFiveResponse moreAirFiveResponse){
        List<MoreAirFiveResponse.DailyBean> data = moreAirFiveResponse.getDaily();


//                ??????????????????
        tv_air_today.setText(WeatherUtil.getAirText(data.get(0).getCategory()));
        iv_air_today.setImageResource(getAirBlock(data.get(0).getCategory()));
        tv_air_tomorrow.setText(WeatherUtil.getAirText(data.get(1).getCategory()));
        iv_air_tomorrow.setImageResource(getAirBlock(data.get(1).getCategory()));

        //24??????????????????????????????
        HourlyIndexBean hourlyIndexBean = (HourlyIndexBean) list.get(ITEM_HOURLY);
        hourlyIndexBean.setAir(data.get(0).getCategory());
        hourlyIndexBean.setViewType(ITEM_HOURLY);
        list.set(ITEM_HOURLY, hourlyIndexBean);

        //15???????????????????????????
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
     * ??????????????????
     *
     * @param response
     */
    @Override
    public void getNowWarnResult(Response<WarningResponse> response) {
//        dismissLoadingDialog();//??????????????????
//        checkAppVersion();//??????????????????
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
            tvWarn.setText(data.get(0).getTitle() + "   " + data.get(0).getText());//???????????????????????????
        } else {//???????????????????????????????????????TextView
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
//                ????????????????????????
        Drawable icon = getAirIcon(nowBean.getCategory());
        icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
        tv_air.setCompoundDrawables(icon, null, null, null);
    }


    @Override
    public void getWeatherDataFailed() {
        refreshLayout.finishRefresh();//????????????
        dismissLoadingDialog();//????????????
        ToastUtils.showShortToast(context, "????????????????????????");
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
     * ????????????????????????
     *
     * @param historyResponse
     * @return
     */
    private DailyResponse.DailyBean history2daily(HistoryResponse historyResponse) {
        HistoryResponse.WeatherDailyDTO dailyDTO = historyResponse.getWeatherDaily();
        HistoryResponse.WeatherHourlyDTO hourlyDay = historyResponse.getWeatherHourly().get(13); //14???00
        HistoryResponse.WeatherHourlyDTO hourlyNight = historyResponse.getWeatherHourly().get(20); //21???00
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
            case "???":
                return getResources().getDrawable(R.drawable.icon_air_1);
            case "???":
                return getResources().getDrawable(R.drawable.icon_air_2);
            case "????????????":
                return getResources().getDrawable(R.drawable.icon_air_3);
            case "????????????":
                return getResources().getDrawable(R.drawable.icon_air_4);
            case "????????????":
                return getResources().getDrawable(R.drawable.icon_air_5);
            case "????????????":
                return getResources().getDrawable(R.drawable.icon_air_6);
        }
        return getResources().getDrawable(R.drawable.icon_air_1);
    }

    private int getAirBlock(String air) {
        switch (air) {
            case "???":
                return R.drawable.air_1_1;
            case "???":
                return R.drawable.air_1_2;
            case "????????????":
                return R.drawable.air_1_3;
            case "????????????":
                return R.drawable.air_1_4;
            case "????????????":
                return R.drawable.air_1_5;
            case "????????????":
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

        index_location.setText(SPUtils.getString("AllLocation","????????????",context));
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
                district = SPUtils.getString(Constant.DISTRICT, "????????????", context);
            }
            if(district.equals("????????????")) {
                if (!isOpenLocationServiceEnable()) {
                    ToastUtils.showLongToast(context, "?????????????????????????????????????????????????????????????????????");
                } else {
                    startLocation();//????????????
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
                index_location.setText(SPUtils.getString(Constant.allLocation,"????????????",context));
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

        // ??????????????????????????????????????????????????????onSuggestionResult()?????????
        CountryScore countryScore = LitePal.order(Constant.countryScore+" desc").findFirst(CountryScore.class);
        KeyScore keyScore = LitePal.order(Constant.keyScore+" desc").findFirst(KeyScore.class);
        if(countryScore!=null||keyScore!=null) {
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                     .keyword(keyScore.getKeyName()) // ?????????
                     .city(countryScore.getCountryName())); // ??????
            } else {
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword("??????") // ?????????
                        .city("??????")); // ??????
            }
        }
    //??????????????????
    private PopupWindow mPopupWindow;
    private AnimationUtil animUtil;
    /**
     * ?????????????????????????????????????????????????????????
     */
    private void showAddWindow() {
        // ??????????????????
        mPopupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.window_add, null));// ????????????????????????????????????????????????????????????????????????
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));// ??????pop????????????
        mPopupWindow.setAnimationStyle(R.style.pop_add);// ??????pop????????????
        mPopupWindow.setFocusable(true);// ??????pop????????????????????????false?????????????????????????????????Activity?????????pop??????Editor?????????focusable????????????true
        mPopupWindow.setTouchable(true);// ??????pop???????????????false??????????????????????????????true
        mPopupWindow.setOutsideTouchable(true);// ????????????pop????????????????????????false??????focusable???true???????????????????????????
        mPopupWindow.showAsDropDown(iv_add, -100, 0);// ????????? + ??????????????????????????????????????????
        // ??????pop??????????????????????????????????????????
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//????????????
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });
        //????????????????????????
        TextView changeCity = mPopupWindow.getContentView().findViewById(R.id.tv_change_city);//????????????
        TextView wallpaper = mPopupWindow.getContentView().findViewById(R.id.tv_wallpaper);//????????????  ???????????????Gai????????????
        TextView searchCity = mPopupWindow.getContentView().findViewById(R.id.tv_search_city);//????????????
        TextView worldCity = mPopupWindow.getContentView().findViewById(R.id.tv_world_city);//????????????  V7
        TextView residentCity = mPopupWindow.getContentView().findViewById(R.id.tv_resident_city);//????????????
//        TextView aboutUs = mPopupWindow.getContentView().findViewById(R.id.tv_about_us);//????????????
        TextView setting = mPopupWindow.getContentView().findViewById(R.id.tv_setting);//????????????
        changeCity.setOnClickListener(view -> {//????????????
            showCityWindow();
            mPopupWindow.dismiss();
        });
        wallpaper.setOnClickListener(view -> {//????????????
            startActivity(new Intent(context, PanoramaActivity.class));
            mPopupWindow.dismiss();
        });
        searchCity.setOnClickListener(view -> {//????????????
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//????????????
            startActivity(new Intent(context, SearchCityActivity.class));
            mPopupWindow.dismiss();
        });
        worldCity.setOnClickListener(view -> {//????????????
            startActivity(new Intent(context, WorldCityActivity.class));
            mPopupWindow.dismiss();
        });
        residentCity.setOnClickListener(view -> {//????????????
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//????????????
            startActivity(new Intent(context, CommonlyUsedCityActivity.class));
            mPopupWindow.dismiss();
        });
//        aboutUs.setOnClickListener(view -> {//????????????
////            startActivity(new Intent(context, AboutUsActivity.class));
//            mPopupWindow.dismiss();
//        });
        setting.setOnClickListener(view -> {//????????????
//            startActivity(new Intent(context, SettingActivity.class));
            jumpToPolicyDetail();
            mPopupWindow.dismiss();
        });
    }
    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;//0.5s
    private static final float START_ALPHA = 0.7f;//???????????????
    private static final float END_ALPHA = 1f;//???????????????
    /**
     * ??????????????????
     */
    private void toggleBright() {
        // ????????????????????????????????? ????????? ??????????????????????????????????????????????????????0.5f--1f???
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimationUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimationUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // ?????????????????????????????????????????????
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }
    private List<String> slist;//???????????????
    private List<CityResponse> provinceList;//???????????????
    private List<CityResponse.CityBean> citylist;//???????????????
    private List<CityResponse.CityBean.AreaBean> arealist;//???/???????????????
    private ProvinceAdapter provinceAdapter;//??????????????????
    private CityAdapter cityAdapter;//??????????????????
    private AreaAdapter areaAdapter;//???/??????????????????
    private String provinceTitle;//??????
    private LiWindow liWindow;//???????????????

    private String district = null;//???/???  ???????????????????????????,????????????????????????????????????????????????
    private String city;//??? ??????????????????  ????????????????????????

    /**
     * ????????????
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
        liWindow.showRightPopupWindow(view);//????????????
        initCityData(recyclerView, areaBack, cityBack, windowTitle);//????????????????????????
    }
    /**
     * ?????????????????????
     *
     * @param recyclerView ??????
     * @param areaBack     ????????????
     * @param cityBack     ?????????
     * @param windowTitle  ????????????
     */
    private void initCityData(RecyclerView recyclerView, ImageView areaBack, ImageView cityBack, TextView windowTitle) {
        //?????????????????? ????????????????????????????????????
        try {
            //??????????????????
            InputStream inputStream = getResources().getAssets().open("City.txt");//????????????csv
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String lines = bufferedReader.readLine();

            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }

            final JSONArray Data = new JSONArray(stringBuffer.toString());
            //??????????????????????????????????????????????????????????????????
            for (int i = 0; i < Data.length(); i++) {
                JSONObject provinceJsonObject = Data.getJSONObject(i);
                String provinceName = provinceJsonObject.getString("name");
                CityResponse response = new CityResponse();
                response.setName(provinceName);
                provinceList.add(response);
            }

            //???????????????????????????
            provinceAdapter = new ProvinceAdapter(R.layout.item_city_list, provinceList);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(provinceAdapter);
            provinceAdapter.notifyDataSetChanged();
            //runLayoutAnimationRight(recyclerView);//????????????
            provinceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    try {
                        //?????????????????????
                        cityBack.setVisibility(View.VISIBLE);
                        cityBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setAdapter(provinceAdapter);
                                provinceAdapter.notifyDataSetChanged();
                                cityBack.setVisibility(View.GONE);
                                windowTitle.setText("??????");
                            }
                        });

                        //????????????????????????????????????????????????????????????????????????
                        JSONObject provinceObject = Data.getJSONObject(position);
                        windowTitle.setText(provinceList.get(position).getName());
                        provinceTitle = provinceList.get(position).getName();
                        final JSONArray cityArray = provinceObject.getJSONArray("city");

                        //??????????????????
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
                                    //?????????????????????
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
                                    //?????????/???????????? ????????????????????????????????????API??????
                                    city = citylist.get(position).getName();
                                    //?????????????????????????????? ??????????????????
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

                                            district = arealist.get(position).getName();//????????????/??????????????????????????????
                                            SPUtils.putString("AllLocation",city+district,context);
                                            newCitySearch.getNewCity(district, true);
                                            //V7?????????????????????????????????ID ,??????????????????????????????????????????????????????
//                                            mPresent.newSearchCity(district);//???????????????
//                                            flag = false;//???????????????????????????????????????????????????????????????????????????
                                            liWindow.closePopupWindow();//????????????
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
     * ???????????????????????????????????????????????????????????????????????????
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // ???????????????????????????????????????????????????????????????
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
     * ?????????????????????????????????????????????????????????App???????????????????????????
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
    //??????
    private void startLocation() {
        //??????LocationClient???
        mLocationClient = new LocationClient(getActivity());
        //??????????????????
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        //?????????????????????????????????????????????????????????????????????true
        option.setIsNeedAddress(true);
        //???????????????????????????????????????????????????????????????????????????????????????false
        option.setNeedNewVersionRgc(true);
        //mLocationClient???????????????????????????LocationClient??????
        //??????????????????LocationClientOption???????????????setLocOption???????????????LocationClient????????????
        mLocationClient.setLocOption(option);
        //????????????
        mLocationClient.start();
        ifShowDialog = false;
    }
    /**
     * ??????????????????
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            AllDatas.getInstance().setBdLocation(location);
            Log.d("TAG", location.toString());
            district = location.getDistrict();//?????????/???
            city = location.getCity();//?????????

            if (district == null) {//??????????????????????????????????????????
                ToastUtils.showShortToast(context, "??????????????????????????????????????????");
                //????????????
            } else {
                //???????????????????????????????????????????????????????????????????????????
                SPUtils.putString("location", district + " " + location.getStreet(), context);
                SPUtils.putString("AllLocation",city+district,context);
                SPUtils.putString(Constant.DISTRICT,district,context);
                //V7?????????????????????????????????ID ,??????????????????????????????????????????????????????
                EventBus.getDefault().post(new SearchCityEvent(district,city));//Adm2 ?????????
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
            //???????????????????????????(??????3???)
            try {
                hourlyView.setColumnNumber(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //???????????????
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
     * ????????????????????????????????????
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
            //???????????????
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
     * 15?????????item???????????????
     */
    private void setDailyView(List<DailyResponse.DailyBean> dailyBeans) {
        //????????????????????????????????????
        dailyView.setDayAndNightLineColor(Color.parseColor("#E4AE47"), Color.parseColor("#4287ed"),
                Color.parseColor("#fdf3ea"), Color.parseColor("#edf4fd"));

        //????????????
        dailyView.setLineWidth(8);
        dailyView.setPointRadius(12);
        dailyView.setHollow(4);
        dailyView.setList(dailyBeans);
        //???????????????????????????(??????3???)
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
            //????????????
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