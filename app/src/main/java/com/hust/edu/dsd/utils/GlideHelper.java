package com.hust.edu.dsd.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by tungts on 10/19/2017.
 */

public class GlideHelper {

    public static void loadImageByDrawable(Context context, ImageView imageView, int drawableImage){
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context)
                .load(drawableImage)
                .apply(options)
                .into(imageView);
    }

    public static void loadImageByPath(Context context, ImageView imageView, String path){
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context)
                .load(path)
                .apply(options)
                .into(imageView);
    }

}
