package com.hust.edu.dsd.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hust.edu.dsd.R;
import com.hust.edu.dsd.utils.Constants;
import com.hust.edu.dsd.utils.GlideHelper;

/**
 * Created by tungts on 3/7/2018.
 */

public class ItemRcvAdapter extends RecyclerView.Adapter<ItemRcvAdapter.Holder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int[][] matrix;
    private int x;
    private CordinatorClick onCordinatorClick;

    public void setOnCordinatorClick(CordinatorClick onCordinatorClick) {
        this.onCordinatorClick = onCordinatorClick;
    }

    public ItemRcvAdapter(Context context, int[][] matrix, int x) {
        this.context = context;
        this.matrix = matrix;
        layoutInflater = LayoutInflater.from(context);
        this.x = x;
    }

    public void notify(int x, int y, int type){
        matrix[x][y] = type;
        this.notifyItemChanged(y);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(layoutInflater.inflate(R.layout.item_coodinator_map,null));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int y = position;
        int type = matrix[x][y];
        recycle(holder.img);
        switch (type){
            case Constants.BACKGROUND:
                holder.img.setImageResource(R.drawable.item_square);
                break;
            case Constants.TREES:
                GlideHelper.loadImageByDrawable(context,holder.img,R.drawable.ic_tree);
                holder.setOnclick(x,y,Constants.TREES);
                break;
            case Constants.BUILDING:
                GlideHelper.loadImageByDrawable(context,holder.img,R.drawable.ic_building);
                break;
            case Constants.TREE_REGISTRATION:
                GlideHelper.loadImageByDrawable(context,holder.img,R.drawable.ic_registration_tree);
                holder.setOnclick(x,y,Constants.TREE_REGISTRATION);
                break;
            case Constants.WATER_SOURCE:
                GlideHelper.loadImageByDrawable(context,holder.img,R.drawable.ic_water_source_map);
                break;
            case Constants.START:
                holder.img.setImageResource(R.drawable.item_start);
                holder.img.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_current_location));
                holder.setOnclick(x,y,Constants.START);
                break;
            case Constants.END:
                break;
            case Constants.DIRECTION:
                holder.img.setImageResource(R.drawable.item_direction);
                break;
            case Constants.TREE_IRRIGATED:
                GlideHelper.loadImageByDrawable(context,holder.img,R.drawable.ic_tree_irrigated);
                holder.setOnclick(x,y,Constants.TREE_IRRIGATED);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    private void recycle(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }

    protected class Holder extends RecyclerView.ViewHolder{

        ImageView img;

        public Holder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }

        public void setOnclick(final int x, final int y, final int type){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCordinatorClick != null){
                        onCordinatorClick.cordinatorClick(x,y,type);
                    }
                }
            });
        }
    }
}
