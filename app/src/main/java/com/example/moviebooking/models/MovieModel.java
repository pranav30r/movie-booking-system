package com.example.moviebooking.models;

public class MovieModel {
    private String title;
    private String genre;
    private String duration;
    private String description;
    private float rating;
    private int posterResId;

    public MovieModel(String title, String genre, String duration, String description, float rating, int posterResId) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.description = description;
        this.rating = rating;
        this.posterResId = posterResId;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public String getDescription() { return description; }
    public float getRating() { return rating; }
    public int getPosterResId() { return posterResId; }
}
