package com.example.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        String movieTitle = getIntent().getStringExtra("movieTitle");
        String theatreName = getIntent().getStringExtra("theatreName");
        String showTime = getIntent().getStringExtra("showTime");
        int seatCount = getIntent().getIntExtra("seatCount", 0);
        String selectedSeats = getIntent().getStringExtra("selectedSeats");
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);

        TextView tvMovieName = findViewById(R.id.tvPayMovieName);
        TextView tvTheatreName = findViewById(R.id.tvPayTheatreName);
        TextView tvShowTime = findViewById(R.id.tvPayShowTime);
        TextView tvSeats = findViewById(R.id.tvPaySeats);
        TextView tvSeatCount = findViewById(R.id.tvPaySeatCount);
        TextView tvTotal = findViewById(R.id.tvPayTotal);
        Button btnPayNow = findViewById(R.id.btnPayNow);
        ImageView ivBack = findViewById(R.id.ivBack);

        tvMovieName.setText(movieTitle);
        tvTheatreName.setText(theatreName);
        tvShowTime.setText(showTime);
        tvSeats.setText(selectedSeats);
        tvSeatCount.setText(seatCount + " Ticket(s)");
        tvTotal.setText("₹" + totalPrice);

        ivBack.setOnClickListener(v -> finish());

        // ========== ALERTDIALOG: Confirm Payment ==========
        btnPayNow.setOnClickListener(v -> {
            new AlertDialog.Builder(PaymentActivity.this)
                    .setTitle("Confirm Payment")
                    .setMessage("Pay ₹" + totalPrice + " for " + seatCount + " ticket(s)?\n\nMovie: " + movieTitle + "\nSeats: " + selectedSeats)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Pay Now", (dialog, which) -> {
                        Intent intent = new Intent(PaymentActivity.this, ConfirmationActivity.class);
                        intent.putExtra("movieTitle", movieTitle);
                        intent.putExtra("theatreName", theatreName);
                        intent.putExtra("showTime", showTime);
                        intent.putExtra("selectedSeats", selectedSeats);
                        intent.putExtra("seatCount", seatCount);
                        intent.putExtra("totalPrice", totalPrice);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setCancelable(true)
                    .show();
        });

        // ========== ALERTDIALOG: Confirm Back Navigation (using OnBackPressedCallback) ==========
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(PaymentActivity.this)
                        .setTitle("Cancel Payment?")
                        .setMessage("Are you sure you want to go back? Your payment will not be processed.")
                        .setPositiveButton("Yes, Go Back", (dialog, which) -> {
                            setEnabled(false);
                            getOnBackPressedDispatcher().onBackPressed();
                        })
                        .setNegativeButton("Stay", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }
}
