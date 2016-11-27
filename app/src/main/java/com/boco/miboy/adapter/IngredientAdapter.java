package com.boco.miboy.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boco.miboy.R;
import com.boco.miboy.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private static final String TAG = IngredientAdapter.class.getSimpleName();
    private final Context context;
    private List<Ingredient> ingredients;

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
        Log.i(TAG, "IngredientAdapter: " + ingredients.size());
    }

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        Log.i(TAG, "onBindViewHolder: ");

        String item = " - " + ingredient.getKey();
        boolean available = ingredient.getValue() == 1;
        int buyBtnVisibility = available ? View.GONE : View.VISIBLE;
        @ColorInt int color = available
                ? ContextCompat.getColor(context, R.color.primary_text)
                : ContextCompat.getColor(context, R.color.secondary_text);


        holder.buy.setVisibility(buyBtnVisibility);
        holder.text.setText(item);
        holder.text.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + ingredients.size());
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView text;
        @BindView(R.id.buy_btn)
        Button buy;

        @OnClick(R.id.buy_btn)
        public void onBuyClick(View v) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_buy, null);
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Buy product")
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}