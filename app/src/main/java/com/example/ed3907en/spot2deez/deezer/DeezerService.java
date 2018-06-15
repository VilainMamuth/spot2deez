package com.example.ed3907en.spot2deez.deezer;

import com.example.ed3907en.spot2deez.Track;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeezerService {



    /**
     * Get Spotify catalog information for a single track identified by their unique Spotify ID.
     *
     * @param trackId The Spotify ID for the track.
     * @return Requested track information
     * @see <a href="https://developers.deezer.com/api/track">Get a Track</a>
     */
    @GET("track/{id}")
    Single<Track> getTrack(@Path("id") String trackId);

}
