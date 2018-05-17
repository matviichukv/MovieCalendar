package com.example.vmatviichuk.moviecalendar;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int MOVIE_PARSE_CODE = 1;

    public static final int STATUS_OK = 100;
    public static final int STATUS_FAIL = 202;

    private List<Movie> movies;

    public static DBHelper dbHelper;

    SimpleAdapter adapter;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        dbHelper = new DBHelper(MainActivity.this);

        parseMoviesToList();

        listView = findViewById(R.id.movieList);

        setListAdapter();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(MainActivity.this, DetailsActivity.class);
                in.putExtra("movie", movies.get(position));
                startActivity(in);
            }
        });

    }

    public void onClickRefresh(View v) {
        Intent serviceIntent = new Intent(this, MovieParserService.class);
        PendingIntent pIn = createPendingResult(MOVIE_PARSE_CODE, new Intent(), 0);
        serviceIntent.putExtra("pendingIntent", pIn);
        startService(serviceIntent);
    }

    void setListAdapter() {
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Movie movie : movies) {
            Map<String, Object> m = new HashMap<>();
            m.put("MovieName", movie.getName());
            m.put("Genres", TextUtils.join(", ", movie.getGenres()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            m.put("PremierDate", sdf.format(new Date(movie.getPremierDate())));
            data.add(m);
        }
        String[] from = {"MovieName", "Genres", "PremierDate"};
        int[] to = { R.id.movieNameTextView, R.id.genreTextView, R.id.premierTextView };

        adapter = new SimpleAdapter(this, data, R.layout.movie_list_item, from, to);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == STATUS_OK) {
            parseMoviesToList();
            setListAdapter();
        }
    }

    void parseMoviesToList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("movies", null, null, null, null, null, "premier_date");
        if (c.moveToFirst()) {
             do {
                int _idColumn = c.getColumnIndex("_id");
                int nameColumn = c.getColumnIndex("name");
                int descColumn = c.getColumnIndex("description");
                int premierColumn = c.getColumnIndex("premier_date");
                int actorsColumn = c.getColumnIndex("actors");
                int producersColumn = c.getColumnIndex("directors");

                Movie movie = new Movie();
                movie.setId(c.getInt(_idColumn));
                movie.setPremierDate(c.getLong(premierColumn));
                movie.setName(c.getString(nameColumn));
                movie.setDescription(c.getString(descColumn));
                movie.setActors(c.getString(actorsColumn));
                movie.setProducers(c.getString(producersColumn));

                List<String> genres = new ArrayList<>();
                Cursor cg = db.query("genres", null,
                        "_id IN (SELECT genre_id FROM movies_genres WHERE movie_id = ?)",
                        new String [] { Integer.toString(movie.getId()) },
                        null, null, null);
                if (cg.moveToFirst()) {
                    do {
                        int genreColumn = cg.getColumnIndex("name");
                        genres.add(cg.getString(genreColumn));
                    } while (cg.moveToNext());
                }
                movie.setGenres(genres);
                movies.add(movie);
            } while (c.moveToNext());
        }
        Log.d("log1", "movies parsed from db");
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }
}
