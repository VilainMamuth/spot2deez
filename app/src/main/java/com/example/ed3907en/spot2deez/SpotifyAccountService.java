package com.example.ed3907en.spot2deez;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpotifyAccountService {

    @FormUrlEncoded
    @POST("api/token")
    Call<Token> getToken(@Header("Authorization") String auth,  @Field("grant_type") String grantType);
}
