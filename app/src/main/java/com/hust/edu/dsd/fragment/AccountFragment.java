package com.hust.edu.dsd.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hust.edu.dsd.AccountUtil;
import com.hust.edu.dsd.LoginActivity;
import com.hust.edu.dsd.R;

/**
 * Created by tungts on 3/14/2018.
 */

public class AccountFragment extends BaseFragment {

    TextView tv_id_staff, tv_name_staff, tv_role;
    TextView tv_username, tv_password, tv_date_of_birth, tv_phone, tv_address;
    TextView btn_logout;

    public static AccountFragment newInstance(){
        AccountFragment accountFragment = new AccountFragment();
        return accountFragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_account;
    }

    @Override
    protected void addControls() {
        btn_logout = (TextView) getView(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountUtil.getInstance(getActivity()).logout();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }
}
