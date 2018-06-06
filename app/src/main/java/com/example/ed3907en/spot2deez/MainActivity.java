package com.example.ed3907en.spot2deez;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity implements LoadImageTask.Listener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private Uri url;

    SpotifyService srv;
    Retrofit retrofit;
    Retrofit retrofit2;

    private static final String CLIENT_ID = "d865062283bf4b128e7e17540f20dd20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        url = i.getData();

        TextView source = (TextView) findViewById(R.id.sourceLocation);
    //source.setText(url.toString());



        retrofit2 = new Retrofit.Builder()
                .baseUrl("https://accounts.spotify.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        //Log.d("toto",tr.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();

        String token = TokenPersister.getToken(this);

        if (token == null){
            //get a new token
        }else{
            grab();
        }
        final Context app = this;
        if( token == null){
            String key = "ZDg2NTA2MjI4M2JmNGIxMjhlN2UxNzU0MGYyMGRkMjA6NzA3NGZiNmNlZWUwNGZmMjk4MjMzNjZkY2MwM2U5NDg=";
            SpotifyAccountService sarv = retrofit2.create(SpotifyAccountService.class);
            Call<Token> calltoken = sarv.getToken("Basic " + key, "client_credentials");

            calltoken.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    Log.d("toto"," ok " + response.toString());
                    Log.d(TAG, "youpi "+ response.body().access_token);

                    TokenPersister.setToken(app, response.body().access_token,response.body().expries_in, TimeUnit.SECONDS );

                    grab();

                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Log.d("toto"," requete foirée");
                }
            });

        } else{
            grab();
        }




    }

    private OkHttpClient clientWithInterceptor(String token){
        Interceptor i = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                return null;
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newrequest = chain.request().newBuilder().addHeader("Authorization","Bearer "+ token).build();
                        return chain.proceed(newrequest);
                    }
                })
                .build();
        return client;
    }

    private void grab(){
         String token = TokenPersister.getToken(this);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client( client)
                .build();
        SpotifyService srv = retrofit.create(SpotifyService.class);

        Call<Track> call = srv.getTrack("1v6wfh5bUCnOttxRUpNST2");

        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                Log.d("toto"," ok " + response.body().name);
                updateActivity(response.body());
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.d("toto","foiré");

            }
        });
    }

    private void updateActivity(Track track){
        new LoadImageTask(this).execute(track.album.images.get(0).url);
        ((TextView) findViewById(R.id.namevalue)).setText(track.name);
        ((TextView) findViewById(R.id.artistvalue)).setText(track.artists.get(0).name);
      //  ((WebView) findViewById(R.id.cover)).loadUrl(track.album.images.get(0).url);

    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        ((ImageView) findViewById(R.id.cover)).setImageBitmap(bitmap);
    }
}
