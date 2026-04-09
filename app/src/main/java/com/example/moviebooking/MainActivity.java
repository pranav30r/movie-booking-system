package com.example.moviebooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.adapters.MovieAdapter;
import com.example.moviebooking.database.BookingDatabaseHelper;
import com.example.moviebooking.models.MovieModel;
import com.example.moviebooking.storage.InternalStorageHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));

        List<MovieModel> movies = getDummyMovies();
        MovieAdapter adapter = new MovieAdapter(this, movies, this);
        rvMovies.setAdapter(adapter);

        // ========== SHARED PREFERENCES: Display selected city from Spinner ==========
        SharedPreferences cityPrefs = getSharedPreferences("CineBookPrefs", Context.MODE_PRIVATE);
        String city = cityPrefs.getString("selected_city", "Mumbai");
        TextView tvCity = findViewById(R.id.tvCityName);
        tvCity.setText("📍 " + city);

        // ========== SHARED PREFERENCES + SQLITE + INTERNAL STORAGE: Show booking info ==========
        showLastBookingInfo();

        // ========== ALERTDIALOG: Exit confirmation on back press ==========
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Exit App")
                        .setMessage("Are you sure you want to exit CineBook?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes, Exit", (dialog, which) -> {
                            finishAffinity();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    // ========== Shows last booking using SharedPreferences + SQLite count + Internal Storage count ==========
    private void showLastBookingInfo() {
        SharedPreferences prefs = getSharedPreferences("CineBookPrefs", Context.MODE_PRIVATE);
        String lastBookingId = prefs.getString("last_booking_id", null);

        if (lastBookingId != null) {
            String movie = prefs.getString("last_movie", "");
            String theatre = prefs.getString("last_theatre", "");
            String showTime = prefs.getString("last_show_time", "");
            String seats = prefs.getString("last_seats", "");
            int totalPrice = prefs.getInt("last_total_price", 0);

            // ========== SQLITE: Read total booking count from database ==========
            BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(this);
            int dbCount = dbHelper.getBookingCount();

            // ========== INTERNAL STORAGE: Read receipt count from file ==========
            int receiptCount = InternalStorageHelper.countReceipts(this);

            // ========== ALERTDIALOG: Show booking summary ==========
            new AlertDialog.Builder(this)
                    .setTitle("🎬 Welcome Back!")
                    .setMessage("Your last booking:\n\n" +
                            "Booking ID: " + lastBookingId + "\n" +
                            "Movie: " + movie + "\n" +
                            "Theatre: " + theatre + "\n" +
                            "Show: " + showTime + "\n" +
                            "Seats: " + seats + "\n" +
                            "Amount: ₹" + totalPrice + "\n\n" +
                            "📊 Total bookings in DB: " + dbCount + "\n" +
                            "📄 Receipts saved: " + receiptCount)
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    private List<MovieModel> getDummyMovies() {
        List<MovieModel> movies = new ArrayList<>();
        movies.add(new MovieModel("Avengers: Endgame", "Action, Sci-Fi", "3h 1m",
                "After the devastating events of Infinity War, the Avengers assemble once more to reverse Thanos' actions and restore balance to the universe.",
                8.4f, R.drawable.poster_1));
        movies.add(new MovieModel("The Dark Knight", "Action, Crime", "2h 32m",
                "When the menace known as the Joker wreaks havoc on Gotham, Batman must accept one of the greatest psychological and physical tests.",
                9.0f, R.drawable.poster_2));
        movies.add(new MovieModel("Inception", "Sci-Fi, Thriller", "2h 28m",
                "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                8.8f, R.drawable.poster_3));
        movies.add(new MovieModel("Interstellar", "Sci-Fi, Adventure", "2h 49m",
                "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                8.6f, R.drawable.poster_4));
        movies.add(new MovieModel("Spider-Man: No Way Home", "Action, Adventure", "2h 28m",
                "With Spider-Man's identity now revealed, Peter asks Doctor Strange for help. When a spell goes wrong, dangerous foes from other worlds begin to appear.",
                8.3f, R.drawable.poster_5));
        movies.add(new MovieModel("Oppenheimer", "Drama, History", "3h 0m",
                "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.",
                8.9f, R.drawable.poster_6));
        return movies;
    }

    // ========== ACTIVITY LIFECYCLE METHODS ========== 
    // Added for Viva demonstration of the sequence of states an Activity goes through
    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.d("LIFECYCLE", "MainActivity: onStart() called - Activity is becoming visible");
    }

    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.d("LIFECYCLE", "MainActivity: onResume() called - Activity is interacting with user");
    }

    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.d("LIFECYCLE", "MainActivity: onPause() called - Activity is partially hidden");
    }

    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.d("LIFECYCLE", "MainActivity: onStop() called - Activity is hidden");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("LIFECYCLE", "MainActivity: onDestroy() called - Activity is being destroyed");
    }

    @Override
    public void onMovieClick(MovieModel movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("genre", movie.getGenre());
        intent.putExtra("duration", movie.getDuration());
        intent.putExtra("description", movie.getDescription());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("posterResId", movie.getPosterResId());
        startActivity(intent);
    }
}
