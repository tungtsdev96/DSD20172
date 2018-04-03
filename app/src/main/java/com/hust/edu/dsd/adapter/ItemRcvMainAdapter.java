package com.hust.edu.dsd.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hust.edu.dsd.R;


/**
 * Created by tungts on 3/7/2018.
 */

public class ItemRcvMainAdapter extends RecyclerView.Adapter<ItemRcvMainAdapter.MainHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int Matrix[][];
    private ItemRcvAdapter adapter;

    private CordinatorClick onCordinatorClick;

    public void setOnCordinatorClick(CordinatorClick onCordinatorClick) {
        this.onCordinatorClick = onCordinatorClick;
    }

    public ItemRcvMainAdapter(Context context, int Matrix[][]) {
        this.context = context;
        this.Matrix = Matrix;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setMatrix(int Matrix[][]){
        this.Matrix = Matrix;
        notifyDataSetChanged();
    }

    public void notify(int x, int y, int type){
        adapter.notify(x,y,type);
        this.notifyItemChanged(x);
    }

    @Override
    public ItemRcvMainAdapter.MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemRcvMainAdapter.MainHolder(layoutInflater.inflate(R.layout.item_rcv_map,null));
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
        adapter = new ItemRcvAdapter(context, Matrix, position);
        adapter.setOnCordinatorClick(new CordinatorClick() {
            @Override
            public void cordinatorClick(int x, int y, int type) {
                if (ItemRcvMainAdapter.this.onCordinatorClick != null){
                    ItemRcvMainAdapter.this.onCordinatorClick.cordinatorClick(x,y,type);
                }
            }
        });
        holder.rcv.setAdapter(adapter);
        holder.rcv.setLayoutManager(new LinearLayoutManager(context,LinearLayout.VERTICAL,false));
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    protected class MainHolder extends RecyclerView.ViewHolder{

        RecyclerView rcv;

        public MainHolder(View itemView) {
            super(itemView);
            rcv = itemView.findViewById(R.id.item_rcv);
        }
    }

}
