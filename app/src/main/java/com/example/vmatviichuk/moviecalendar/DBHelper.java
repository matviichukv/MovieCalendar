package com.example.vmatviichuk.moviecalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "moviesCalendarDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE genres(" +
                "_id int primary key autoincrement," +
                "name text);");
        db.execSQL("CREATE TABLE movies(" +
                "_id integer primary key autoincrement," +
                "name text," +
                "premier_date unsigned big int," +
                "description text," +
                "release_year int," +
                "actors text," +
                "producers text," +
                "origin_countries text," +
                "duration int);");

        db.execSQL("CREATE TABLE movies_genres(" +
                "movie_id int," +
                "genre_id int," +
                "FOREIGN KEY(movie_id) REFERENCES movies (_id)," +
                "FOREIGN KEY(genre_id) REFERENCES genres (_id));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE movies_genres; ");
        db.execSQL("DROP TABLE genres; ");
        db.execSQL("DROP TABLE movies;");

        db.execSQL("CREATE TABLE genres(" +
                "_id int primary key autoincrement," +
                "name text);");
        db.execSQL("CREATE TABLE movies(" +
                "_id integer primary key," +
                "name text," +
                "premier_date unsigned big int," +
                "description text," +
                "release_year int," +
                "actors text," +
                "producers text," +
                "origin_countries text," +
                "duration int);");

        db.execSQL("CREATE TABLE movies_genres(" +
                "movie_id int," +
                "genre_id int," +
                "FOREIGN KEY(movie_id) REFERENCES movies (_id)," +
                "FOREIGN KEY(genre_id) REFERENCES genres (_id));");
    }
}
