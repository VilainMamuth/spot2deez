package net.damota.android.xmod;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import net.damota.android.xmod.deezer.DeezerApi;
import net.damota.android.xmod.room.AppDatabase;
import net.damota.android.xmod.room.Entry;
import net.damota.android.xmod.spotify.SpotifyApi;

public class MainActivity extends AppCompatActivity implements LoadImageTask.Listener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private PackageManager pm;
    private final static String PACKAGE_DEEZER = "deezer.android.app";
    private final static String PACKAGE_YOUTUBE_MUSIC = "com.google.android.apps.youtube.music";
    private final static String PACKAGE_AMAZON_MUSIC = "com.amazon.mp3";

    private static final String PROVIDER_DEEZER = "deezer";
    private final static String PROVIDER_SPOTIFY = "spotify";
    private final static String TYPE_TRACK = "track";
    private final static String TYPE_ALBUM = "album";
    private final static String TYPE_EPISODE = "episode"; //podcast episode
    private Uri url;

    private String sourceProviderName;
    private String sourceProviderType;
    private String sourceProviderId;
    private ProviderApi sourceApi;


    private TextView artistName;
    /**
     * Le nom peut être le titre d'une piste ou le nom d'un album
     */
    private TextView name;
    private TextView duration;
    private TextView release;


    private AdView mAdView;
    private final static String AD_BANNER_ID = "ca-app-pub-1432851353322917/6878353485";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_logo);
        setSupportActionBar(myToolbar);*/


        MobileAds.initialize(this, "ca-app-pub-1432851353322917~2955296637");
        mAdView = findViewById(R.id.lapub);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F359BC9CD18E28CA1BC71FCB07389250").build();
        mAdView.loadAd(adRequest);

        this.pm = getPackageManager();
        hideNotInstalledApps();

        Intent i = getIntent();

        url = i.getData();

        artistName = findViewById(R.id.artistvalue);
        name = findViewById(R.id.namevalue);
        duration = findViewById(R.id.timeValue);
        release = findViewById(R.id.dateValue);

        Log.d(TAG, "onCreate: url source " + url.toString());

        parseUrl(url);


        switch (sourceProviderName) {
            case PROVIDER_SPOTIFY:
                sourceApi = new SpotifyApi(this);
                break;
            case PROVIDER_DEEZER:
                sourceApi = new DeezerApi();
                break;
        }

/*
        if (sourceApi.needToken()) {
            //si on a un token valide en cache , alors on en demande pas un nouveau
            Token spotifyAccessToken = TokenPersister.getToken(this);
            if (spotifyAccessToken != null) {
                ((SpotifyApi) sourceApi).setAccessToken(spotifyAccessToken);
            }
        }
*/

        Entry e = null;
        switch (sourceProviderType) {
            case TYPE_TRACK:
                e = this.getTrack(sourceApi, sourceProviderId);
                break;
            case TYPE_ALBUM:
                e = this.getAlbum(sourceApi, sourceProviderId);
                break;
            case TYPE_EPISODE:
                e = this.getEpisode(sourceApi, sourceProviderId);
                break;
        }


