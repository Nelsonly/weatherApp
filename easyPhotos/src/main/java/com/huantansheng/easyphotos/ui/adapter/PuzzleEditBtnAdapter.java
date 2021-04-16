package com.huantansheng.easyphotos.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.models.puzzle.PuzzleLayout;
import com.huantansheng.easyphotos.models.puzzle.template.slant.NumberSlantLayout;
import com.huantansheng.easyphotos.models.puzzle.template.straight.NumberStraightLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */
public class PuzzleEditBtnAdapter extends RecyclerView.Adapter<PuzzleEditBtnAdapter.BtnViewHolder> {

    private static final String TAG = "zhangyuhong";
    private List<String> btnText = new ArrayList<>();
    private List<Integer> btnId = new ArrayList<>();
    private OnBtnItemClickListener onItemClickListener;
    private int selectedNumber = 0;
    @Override
    public BtnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle_edit_btns, parent, false);
        return new BtnViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(BtnViewHolder holder, int position) {
        final int p = position;
        holder.imageView.setImageResource(btnId.get(p));
        holder.textView.setText(btnText.get(p));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNumber == p) {
                    return;
                }
                if (onItemClickListener != null) {
                    selectedNumber = p;
                    onItemClickListener.onBtnItemClick(btnId.get(selectedNumber));
                    notifyDataSetChanged();
                }
            }
        });
//        if (selectedNumber == position) {
//            holder.imageView.setImageResource(btnId.get(p));
//        } else {
//            holder.imageView.setImageResource(btnId.get(p));
//        }
//        holder.imageView.setImageResource(selectUrls.getResourceId( position,0));

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedNumber == p) {
//                    return;
//                }
//                if (onItemClickListener != null) {
//                    int themeId = 0;
//                    selectedNumber = p;
//                    onItemClickListener.onItemClick(themeId);
//                    notifyDataSetChanged();
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return btnText == null ? 0 : btnText.size();
    }

    public void refreshData(List<String> btnText,List<Integer> btnId) {
        this.btnText = btnText;
        this.btnId = btnId;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnBtnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class BtnViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public BtnViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.puzzle_btn_icon);
            textView = itemView.findViewById(R.id.puzzle_btn_text);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_puzzle_btns);
        }
    }

    public interface OnBtnItemClickListener {
        void onBtnItemClick( int themeId);
    }
}
