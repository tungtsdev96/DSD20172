package com.hust.edu.dsd;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.hust.edu.dsd.adapter.ItemRcvMainAdapter;
import com.hust.edu.dsd.fragment.AccountFragment;
import com.hust.edu.dsd.fragment.HistoryWorkFragment;
import com.hust.edu.dsd.fragment.HomeFragmnet;
import com.hust.edu.dsd.utils.Constants;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("DSD-29");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeFragment(HomeFragmnet.newInstance());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch (id){
            case R.id.menu_navi_home:
                changeFragment(HomeFragmnet.newInstance());
                break;
            case R.id.menu_navi_account:
                changeFragment(AccountFragment.newInstance());
                break;
            case R.id.menu_navi_history_work:
                changeFragment(HistoryWorkFragment.newInstance());
                break;
            case R.id.menu_navi_map:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, MapActivity.class));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                },1000);
                break;
            case R.id.menu_navi_tutorials:
                break;
        }
        return true;
    }

    Handler handler = new Handler();
    private void changeFragment(final Fragment fragment){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in,0);
                ft.replace(R.id.content_main, fragment).commit();
            }
        };
        handler.postDelayed(runnable,300);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AccountUtil.getInstance(this).logout();
        finish();
    }
}
