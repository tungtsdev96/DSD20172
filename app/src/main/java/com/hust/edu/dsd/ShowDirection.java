package com.hust.edu.dsd;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.hust.edu.dsd.adapter.ItemRcvMainAdapter;
import com.hust.edu.dsd.algorithm.SearchDirection;

import java.util.ArrayList;

/**
 * Created by tungts on 3/22/2018.
 */

public class ShowDirection {

    interface ShowDirectionDone{
        void onDone();
    }

    private static boolean isShowing = false;
    private ShowDirectionDone onShowDirectionDone;
    private static ShowDirection showDirection;
    Handler handler;

    private ShowDirection(){
        handler = new Handler();
    }

    public static boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public static ShowDirection getInstance(ShowDirectionDone onShowDirectionDone){
        if (showDirection == null){
            showDirection = new ShowDirection();
        }
        showDirection.onShowDirectionDone = onShowDirectionDone;
        return showDirection;
    }

    int dem;
    public void showDirection(final ArrayList<SearchDirection.ToaDo> arrYoaDo, final int type, final MapActivity mapActivity){
        dem = arrYoaDo.size() - 2;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mapActivity.notifyAdapter(arrYoaDo.get(dem).x, arrYoaDo.get(dem).y, type);
                dem--;
                handler.postDelayed(this,200);
                setShowing(true);
                if (dem == 0) {
                    if (onShowDirectionDone != null) {
                        setShowing(false);
                        onShowDirectionDone.onDone();
                    };
                    handler.removeCallbacks(this);
                    return;
                }
            }
        };
        handler.postDelayed(runnable,500);
    }

}
