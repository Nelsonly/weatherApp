package com.nelson.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nelson.weather.R;
import com.nelson.weather.bean.LifestyleResponse;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.LiWindow;
import com.nelson.weather.utils.SPUtils;
import com.nelson.weather.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LifeViewPagerAdapter extends PagerAdapter {
    private Context context;
    private GridView gridView;
    LiWindow liWindow;

    private List<LifestyleResponse.DailyBean> beanList;

    public static final  int maxLoop = 500;
    public static final  int pageNum = 2;

    private int[] icons = {
            R.drawable.movement,
            R.drawable.wash_the_car,
            R.drawable.dress,
            R.drawable.fishing,
            R.drawable.ultraviolet_uv,
            R.drawable.tourism,
            R.drawable.allergy,
            R.drawable.comfort,
            R.drawable.catch_a_cold,
            R.drawable.air_pollution,
            R.drawable.air_conditioner,
            R.drawable.sunglasses,
            R.drawable.makeup,
            R.drawable.air_is_basked_in,
            R.drawable.traffic,
            R.drawable.spf
    };


    public LifeViewPagerAdapter(Context context, List<LifestyleResponse.DailyBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    @Override
    public int getCount() {
        return maxLoop;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //type从1-16排列
        Collections.sort(beanList, new TypeComparator());

        View view = View.inflate(context, R.layout.item_life_content,null);
        List<LifestyleResponse.DailyBean> data = new ArrayList<>();
        if (position % pageNum==0) {
            data.add(beanList.get(9));  //type的数-1
            data.add(beanList.get(7));
            data.add(beanList.get(6));
            data.add(beanList.get(10));
            data.add(beanList.get(13));
            data.add(beanList.get(3));
            data.add(beanList.get(4));
            data.add(beanList.get(5));
        } else {
            data.add(beanList.get(2));
            data.add(beanList.get(15));
            data.add(beanList.get(12));
            data.add(beanList.get(8));
            data.add(beanList.get(0));
            data.add(beanList.get(14));
            data.add(beanList.get(11));
            data.add(beanList.get(1));
        }
        gridView = view.findViewById(R.id.grid_life);
        gridView.setGravity(1);
        gridView.setAdapter(new GridAdapter(data));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showLifeWindow(data.get(position));
            }
        });
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    private static class TypeComparator implements Comparator<LifestyleResponse.DailyBean> {
        @Override
        public int compare(LifestyleResponse.DailyBean o1, LifestyleResponse.DailyBean o2) {
            if (o1.getType() == o2.getType()) {
                return 0;
            } else if (Integer.parseInt(o1.getType()) > Integer.parseInt(o2.getType())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private class GridAdapter extends BaseAdapter {
        List<LifestyleResponse.DailyBean> data;
        public GridAdapter(List<LifestyleResponse.DailyBean> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_life_detail, parent, false);
            ImageView iv = v.findViewById(R.id.iv_icon);
            TextView tvLevel = v.findViewById(R.id.tv_level);
            TextView tvName = v.findViewById(R.id.tv_name);

            int type = Integer.parseInt(data.get(position).getType());
            iv.setImageResource(icons[type-1]);

            String category = data.get(position).getCategory();;
            if (type==11 && category.length()>5)  //空调 开启制暖空调 -> 开启制暖
                category = (category.substring(0, category.length() - 2));
            tvLevel.setText(category);

            //舒适度指数 去掉指数两个字
            if (type==10) tvName.setText("空气");
            else if (type==11) tvName.setText("空调");
            else {
                tvName.setText(data.get(position).getName().substring(0, data.get(position).getName().length() - 2));
            }
            return v;
        }
    }


    //显示小时详情天气信息弹窗
    private void showLifeWindow(LifestyleResponse.DailyBean data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_life_detail, null);
        ImageView iv = view.findViewById(R.id.iv_icon);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvLocation = view.findViewById(R.id.tv_location);
        TextView tvAdvice = view.findViewById(R.id.tv_advice);
        tvLocation.setText(SPUtils.getString("location","   ",context));

        int type = Integer.parseInt(data.getType());
        iv.setImageResource(icons[type - 1]);
        //舒适度指数 去掉指数两个字
        String title = data.getName();
        if (type==10)
            title = "空气";
        else if (type==11)
            title = "空调";
        else {
            title = (title.substring(0, title.length() - 2));
        }
        String category = data.getCategory();
        tvTitle.setText(title + "：" + category);

        tvAdvice.setText(data.getText());

        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, view.getHeight()), true);
    }
}
