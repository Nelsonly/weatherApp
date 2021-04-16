package com.huantansheng.easyphotos.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.models.puzzle.PuzzleLayout;
import com.huantansheng.easyphotos.models.puzzle.SquarePuzzleView;
import com.huantansheng.easyphotos.models.puzzle.template.slant.NumberSlantLayout;
import com.huantansheng.easyphotos.models.puzzle.template.straight.NumberStraightLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */
public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.PuzzleViewHolder> {

    private static final String TAG = "zhangyuhong";
    private List<PuzzleLayout> layoutData = new ArrayList<>();
    private TypedArray selectUrls;
    private TypedArray unSelectUrls;
    private OnItemClickListener onItemClickListener;
    private int selectedNumber = 0;
    private final int puzzleModelBgUnselectColor = Color.parseColor("#FFFFFF");
    private final int puzzleModelBgSelectColor = Color.parseColor("#F88888");
    private final int puzzleModelSelectLineColor = Color.parseColor("#F83628");
    private final int puzzleModelUnselectLineColor = Color.parseColor("#DDD5D5");
    @Override
    public PuzzleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle_easy_photos, parent, false);
        return new PuzzleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PuzzleViewHolder holder, final int position) {
        final PuzzleLayout puzzleLayout = layoutData.get(position);
        final int p = position;
        if (selectedNumber == position) {
            holder.imageView.setImageResource(selectUrls.getResourceId(position,0));
        } else {
            holder.imageView.setImageResource(unSelectUrls.getResourceId(position,0));
        }
//        holder.imageView.setImageResource(selectUrls.getResourceId( position,0));

        holder.puzzleView.setNeedDrawLine(true);
        holder.puzzleView.setNeedDrawOuterLine(true);
        holder.puzzleView.setTouchEnable(false);
        holder.puzzleView.setPuzzleLayout(puzzleLayout);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNumber == p) {
                    return;
                }
                if (onItemClickListener != null) {
                    int themeType = 0;
                    int themeId = 0;
                    if (puzzleLayout instanceof NumberSlantLayout) {
                        themeType = 0;
                        themeId = ((NumberSlantLayout) puzzleLayout).getTheme();
                    } else if (puzzleLayout instanceof NumberStraightLayout) {
                        themeType = 1;
                        themeId = ((NumberStraightLayout) puzzleLayout).getTheme();
                    }
                    selectedNumber = p;
                    onItemClickListener.onItemClick(themeType, themeId,position);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return layoutData == null ? 0 : layoutData.size();
    }

    public void refreshData(List<PuzzleLayout> layoutData) {
        this.layoutData = layoutData;
        notifyDataSetChanged();
    }

    /**
     *
     * @param layoutData puzzleLayout集
     * @param unSelectUrls 未选择的icon的数组，存在array.xml中
     * @param selectUrls 选择的icon的数组，存在array.xml中
     */
    public void refreshData(List<PuzzleLayout> layoutData, TypedArray unSelectUrls, TypedArray selectUrls){
        this.layoutData = layoutData;
        this.unSelectUrls = unSelectUrls;
        this.selectUrls = selectUrls;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class PuzzleViewHolder extends RecyclerView.ViewHolder {

        SquarePuzzleView puzzleView;
        ImageView imageView;

        public PuzzleViewHolder(View itemView) {
            super(itemView);
            puzzleView = (SquarePuzzleView) itemView.findViewById(R.id.puzzle);
            imageView = itemView.findViewById(R.id.m_selector);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int themeType, int themeId,int pos);
    }
}
