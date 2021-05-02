package com.nelson.weather.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.nelson.weather.R;
import com.nelson.weather.WeatherMainActivity;
import com.nelson.weather.adapter.DateAdapter;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.AllDatas;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.HistoryAirResponse;
import com.nelson.weather.bean.HistoryResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.bean.NewSearchCityResponse;
import com.nelson.weather.contract.MoreDailyContract;
import com.nelson.weather.utils.CodeToStringUtils;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.DateUtils;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.ToastUtils;
import com.nelson.mvplibrary.mvp.MvpFragment;
import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyFragment extends MvpFragment<MoreDailyContract.MoreDailyPresenter> implements MoreDailyContract.IMoreDailyView {

    private List<CusFragment> mFragments = new Vector<>();
//    private ChildViewPager mViewPager;
    private ViewPager2 mViewPager;
    private TabFlowLayout flowLayout;
//    private RefreshLayout refreshLayout;
    private TextView tv_location;
    private List<String> showWeek = new ArrayList<>();
    private NewSearchCityResponse newSearchCityResponse;
    private HistoryAirResponse historyAirResponse;
    private HistoryResponse historyResponse;

    private long oldTime = 0;
    private long nowTime = 0;


    public DailyFragment() {
        // Required empty public constructor
    }


    @Override
    public void initData(Bundle savedInstanceState) {

        flowLayout = getActivity().findViewById(R.id.rectflow);
        initList();
        tv_location = getActivity().findViewById(R.id.tv_location);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flowLayout.setItemClickByOutSet(10);
            }
        });
    }

    private void initList() {
        mViewPager = getActivity().findViewById(R.id.viewpager);
        for (int i = 0; i < 15; i++) {
            mFragments.add(CusFragment.newInStance((WeatherMainActivity) getActivity()));
        }
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setAdapter(new DateAdapter(getChildFragmentManager(), getLifecycle(),mFragments));
        Refreshall();
    }

    /**
     * 刷新数据统一方法调用
     */
    public void Refreshall(){
        if (AllDatas.getInstance().getDailyResponse() != null)
            Daily_refresh(AllDatas.getInstance().getDailyResponse());
        if (AllDatas.getInstance().getMoreAirFiveResponse() != null)
            MoreAirFive_Refresh(AllDatas.getInstance().getMoreAirFiveResponse());
        if(AllDatas.getInstance().getNowResponse().getNow()!=null){
            AirNow_Refresh(AllDatas.getInstance().getAirNowResponse().getNow());
        }
    }

    @Override
    public void getMoreDailyResult(Response<DailyResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            Daily_refresh(response.body());
//             mFragments.get(0).set_ad_visibility();
            mFragments.get(0).Refresh();
            mFragments.get(1).Refresh();
        } else {
            ToastUtils.showShortToast(this.getContext(), CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    private void Daily_refresh(DailyResponse response) {
        List<DailyResponse.DailyBean> data = response.getDaily();
        int i = 0;
        if(data.size()==16){
            mFragments.add(CusFragment.newInStance((WeatherMainActivity) getActivity()));
            String yesterday =DateUtils.updateTime_month_2(data.get(0).getFxDate());
            String week = DateUtils.Week(yesterday);
            String dates = week + "x" + yesterday.substring(5).replace("-", "/");
            if (showWeek.size()!=0) {
                showWeek.clear();
            }
            showWeek.add(dates);
        }
        for (DailyResponse.DailyBean s : data) {
            String week = DateUtils.Week(s.getFxDate());
            String dates = week + "x" + s.getFxDate().replace("-", "/").substring(5, s.getFxDate().length());
            showWeek.add(dates);
            mFragments.get(i).setDailyResponse(s);
            i++;
        }
//        mFragments.get(1).set_ad_visibility();
        mFragments.get(1).setIstoday(true);
        rectFlow(showWeek);
    }

    @Override
    public void getMoreAirFiveResult(Response<MoreAirFiveResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            MoreAirFive_Refresh(response.body());
            mFragments.get(0).Refresh();
            mFragments.get(1).Refresh();
        } else {
            ToastUtils.showShortToast(this.getContext(), CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    private void MoreAirFive_Refresh(MoreAirFiveResponse response) {
        List<MoreAirFiveResponse.DailyBean> data = response.getDaily();
        if (data.size() > 0) {
            int i = 0;
            if (mFragments.size() == 16) {
                i = 1;
            }
            for (MoreAirFiveResponse.DailyBean s : data) {
                mFragments.get(i).setMoreAirFiveResponse(s);
                mFragments.get(i).setvisiableair(true);
                i++;
            }
        }
    }
    private void AirNow_Refresh(AirNowResponse.NowBean nowBean) {
        AirNowToMoreFive(nowBean);
    }
    private void AirNowToMoreFive(AirNowResponse.NowBean nowBean){
        MoreAirFiveResponse.DailyBean dailyBean = new MoreAirFiveResponse.DailyBean();
        dailyBean.setAqi(nowBean.getAqi());
        dailyBean.setPrimary(nowBean.getPrimary());
        dailyBean.setCategory(nowBean.getCategory());
        mFragments.get(1).setMoreAirFiveResponse(dailyBean);
        mFragments.get(1).setvisiableair(true);
    }

    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "网络异常");
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_daily;
    }

    private void rectFlow(List<String> mTitle2) {
        flowLayout.setViewPager(mViewPager)
                .setTextId(R.id.item_text)
                .setSelectedColor(Color.WHITE)
                .setDefaultPosition(1)
                .setUnSelectedColor(getResources().getColor(R.color.unselect));
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_daily_week, mTitle2) {
//            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void bindView(View view, String data, int position) {
                String s = data.replace("x","\n");
                SpannableString spannableString = new SpannableString(s);
                spannableString.setSpan(new RelativeSizeSpan((float) (12.0/14.0)),3,8,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                TextView textView = view.findViewById(R.id.item_text);
                textView.setText(spannableString);
            }
            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }

    /**
     * 历史数据转换
     * @param historyResponse
     */
    private void HistoryResponsetoDaily(HistoryResponse historyResponse){
        DailyResponse.DailyBean dailyBean = new DailyResponse.DailyBean();
        dailyBean.setIconDay(historyResponse.getWeatherHourly().get(13).getIcon());
        dailyBean.setTextDay(historyResponse.getWeatherHourly().get(13).getText());
        dailyBean.setWindDirDay(historyResponse.getWeatherHourly().get(13).getWindDir());
        dailyBean.setWindScaleDay(historyResponse.getWeatherHourly().get(13).getWindScale());
        dailyBean.setTempMax(historyResponse.getWeatherDaily().getTempMax());
        dailyBean.setTempMin(historyResponse.getWeatherDaily().getTempMin());
        dailyBean.setHumidity(historyResponse.getWeatherDaily().getHumidity());
        dailyBean.setPressure(historyResponse.getWeatherDaily().getPressure());
        dailyBean.setSunrise(historyResponse.getWeatherDaily().getSunrise());
        dailyBean.setSunset(historyResponse.getWeatherDaily().getSunset());
        mFragments.get(0).setDailyResponse(dailyBean);

    }
    private void HistoryAirtoAir(HistoryAirResponse historyAirResponse){
        MoreAirFiveResponse.DailyBean moreAirFiveResponse = new MoreAirFiveResponse.DailyBean();
        moreAirFiveResponse.setAqi(historyAirResponse.getAirHourly().get(13).getAqi());
        moreAirFiveResponse.setCategory(historyAirResponse.getAirHourly().get(13).getCategory());
        mFragments.get(0).setMoreAirFiveResponse(moreAirFiveResponse);
        mFragments.get(0).setvisiableair(true);
    }

    private void resFlow(List<String> mTitle2) {
        final TabFlowLayout flowLayout = getActivity().findViewById(R.id.rectflow);

        /**
         * 配置自定义属性
         */

        TabBean bean = new TabBean();
        bean.tabType = FlowConstants.RES;
        //bean.tabItemRes = R.drawable.xian2;
        bean.tabClickAnimTime = 300;
        bean.tabMarginLeft = 5;
        bean.tabMarginTop = 10;
        bean.tabMarginRight = 5;
        bean.tabMarginBottom = 0;
        bean.autoScale = true;
        bean.scaleFactor = 1.2f;
        bean.tabWidth = 100;
        flowLayout.setTabBean(bean);

        flowLayout.setViewPager(mViewPager);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_daily_week, mTitle2) {
            @Override
            public void bindView(View view, String data, int position) {
                Log.d("TAGS", data);
                setText(view, R.id.item_text, data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected MoreDailyContract.MoreDailyPresenter createPresent() {
        return new MoreDailyContract.MoreDailyPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!tv_location.getText().equals(SPUtils.getString(Constant.allLocation," ",context))) {
            tv_location.setText(SPUtils.getString(Constant.allLocation, " ", context));
            //Log.d("dailyfragment","new search city ------>"+AllDatas.getInstance().getAirNowResponse().getNow().getAqi());
            Refreshall();
        }
        if(SPUtils.getBoolean(Constant.isindexfragment,false,context)){
            flowLayout.setItemClickStatus(true);
            flowLayout.setItemClickByOutSet(SPUtils.getInt("select_item", 1, context));
            SPUtils.putInt("select_item",1,context);
            SPUtils.putBoolean(Constant.isindexfragment,false,context);
        }
    }
}