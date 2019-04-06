package net.damota.android.xmod;

import io.reactivex.Single;

public abstract class ProviderApi {

    public abstract Single<Track> getTrack(String trackId) throws Exception;

    public abstract Single<Episode> getEpisode(String episodeId) throws Exception;

    public abstract Single<Album> getAlbum(String albumId) throws Exception;

    public abstract boolean needToken();
}
