package com.boco.miboy.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.boco.miboy.R;
import com.boco.miboy.fragment.QueryFragment;
import com.boco.miboy.model.Question;
import com.boco.miboy.other.AssetUtil;
import com.boco.miboy.other.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = QueryActivity.class.getSimpleName();
    private int questionNumber;
    private List<QueryFragment> queries;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.toolbar_query);
        setSupportActionBar(toolbar);

        List<Question> questions = getImagesForQuery();

        queries = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            QueryFragment queryFragment = QueryFragment.newInstance(questions.get(i));
            queries.add(queryFragment);
        }
        showQuestion();
    }

    public List<Question> getImagesForQuery() {
        List<Question> questions = new ArrayList<>();
        AssetUtil assetUtil = new AssetUtil(this);
        Map<Integer, List<Drawable>> questionMap = assetUtil.getQuestions();
        for (Integer position : questionMap.keySet()) {
            List<Drawable> drawables = questionMap.get(position);
            questions.add(new Question(position, "Question number " + position + "?", drawables));
        }
        return questions;
    }

    public void showQuestion() {
        Log.i(TAG, "showQuestion: " + questionNumber);
        if (questionNumber < queries.size()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            QueryFragment fragment = queries.get(questionNumber);

            fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
            questionNumber++;
        } else {
            //TODO: send answer to backend
            Log.i(TAG, "showQuestion: Query done");
            Storage.getInstance(this).setQueryRequired(false);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        showQuestion();
    }
}