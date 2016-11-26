package com.boco.miboy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.boco.miboy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {
    private static final String TAG = QueryAdapter.class.getSimpleName();
    private final Context context;
    private final Button nextFragmentBtn;
    private List<Drawable> images;
    private int checkedItem = -1;
    private View.OnClickListener checkedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            Log.i(TAG, "onClick: " + position);
            checkedItem = position;
            nextFragmentBtn.setTag(checkedItem);
            nextFragmentBtn.setEnabled(true);
            notifyDataSetChanged();
        }
    };

    public QueryAdapter(Context context, List<Drawable> images, Button nextFragmentBtn) {
        this.context = context;
        this.images = images;
        this.nextFragmentBtn = nextFragmentBtn;
    }

    @Override
    public QueryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query, parent, false));
    }

    @Override
    public void onBindViewHolder(QueryAdapter.ViewHolder holder, int position) {
        Drawable drawable = images.get(position);
        holder.photo.setImageDrawable(drawable);

        if (checkedItem == position) {
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_light));
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(checkedListener);
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