package com.hust.edu.dsd.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hust.edu.dsd.R;
import com.hust.edu.dsd.interfaces.AlertListener;

/**
 * Created by tungts on 10/19/2017.
 */

public abstract class BaseFragment extends Fragment {

    View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(getLayoutResource(),container,false);
        addControls();
        return root;
    }

    public View getView(int id){
        return root.findViewById(id);
    }

    public abstract int getLayoutResource();

    protected abstract void addControls();

    protected void showAlert(final String title, final String message, final String yesTitle, final String noTitle, final AlertListener listener) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog dialog =  new AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                        .setPositiveButton(yesTitle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (listener != null) {
                                    listener.onYesClicked();
                                }
                            }
                        })
                        .setNegativeButton(noTitle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (listener != null) {
                                    listener.onNoClicked();
                                }
                            }
                        }).create();
                dialog.show();
                Button buttonbackground = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);;
                buttonbackground.setTextColor(getResources().getColor(R.color.colorgreen));

                Button buttonbackground1 = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonbackground1.setTextColor(getResources().getColor(R.color.colorgreen));
            }
        });
    }

}
