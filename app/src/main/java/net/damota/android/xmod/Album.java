package net.damota.android.xmod;

import java.util.List;

public interface Album {

    String getTitle();

    @Override
     String toString();

    String getCoverUrl();

    String getArtistsNames();

    String getGenresNames();
}
