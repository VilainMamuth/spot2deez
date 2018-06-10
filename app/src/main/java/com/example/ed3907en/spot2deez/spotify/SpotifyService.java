package com.example.ed3907en.spot2deez.spotify;

import com.example.ed3907en.spot2deez.Track;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpotifyService {

    @FormUrlEncoded
    @POST("https://accounts.spotify.com/api/token")
    Observable<Token> getToken(@Header("Authorization") String auth, @Field("grant_type") String grantType);

    /**
     * Get Spotify catalog information for a single track identified by their unique Spotify ID.
     *
     * @param trackId The Spotify ID for the track.
     * @return Requested track information
     * @see <a href="https://developer.spotify.com/web-api/get-track/">Get a Track</a>
     */
    @GET("tracks/{id}")
    Observable<Track> getTrack(@Path("id") String trackId);

}
