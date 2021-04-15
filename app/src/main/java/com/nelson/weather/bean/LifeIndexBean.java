package com.nelson.weather.bean;

import com.nelson.weather.adapter.IndexAdapter;
import com.nelson.mvplibrary.base.BaseBean;

public class LifeIndexBean extends BaseBean {
    private LifestyleResponse lifestyleResponse;

    public LifeIndexBean() {
        this.setViewType(IndexAdapter.ITEM_LIFE);
    }

    public LifestyleResponse getLifestyleResponse() {
        return lifestyleResponse;
    }

    public void setLifestyleResponse(LifestyleResponse lifestyleResponse) {
        this.lifestyleResponse = lifestyleResponse;
    }
}
