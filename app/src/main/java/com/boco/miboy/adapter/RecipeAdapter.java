package com.boco.miboy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boco.miboy.R;
import com.boco.miboy.activity.RecipeActivity;
import com.boco.miboy.backend.Urls;
import com.boco.miboy.model.Ingredient;
import com.boco.miboy.model.Recipe;
import com.boco.miboy.other.CircleTransform;
import com.boco.miboy.other.Const;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final Context context;
    private List<Recipe> recipes;
    private View.OnClickListener onHistoryClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();
            Recipe recipe = recipes.get(pos);

            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(Const.RECIPE_EXTRA, recipe);
            context.startActivity(intent);
        }
    };

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        List<Ingredient> ingredients = recipe.getIngredients();

        int availableCount = 0;
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getValue() == 1) {
                availableCount++;
            }
        }

        String time = recipe.getTime() + " min";
//        String match = availableCount + "/" + ingredients.size();
        String match = ingredients.size() + "";

        holder.title.setText(recipe.getTitle());
        holder.time.setText(time);
        holder.match.setText(match);
        Picasso.with(context)
                .load(Urls.imageUrl + recipe.getImage())
                .placeholder(R.drawable.ic_miboy_vr2_app)
                .transform(new CircleTransform())
                .into(holder.photo);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onHistoryClick);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.photo)
        ImageView photo;
        @BindView(R.id.ingredient_count)
        TextView match;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}