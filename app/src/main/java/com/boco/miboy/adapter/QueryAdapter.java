package com.boco.miboy.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boco.miboy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {
    private static final String TAG = QueryAdapter.class.getSimpleName();
    private final Activity activity;
    private List<Drawable> images;

    public QueryAdapter(Activity activity, List<Drawable> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public QueryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query, parent, false));
    }

    @Override
    public void onBindViewHolder(QueryAdapter.ViewHolder holder, int position) {
        Drawable drawable = images.get(position);
        holder.photo.setImageDrawable(drawable);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener((View.OnClickListener) activity);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        ImageView photo;
        @BindView(R.id.parent)
        ViewGroup parent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}