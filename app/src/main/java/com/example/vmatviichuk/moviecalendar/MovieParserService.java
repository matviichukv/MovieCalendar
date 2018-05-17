package com.example.vmatviichuk.moviecalendar;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieParserService extends Service {
//    Document doc = Jsoup.parse(new URL("https",
//            "www.kinonews.ru",
//            -1,
//            "/premiers_world/") , 10000);

    private PendingIntent pendingIntent;

    private final String protocol = "https";
    private final String host = "www.imdb.com";

    public MovieParserService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("log1", "service on create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("log1", "111");
        //dbHelper = new DBHelper(this);
        pendingIntent = intent.getParcelableExtra("pendingIntent");
        new Thread(new Runnable() {
            @Override
            public void run() {
                parse();
            }
        }).start();
        return Service.START_STICKY;
    }

    public void parse() {
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(protocol, host, -1, "/calendar/")
                    , 10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < doc.select("#main li").size(); i++) {
            Movie movie = parseMoviePage(doc.select("#main li > a").get(i).attr("href"));
            if (movie != null) {
                writeMovieToDb(movie);
                Log.d("log1", movie.getName());
            }
        }
        try {
            pendingIntent.send(MainActivity.STATUS_OK);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public Movie parseMoviePage(String movieLink) {
        Document doc = null;
        SQLiteDatabase db = MainActivity.dbHelper.getReadableDatabase();
        try {
            doc = Jsoup.parse(new URL(protocol, host, -1, movieLink)
                    , 10000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Movie movie = new Movie();
        Pattern pattern = Pattern.compile(".+?(\\d+).+");
        Matcher matcher = pattern.matcher(movieLink);
        matcher.matches();
        movie.setId(Integer.valueOf(matcher.group(1)));
        if (db.query("movies", null, "_id = ?", new String[] { Integer.toString(movie.getId()) }, null, null, null).moveToNext()) {
            return null;
        }
        movie.setName( doc.select("h1").get(0).text());
        movie.setDescription( doc.select(".summary_text").get(0).text());
        List<String> genreList = movie.getGenres();
        for(int i = 0;i < doc.select(".subtext .itemprop").size(); i++) {
            genreList.add(doc.select(".subtext .itemprop").get(i).text());
        }
        movie.setProducers( doc.select(".summary_text+ .credit_summary_item .itemprop").get(0).text());
        try {
            movie.setActors(doc.select(".credit_summary_item~ .credit_summary_item+ .credit_summary_item .itemprop").get(0).text());
        } catch (Exception _) {
            try {
                movie.setActors(doc.select(".credit_summary_item+ .credit_summary_item .itemprop").get(0).text());
            } catch (Exception _1) {
                movie.setActors("No known actors yet. Sorry :(");
            }

        }
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        try {
            String date = doc.select(".subtext a~ .ghost+ a").get(0).text();
            Pattern p = Pattern.compile("(\\d{1,2} \\w+? \\d{4}) .+");
            Matcher m = p.matcher(date);
            m.matches();
            movie.setPremierDate((df.parse(m.group(1)).getTime()));
        } catch (ParseException e) {
            return null;
        }
        catch (IllegalStateException e) {
            return null;
        }


        return movie;
    }

    private void writeMovieToDb (Movie movie) {
        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        writeGenresToDb(movie.getGenres(), db);
        ContentValues cv = new ContentValues();
        cv.put("_id", movie.getId());
        cv.put("name", movie.getName());
        cv.put("premier_date", movie.getPremierDate());
        cv.put("description", movie.getDescription());
        cv.put("actors", movie.getActors());
        cv.put("directors", movie.getProducers());
        db.insert("movies", null, cv);
        linkMoviesAndGenres(movie.getId(), movie.getGenres(), db);

    }

    private void writeGenresToDb(List<String> genres, SQLiteDatabase db) {
        for (int i = 0; i < genres.size(); i++) {
           Cursor c = db.query("genres", null, "name LIKE ?", new String[] {genres.get(i)}, null, null, null);
           if (!c.moveToFirst()) {
               ContentValues cv = new ContentValues();
               cv.put("name", genres.get(i));
               db.insert("genres", null, cv);
           }
        }
    }

    private void linkMoviesAndGenres(int movieId, List<String> genres, SQLiteDatabase db) {
        for (int i = 0; i < genres.size(); i++) {
            Cursor c = db.query("genres", null, "name LIKE ?", new String[] { genres.get(i) },
            null, null, null);
            c.moveToFirst();
            int genreId = c.getInt(c.getColumnIndex("_id"));
            ContentValues cv = new ContentValues();
            cv.put("movie_id", movieId);
            cv.put("genre_id", genreId);
            db.insert("movies_genres", null, cv);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
