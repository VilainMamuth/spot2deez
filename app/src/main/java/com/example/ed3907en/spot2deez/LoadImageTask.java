package com.example.ed3907en.spot2deez;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    private Listener mListener;
    public LoadImageTask(Listener listener) {

        mListener = listener;
    }
    public interface Listener{

        void onImageLoaded(Bitmap bitmap);
     //   void onError();
    }

    @Override
    protected Bitmap doInBackground(String... objects) {
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(objects[0]).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {

            mListener.onImageLoaded(bitmap);

        }
    }
}
