package com.example.vmatviichuk.moviecalendar;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieParserService extends Service {
//    Document doc = Jsoup.parse(new URL("https",
//            "www.kinonews.ru",
//            -1,
//            "/premiers_world/") , 10000);

    private PendingIntent pendingIntent;

    private final String protocol = "https";
    private final String host = "www.kinonews.ru";


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
        new Thread(new Runnable() {
            @Override
            public void run() {
                parse(10);
            }
        }).start();
        return Service.START_STICKY;
    }

    public void parse(int count, int pageSkip) {
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(protocol, host, -1, "/premiers_usa/")
                    , 10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < count; i++) {
            Movie movie = parseMoviePage(doc.select(".premier_date_mobil+ .titlefilm").get(i).attr("href"));
            Log.d("log1", movie.getName());
        }
    }

    public Movie parseMoviePage(String movieLink) {
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(protocol, host, -1, movieLink)
                    , 10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Movie movie = new Movie();
        movie.setName( doc.select(".film").get(0).text());
        movie.setDescription( doc.select(".game_main .textart15").get(0).text());
        List<String> genreList = movie.getGenres();
        for(String genre : doc.select(".textgray")
                .get(0)
                .text()
                .replace(", ", ",")
                .split(",")) {
            genreList.add(genre);
        }
        movie.setReleaseYear(Integer.valueOf(doc.select(".tab-film tr").get(1).child(1).child(0).text()));
        movie.setProducers( doc.select("tr:nth-child(7) td+ td").get(0).text());
        movie.setActors( doc.select("tr:nth-child(8) td+ td").get(0).text());
        movie.setOriginCountry( doc.select("tr:nth-child(9) td+ td").get(0).text());
        movie.setDuration(Integer.valueOf(doc.select("tr:nth-child(12) td+ td").get(0).text().replace(" мин.", "")));
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            movie.setPremierDate(( df.parse(doc.select("tr:nth-child(10) td+ td").get(0).text()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(doc.baseUri());
        movie.setId(Integer.valueOf(matcher.group(1)));
        return movie;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
