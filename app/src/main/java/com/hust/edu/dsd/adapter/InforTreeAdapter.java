package com.hust.edu.dsd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hust.edu.dsd.R;
import com.hust.edu.dsd.model.Trees;
import com.hust.edu.dsd.utils.GlideHelper;

import java.util.List;

/**
 * Created by tungts on 3/13/2018.
 */

public class InforTreeAdapter extends RecyclerView.Adapter<InforTreeAdapter.TreeInforHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Trees> listTrees;

    public InforTreeAdapter(Context context, List<Trees> listTrees) {
        this.context = context;
        this.listTrees = listTrees;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TreeInforHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TreeInforHolder(mLayoutInflater.inflate(R.layout.item_infor_trees,null));
    }

    @Override
    public void onBindViewHolder(TreeInforHolder holder, int position) {
        Trees trees = listTrees.get(position);
        holder.tv_id_tree.setText(String.format(context.getString(R.string.id_tree),trees.getTree_id()));
        holder.tv_name_tree.setText(String.format(context.getString(R.string.name_tree),trees.getTree_name()));
        holder.tv_standard_humidity.setText(String.format(context.getString(R.string.standard_humidity),50));
        holder.tv_id_standard_temperature.setText(String.format(context.getString(R.string.standard_temperature),20));
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
        return listTrees.size();
    }

    protected class TreeInforHolder extends RecyclerView.ViewHolder{

        ImageView img_tree;
        TextView tv_id_tree,tv_name_tree,tv_standard_humidity,tv_id_standard_temperature;

        public TreeInforHolder(View itemView) {
            super(itemView);
            img_tree = itemView.findViewById(R.id.img_tree);
            tv_id_tree = itemView.findViewById(R.id.tv_id_tree);
            tv_name_tree = itemView.findViewById(R.id.tv_name_tree);
            tv_standard_humidity = itemView.findViewById(R.id.tv_standard_humidity);
            tv_id_standard_temperature = itemView.findViewById(R.id.tv_id_standard_temperature);
        }
    }

}
