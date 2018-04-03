package com.hust.edu.dsd;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.hust.edu.dsd.interfaces.AlertListener;

/**
 * Created by tungts on 3/13/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        addControls();
    }

    public abstract int getLayoutResourceId();

    protected abstract void addControls();

    protected void setLeftActionIcon(int resId,Toolbar toolbar) {
        toolbar.setNavigationIcon(resId);
    }

    public void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    protected void showKeyboard(View target) {
        if (target == null) {
            return;
        }
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(target,
                InputMethodManager.SHOW_IMPLICIT);
    }

    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showAlert(final String title, final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                new AlertDialog.Builder(BaseActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.ok, null).create()
                        .show();
            }
        });
    }

    protected void showAlert(final String title, final String message, final String yesTitle, final String noTitle, final AlertListener listener) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog dialog =  new AlertDialog.Builder(BaseActivity.this)
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
