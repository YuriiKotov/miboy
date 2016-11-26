package com.boco.miboy.adapter;

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
    private List<Drawable> images;

    public QueryAdapter(List<Drawable> images) {
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
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}