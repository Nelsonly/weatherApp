package com.nelson.weather.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nelson.weather.fragment.CusFragment;

import java.util.List;

public class DateAdapter extends FragmentStateAdapter {
    private List<CusFragment> mFragments ;

    public DateAdapter(FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<CusFragment> fragments) {
        super(fragmentManager,lifecycle);
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
