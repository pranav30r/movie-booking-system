package com.example.moviebooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.R;
import com.example.moviebooking.models.TheatreModel;

import java.util.List;

public class TheatreAdapter extends RecyclerView.Adapter<TheatreAdapter.TheatreViewHolder> {

    private Context context;
    private List<TheatreModel> theatreList;
    private OnShowTimeClickListener listener;

    public interface OnShowTimeClickListener {
        void onShowTimeClick(TheatreModel theatre, String showTime);
    }

    public TheatreAdapter(Context context, List<TheatreModel> theatreList, OnShowTimeClickListener listener) {
        this.context = context;
        this.theatreList = theatreList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TheatreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_theatre, parent, false);
        return new TheatreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheatreViewHolder holder, int position) {
        TheatreModel theatre = theatreList.get(position);
        holder.tvName.setText(theatre.getName());
        holder.tvLocation.setText(theatre.getLocation());

        holder.llShowTimes.removeAllViews();
        for (String time : theatre.getShowTimings()) {
            TextView tvTime = new TextView(context);
            tvTime.setText(time);
            tvTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
            tvTime.setBackgroundResource(R.drawable.time_slot_bg);
            tvTime.setPadding(32, 16, 32, 16);
            tvTime.setTextSize(13);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 16, 0);
            tvTime.setLayoutParams(params);

            tvTime.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShowTimeClick(theatre, time);
                }
            });

            holder.llShowTimes.addView(tvTime);
        }
    }

    @Override
    public int getItemCount() {
        return theatreList.size();
    }

    static class TheatreViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation;
        LinearLayout llShowTimes;

        TheatreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTheatreName);
            tvLocation = itemView.findViewById(R.id.tvTheatreLocation);
            llShowTimes = itemView.findViewById(R.id.llShowTimes);
        }
    }
}
