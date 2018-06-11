package com.example.ed3907en.spot2deez;

import io.reactivex.Observable;
import io.reactivex.Single;

public abstract class ProviderApi {

    public abstract Single<Track> getTrack(String trackId) throws Exception;
}
