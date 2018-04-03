package com.hust.edu.dsd;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hust.edu.dsd.api.ApiFactory;
import com.hust.edu.dsd.api.BaseCallBack;
import com.hust.edu.dsd.model.staff.Staff;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by tungts on 3/14/2018.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Toolbar toolbar;

    EditText edt_user_name,edt_pass;
    TextView tv_error_username,tv_error_pass;
    TextView btn_login;
    TextView tv_forget_pass;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void addControls() {
        AccountUtil.getInstance(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Đăng nhập");

        /////////////////////////////
        edt_user_name = findViewById(R.id.edt_user_name);
        tv_error_username = findViewById(R.id.tv_error_username);
        edt_pass = findViewById(R.id.edt_pass);
        tv_error_pass = findViewById(R.id.tv_error_pass);
        btn_login = findViewById(R.id.btn_login);
        tv_forget_pass = findViewById(R.id.tv_forget_pass);
        addEvents();
    }

    private void addEvents() {
        btn_login.setOnClickListener(this);
        tv_forget_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_forget_pass:
                forgetPass();
                break;
        }
    }

    private void forgetPass() {

    }

    private void login() {
        String userName = edt_user_name.getText().toString();
        String pass = edt_pass.getText().toString();
        if (TextUtils.isEmpty(userName)){
            tv_error_username.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(pass)) {
                tv_error_pass.setVisibility(View.VISIBLE);
            }
            return;
        }

        checkLogin(userName,pass);
    }

    private void checkLogin(String userName, String pass) {
        ApiFactory.getApiHust().login(userName,pass).enqueue(new BaseCallBack<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody result) {
                try {
                    if (result == null){
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result.string());
                    //luu lai token
                    String token = object.getString("token");
                    AccountUtil.getInstance(LoginActivity.this).setToken(token);

                    //phan quyen, change Activity
                    Staff staff = new Gson().fromJson(object.getString("result"),Staff.class);
                    changeActivity(staff);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void changeActivity(Staff staff) {
        AccountUtil.getInstance(getApplicationContext()).setUsers(staff);
        if (staff.getStaff_role().equals("TNV")){
            AccountUtil.getInstance(getApplicationContext()).setVolunteer(true);
        }
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}
