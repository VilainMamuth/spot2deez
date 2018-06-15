package com.example.ed3907en.spot2deez.deezer;

import android.util.Log;

import com.example.ed3907en.spot2deez.ProviderApi;
import com.example.ed3907en.spot2deez.Track;
import com.example.ed3907en.spot2deez.spotify.SpotifyAccountService;
import com.example.ed3907en.spot2deez.spotify.SpotifyService;
import com.example.ed3907en.spot2deez.spotify.Token;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class DeezerApi extends ProviderApi {

    private final static String TAG = DeezerApi.class.getSimpleName();

    private Token accessToken;

    private SpotifyAccountService sas;

    private DeezerService service;

    private Response rep = null;

    public DeezerApi() {
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
                .baseUrl("https://api.deezer.com/")
                .addCallAdapterFactory(rxCallAdapter)
                .addConverterFactory(MoshiConverterFactory.create())
                .client( client)
                .build();
        service = retrofit.create(DeezerService.class);


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
        Log.d(TAG, "getValidAccessToken: " + accessToken);
        if (accessToken == null || (accessToken!=null && !accessToken.isValid()) ){
            Log.d(TAG, "getValidAccessToken: Token non valide, get a new");
            return newAccessToken();
        } else {
            Log.d(TAG, "getValidAccessToken: token valide");
            return Single.just(accessToken);
        }
    }

    @Override
    public Single<Track> getTrack(String trackId) {
        Log.d(TAG, "getTrack: ");

        return getValidAccessToken()
                .observeOn(Schedulers.io())
                .flatMap(token -> { return service.getTrack(trackId);});
    }

    private Single<Token> newAccessToken(){
        Log.d(TAG, "newAccessToken: start");
        String key = "ZDg2NTA2MjI4M2JmNGIxMjhlN2UxNzU0MGYyMGRkMjA6NzA3NGZiNmNlZWUwNGZmMjk4MjMzNjZkY2MwM2U5NDg=";

        return sas.getToken("Basic " + key, "client_credentials")
                .subscribeOn(Schedulers.io())
                .doOnSuccess(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Exception {

                        long now = System.currentTimeMillis();
                        long expiresAt = now + TimeUnit.SECONDS.toMillis(token.getExpries_in());
                        Log.d(TAG, "newAccessToken: " + now + " : " + expiresAt);

                        token.setExpiresAt(expiresAt);
                        setAccessToken(token);
                    }
                }
        );

    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        Log.d(TAG, "setAccessToken: " + accessToken);
        this.accessToken = accessToken;
    }
}
