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

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoadImageTask.Listener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private Uri url;

    private ProviderApi sourceApi;

    private static final String CLIENT_ID = "d865062283bf4b128e7e17540f20dd20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        url = i.getData();

        TextView source = (TextView) findViewById(R.id.sourceLocation);
    //source.setText(url.toString());


        sourceApi = new SpotifyApi();
        //si on a un token valide en cache , alors on en demande pas un nouveau
        Token spotifyAccessToken = TokenPersister.getToken(this);
        if (spotifyAccessToken != null){
            ((SpotifyApi)sourceApi).setAccessToken(spotifyAccessToken);
        }

        Log.d(TAG, "onCreate: "+((SpotifyApi) sourceApi).getAccessToken());
        Track tr = null;
        try {
            tr = sourceApi.getTrack("1v6wfh5bUCnOttxRUpNST2");
        } catch (Exception e) {
            Toast.makeText(this, "Pb pour récupérer les infos de cette track", Toast.LENGTH_SHORT).show();
        }
        //Track tr = null;
        //        //Log.d("toto",tr.toString());
        if (tr != null){
            updateActivity(tr);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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
