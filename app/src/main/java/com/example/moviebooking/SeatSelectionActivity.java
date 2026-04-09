package com.example.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.adapters.SeatAdapter;
import com.example.moviebooking.models.SeatModel;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity implements SeatAdapter.OnSeatClickListener {

    private static final int PRICE_PER_SEAT = 200;
    private static final int COLUMNS = 8;
    private static final int ROWS = 5;

    private TextView tvSelectedCount, tvTotalPrice;
    private Button btnProceed;
    private List<SeatModel> seatList;
    private SeatAdapter adapter;
    private String movieTitle, theatreName, showTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        movieTitle = getIntent().getStringExtra("movieTitle");
        theatreName = getIntent().getStringExtra("theatreName");
        showTime = getIntent().getStringExtra("showTime");

        TextView tvMovieInfo = findViewById(R.id.tvSeatMovieInfo);
        tvMovieInfo.setText(movieTitle + " | " + showTime);

        TextView tvTheatreInfo = findViewById(R.id.tvSeatTheatreInfo);
        tvTheatreInfo.setText(theatreName);

        tvSelectedCount = findViewById(R.id.tvSelectedCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnProceed = findViewById(R.id.btnProceedPayment);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        RecyclerView rvSeats = findViewById(R.id.rvSeats);
        rvSeats.setLayoutManager(new GridLayoutManager(this, COLUMNS));

        seatList = generateSeats();
        adapter = new SeatAdapter(this, seatList, this);
        rvSeats.setAdapter(adapter);

        updateUI();

        btnProceed.setOnClickListener(v -> {
            int selectedCount = getSelectedCount();
            if (selectedCount > 0) {
                // ========== TOAST: Proceeding to payment ==========
                Toast.makeText(this, "Proceeding with " + selectedCount + " seat(s)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SeatSelectionActivity.this, PaymentActivity.class);
                intent.putExtra("movieTitle", movieTitle);
                intent.putExtra("theatreName", theatreName);
                intent.putExtra("showTime", showTime);
                intent.putExtra("seatCount", selectedCount);
                intent.putExtra("selectedSeats", getSelectedSeatNumbers());
                intent.putExtra("totalPrice", selectedCount * PRICE_PER_SEAT);
                startActivity(intent);
            } else {
                // ========== TOAST: No seats selected warning ==========
                Toast.makeText(this, "Please select at least one seat!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<SeatModel> generateSeats() {
        List<SeatModel> seats = new ArrayList<>();
        String[] rowLabels = {"A", "B", "C", "D", "E"};

        for (int i = 0; i < ROWS; i++) {
            for (int j = 1; j <= COLUMNS; j++) {
                String seatNumber = rowLabels[i] + j;
                int status = SeatModel.AVAILABLE;
                // Mark some seats as booked (hardcoded)
                if ((i == 0 && (j == 3 || j == 4 || j == 5)) ||
                        (i == 1 && (j == 6 || j == 7)) ||
                        (i == 2 && (j == 1 || j == 2)) ||
                        (i == 3 && (j == 4 || j == 5 || j == 8)) ||
                        (i == 4 && (j == 2 || j == 6 || j == 7))) {
                    status = SeatModel.BOOKED;
                }
                seats.add(new SeatModel(seatNumber, status));
            }
        }
        return seats;
    }

    @Override
    public void onSeatClick(int position) {
        SeatModel seat = seatList.get(position);
        if (seat.getStatus() == SeatModel.AVAILABLE) {
            seat.setStatus(SeatModel.SELECTED);
            // ========== TOAST: Seat selected ==========
            Toast.makeText(this, "Seat " + seat.getSeatNumber() + " selected", Toast.LENGTH_SHORT).show();
        } else if (seat.getStatus() == SeatModel.SELECTED) {
            seat.setStatus(SeatModel.AVAILABLE);
            // ========== TOAST: Seat deselected ==========
            Toast.makeText(this, "Seat " + seat.getSeatNumber() + " deselected", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyItemChanged(position);
        updateUI();
    }

    private void updateUI() {
        int count = getSelectedCount();
        int total = count * PRICE_PER_SEAT;
        tvSelectedCount.setText(count + " Seat(s) Selected");
        tvTotalPrice.setText("₹" + total);
        btnProceed.setEnabled(count > 0);
        btnProceed.setAlpha(count > 0 ? 1.0f : 0.5f);
    }

    private int getSelectedCount() {
        int count = 0;
        for (SeatModel seat : seatList) {
            if (seat.getStatus() == SeatModel.SELECTED) count++;
        }
        return count;
    }

    private String getSelectedSeatNumbers() {
        StringBuilder sb = new StringBuilder();
        for (SeatModel seat : seatList) {
            if (seat.getStatus() == SeatModel.SELECTED) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(seat.getSeatNumber());
            }
        }
        return sb.toString();
    }
}
