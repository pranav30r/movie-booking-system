package com.example.moviebooking.models;

import java.util.List;

public class TheatreModel {
    private String name;
    private String location;
    private List<String> showTimings;

    public TheatreModel(String name, String location, List<String> showTimings) {
        this.name = name;
        this.location = location;
        this.showTimings = showTimings;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public List<String> getShowTimings() { return showTimings; }
}
