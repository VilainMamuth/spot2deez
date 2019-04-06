package net.damota.android.xmod.deezer;

import android.util.Log;

import net.damota.android.xmod.Album;
import net.damota.android.xmod.ProviderApi;
import net.damota.android.xmod.Track;
import net.damota.android.xmod.Episode;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class DeezerApi extends ProviderApi {

    private final static String TAG = DeezerApi.class.getSimpleName();

    private DeezerService service;

    private Response rep = null;

    public DeezerApi() {
        Log.d(TAG, "DeezerApi: ");

        RxJava2CallAdapterFactory rxCallAdapter = RxJava2CallAdapterFactory.create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
// Can be Level.BASIC, Level.HEADERS, or Level.BODY
// See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.deezer.com/")
                .addCallAdapterFactory(rxCallAdapter)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(DeezerService.class);
    }

    @Override
    public Single<Track> getTrack(String trackId) {
        Log.d(TAG, "getTrack: id " + trackId);

        return  service.getTrack(trackId)
                .subscribeOn(Schedulers.io())
                .cast(Track.class);
    }

    @Override
    public Single<Episode> getEpisode(String episodeId) {
        Log.d(TAG, "getEpisode: id " + episodeId);

        return  null;
    }

    @Override
    public Single<Album> getAlbum(String albumId) {
        Log.d(TAG, "getAlbum: ");
        return service.getAlbum(albumId)
                .subscribeOn(Schedulers.io())
                .cast(Album.class);
    }


    @Override
    public boolean needToken() {
        return false;
    }
}
