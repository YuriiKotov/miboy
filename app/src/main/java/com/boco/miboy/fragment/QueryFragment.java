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
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.question)
    TextView questionView;
    private Question question;
    private QueryAdapter queryAdapter;

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
        nextBtn.setOnClickListener((View.OnClickListener) getActivity());
        nextBtn.setEnabled(false);
        questionView.setText(question.getQuestion());

        queryAdapter = new QueryAdapter(getContext(), question.getImages(), nextBtn);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(queryAdapter);
    }
}