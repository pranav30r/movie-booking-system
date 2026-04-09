package com.example.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.adapters.TheatreAdapter;
import com.example.moviebooking.models.TheatreModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheatreActivity extends AppCompatActivity implements TheatreAdapter.OnShowTimeClickListener {

    private String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre);

        movieTitle = getIntent().getStringExtra("movieTitle");

        TextView tvTitle = findViewById(R.id.tvTheatreTitle);
        tvTitle.setText(movieTitle);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        RecyclerView rvTheatres = findViewById(R.id.rvTheatres);
        rvTheatres.setLayoutManager(new LinearLayoutManager(this));

        List<TheatreModel> theatres = getDummyTheatres();
        TheatreAdapter adapter = new TheatreAdapter(this, theatres, this);
        rvTheatres.setAdapter(adapter);
    }

    private List<TheatreModel> getDummyTheatres() {
        List<TheatreModel> theatres = new ArrayList<>();
        theatres.add(new TheatreModel("PVR Cinemas", "Phoenix Mall, Mumbai",
                Arrays.asList("10:00 AM", "1:30 PM", "4:45 PM", "7:00 PM", "10:15 PM")));
        theatres.add(new TheatreModel("INOX Megaplex", "R-City Mall, Ghatkopar",
                Arrays.asList("9:30 AM", "12:45 PM", "4:00 PM", "7:30 PM", "11:00 PM")));
        theatres.add(new TheatreModel("Cinépolis", "Viviana Mall, Thane",
                Arrays.asList("11:00 AM", "2:15 PM", "5:30 PM", "9:00 PM")));
        theatres.add(new TheatreModel("Carnival Cinemas", "Andheri West, Mumbai",
                Arrays.asList("10:30 AM", "1:00 PM", "6:00 PM", "9:30 PM")));
        return theatres;
    }

    @Override
    public void onShowTimeClick(TheatreModel theatre, String showTime) {
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        intent.putExtra("movieTitle", movieTitle);
        intent.putExtra("theatreName", theatre.getName());
        intent.putExtra("showTime", showTime);
        startActivity(intent);
    }
}
