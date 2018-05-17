package com.example.vmatviichuk.moviecalendar;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable{

    int id;
    String name;
    String description;
    List<String> genres;
    long premierDate;
    String actors;
    String producers;



    public Movie() {

        genres = new ArrayList<>();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        // распаковываем объект из Parcel
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel p) {
        genres = new ArrayList<>();
        setId(p.readInt());
        setName(p.readString());
        setDescription(p.readString());
        p.readStringList(genres);
        setPremierDate(p.readLong());
        setActors(p.readString());
        setProducers(p.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringList(genres);
        dest.writeLong(premierDate);
        dest.writeString(actors);
        dest.writeString(producers);
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
