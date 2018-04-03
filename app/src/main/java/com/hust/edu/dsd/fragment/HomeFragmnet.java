package com.hust.edu.dsd.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hust.edu.dsd.R;
import com.hust.edu.dsd.adapter.InforTreeAdapter;
import com.hust.edu.dsd.api.ApiFactory;
import com.hust.edu.dsd.api.BaseCallBack;
import com.hust.edu.dsd.model.Trees;

import java.util.ArrayList;

/**
 * Created by tungts on 3/13/2018.
 */

public class HomeFragmnet extends BaseFragment {

    RecyclerView rcv_list_trees;
    InforTreeAdapter inforTreeAdapter;
    ArrayList listTrees;

    public static HomeFragmnet newInstance(){
        HomeFragmnet homeFragmnet = new HomeFragmnet();
        return homeFragmnet;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void addControls() {
        listTrees = new ArrayList();
        ApiFactory.getApiHust().getAllTrees().enqueue(new BaseCallBack<ArrayList<Trees>>(getActivity()) {
            @Override
            public void onSuccess(ArrayList<Trees> result) {
                listTrees = result;
                innitRecycleViewTrees();
            }
        });
    }

    private void innitRecycleViewTrees() {
        rcv_list_trees = (RecyclerView) getView(R.id.rcv_list_trees);
        inforTreeAdapter = new InforTreeAdapter(getContext(),listTrees);
        rcv_list_trees.setAdapter(inforTreeAdapter);
        rcv_list_trees.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
