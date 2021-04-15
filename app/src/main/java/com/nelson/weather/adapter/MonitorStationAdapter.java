package com.nelson.weather.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nelson.weather.R;
import com.nelson.weather.bean.AirNowResponse;
import com.nelson.weather.utils.WeatherUtil;

import java.util.List;

/**
 * @author yating
 */
public class MonitorStationAdapter extends BaseQuickAdapter<AirNowResponse.StationBean, BaseViewHolder> {
    public MonitorStationAdapter(int layoutResId, @Nullable List<AirNowResponse.StationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AirNowResponse.StationBean item) {
        helper.setText(R.id.monitor_locate, item.getName())
                .setText(R.id.monitor_aqi, item.getAqi())
                .setText(R.id.monitor_score, WeatherUtil.updateCategory(item.getCategory()));
        TextView textView = helper.getView(R.id.monitor_score);
        String category = item.getCategory();
        WeatherUtil.categoryToBgIcon(textView, item.getCategory());

        if (helper.getAdapterPosition()==0) {
            helper.setImageResource(R.id.monitior_locate_icon, R.mipmap.air_locate_icon);
        }
    }
}
