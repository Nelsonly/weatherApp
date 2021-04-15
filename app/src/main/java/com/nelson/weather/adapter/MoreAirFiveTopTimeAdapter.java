package com.nelson.weather.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nelson.weather.R;
import com.nelson.weather.bean.AirFiveBean;
import com.nelson.weather.utils.DateUtils;

import java.util.List;

/**
 * @author yating
 */
public class MoreAirFiveTopTimeAdapter extends BaseQuickAdapter<AirFiveBean, BaseViewHolder> {
    public MoreAirFiveTopTimeAdapter(int layoutResId, List<AirFiveBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AirFiveBean item) {
        if (item.getDate()==null) return;
        helper.setText(R.id.airQuality_week, DateUtils.Week(item.getDate().substring(0,10)))
                .setText(R.id.airQuality_time,DateUtils.dateSplit(item.getDate().substring(0,10)))
                .setText(R.id.airQuality_aqi,item.getAqi());

        if(helper.getAdapterPosition()==0){
            helper.setTextColor(R.id.airQuality_week, Color.parseColor("#a7a7a7"))
                    .setTextColor(R.id.airQuality_time,Color.parseColor("#a7a7a7"))
                    .setTextColor(R.id.airQuality_aqi,Color.parseColor("#a7a7a7"));
        }
    }
}
