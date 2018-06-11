package com.example.ed3907en.spot2deez.spotify;

import android.util.Log;

import com.example.ed3907en.spot2deez.ProviderApi;
import com.example.ed3907en.spot2deez.Track;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
// Can be Level.BASIC, Level.HEADERS, or Level.BODY
// See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newrequest = chain.request().newBuilder().addHeader("Authorization","Bearer "+ accessToken.getAccess_token()).build();
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


        Retrofit retrofitAccount = new Retrofit.Builder()
                .baseUrl("https://accounts.spotify.com/")
                .addCallAdapterFactory(rxCallAdapter)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(new OkHttpClient.Builder().addNetworkInterceptor(httpLoggingInterceptor).build())
                .build();
        sas = retrofitAccount.create(SpotifyAccountService.class);

        accessToken = null;

    }

    private Single<Token> getValidAccessToken(){
        Log.d(TAG, "getValidAccessToken: ");
        if (accessToken == null || (accessToken!=null && !!accessToken.isValid()) ){
            return newAccessToken();
        } else {
            return Single.just(accessToken);
        }
    }

    @Override
    public Single<Track> getTrack(String trackId) {
        Log.d(TAG, "getTrack: ");

        return getValidAccessToken().flatMap(token -> { return service.getTrack(trackId);});
    }

    private Single<Token> newAccessToken(){
        Log.d(TAG, "newAccessToken: start");
        String key = "ZDg2NTA2MjI4M2JmNGIxMjhlN2UxNzU0MGYyMGRkMjA6NzA3NGZiNmNlZWUwNGZmMjk4MjMzNjZkY2MwM2U5NDg=";

        return sas.getToken("Basic " + key, "client_credentials")
                .subscribeOn(Schedulers.io())
                .doOnSuccess(this::setAccessToken);

    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }
}
