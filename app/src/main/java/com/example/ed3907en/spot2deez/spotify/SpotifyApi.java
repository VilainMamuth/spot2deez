package com.example.ed3907en.spot2deez.spotify;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.ed3907en.spot2deez.ProviderApi;
import com.example.ed3907en.spot2deez.Track;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class SpotifyApi extends ProviderApi {

    private final static String TAG = SpotifyApi.class.getSimpleName();

    private Token accessToken;

    private SpotifyAccountService sas;

    private SpotifyService service;

    private Response rep = null;

    public SpotifyApi() {
        Log.d(TAG, "SpotifyApi: new spotifyapi");

        RxJava2CallAdapterFactory rxCallAdapter = RxJava2CallAdapterFactory.create();

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
                .addCallAdapterFactory(rxCallAdapter)
                .addConverterFactory(MoshiConverterFactory.create())
                .client( client)
                .build();
        service = retrofit.create(SpotifyService.class);

        accessToken = null;

    }

    @Override
    public Track getTrack(String trackId) throws Exception {
         Track tr = null;

        if (accessToken == null){
            newAccessToken();
        }

        Observable<Track> call = service.getTrack(trackId);

        call.subenqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                Log.d("toto"," ok " + response.body().name);
                if (response.isSuccessful()){
                    rep = response;
                }

            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.d("toto","foiré");

            }
        });

        return (Track)rep.body();
    }

    public Token newAccessToken(){
        Log.d(TAG, "newAccessToken: start");
        String key = "ZDg2NTA2MjI4M2JmNGIxMjhlN2UxNzU0MGYyMGRkMjA6NzA3NGZiNmNlZWUwNGZmMjk4MjMzNjZkY2MwM2U5NDg=";

        Call<Token> calltoken = sas.getToken("Basic " + key, "client_credentials");

        calltoken.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d(TAG," ok " + response.body().toString());
                Log.d(TAG, "youpi "+ response.body().access_token);

                //TokenPersister.setToken(app, response.body().access_token,response.body().expries_in, TimeUnit.SECONDS );

                accessToken =  response.body();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG," requete foirée");
                accessToken = null;
            }
        });
        if (calltoken.isExecuted()){
            Log.d(TAG, "newAccessToken: executed");
        }else {
            Log.d(TAG, "newAccessToken: not executed");
        }
        Log.d(TAG, "newAccessToken: " + accessToken);
        return accessToken;
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }
}
