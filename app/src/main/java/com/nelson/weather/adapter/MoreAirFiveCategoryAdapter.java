package com.nelson.weather.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nelson.weather.R;
import com.nelson.weather.bean.AirFiveBean;
import com.nelson.weather.utils.WeatherUtil;

import java.util.List;

/**
 * @author yating
 */
public class MoreAirFiveCategoryAdapter extends BaseQuickAdapter<AirFiveBean, BaseViewHolder> {
    public MoreAirFiveCategoryAdapter(int layoutResId, List<AirFiveBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AirFiveBean item) {
        helper.setText(R.id.airQuality_category,WeatherUtil.updateCategory(item.getCategory()));
        TextView textView = helper.getView(R.id.airQuality_category);
        String category = item.getCategory();
        WeatherUtil.categoryToBgIcon(textView,item.getCategory());
    }
}
