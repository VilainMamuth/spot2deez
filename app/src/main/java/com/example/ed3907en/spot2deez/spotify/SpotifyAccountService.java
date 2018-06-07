package com.example.ed3907en.spot2deez.spotify;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SpotifyAccountService {

    @FormUrlEncoded
    @POST("api/token")
    Call<Token> getToken(@Header("Authorization") String auth,  @Field("grant_type") String grantType);
}
