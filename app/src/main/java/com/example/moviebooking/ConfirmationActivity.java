package com.example.moviebooking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.moviebooking.database.BookingDatabaseHelper;
import com.example.moviebooking.storage.InternalStorageHelper;

import java.util.UUID;

public class ConfirmationActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "booking_channel";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        String movieTitle = getIntent().getStringExtra("movieTitle");
        String theatreName = getIntent().getStringExtra("theatreName");
        String showTime = getIntent().getStringExtra("showTime");
        String selectedSeats = getIntent().getStringExtra("selectedSeats");
        int seatCount = getIntent().getIntExtra("seatCount", 0);
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);

        String bookingId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        TextView tvBookingId = findViewById(R.id.tvBookingId);
        TextView tvConfirmMovie = findViewById(R.id.tvConfirmMovie);
        TextView tvConfirmTheatre = findViewById(R.id.tvConfirmTheatre);
        TextView tvConfirmShowTime = findViewById(R.id.tvConfirmShowTime);
        TextView tvConfirmSeats = findViewById(R.id.tvConfirmSeats);
        TextView tvConfirmTotal = findViewById(R.id.tvConfirmTotal);
        Button btnBackHome = findViewById(R.id.btnBackToHome);
        Button btnShareReceipt = findViewById(R.id.btnShareReceipt);

        tvBookingId.setText("Booking ID: " + bookingId);
        tvConfirmMovie.setText(movieTitle);
        tvConfirmTheatre.setText(theatreName);
        tvConfirmShowTime.setText(showTime);
        tvConfirmSeats.setText(selectedSeats + " (" + seatCount + " Tickets)");
        tvConfirmTotal.setText("₹" + totalPrice);

        // ========== SHARED PREFERENCES: Save Booking Data ==========
        saveBookingToPreferences(bookingId, movieTitle, theatreName, showTime, selectedSeats, seatCount, totalPrice);

        // ========== SQLITE DATABASE: Insert booking record ==========
        BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(this);
        dbHelper.insertBooking(bookingId, movieTitle, theatreName, showTime, selectedSeats, seatCount, totalPrice);

        // ========== INTERNAL STORAGE: Save receipt to file ==========
        InternalStorageHelper.saveReceipt(this, bookingId, movieTitle, theatreName, showTime, selectedSeats, seatCount, totalPrice);

        // ========== NOTIFICATION: Show Booking Confirmation ==========
        createNotificationChannel();
        showBookingNotification(bookingId, movieTitle, seatCount, totalPrice);

        // ========== TOAST: Booking success ==========
        Toast.makeText(this, "🎬 Booking Confirmed! ID: " + bookingId, Toast.LENGTH_LONG).show();

        // ========== IMPLICIT INTENT: Share Receipt (Syllabus Requirement) ==========
        btnShareReceipt.setOnClickListener(v -> {
            String receiptText = "🎬 CineBook Receipt 🎬\n\n" +
                    "Movies: " + movieTitle + "\n" +
                    "Theatre: " + theatreName + "\n" +
                    "Showtime: " + showTime + "\n" +
                    "Seats: " + selectedSeats + "\n" +
                    "Amount Paid: ₹" + totalPrice + "\n" +
                    "Booking ID: " + bookingId + "\n\n" +
                    "Enjoy your movie!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Movie Tickets");
            shareIntent.putExtra(Intent.EXTRA_TEXT, receiptText);
            startActivity(Intent.createChooser(shareIntent, "Share Receipt via"));
        });

        // ========== EXPLICIT INTENT: Go Back to Home ==========
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // ========== SHARED PREFERENCES: Save booking details locally ==========
    private void saveBookingToPreferences(String bookingId, String movie, String theatre,
                                          String showTime, String seats, int seatCount, int totalPrice) {
        SharedPreferences prefs = getSharedPreferences("CineBookPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save the latest booking details
        editor.putString("last_booking_id", bookingId);
        editor.putString("last_movie", movie);
        editor.putString("last_theatre", theatre);
        editor.putString("last_show_time", showTime);
        editor.putString("last_seats", seats);
        editor.putInt("last_seat_count", seatCount);
        editor.putInt("last_total_price", totalPrice);

        // Increment total bookings counter
        int totalBookings = prefs.getInt("total_bookings", 0);
        editor.putInt("total_bookings", totalBookings + 1);

        editor.apply();
    }

    // ========== NOTIFICATION: Create notification channel (required for Android 8.0+) ==========
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Booking Notifications";
            String description = "Notifications for movie booking confirmations";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // ========== NOTIFICATION: Display booking confirmation notification ==========
    private void showBookingNotification(String bookingId, String movieTitle, int seatCount, int totalPrice) {
        // Create intent to open the app when notification is tapped
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("🎬 Booking Confirmed!")
                .setContentText("Booking ID: " + bookingId + " | " + movieTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Booking ID: " + bookingId +
                                "\nMovie: " + movieTitle +
                                "\nTickets: " + seatCount +
                                "\nAmount Paid: ₹" + totalPrice +
                                "\n\nEnjoy your movie! 🍿"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Show the notification (check permission for Android 13+)
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
                return;
            }
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
