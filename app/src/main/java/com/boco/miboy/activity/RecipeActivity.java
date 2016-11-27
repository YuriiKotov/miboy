package com.boco.miboy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boco.miboy.R;
import com.boco.miboy.adapter.IngredientAdapter;
import com.boco.miboy.backend.Urls;
import com.boco.miboy.model.Recipe;
import com.boco.miboy.other.Const;
import com.boco.miboy.other.TextUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {
    private static final String TAG = RecipeActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_image)
    ImageView image;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.video_block)
    ViewGroup videoBlock;
    @BindView(R.id.video)
    TextView video;
    @BindView(R.id.my_ctl)
    CollapsingToolbarLayout collapsingToolbarLayout;

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
        Log.i(TAG, "onCreate: " + recipe.toString());
        //Toolbar
        toolbar.setTitle(recipe.getTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        //Photo
        Picasso.with(this)
                .load(Urls.imageUrl + recipe.getImage())
                .placeholder(R.drawable.ic_miboy_vr1_app)
                .into(image);
        //Ingredients
        IngredientAdapter ingredientAdapter = new IngredientAdapter(this, recipe.getIngredients());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ingredientAdapter);
        //Description
        description.setText(recipe.getInstructions());
        //Time
        String timer = recipe.getTime() + " min";
        time.setText(timer);
        //Video
        if (recipe.getVideo() != null && !recipe.getVideo().isEmpty()) {
            videoBlock.setVisibility(View.VISIBLE);
            video.setText(recipe.getVideo());
        } else {
            videoBlock.setVisibility(View.GONE);
        }

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