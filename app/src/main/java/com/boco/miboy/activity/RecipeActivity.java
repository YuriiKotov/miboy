package com.boco.miboy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.boco.miboy.R;
import com.boco.miboy.adapter.IngredientAdapter;
import com.boco.miboy.backend.Urls;
import com.boco.miboy.model.Recipe;
import com.boco.miboy.other.Const;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {
    private static final String TAG = RecipeActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        Recipe recipe = getIntent().getParcelableExtra(Const.RECIPE_EXTRA);
        if (recipe == null) {
            startMain();
            return;
        }

        toolbar.setTitle(recipe.getTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        Picasso.with(this)
                .load(Urls.imageUrl + recipe.getImage())
                .into(image);

        Log.i(TAG, "onCreate: " + recipe.toString());

        IngredientAdapter ingredientAdapter = new IngredientAdapter(recipe.getIngredients());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ingredientAdapter);
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