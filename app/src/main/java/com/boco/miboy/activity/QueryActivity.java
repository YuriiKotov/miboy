package com.boco.miboy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.boco.miboy.R;
import com.boco.miboy.backend.ApiCall;
import com.boco.miboy.enums.QueryEvent;
import com.boco.miboy.fragment.QueryFragment;
import com.boco.miboy.model.Question;
import com.boco.miboy.model.Questionnaire;
import com.boco.miboy.other.AssetUtil;
import com.boco.miboy.other.Storage;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = QueryActivity.class.getSimpleName();
    private int questionNumber;
    private List<QueryFragment> queries;
    private Map<Integer, Integer> answers;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(QueryEvent queryEvent) {
        progressDialog.dismiss();
        Log.i(TAG, "onMessageEvent: " + queryEvent);
        if (queryEvent == QueryEvent.SUCCESS) {
            Storage.getInstance(this).setQueryRequired(false);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        toolbar.setTitle(R.string.toolbar_query);
        setSupportActionBar(toolbar);
        if (!Storage.getInstance(this).isQueryRequired()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
            }
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait please...");
        progressDialog.setCancelable(false);

        answers = new TreeMap<>();
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
        String question = "Choose what you like";
        Map<Integer, List<Drawable>> questionMap = AssetUtil.getInstance().getQuestions(this);
        for (Integer position : questionMap.keySet()) {
            List<Drawable> drawables = questionMap.get(position);
            questions.add(new Question(position, question, drawables));
        }
        return questions;
    }

    public void showQuestion() {
        Log.i(TAG, "showQuestion: " + questionNumber);
        if (questionNumber < queries.size()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            QueryFragment fragment = queries.get(questionNumber);

            fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
        } else {
            progressDialog.show();
            Log.i(TAG, "showQuestion: Query done");
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            new ApiCall().questionnaire(new Questionnaire(userUid, answers));
        }
    }

    @Override
    public void onClick(View view) {
        //TODO: save answers
        int selectedImgPos = (int) view.getTag();
        answers.put(questionNumber + 1, selectedImgPos);

        questionNumber++;
        showQuestion();
    }

    @Override
    public void onBackPressed() {
        if (Storage.getInstance(this).isQueryRequired()) {
            super.onBackPressed();
        } else {
            startMain();
        }
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startMain();
                break;
            default:
                break;
        }
        return true;
    }
}