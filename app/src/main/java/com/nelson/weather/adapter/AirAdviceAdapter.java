package com.nelson.weather.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nelson.weather.R;
import com.nelson.weather.bean.LifestyleResponse;
import com.nelson.weather.utils.WeatherUtil;

import java.util.List;

/**
 * @author yating
 */
public class AirAdviceAdapter extends BaseQuickAdapter<LifestyleResponse.DailyBean, BaseViewHolder> {
    public AirAdviceAdapter(int layoutResId, List<LifestyleResponse.DailyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, LifestyleResponse.DailyBean item) {
        ImageView imageView = holder.getView(R.id.air_advice_icons);
        WeatherUtil.setIcon(imageView, item.getType());
        holder.setText(R.id.air_advice_title, item.getName().substring(0,4))
                .setText(R.id.air_advice, item.getText());
        holder.addOnClickListener(R.id.item_air_advice);
    }
}
