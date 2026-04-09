package com.example.moviebooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.R;
import com.example.moviebooking.models.SeatModel;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private Context context;
    private List<SeatModel> seatList;
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatClick(int position);
    }

    public SeatAdapter(Context context, List<SeatModel> seatList, OnSeatClickListener listener) {
        this.context = context;
        this.seatList = seatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        SeatModel seat = seatList.get(position);
        holder.tvSeat.setText(seat.getSeatNumber());

        switch (seat.getStatus()) {
            case SeatModel.AVAILABLE:
                holder.tvSeat.setBackgroundResource(R.drawable.seat_available);
                holder.tvSeat.setTextColor(context.getResources().getColor(R.color.colorTextPrimary));
                break;
            case SeatModel.SELECTED:
                holder.tvSeat.setBackgroundResource(R.drawable.seat_selected);
                holder.tvSeat.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case SeatModel.BOOKED:
                holder.tvSeat.setBackgroundResource(R.drawable.seat_booked);
                holder.tvSeat.setTextColor(context.getResources().getColor(R.color.colorTextHint));
                break;
        }

        holder.tvSeat.setOnClickListener(v -> {
            if (seat.getStatus() != SeatModel.BOOKED && listener != null) {
                listener.onSeatClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeat;

        SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeat = itemView.findViewById(R.id.tvSeatNumber);
        }
    }
}
