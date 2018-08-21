package net.damota.android.xmod;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import net.damota.android.xmod.deezer.DeezerApi;
import net.damota.android.xmod.spotify.SpotifyApi;
import net.damota.android.xmod.spotify.Token;
import net.damota.android.xmod.spotify.TokenPersister;

public class MainActivity extends AppCompatActivity implements LoadImageTask.Listener  {
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final String PROVIDER_DEEZER = "deezer";
    private final static String PROVIDER_SPOTIFY = "spotify";
    private final static String TYPE_TRACK = "track";
    private final static String TYPE_ALBUM = "album";
    private Uri url;

    private String sourceProviderName;
    private String sourceProviderType;
    private String sourceProviderId;
    private ProviderApi sourceApi;


    private TextView artistName;
    /** Le nom peut être le titre d'une piste ou le nom d'un album */
    private TextView name;
    private TextView duration;
    private TextView release;



    private AdView mAdView;
    private final static String AD_BANNER_ID = "ca-app-pub-1432851353322917/6878353485";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_logo);
        setSupportActionBar(myToolbar);

        MobileAds.initialize(this, "ca-app-pub-1432851353322917~2955296637");
        mAdView = findViewById(R.id.lapub);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Intent i = getIntent();

        url = i.getData();

        artistName = findViewById(R.id.artistvalue);
        name = findViewById(R.id.namevalue);
        duration = findViewById(R.id.timeValue);
        release = findViewById(R.id.dateValue);

        Log.d(TAG, "onCreate: url source " + url.toString());

        parseUrl(url);


        switch (sourceProviderName){
            case PROVIDER_SPOTIFY:
                sourceApi = new SpotifyApi();
                break;
             case PROVIDER_DEEZER:
                sourceApi = new DeezerApi();
                break;
        }

        if (sourceApi.needToken()){
            //si on a un token valide en cache , alors on en demande pas un nouveau
            Token spotifyAccessToken = TokenPersister.getToken(this);
            if (spotifyAccessToken != null) {
                ((SpotifyApi) sourceApi).setAccessToken(spotifyAccessToken);
            }
        }


        switch (sourceProviderType){
            case TYPE_TRACK:
                this.getTrack(sourceApi, sourceProviderId);
                break;
            case TYPE_ALBUM:
                this.getAlbum(sourceApi, sourceProviderId);
                break;
        }


        if (sourceApi.needToken()) {
            TokenPersister.setToken(this, ((SpotifyApi) sourceApi).getAccessToken());
        }



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

    private void getAlbum(ProviderApi from, String albumId) {
        try {
            Album al = from.getAlbum(albumId).blockingGet();
            updateActivity(al);
        } catch (Exception e) {
            //TODO: internationaliser le message
            Toast.makeText(this, "Pb pour récupérer les infos de cet album", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateActivity(Track track) {
        new LoadImageTask(this).execute(track.getAlbum().getCoverUrl());
        name.setText(track.getTitle());
        artistName.setText(track.getArtistsNames());
        duration.setText(track.getDuration());
        release.setText("");
    }
    private void updateActivity(Album album) {
        new LoadImageTask(this).execute(album.getCoverUrl());
        name.setText(album.getTitle());
        artistName.setText(album.getArtistsNames());
        duration.setText("");
        release.setText(album.getReleaseYear());
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
                    case "www.deezer.com":
                        sourceProviderName = PROVIDER_DEEZER;
                        break;
                }

                break; // optional

            // You can have any number of case statements.
            default : //
                Toast.makeText(this, "Url non conforme", Toast.LENGTH_SHORT).show();
        }
        hideSourceProvider(sourceProviderName);

        switch (sourceProviderName){
            case PROVIDER_SPOTIFY:
                sourceProviderType = uri.getPathSegments().get(0);
                sourceProviderId = uri.getLastPathSegment();
                break;
            case PROVIDER_DEEZER:
                sourceProviderType = uri.getPathSegments().get(0);
                sourceProviderId = uri.getLastPathSegment();
                break;
        }

    }

    public void openApp(View view){
        Log.d(TAG, "openApp: " + view.toString());
        Intent i = null;
        switch (view.getId()){
            case R.id.buttonDeez:
                 i = new Intent (Intent.ACTION_VIEW, Uri.parse("deezer://www.deezer.com/search/" + artistName.getText() + " " + name.getText()) );
                break;
            case R.id.buttonSpot:
                 i = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:search:" + artistName.getText() + " " + name.getText()) );
                break;
            case R.id.buttonYM:
                 i = new Intent(Intent.ACTION_SEARCH );
                 i.putExtra(SearchManager.QUERY, artistName.getText() + " " + name.getText())
                 .setPackage("com.google.android.apps.youtube.music");
                //i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://music.youtube.com/search?query=" + artistName.getText() + " " + name.getText()) );
                break;
            case R.id.buttonAmazon:
                 i = new Intent("android.intent.action.MEDIA_SEARCH" );
                 i.putExtra(SearchManager.QUERY, artistName.getText() + " " + name.getText())
                         .putExtra(MediaStore.EXTRA_MEDIA_ARTIST,artistName.getText())
                         .putExtra(MediaStore.EXTRA_MEDIA_TITLE,artistName.getText())
                 .setPackage("com.amazon.mp3");
                //i = new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://mp3/" + artistName.getText() + " " + name.getText()) );
                break;


        }

        if (i != null) {
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
                //this.finish();
            }else{
                Toast.makeText(this, R.string.app_not_installed, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void hideButton(int id){
        findViewById(id).setVisibility(View.INVISIBLE);
    }

    private void hideSourceProvider(String name){
        switch (name){
            case PROVIDER_SPOTIFY:
                hideButton(R.id.buttonSpot);
                break;
            case PROVIDER_DEEZER:
                hideButton(R.id.buttonDeez);
                break;
        }
    }
}
