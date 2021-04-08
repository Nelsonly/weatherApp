package com.nelson.weather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nelson.weather.R;
import com.nelson.weather.adapter.CountryAdapter;
import com.nelson.weather.utils.StatusBarUtil;
import com.nelson.mvplibrary.base.BaseActivity;
import com.nelson.mvplibrary.bean.Country;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 世界城市  列举世界上两百多个国家其中包括地区，
 *
 * @author llw
 */
public class WorldCityActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 国家/地区列表适配器
     */
    CountryAdapter mAdapter;
    List<Country> mList = new ArrayList<>();

    @Override
    public void initData(Bundle savedInstanceState) {
        //白色底  状态栏
        StatusBarUtil.setStatusBarColor(context, R.color.white);
        //状态栏 黑色字体
        StatusBarUtil.StatusBarLightMode(context);
        Back(toolbar);
        initList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_world_city;
    }

    /**
     * 初始化列表数据
     */
    private void initList() {
        mList = LitePal.findAll(Country.class);
        mAdapter = new CountryAdapter(R.layout.item_country_list, mList);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(context, WorldCityListActivity.class);
                intent.putExtra("name", mList.get(position).getName());
                intent.putExtra("code", mList.get(position).getCode());
                startActivity(intent);

            }
        });
    }


}
