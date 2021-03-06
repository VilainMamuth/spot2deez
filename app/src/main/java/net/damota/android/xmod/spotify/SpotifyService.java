package net.damota.android.xmod.spotify;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SpotifyService {



    /**
     * Get Spotify catalog information for a single track identified by their unique Spotify ID.
     *
     * @param trackId The Spotify ID for the track.
     * @return Requested track information
     * @see <a href="https://developer.spotify.com/web-api/get-track/">Get a Track</a>
     */
    @GET("tracks/{id}")
    Single<SpotifyTrack> getTrack(@Path("id") String trackId);

    /**
     * Get Spotify catalog information for a single track identified by their unique Spotify ID.
     *
     * @param albumId The Spotify ID for the album.
     * @return Requested album information
     */
    @GET("albums/{id}")
    Single<SpotifyAlbum> getAlbum(@Path("id") String albumId);

    /**
     * Get Spotify catalog information for a single podcast episode identified by their unique Spotify ID.
     *
     * @param episodeId The Spotify ID for the episode.
     * @return Requested episode information
     */
    @GET("episodes/{id}")
    Single<SpotifyEpisode> getEpisode(@Path("id") String episodeId);


}
