package com.example.ed3907en.spot2deez;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Uri url;

    SpotifyService srv;
    Retrofit retrofit;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        url = i.getData();

        TextView source = (TextView) findViewById(R.id.sourceLocation);
    source.setText(url.toString());

         retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();



        //Log.d("toto",tr.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();



        SpotifyService srv = retrofit.create(SpotifyService.class);

        Call<Track> call = srv.getTrack("1v6wfh5bUCnOttxRUpNST2");

        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                Log.d("toto"," ok " + response.toString());
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.d("toto","foiré");

            }
        });


    }

}
