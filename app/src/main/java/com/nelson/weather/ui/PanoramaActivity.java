package com.nelson.weather.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.pano.platform.plugin.indooralbum.IndoorAlbumPlugin;
import com.nelson.mvplibrary.base.BaseActivity;
import com.nelson.mvplibrary.bean.CountryScore;
import com.nelson.mvplibrary.bean.KeyScore;

import com.baidu.lbsapi.panoramaview.*;
import com.baidu.lbsapi.BMapManager;
import com.nelson.mvplibrary.mvp.MvpActivity;
import com.nelson.weather.R;
import com.nelson.weather.WeatherApplication;
import com.nelson.weather.adapter.PanoramaAdapter;
import com.nelson.weather.bean.NewSearchCityResponse;
import com.nelson.weather.bean.NowResponse;
import com.nelson.weather.contract.NowContract;
import com.nelson.weather.utils.Constant;


import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

/**
 * @author nelson
 */
public class PanoramaActivity extends MvpActivity<NowContract.NowPresenter> implements NowContract.IWeatherView, OnGetSuggestionResultListener , PanoramaAdapter.OnItemClickListener {

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
    protected NowContract.NowPresenter createPresent() {
        return new NowContract.NowPresenter();
    }

    @BindView(R.id.panorama)
    PanoramaView mPanoView;
    @BindView(R.id.tv_pan_temp)
    TextView tvPanTemp;
    @BindView(R.id.tv_pan_wind)
    TextView tvPanWind;
    @BindView(R.id.tv_pan_weather)
    TextView tvPanWeather;
    private SuggestionSearch mSuggestionSearch = null;

    // 搜索关键字输入窗口
    private EditText mEditCity = null;
    private AutoCompleteTextView mKeyWordsView = null;
    private RecyclerView mSugListView;
    PanoramaAdapter panoramaAdapter;
    private long startTime;
    private long endTime;
    private String city;
    private int score;
    private String uid;
    private String key;
    private String dis;
    //    PanoramaView mPanoView;
    @Override
    public void initData(Bundle savedInstanceState) {
        Intent intent =getIntent();
        key = intent.getStringExtra("tag");
        city = intent.getStringExtra("city");
        uid  = intent.getStringExtra("path");
        dis = intent.getStringExtra("dis");
        mPresent.newSearchCity(dis);
        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {
            @Override
            public void onLoadPanoramaBegin() {
                startTime = System.currentTimeMillis();
            }

            @Override
            public void onLoadPanoramaEnd(String json) {
            }

            @Override
            public void onLoadPanoramaError(String error) {
            }

            @Override
            public void onDescriptionLoadEnd(String json) {

            }

            @Override
            public void onMessage(String msgName, int msgType) {

            }

            @Override
            public void onCustomMarkerClick(String key) {

            }

            @Override
            public void onMoveStart() {

            }

            @Override
            public void onMoveEnd() {

            }
        });


        // 默认相册
//        mPanoView  =  findViewById(R.id.panorama);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        // 初始化view
        mEditCity = (EditText) findViewById(R.id.city);
        mSugListView = (RecyclerView) findViewById(R.id.sug_list);
        mKeyWordsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        mKeyWordsView.setThreshold(1);

        IndoorAlbumPlugin.getInstance().init();
        mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionMiddle);
        mPanoView.setPanoramaZoomLevel(5);
        mPanoView.setArrowTextureByUrl("http://d.lanrentuku.com/down/png/0907/system-cd-disk/arrow-up.png");
        if(uid == null) {
            uid= "7aea43b75f0ee3e17c29bd71";
        }
        mPanoView.setPanoramaByUid(uid, PanoramaView.PANOTYPE_STREET);
        // 当输入关键字变化时，动态更新建议列表
        mKeyWordsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString()) // 关键字
                        .city(mEditCity.getText().toString())); // 城市
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_panorama;
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
    public void onDestroy() {
        saveToSql(key,city,score);
        mPanoView.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }
    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     *
     * @param suggestionResult    Sug检索结果
     */
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
                map.put("dis",info.getDistrict());
                map.put("city",info.getCity());
                map.put("uid",info.getUid());
                map.put("address",info.getAddress());
                suggest.add(map);
            }
        }
        panoramaAdapter = new PanoramaAdapter(this);
        panoramaAdapter.addData(suggest);
        panoramaAdapter.setOnItemClickListener(this::onCitysClick);
        mSugListView.setAdapter(panoramaAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        mSugListView.setLayoutManager(llm);
    }

    @Override
    public void onCitysClick(String key,String uid,String city,String dis) {
        mPresent.newSearchCity(dis);

        endTime = System.currentTimeMillis();
        int score = (int) (endTime-startTime)/3600/10+5;
        saveToSql(this.key,this.city,this.score);
        mPanoView.setPanoramaByUid(uid,PanoramaView.PANOTYPE_STREET);
        panoramaAdapter.clearAll();
        this.key = key;
        this.city = city;
        this.score = score;
    }
    private void saveToSql(String key,String city,int score){
        if("".equals(city) || "".equals(key)) {
            return;
        }
        if (score == 0){
            endTime = System.currentTimeMillis();
            this.score = (int) (endTime-startTime)/3600/30 + 5;
        }
        CountryScore tempContryScore = LitePal.where(Constant.countryName+"=?",city).findFirst(CountryScore.class);
        if(tempContryScore != null){
            int oldscore = tempContryScore.getCountryScore();
            ContentValues values = new ContentValues();
            values.put(Constant.countryScore,score+oldscore);
            LitePal.updateAll(CountryScore.class,values,Constant.countryName+"=?",city);
        }else {
            CountryScore countryScore = new CountryScore();
            countryScore.setCountryName(city);
            countryScore.setCountryScore(score);
            countryScore.save();
        }
        KeyScore keyScore = LitePal.where(Constant.keyName+"=?",key).findFirst(KeyScore.class);
        if(keyScore != null){
            int oldscore = keyScore.getKeyScore();
            ContentValues values = new ContentValues();
            values.put(Constant.keyScore,score+oldscore);
            LitePal.updateAll(KeyScore.class,values,Constant.keyName+"=?",key);
        }else {
            KeyScore keyScore1 = new KeyScore();
            keyScore1.setKeyName(key);
            keyScore1.setKeyScore(score);
            keyScore1.save();
        }
    }

    @Override
    public void getWeatherDataFailed() {

    }

    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            mPresent.nowWeather(response.body().getLocation().get(0).getId());
        }
    }

    @Override
    public void getNowResult(Response<NowResponse> response) {
        if(response.body().getCode().equals(Constant.SUCCESS_CODE)){
            NowResponse nowResponse = response.body();
            tvPanTemp.setText(nowResponse.getNow().getTemp()+"°C");
            tvPanWeather.setText(nowResponse.getNow().getText());
            tvPanWind.setText(nowResponse.getNow().getWindDir()+" "+nowResponse.getNow().getWindSpeed()+"级");
        }
    }
}