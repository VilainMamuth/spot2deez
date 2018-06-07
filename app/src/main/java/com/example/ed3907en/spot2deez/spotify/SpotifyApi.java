package com.example.ed3907en.spot2deez.spotify;

import android.content.Context;
import android.util.Log;

import com.example.ed3907en.spot2deez.ProviderApi;
import com.example.ed3907en.spot2deez.Track;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class SpotifyApi extends ProviderApi {

    private String accessToken;

    private SpotifyAccountService sas;

    private SpotifyService service;


    public SpotifyApi() {

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://accounts.spotify.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        sas = retrofit2.create(SpotifyAccountService.class);

        /*************************************************/
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newrequest = chain.request().newBuilder().addHeader("Authorization","Bearer "+ accessToken).build();
                        return chain.proceed(newrequest);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client( client)
                .build();
        SpotifyService srv = retrofit.create(SpotifyService.class);

        accessToken = TokenPersister.getToken();

    }

    private boolean hasValidAccessToken(){
        return
    }

    @Override
    public Track getTrack(String trackId) {

        String token = TokenPersister.getToken(this);

        if (token == null){
            //get a new token
        }else{
            grab();
        }
        final Context app = this;
        if( token == null){
            String key = "ZDg2NTA2MjI4M2JmNGIxMjhlN2UxNzU0MGYyMGRkMjA6NzA3NGZiNmNlZWUwNGZmMjk4MjMzNjZkY2MwM2U5NDg=";

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

    private void grab(){
        String token = TokenPersister.getToken(this);



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
}
