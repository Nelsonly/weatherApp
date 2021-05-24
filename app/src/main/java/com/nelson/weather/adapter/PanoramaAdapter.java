package com.nelson.weather.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.nelson.weather.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PanoramaAdapter extends RecyclerView.Adapter<PanoramaAdapter.MyViewHolder> {

    Context context;
    OnItemClickListener itemClicklistener;
    WeakReference<Context> weakRefs;
    List<HashMap<String, String>> suggest = new ArrayList<>();

    public PanoramaAdapter(Context context){
        weakRefs = new WeakReference(context);
        this.context = weakRefs.get();

    }

    /**
     * 创建viewHolder
     * @param parent 父容器
     * @param viewType view类型
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 绑定数据到控件
     * @param holder viewHolder
     * @param position  位置索引
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ttSugCity.setText(suggest.get(position).get("city"));
        holder.ttSugDis.setText(suggest.get(position).get("dis"));
        holder.ttSugKey.setText(suggest.get(position).get("key"));

        holder.linearLayout.setOnClickListener(v -> {
            if(itemClicklistener != null){
                itemClicklistener.onCitysClick(suggest.get(position).get("tag"),suggest.get(position).get("uid"),suggest.get(position).get("city"),suggest.get(position).get("dis"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggest.size();
    }

    public void addData(List<HashMap<String, String>> suggest ) {
        this.suggest.addAll(suggest);
        notifyDataSetChanged();
    }
    public void clearAll(){
        this.suggest.clear();
        notifyDataSetChanged();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.city_click)
        public LinearLayout linearLayout;
        @BindView(R.id.sug_key)
        TextView ttSugKey;
        @BindView(R.id.sug_city)
        TextView ttSugCity;
        @BindView(R.id.sug_dis)
        TextView ttSugDis;
        public MyViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        /**
         * 点击表情回调地址
         * @param path
         */
        void onCitysClick(String key,String path,String city,String dis);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClicklistener = listener;
    }

}
