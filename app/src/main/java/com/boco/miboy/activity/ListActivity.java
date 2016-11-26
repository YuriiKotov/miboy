package com.boco.miboy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.boco.miboy.R;
import com.boco.miboy.adapter.RecipeAdapter;
import com.boco.miboy.model.History;
import com.boco.miboy.model.ServerResponse;
import com.boco.miboy.other.Const;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ListActivity extends AppCompatActivity {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Realm realm;

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        toolbar.setTitle(R.string.toolbar_list);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        long id = getIntent().getLongExtra(Const.HISTORY_ID_EXTRA, -1);
        if (id == -1) {
            startMain();
            return;
        }

        History history = realm.where(History.class).equalTo("timestamp", id).findFirst();
        if (history == null) {
            startMain();
            return;
        }

        Gson gson = new Gson();
        ServerResponse serverResponse = gson.fromJson(history.getJson(), ServerResponse.class);
        RecipeAdapter recipeAdapter = new RecipeAdapter(this, serverResponse.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);
    }

    private void startMain() {
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

    @Override
    public void onBackPressed() {
        startMain();
    }
}