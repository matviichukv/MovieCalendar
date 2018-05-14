package com.example.vmatviichuk.moviecalendar;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    int id;
    String name;
    String description;
    List<String> genres;
    int releaseYear;
    long premierDate;
    String actors;
    String producers;
    String originCountry;
    int duration;

    public Movie() {
        genres = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseDate) {
        this.releaseYear = releaseDate;
    }

    public long getPremierDate() {
        return premierDate;
    }

    public void setPremierDate(long premierDate) {
        this.premierDate = premierDate;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
