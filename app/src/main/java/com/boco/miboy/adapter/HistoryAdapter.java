package com.boco.miboy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boco.miboy.R;
import com.boco.miboy.activity.ListActivity;
import com.boco.miboy.model.History;
import com.boco.miboy.other.Const;
import com.boco.miboy.other.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final Context context;
    private List<History> histories;
    private View.OnClickListener onHistoryClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();
            History history = histories.get(pos);

            Intent intent = new Intent(context, ListActivity.class);
            intent.putExtra(Const.HISTORY_ID_EXTRA, history.getTimestamp());
            context.startActivity(intent);
        }
    };

    public HistoryAdapter(Context context, List<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        History history = histories.get(position);

        Bitmap bitmap = BitmapFactory.decodeByteArray(history.getPhoto(), 0, history.getPhoto().length);

        holder.photo.setImageBitmap(bitmap);
        holder.time.setText(TextUtil.getTime(history.getTimestamp()));

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onHistoryClick);
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.photo)
        ImageView photo;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}