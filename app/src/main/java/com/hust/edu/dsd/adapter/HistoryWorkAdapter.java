package com.hust.edu.dsd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hust.edu.dsd.R;
import com.hust.edu.dsd.model.staff.HistoryWork;
import com.hust.edu.dsd.utils.GlideHelper;

import java.util.List;

/**
 * Created by tungts on 3/14/2018.
 */

public class HistoryWorkAdapter extends RecyclerView.Adapter<HistoryWorkAdapter.HistryViewHolder> {

    private Context context;
    private List<HistoryWork> historyWorks;
    private LayoutInflater mLayoutInflater;

    public HistoryWorkAdapter(Context context, List<HistoryWork> historyWorks) {
        this.context = context;
        this.historyWorks = historyWorks;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public HistryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistryViewHolder(mLayoutInflater.inflate(R.layout.item_history_work,null));
    }

    @Override
    public void onBindViewHolder(HistryViewHolder holder, int position) {
        HistoryWork historyWork = historyWorks.get(position);
        holder.tv_id_tree.setText("Mã cây: " + historyWork.getTree_id()+"");
        holder.tv_date.setText(historyWork.getTime());
        holder.tv_name_tree.setText("Tên cây: " + historyWork.getTree_name()+"");
        holder.tv_water_flush.setText("Lượng nước đã tưới: " + historyWork.getVolume() * 1000+" ml");
        switch (position % 4){
            case 0:
                GlideHelper.loadImageByDrawable(context,holder.img_tree,R.drawable.tree_demo_1);
                break;
            case 1:
                GlideHelper.loadImageByDrawable(context,holder.img_tree,R.drawable.tree_demo_2);
                break;
            case 2:
                GlideHelper.loadImageByDrawable(context,holder.img_tree,R.drawable.tree_demo_3);
                break;
            case 3:
                GlideHelper.loadImageByDrawable(context,holder.img_tree,R.drawable.tree_demo_4);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return historyWorks.size();
    }

    protected class HistryViewHolder extends RecyclerView.ViewHolder{

        ImageView img_tree;

        //luong nuoc , ten, ma cay
        TextView tv_water_flush, tv_name_tree, tv_id_tree;

        //ngay tuoi, gio tuoi
        TextView tv_date,tv_time_water;

        public HistryViewHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time_water = itemView.findViewById(R.id.tv_time_water);
            tv_water_flush = itemView.findViewById(R.id.tv_water_flush);
            tv_name_tree = itemView.findViewById(R.id.tv_name_tree);
            tv_id_tree = itemView.findViewById(R.id.tv_id_tree);
            img_tree = itemView.findViewById(R.id.img_tree);
        }
    }

}
