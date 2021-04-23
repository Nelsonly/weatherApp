package com.nelson.weather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.nelson.weather.R;
import com.nelson.weather.bean.DailyIndexBean;
import com.nelson.weather.bean.DailyResponse;
import com.nelson.weather.bean.HourlyIndexBean;
import com.nelson.weather.bean.HourlyResponse;
import com.nelson.weather.bean.LifeIndexBean;
import com.nelson.weather.bean.LifestyleResponse;
import com.nelson.weather.bean.MoreAirFiveResponse;
import com.nelson.weather.utils.Constant;
import com.nelson.weather.utils.DateUtils;
import com.nelson.weather.utils.LiWindow;
import com.nelson.weather.utils.SizeUtils;
import com.nelson.weather.utils.WeatherUtil;
import com.nelson.weather.view.DailyItemView;
import com.nelson.weather.view.DailyView;
import com.nelson.weather.view.HourlyItemView;
import com.nelson.weather.view.HourlyView;
import com.nelson.mvplibrary.base.BaseBean;

import java.util.ArrayList;
import java.util.List;


public class IndexAdapter extends RecyclerView.Adapter {

    public static final int ITEM_HOURLY = 0;
    public static final int ITEM_DAILY = 1;
    public static final int ITEM_LIFE = 2;

    public static final int LIFE_PAGE_NUM = 2;

    private List<BaseBean> list;
    /**
     * 自定义弹窗
     */
    LiWindow liWindow;
    private Context context = null;
    private final OnRecyclerViewItemClick onRecyclerViewItemClick;


