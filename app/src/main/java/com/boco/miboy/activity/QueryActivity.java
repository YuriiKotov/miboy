package com.boco.miboy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.boco.miboy.R;
import com.boco.miboy.fragment.QueryFragment;
import com.boco.miboy.model.Question;
import com.boco.miboy.other.Storage;

import java.util.ArrayList;
import java.util.List;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = QueryActivity.class.getSimpleName();
    private int questionNumber;
    private List<QueryFragment> queries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        Log.i(TAG, "onCreate: ");

        List<Question> questions = getImagesForQuery();

        queries = new ArrayList<>();
//        for (int i = 0; i < questions.size(); i++) {
        for (int i = 0; i < 4; i++) {
            QueryFragment queryFragment = QueryFragment.newInstance(questions.get(i));
            queries.add(queryFragment);
        }
        showQuestion();
    }

    public List<Question> getImagesForQuery() {
        //TODO:
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Question number 1", null));
        questions.add(new Question("Question number 2", null));
        questions.add(new Question("Question number 3", null));
        questions.add(new Question("Question number 4", null));
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