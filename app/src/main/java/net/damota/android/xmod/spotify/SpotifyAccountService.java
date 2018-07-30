package net.damota.android.xmod.spotify;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SpotifyAccountService {

    @FormUrlEncoded
    @POST("api/token")
    Single<Token> getToken(@Header("Authorization") String auth, @Field("grant_type") String grantType);
}