    public IndexAdapter(Context context, List<BaseBean> list, OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.context = context;
        this.list = list;
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(int item);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case ITEM_HOURLY:
                v = layoutInflater.inflate(R.layout.item_forecast_24hr, parent, false);
                return new HourlyViewHolder(v);
            case ITEM_DAILY:
                v = layoutInflater.inflate(R.layout.item_forecast_15day, parent, false);
                return new DailyViewHolder(v);
            case ITEM_LIFE:
                v = layoutInflater.inflate(R.layout.item_life, parent, false);
                return new LifeViewHolder(v);
            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (list.size() > 0) {
            if (holder instanceof HourlyViewHolder) {
                initHourly((HourlyViewHolder) holder, position);
            } else if (holder instanceof DailyViewHolder) {
                initDaily((DailyViewHolder) holder, position);
            } else if (holder instanceof LifeViewHolder) {
                initLifeStyle((LifeViewHolder) holder, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getViewType() == ITEM_HOURLY) {
            return ITEM_HOURLY;
        } else if (list.get(position).getViewType() == ITEM_DAILY) {
            return ITEM_DAILY;
        } else if (list.get(position).getViewType() == ITEM_LIFE) {
            return ITEM_LIFE;
        } else {
            return super.getItemViewType(position);
        }
    }


    private void initHourly(HourlyViewHolder holder, int position) {
//        if (list.get(position).getViewType() != ITEM_HOURLY) return;
        HourlyIndexBean bean = (HourlyIndexBean) list.get(ITEM_HOURLY);
        if (bean.getHourlyBeans() != null && bean.getHourlyBeans().size() > 0) {
            List<HourlyResponse.HourlyBean> hourlyBeans = new ArrayList<>();
            if (bean.getNowBean() != null) {
                hourlyBeans.add(bean.getNowBean());
            }
            hourlyBeans.addAll(bean.getHourlyBeans());

            holder.hourlyView.setLineColor(Color.parseColor("#4287ed"), Color.parseColor("#e4eefc"));
            holder.hourlyView.setLineWidth(8f);
            holder.hourlyView.setPointRadius(12);
            holder.hourlyView.setHollow(4);
            holder.hourlyView.setList(hourlyBeans);
            //设置一屏幕显示几列(最少3列)
            try {
                holder.hourlyView.setColumnNumber(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //点击某一列
            holder.hourlyView.setOnWeatherItemClickListener(new HourlyView.OnWeatherItemClickListener() {
                @Override
                public void onItemClick(HourlyItemView itemView, int position, HourlyResponse.HourlyBean bean) {
                    HourlyResponse.HourlyBean clickData = hourlyBeans.get(position);
                    showHourlyWindow(clickData);
                }
            });
            holder.hourlyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            holder.hourlyView.clearSelected();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }

        if (bean.getSunrise() != null) {
            holder.tvSunrise.setText(bean.getSunrise());
        }
        if (bean.getSunset() != null) {
            holder.tvSunset.setText(bean.getSunset());
        }

        if (bean.getAir() != null) {
            holder.hourlyView.setAir(bean.getAir());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDaily(DailyViewHolder holder, int position) {
        DailyIndexBean dailyIndexBean = new DailyIndexBean();
        for(BaseBean i : list){
            if(i instanceof DailyIndexBean) {
                dailyIndexBean = (DailyIndexBean) i;
            }
        }
        if (dailyIndexBean.getDailyBeans() != null && dailyIndexBean.getDailyBeans().size() > 0) {
            List<DailyResponse.DailyBean> dailyBeans = new ArrayList<>();
            if (dailyIndexBean.getHisDailyBean() != null) {
                dailyBeans.add(dailyIndexBean.getHisDailyBean());
            }
            dailyBeans.addAll(dailyIndexBean.getDailyBeans());
            setDailyView(holder, dailyBeans);
            //点击某一列
            holder.dailyView.setOnWeatherItemClickListener(new DailyView.OnWeatherItemClickListener() {
                @Override
                public void onItemClick(DailyItemView itemView, int position, DailyResponse.DailyBean bean) {
                    onRecyclerViewItemClick.onItemClick(position);
                }
            });
            holder.dailyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            holder.dailyView.clearSelected();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }

        if (dailyIndexBean.getAirBeans() != null) {
            List<MoreAirFiveResponse.DailyBean> airBeans = new ArrayList<>();
            if (dailyIndexBean.getHisDailyBean() != null) {
                airBeans.add(dailyIndexBean.getHisAirBean());
            }
            airBeans.addAll(dailyIndexBean.getAirBeans());
            holder.dailyView.setAir(airBeans);
        }
    }

    /**
     * 15日天气item的基本设置
     */
    private void setDailyView(DailyViewHolder holder, List<DailyResponse.DailyBean> dailyBeans) {
        //设置白天和晚上线条的颜色
        holder.dailyView.setDayAndNightLineColor(Color.parseColor("#E4AE47"), Color.parseColor("#4287ed"),
                Color.parseColor("#fdf3ea"), Color.parseColor("#edf4fd"));

        //设置线宽
        holder.dailyView.setLineWidth(8);
        holder.dailyView.setPointRadius(12);
        holder.dailyView.setHollow(4);
        holder.dailyView.setList(dailyBeans);
        //设置一屏幕显示几列(最少3列)
        try {
            holder.dailyView.setColumnNumber(6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initLifeStyle(LifeViewHolder holder, int position) {
        LifeIndexBean lifeIndexBean = new LifeIndexBean();
        for (BaseBean i : list){
            if (i instanceof LifeIndexBean){
                lifeIndexBean = (LifeIndexBean) i;
            }
        }
        if (lifeIndexBean.getLifestyleResponse() != null) {
            List<LifestyleResponse.DailyBean> data = lifeIndexBean.getLifestyleResponse().getDaily();
            //下方圆点
            ImageView[] dotArray = new ImageView[LIFE_PAGE_NUM];
            if (holder.ll.getChildCount() == 0) {
                for (int i = 0; i < LIFE_PAGE_NUM; i++) {
                    dotArray[i] = new ImageView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                    if (i != 0) {
                        params.leftMargin = 20;
                        dotArray[i].setImageResource(R.drawable.point_next);
                    } else {
                        dotArray[i].setImageResource(R.drawable.point_current);
                    }
                    dotArray[i].setLayoutParams(params);
                    holder.ll.addView(dotArray[i]);
                }
            }

            holder.vp.setAdapter(new com.nelson.weather.adapter.LifeViewPagerAdapter(context, data));
            holder.vp.setCurrentItem(com.nelson.weather.adapter.LifeViewPagerAdapter.maxLoop / 2);
            holder.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < LIFE_PAGE_NUM; i++) {
                        ImageView dot = (ImageView) (holder.ll.getChildAt(i));
                        if (position % 2 == i) {
                            dot.setImageResource(R.drawable.point_next);
                        } else {
                            dot.setImageResource(R.drawable.point_current);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }



    static class HourlyViewHolder extends RecyclerView.ViewHolder {
        //        public FrameLayout frameLayout;
        public HourlyView hourlyView;
        public TextView tvSunrise;
        public TextView tvSunset;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.hourlyView = (HourlyView) itemView.findViewById(R.id.hourly_view);
            this.tvSunrise = (TextView) itemView.findViewById(R.id.tv_sunrise);
            this.tvSunset = (TextView) itemView.findViewById(R.id.tv_sunset);
        }
    }

    static class DailyViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout frameLayout;
        public DailyView dailyView;

//        public FrameLayout adTitleContainer;
//        public ImageView adTitleBtnClose;
//        public LinearLayout adLayout;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            dailyView = (DailyView) itemView.findViewById(R.id.daily_view);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.item_15day);

//            adTitleContainer = (FrameLayout) itemView.findViewById(R.id.ad_title_container_daily);
//            adTitleBtnClose = (ImageView) itemView.findViewById(R.id.ad_titledaily15_btnclose);
//            adLayout = (LinearLayout) itemView.findViewById(R.id.ad_daily_container);
        }
    }

    static class LifeViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout frameLayout;
        public ViewPager vp;
        public LinearLayout ll;

        public LifeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.vp = (ViewPager) itemView.findViewById(R.id.vp_life);
            this.ll = (LinearLayout) itemView.findViewById(R.id.ll_dots);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.item_life);
        }
    }


    /**
     * 显示小时详情天气信息弹窗
     */
    private void showHourlyWindow(HourlyResponse.HourlyBean data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_hourly_detail, null);
        TextView tvTime = view.findViewById(R.id.tv_time);
        TextView tvTem = view.findViewById(R.id.tv_tem);
        TextView tvCondTxt = view.findViewById(R.id.tv_cond_txt);
        TextView tvWindDeg = view.findViewById(R.id.tv_wind_deg);
        TextView tvWindDir = view.findViewById(R.id.tv_wind_dir);
        TextView tvWindSc = view.findViewById(R.id.tv_wind_sc);
        TextView tvWindSpd = view.findViewById(R.id.tv_wind_spd);
        TextView tvHum = view.findViewById(R.id.tv_hum);
        TextView tvPres = view.findViewById(R.id.tv_pres);
        TextView tvPop = view.findViewById(R.id.tv_pop);
        TextView tvDew = view.findViewById(R.id.tv_dew);
        TextView tvCloud = view.findViewById(R.id.tv_cloud);

        String time = DateUtils.updateTime(data.getFxTime());

        tvTime.setText(String.format(view.getResources().getString(R.string.index_tv_time), WeatherUtil.showTimeInfo(time), time));
        tvTem.setText(String.format(view.getResources().getString(R.string.index_tv_temp), data.getTemp()));
        tvCondTxt.setText(data.getText());
        tvWindDeg.setText(String.format(view.getResources().getString(R.string.index_tv_wind_deg), data.getWind360()));
        tvWindDir.setText(data.getWindDir());
        tvWindSc.setText(String.format(view.getResources().getString(R.string.index_tv_wind_sc), data.getWindScale()));
        tvWindSpd.setText(String.format(view.getResources().getString(R.string.index_tv_wind_spd), data.getWindSpeed()));
        tvHum.setText(data.getHumidity() + "%");
        tvPres.setText(String.format(view.getResources().getString(R.string.index_tv_pres), data.getPressure()));
        tvPop.setText(data.getPop() + "%");
        tvDew.setText(String.format(view.getResources().getString(R.string.index_tv_dew), data.getDew()));
        tvCloud.setText(data.getCloud() + "%");
        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 400), true);
    }
}
