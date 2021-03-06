  package com.nelson.weather.fragment;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nelson.weather.R;
import com.nelson.weather.adapter.AirAdviceAdapter;
import com.nelson.weather.adapter.MonitorStationAdapter;
import com.nelson.weather.adapter.MoreAirFiveCategoryAdapter;
import com.nelson.weather.adapter.MoreAirFiveTopTimeAdapter;
import com.nelson.weather.bean.AirFiveBean;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.bean.AllDatas;
import com.nelson.weather.bean.HistoryAirResponse;
import com.nelson.weather.bean.LifestyleResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.bean.NewSearchCityResponse;
import com.nelson.weather.contract.AirQualityContract;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.DateUtils;
import com.nelson.weather.utils.DisplayUtil;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.SizeUtils;
import com.nelson.weather.utils.WeatherUtil;
import com.nelson.weather.view.LineChartView;
import com.nelson.weather.view.SuperCircleView;
import com.nelson.mvplibrary.mvp.MvpFragment;
import com.nelson.mvplibrary.utils.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

  /**
   * A simple {@link Fragment} subclass.
   * Use the {@link AriQualityFragment#} factory method to
   * create an instance of this fragment.
   *
   * @author yangyating
   */
  public class AriQualityFragment extends MvpFragment<AirQualityContract.AirQualityPresenter> implements AirQualityContract.IAirQualityView {

      /**
       * ??????title??????
       */
      @BindView(R.id.locate)
      TextView tv_locate;
      @BindView(R.id.update_time)
      TextView tv_upDateTime;
      @BindView(R.id.superCircle_background)
      ImageView superCircle_background;
      @BindView(R.id.airquality_describe)
      TextView tv_airQuality_describe;
      /**
       * ?????????????????????
       * */
      @BindView(R.id.air_PM2_5)
      TextView tv_PM2_5;
      @BindView(R.id.air_PM10)
      TextView tv_PM10;
      @BindView(R.id.air_SO2)
      TextView tv_SO2;
      @BindView(R.id.air_NO2)
      TextView tv_NO2;
      @BindView(R.id.air_O3)
      TextView tv_O3;
      @BindView(R.id.air_CO)
      TextView tv_CO;
      @BindView(R.id.air_img_PM2_5)
      ImageView img_PM2_5;
      @BindView(R.id.air_img_PM10)
      ImageView img_PM10;
      @BindView(R.id.air_img_SO2)
      ImageView img_SO2;
      @BindView(R.id.air_img_NO2)
      ImageView img_NO2;
      @BindView(R.id.air_img_O3)
      ImageView img_O3;
      @BindView(R.id.air_img_CO)
      ImageView img_CO;
      @BindView(R.id.air_PM2_5_content)
      TextView tv_pm25_content;
      @BindView(R.id.air_PM10_content)
      TextView tv_pm10_content;
      @BindView(R.id.air_SO2_content)
      TextView tv_so2_content;
      @BindView(R.id.air_NO2_content)
      TextView tv_no2_content;
      @BindView(R.id.air_O3_content)
      TextView tv_03_content;
      @BindView(R.id.air_CO_content)
      TextView tv_co_content;
      @BindView(R.id.air_advice_recycler)
      RecyclerView rv_airAdvice;
      @BindView(R.id.airquality_fivedays_toptime)
      RecyclerView rv_airFive_topTime;
      @BindView(R.id.airquality_fivedays_category)
      RecyclerView rv_airFive_category;
      @BindView(R.id.monitor_station_data)
      RecyclerView rv_monitorStation;
      @BindView(R.id.superCircleView)
      SuperCircleView mSuperCircleView;
      @BindView(R.id.lineChartView)
      LineChartView myLineChart;

      /**??????????????????*/
      private PopupWindow mPopupWindow;
      private AnimationUtil animUtil;
      private float bgAlpha = 1f;
      private boolean bright = false;
      /**0.5s*/
      private static final long DURATION = 500;
      /**???????????????*/
      private static final float START_ALPHA = 0.7f;
      /**???????????????*/
      private static final float END_ALPHA = 1f;

      private AirAdviceAdapter adviceAdapter;
      private MonitorStationAdapter monitorAdapter;

      private final List<LifestyleResponse.DailyBean> dailyAdvice = new ArrayList<>();
      private final List<AirFiveBean> fiveAirList = new ArrayList<>();
      private final List<AirNowResponse.StationBean> station = new ArrayList<>();
      private final List<String> aqi = new ArrayList<>();
      private final List<String> yAqi = new ArrayList<>();
      private NewSearchCityResponse newSearchCityResponse;
      private AirNowResponse airNowResponse;
      private MoreAirFiveResponse moreAirFiveResponse;
      private LifestyleResponse lifestyleResponse;
      private HistoryAirResponse historyAirResponse;
      private AirNowResponse.NowBean airNowData;

      private String city = " ";
      private String locationId = " ";

      private View monitorStation;

      private long oldTime = 0;
      private long nowTime = 0;

      private final Handler handlerRabbit = new Handler();
      private final Handler handlerSheep = new Handler();
      private Runnable task;
      private Runnable task2;
      private final int delay = 10000;

      public AriQualityFragment(){}
      @Override
      public int getLayoutId() {
          return R.layout.module_fragment_air;
      }

      @Override
      protected AirQualityContract.AirQualityPresenter createPresent() {
          return new AirQualityContract.AirQualityPresenter();
      }

      @Override
      public void onActivityCreated(@Nullable Bundle savedInstanceState) {
          super.onActivityCreated(savedInstanceState);
    }

      @Override
      public void onStart() {
          super.onStart();
      }

      @Override
      public void onResume() {
          if((!tv_locate.getText().equals(SPUtils.getString(Constant.allLocation,"",context)))&&AllDatas.getInstance().getNewSearchCityResponse()!=null)
          {
              initList();
              tv_locate.setText(SPUtils.getString("AllLocation", " ", context));
          }
          super.onResume();

      }



      @Override
      public void initData(Bundle savedInstanceState) {
  //        SPUtils.putString("tabAdDate",DateUtils.getNowDate(),context);
          monitorStation = getView().findViewById(R.id.include_monitor);
          mPopupWindow = new PopupWindow(context);
          animUtil = new AnimationUtil();

          if(AllDatas.getInstance().getNewSearchCityResponse()!=null){

              initList();
          }
          showLinChart();
      }


      /**
       * ?????????list??????
       * */
      private void initList() {
//          if(AllDatas.getInstance().getHistoryResponse()!=null){
//              historyAirResponse = AllDatas.getInstance().getHistoryAirResponse();
//          }
          if(AllDatas.getInstance().getNewSearchCityResponse()!=null){
              newSearchCityResponse = AllDatas.getInstance().getNewSearchCityResponse();
          }
          if(AllDatas.getInstance().getAirNowResponse()!=null){
              airNowResponse = AllDatas.getInstance().getAirNowResponse();
              //??????????????????????????????
              showAirBasicData(airNowResponse.getNow());
          }
          if (AllDatas.getInstance().getMoreAirFiveResponse()!=null){
              moreAirFiveResponse = AllDatas.getInstance().getMoreAirFiveResponse();
          }
          if(AllDatas.getInstance().getLifestyleResponse()!=null){
              lifestyleResponse = AllDatas.getInstance().getLifestyleResponse();
          }
          city = AllDatas.getInstance().getNewSearchCityResponse().getLocation().get(0).getAdm1();
          mPresent.searchCityId(city);
          //????????????
          monitorAdapter = new MonitorStationAdapter(R.layout.module_recycle_item_air_monitorstation,station);
          LinearLayoutManager layoutManager = new LinearLayoutManager(context);
          layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
          rv_monitorStation.setLayoutManager(layoutManager);
          rv_monitorStation.setAdapter(monitorAdapter);
          //5??????????????? - ????????????recyclerview
          fiveAirList.clear();
          AirFiveBean airFiveBean1 = new AirFiveBean();
//          airFiveBean1.setAqi(historyAirResponse.getAirHourly().get(0).getAqi());
//          airFiveBean1.setCategory(historyAirResponse.getAirHourly().get(0).getCategory());
//          airFiveBean1.setDate(historyAirResponse.getAirHourly().get(0).getPubTime());
          fiveAirList.add(airFiveBean1);
          AirFiveBean airFiveBean2 = new AirFiveBean();
          airFiveBean2.setAqi(airNowResponse.getNow().getAqi());
          airFiveBean2.setCategory(airNowResponse.getNow().getCategory());
          airFiveBean2.setDate(airNowResponse.getNow().getPubTime());
          fiveAirList.add(airFiveBean2);
          for (int i = 1; i < moreAirFiveResponse.getDaily().size(); i++) {
              AirFiveBean airFiveBean3 = new AirFiveBean();
              airFiveBean3.setAqi(moreAirFiveResponse.getDaily().get(i).getAqi());
              airFiveBean3.setCategory(moreAirFiveResponse.getDaily().get(i).getCategory());
              airFiveBean3.setDate(moreAirFiveResponse.getDaily().get(i).getFxDate());
              fiveAirList.add(airFiveBean3);
          }
          MoreAirFiveTopTimeAdapter topTimeAdapter = new MoreAirFiveTopTimeAdapter(R.layout.module_recycle_item_air_fivedaytop, fiveAirList);
          GridLayoutManager manager1 = new GridLayoutManager(context, fiveAirList.size());
          rv_airFive_topTime.setLayoutManager(manager1);
          rv_airFive_topTime.setAdapter(topTimeAdapter);

          showLinChart();

          //5??????????????? - ????????????recyclerview
          MoreAirFiveCategoryAdapter categoryAdapter = new MoreAirFiveCategoryAdapter(R.layout.module_recycle_item_air_fivedaycategory, fiveAirList);
          GridLayoutManager manager2 = new GridLayoutManager(context, fiveAirList.size());
          rv_airFive_category.setLayoutManager(manager2);
          rv_airFive_category.setAdapter(categoryAdapter);

          //????????????
          List<LifestyleResponse.DailyBean> daily = lifestyleResponse.getDaily();
          dailyAdvice.clear();
          for (int i = 0; i < daily.size(); i++) {
              if ("1".equals(daily.get(i).getType()) || "9".equals(daily.get(i).getType()) || "7".equals(daily.get(i).getType()) || "10".equals(daily.get(i).getType())) {
                  dailyAdvice.add(lifestyleResponse.getDaily().get(i));
              }
          }
          adviceAdapter = new AirAdviceAdapter(R.layout.module_recycle_item_air_advice, dailyAdvice);
          GridLayoutManager manager = new GridLayoutManager(context, 2);
          rv_airAdvice.setLayoutManager(manager);
          rv_airAdvice.setAdapter(adviceAdapter);
          //item???????????????
          adviceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
              @Override
              public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                  //????????????
                  LifestyleResponse.DailyBean data = adviceAdapter.getItem(position);
                  showPopupWindow(data);    //??????
                  toggleBright();   //????????????
              }
          });

      }

      /**
       * ??????????????????????????????
       */
      private void showAirBasicData(AirNowResponse.NowBean airData) {
          airNowData = airData;

          WeatherUtil.categoryToBg(superCircle_background, airNowData.getCategory());
          mSuperCircleView.setSelect(Float.valueOf(airNowData.getAqi()));
          mSuperCircleView.setSecondText(airNowData.getAqi());
          mSuperCircleView.setThirdText(airNowData.getCategory());
          Log.d("airQuality",airNowData.getAqi());
          mSuperCircleView.invalidate();
          mSuperCircleView.forceLayout();
          mSuperCircleView.requestLayout();
          tv_airQuality_describe.setText(WeatherUtil.updateAirDescribe(airNowData.getCategory()));
          String time = DateUtils.updateTime(airNowResponse.getUpdateTime());
          tv_upDateTime.setText(String.format(getResources().getString(R.string.air_update_time),time));
          tv_locate.setText(SPUtils.getString("AllLocation","????????????",context));

          tv_PM2_5.setText(airNowData.getPm2p5());
          tv_PM10.setText(airNowData.getPm10());
          tv_SO2.setText(airNowData.getSo2());
          tv_NO2.setText(airNowData.getNo2());
          tv_O3.setText(airNowData.getO3());
          tv_CO.setText(airNowData.getCo());
          WeatherUtil.PM2_5_ToIcon(img_PM2_5, airNowData.getPm2p5());
          WeatherUtil.PM10_ToIcon(img_PM10, airNowData.getPm10());
          WeatherUtil.SO2_ToIcon(img_SO2, airNowData.getSo2());
          WeatherUtil.NO2_ToIcon(img_NO2, airNowData.getNo2());
          WeatherUtil.CO_ToIcon(img_CO, airNowData.getCo());
          WeatherUtil.O3_ToIcon(img_O3, airNowData.getO3());

          tv_pm25_content.setText(SizeUtils.changeWord(DisplayUtil.spToPx(context, 8), "PM2.5", 2, 5));
          tv_pm10_content.setText(SizeUtils.changeWord(DisplayUtil.spToPx(context, 8), "PM10", 2, 4));
          tv_so2_content.setText(SizeUtils.changeWord(DisplayUtil.spToPx(context, 8), "SO2", 2, 3));
          tv_no2_content.setText(SizeUtils.changeWord(DisplayUtil.spToPx(context, 8), "NO2", 2, 3));
          tv_03_content.setText(SizeUtils.changeWord(DisplayUtil.spToPx(context, 8), "O3", 1, 2));


          Log.i("super", "getMoreAirResult: ?????????");
      }

      private void showLinChart() {
          //???????????????
          aqi.clear();
          for (int i = 0; i < fiveAirList.size(); i++) {
              aqi.add(fiveAirList.get(i).getAqi());
          }
          yAqi.add("0");
          yAqi.add("550");
          myLineChart.setLineColor(Color.parseColor("#4287ed"));
          myLineChart.setLineWidth(DisplayUtil.dpToPx(context, 1));
          myLineChart.setRadius(3);
          //??????y????????????x?????????
          myLineChart.setInfo(yAqi, aqi);
      }

      /**
       * ??????????????????
       *
       * @param data
       */
      public void showPopupWindow(LifestyleResponse.DailyBean data) {
          String locate = newSearchCityResponse.getLocation().get(0).getName();
          View view = LayoutInflater.from(context).inflate(R.layout.module_popwindow_air, null);
          mPopupWindow = new PopupWindow(view, DisplayUtil.dpToPx(context, 300), FrameLayout.LayoutParams.WRAP_CONTENT, true);
          mPopupWindow.setContentView(view);

          ImageView popImg = view.findViewById(R.id.pop_img);
          TextView popTvTitle = view.findViewById(R.id.pop_title);
          TextView popTvLocate = view.findViewById(R.id.pop_tv_locate);
          TextView popTvAdvice = view.findViewById(R.id.pop_tv_advice);

          String address = SPUtils.getString("location", "", context);
          WeatherUtil.setIcon(popImg, data.getType());
          popTvTitle.setText(data.getName().substring(0,4));
          popTvAdvice.setText(data.getText());
          popTvLocate.setText(address);

          View parents = LayoutInflater.from(context).inflate(R.layout.module_fragment_air, null);
          mPopupWindow.showAtLocation(parents, Gravity.CENTER_HORIZONTAL, 0, 0);
          //????????????
          mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
              @Override
              public void onDismiss() {
                  toggleBright();
              }
          });
      }

      @Override
      public void getSearchCityIdResult(Response<NewSearchCityResponse> response) {
          List<NewSearchCityResponse.LocationBean> data = response.body().getLocation();
          if (data != null) {
              locationId = data.get(0).getId();
              mPresent.air(data.get(0).getId());
          }
      }

      @Override
      public void getMoreAirResult(Response<AirNowResponse> response) {
          List<AirNowResponse.StationBean> data = response.body().getStation();
          if (data != null) {
              station.clear();
              station.addAll(data);
              monitorAdapter.notifyDataSetChanged();
              airNowData = response.body().getNow();
              monitorStation.setVisibility(View.VISIBLE);
          }else {
              monitorStation.setVisibility(View.GONE);
          }
      }

      @Override
      public void getDataFailed() {

      }

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
  }