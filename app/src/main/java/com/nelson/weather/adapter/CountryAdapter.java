package com.nelson.weather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nelson.mvplibrary.bean.Country;
import com.nelson.weather.R;
import java.util.List;

/**
 * 国家列表适配器
 *
 * @author
 */

public class CountryAdapter extends BaseQuickAdapter<Country, BaseViewHolder> {
    public CountryAdapter(int layoutResId, @Nullable List<Country> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Country item) {
        //设置名称
        helper.setText(R.id.tv_country_name, item.getName());
        //点击事件
        helper.addOnClickListener(R.id.tv_country_name);
    }
}
