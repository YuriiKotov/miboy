package com.boco.miboy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boco.miboy.R;
import com.boco.miboy.adapter.HistoryAdapter;
import com.boco.miboy.model.History;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Sort;

public class HistoryFragment extends BaseFragment {
    private static final String TAG = HistoryFragment.class.getSimpleName();
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<History> histories = getMainActivity().realm.where(History.class).findAllSorted("timestamp", Sort.DESCENDING);
        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), histories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}