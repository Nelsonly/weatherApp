package com.nelson.weather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nelson.weather.R;
import com.nelson.weather.bean.NewSearchCityResponse;

import java.util.List;

/**
 * 搜索城市结果列表适配器
 *
 * @author nelson
 */
public class SearchCityAdapter extends BaseQuickAdapter<NewSearchCityResponse.LocationBean, BaseViewHolder> {
    public SearchCityAdapter(int layoutResId, @Nullable List<NewSearchCityResponse.LocationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewSearchCityResponse.LocationBean item) {
        //城市名称
        helper.setText(R.id.tv_city_name, item.getName());
        //绑定点击事件
        helper.addOnClickListener(R.id.tv_city_name);

    }
}
