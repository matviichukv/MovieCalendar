package com.example.vmatviichuk.moviecalendar;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final int MOVIE_PARSE_CODE = 1;

    public static final int STATUS_OK = 100;
    public static final int STATUS_FAIL = 202;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //dbHelper = new DBHelper(this);
        Intent serviceIntent = new Intent(this, MovieParserService.class);
        PendingIntent pIn = createPendingResult(MOVIE_PARSE_CODE, new Intent(), 0);
        serviceIntent.putExtra("pendingIntent", pIn);
        startService(serviceIntent);

    }

    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == STATUS_OK) {
            // TODO: use parsed results
        }
    }
}
