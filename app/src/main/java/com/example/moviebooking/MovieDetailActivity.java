package com.example.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        String title = getIntent().getStringExtra("title");
        String genre = getIntent().getStringExtra("genre");
        String duration = getIntent().getStringExtra("duration");
        String description = getIntent().getStringExtra("description");
        float rating = getIntent().getFloatExtra("rating", 0f);
        int posterResId = getIntent().getIntExtra("posterResId", 0);

        ImageView ivPoster = findViewById(R.id.ivMoviePoster);
        TextView tvTitle = findViewById(R.id.tvMovieTitle);
        TextView tvGenre = findViewById(R.id.tvMovieGenre);
        TextView tvDuration = findViewById(R.id.tvMovieDuration);
        TextView tvDescription = findViewById(R.id.tvMovieDescription);
        TextView tvRating = findViewById(R.id.tvMovieRating);
        Button btnBookTickets = findViewById(R.id.btnBookTickets);
        ImageView ivBack = findViewById(R.id.ivBack);

        ivPoster.setImageResource(posterResId);
        tvTitle.setText(title);
        tvGenre.setText(genre);
        tvDuration.setText(duration);
        tvDescription.setText(description);
        tvRating.setText("⭐ " + rating);

        ivBack.setOnClickListener(v -> finish());

        btnBookTickets.setOnClickListener(v -> {
            Intent intent = new Intent(MovieDetailActivity.this, TheatreActivity.class);
            intent.putExtra("movieTitle", title);
            startActivity(intent);
        });
    }
}
