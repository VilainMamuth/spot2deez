package com.example.ed3907en.spot2deez;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ed3907en.spot2deez.spotify.SpotifyAccountService;
import com.example.ed3907en.spot2deez.spotify.SpotifyApi;
import com.example.ed3907en.spot2deez.spotify.Token;
import com.example.ed3907en.spot2deez.spotify.TokenPersister;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoadImageTask.Listener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private Uri url;

    private String sourceProviderName;
    private String sourceProviderType;
    private String sourceProviderId;
    private ProviderApi sourceApi;

    private static final String CLIENT_ID = "d865062283bf4b128e7e17540f20dd20";

    private final static String PROVIDER_SPOTIFY = "spotify";
    private final static String TYPE_TRACK = "track";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        url = i.getData();

        TextView source = (TextView) findViewById(R.id.sourceLocation);

        Log.d(TAG, "onCreate: url source " + url.toString());

        parseUrl(url);


        switch (sourceProviderName){
            case PROVIDER_SPOTIFY:
                sourceApi = new SpotifyApi();
                break;
        }

        //si on a un token valide en cache , alors on en demande pas un nouveau
        Token spotifyAccessToken = TokenPersister.getToken(this);
        if (spotifyAccessToken != null) {
            ((SpotifyApi) sourceApi).setAccessToken(spotifyAccessToken);
        }

        switch (sourceProviderType){
            case TYPE_TRACK:
                this.getTrack(sourceApi, sourceProviderId);
                break;
        }



        TokenPersister.setToken(this, ((SpotifyApi) sourceApi).getAccessToken());

    }


    private void getTrack(ProviderApi from, String trackId) {
        try {
            Track tr = from.getTrack(trackId).blockingGet();
            updateActivity(tr);
        } catch (Exception e) {
            Toast.makeText(this, "Pb pour récupérer les infos de cette track", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateActivity(Track track) {
        new LoadImageTask(this).execute(track.album.images.get(0).url);
        ((TextView) findViewById(R.id.namevalue)).setText(track.name);
        ((TextView) findViewById(R.id.artistvalue)).setText(track.artists.get(0).name);
        //  ((WebView) findViewById(R.id.cover)).loadUrl(track.album.images.get(0).url);

    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        ((ImageView) findViewById(R.id.cover)).setImageBitmap(bitmap);
    }

    /**
     * Basic parsing, need refactoring
     * @param uri
     */
    private void parseUrl(Uri uri){
        switch(uri.getScheme()){
            case "spotify" :
                sourceProviderName = PROVIDER_SPOTIFY;
                break; // optional

            case "http" :
            case "https" :

                switch (uri.getHost()){
                    case "www.spotify.com":
                    case "open.spotify.com":
                        sourceProviderName = PROVIDER_SPOTIFY;
                        break;
                }

                break; // optional

            // You can have any number of case statements.
            default : //
                Toast.makeText(this, "Url non conforme", Toast.LENGTH_SHORT).show();
        }

        switch (sourceProviderName){
            case PROVIDER_SPOTIFY:
                sourceProviderType = uri.getPathSegments().get(0);
                sourceProviderId = uri.getLastPathSegment();
                break;
        }


    }

}
