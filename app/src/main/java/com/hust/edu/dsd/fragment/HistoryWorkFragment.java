package com.hust.edu.dsd.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hust.edu.dsd.AccountUtil;
import com.hust.edu.dsd.R;
import com.hust.edu.dsd.adapter.HistoryWorkAdapter;
import com.hust.edu.dsd.api.ApiFactory;
import com.hust.edu.dsd.api.BaseCallBack;
import com.hust.edu.dsd.model.WaterStation;
import com.hust.edu.dsd.model.staff.HistoryWork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tungts on 3/14/2018.
 */

public class HistoryWorkFragment extends BaseFragment {

    Spinner spinner_date;
    ArrayAdapter<String> date_adapter;
    List<String> list_date;

    RecyclerView rcv_history_work;
    HistoryWorkAdapter historyWorkAdapter;
    ArrayList<HistoryWork> historyWorks;

    public static HistoryWorkFragment newInstance(){
        HistoryWorkFragment historyWorkFragment = new HistoryWorkFragment();
        return historyWorkFragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragmen_history;
    }

    @Override
    protected void addControls() {
        innitSpinner();
        innitRecycleview();
    }

    private void innitRecycleview() {
        historyWorks = new ArrayList<>();
        ApiFactory.getApiHust().getHistoryWork(AccountUtil.getInstance(getActivity()).getStaff().getStaff_id()).enqueue(new BaseCallBack<ArrayList<HistoryWork>>(getActivity()) {
            @Override
            public void onSuccess(ArrayList<HistoryWork> result) {
                historyWorks.addAll(result);
                rcv_history_work = (RecyclerView) getView(R.id.rcv_history_work);
                historyWorkAdapter = new HistoryWorkAdapter(getContext(),historyWorks);
                rcv_history_work.setAdapter(historyWorkAdapter);
                rcv_history_work.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }

    private void innitSpinner() {
        spinner_date = (Spinner) getView(R.id.spinner_date);
        list_date = new ArrayList<>();
        list_date.add("Tháng 1");
        list_date.add("Tháng 2");
        date_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_date);
        spinner_date.setAdapter(date_adapter);

    }

}
