package com.example.ed3907en.spot2deez;

import java.util.List;

public class Album {
    /**
     * The name of the album. In case of an album takedown, the value may be an empty string.
     */
    public String name;
    /**
     * The cover art for the album in various sizes, widest first.
     */
    public List<Image> images;

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", images=" + images +
                '}';
    }
}
