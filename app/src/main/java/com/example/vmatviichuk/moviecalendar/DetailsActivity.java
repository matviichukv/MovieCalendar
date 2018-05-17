package com.example.vmatviichuk.moviecalendar;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    Intent mainIntent;

    Movie movie;

    TextView movieNameView;
    TextView genresTextView;
    TextView premierDateView;
    TextView descriptionTextView;
    TextView directorsTextView;
    TextView actorsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mainIntent = getIntent();

        movie = mainIntent.getParcelableExtra("movie");

        movieNameView = findViewById(R.id.movieNameView);
        genresTextView = findViewById(R.id.genresTextView);
        premierDateView = findViewById(R.id.premierDateView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        directorsTextView = findViewById(R.id.directorsTextView);
        actorsTextView = findViewById(R.id.actorsTextView);

        movieNameView.setText(movie.getName());
        genresTextView.setText("Genres: " + TextUtils.join(", ", movie.getGenres()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        premierDateView.setText("Premier date: " + sdf.format(new Date(movie.getPremierDate())));
        descriptionTextView.setText(movie.getDescription());
        directorsTextView.setText("Directors: " + movie.getProducers());
        actorsTextView.setText("Actors: " + movie.getActors());

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
