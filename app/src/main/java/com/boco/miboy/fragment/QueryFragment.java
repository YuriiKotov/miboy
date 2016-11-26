package com.boco.miboy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boco.miboy.R;
import com.boco.miboy.adapter.QueryAdapter;
import com.boco.miboy.model.Question;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryFragment extends Fragment {
    private static final String TAG = QueryFragment.class.getSimpleName();
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.result_tv)
    TextView resultView;
    @BindView(R.id.question)
    TextView questionView;
    private Question question;

    public static QueryFragment newInstance(Question question) {
        QueryFragment queryFragment = new QueryFragment();
        queryFragment.question = question;
        return queryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query, container, false);
        ButterKnife.bind(this, view);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
        String resultText = question.getNumber() + " / 7";

        resultView.setText(resultText);
        questionView.setText(question.getQuestion());

        QueryAdapter queryAdapter = new QueryAdapter(getActivity(), question.getImages());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(queryAdapter);
    }
}