/*
        if (sourceApi.needToken()) {
            TokenPersister.setToken(this, ((SpotifyApi) sourceApi).getAccessToken());
        }
*/
        if (e != null) {
            updateActivity(e);
            addToHistory(e);
        }

    }


    private Entry getTrack(ProviderApi from, String trackId) {
        Entry entry = null;
        try {
            Track tr = from.getTrack(trackId).blockingGet();
            return buildEntry(tr);
        } catch (Exception e) {
            Toast.makeText(this, "Pb pour récupérer les infos de cette track", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return entry;
    }

    private Entry getEpisode(ProviderApi from, String episodeId) {
        Entry entry = null;
        try {
            Episode ep = from.getEpisode(episodeId).blockingGet();
            return buildEntry(ep);
        } catch (Exception e) {
            Toast.makeText(this, "Pb pour récupérer les infos de cet episode", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return entry;
    }

    private Entry getAlbum(ProviderApi from, String albumId) {
        Entry entry = null;
        try {
            Album al = from.getAlbum(albumId).blockingGet();
            return buildEntry(al);
        } catch (Exception e) {
            //TODO: internationaliser le message
            Toast.makeText(this, "Pb pour récupérer les infos de cet album", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();

        }
        return entry;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Entry buildEntry(Track track) {
        Entry entry = new Entry();
        entry.setCoverUrl(track.getAlbum().getCoverUrl());
        entry.setTitle(track.getTitle());
        entry.setArtist(track.getArtistsNames());
        entry.setDuration(track.getDuration());
        entry.setReleaseYear("");
        return entry;
    }

    private Entry buildEntry(Episode episode) {
        Entry entry = new Entry();
        entry.setCoverUrl(episode.getCoverUrl());
        entry.setTitle(episode.getTitle());
        entry.setArtist("");
        entry.setDuration(episode.getDuration());
        entry.setReleaseYear("");
        return entry;
    }

    private Entry buildEntry(Album album) {
        Entry entry = new Entry();
        entry.setCoverUrl(album.getCoverUrl());
        entry.setTitle(album.getTitle());
        entry.setArtist(album.getArtistsNames());
        entry.setDuration("");
        entry.setReleaseYear(album.getReleaseYear());
        return entry;
    }

    private void updateActivity(Entry entry) {
        new LoadImageTask(this).execute(entry.getCoverUrl());
        name.setText(entry.getTitle());
        artistName.setText(entry.getArtist());
        duration.setText(entry.getDuration());
        release.setText(entry.getReleaseYear());
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        ((ImageView) findViewById(R.id.cover)).setImageBitmap(bitmap);
    }

    /**
     * Basic parsing, need refactoring
     *
     * @param uri
     */
    private void parseUrl(Uri uri) {
        switch (uri.getScheme()) {
            case "spotify":
                sourceProviderName = PROVIDER_SPOTIFY;
                break; // optional

            case "http":
            case "https":

                switch (uri.getHost()) {
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
            default: //
                Toast.makeText(this, "Url non conforme", Toast.LENGTH_SHORT).show();
        }
        hideSourceProvider(sourceProviderName);

        switch (sourceProviderName) {
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

    public void openApp(View view) {
        Log.d(TAG, "openApp: " + view.toString());
        Intent i = null;
        Uri backup = null;
        switch (view.getId()) {
            case R.id.buttonDeez:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("deezer://www.deezer.com/search/" + artistName.getText() + " " + name.getText()));
                // ne fonctionne pas toujours backup = Uri.parse("https://www.deezer.com/search/" + artistName.getText() + " " + name.getText());
                break;
            case R.id.buttonSpot:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:search:" + artistName.getText() + " " + name.getText()));
                break;
            case R.id.buttonYM:
                i = new Intent(Intent.ACTION_SEARCH);
                i.putExtra(SearchManager.QUERY, artistName.getText() + " " + name.getText())
                        .setPackage("com.google.android.apps.youtube.music");
                //i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://music.youtube.com/search?query=" + artistName.getText() + " " + name.getText()) );
                break;
            case R.id.buttonAmazon:
                i = new Intent("android.intent.action.MEDIA_SEARCH");
                i.putExtra(SearchManager.QUERY, artistName.getText() + " " + name.getText())
                        .putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artistName.getText())
                        .putExtra(MediaStore.EXTRA_MEDIA_TITLE, artistName.getText())
                        .setPackage("com.amazon.mp3");
                //i = new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://mp3/" + artistName.getText() + " " + name.getText()) );
                break;
            case R.id.buttonDDG:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://duckduckgo.com/?q=+" + artistName.getText() + " +" + name.getText()));
                // ne fonctionne pas toujours backup = Uri.parse("https://www.deezer.com/search/" + artistName.getText() + " " + name.getText());
                break;

        }

        if (i != null) {
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
                //this.finish();
            } else {
                if (backup != null) {
                    i = new Intent(Intent.ACTION_VIEW, backup);
                    startActivity(i);
                } else {
                    Toast.makeText(this, R.string.app_not_installed, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void hideButton(int id) {
        findViewById(id).setVisibility(View.GONE);
    }

    private void hideSourceProvider(String name) {
        switch (name) {
            case PROVIDER_SPOTIFY:
                hideButton(R.id.buttonSpot);
                break;
            case PROVIDER_DEEZER:
                hideButton(R.id.buttonDeez);
                break;
        }
    }

    private boolean isAppLaunchable(String packageName){
        // App is not launchable if not installed or disabled
        Log.d(TAG, "isAppLaunchable: checking " + packageName);
        try {
            int result = getPackageManager().getApplicationEnabledSetting(packageName);
            return !(result>0);
        }catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void hideNotInstalledApps() {
        if(!isAppLaunchable(PACKAGE_DEEZER)){ hideButton(R.id.buttonDeez);}
        if(!isAppLaunchable(PACKAGE_AMAZON_MUSIC)){ hideButton(R.id.buttonAmazon);}
        if(!isAppLaunchable(PACKAGE_YOUTUBE_MUSIC)){ hideButton(R.id.buttonYM);}
    }


    private void addToHistory(Entry e){
        AddToHistoryTask t = new AddToHistoryTask(this,e);
        t.execute();
    }

}
