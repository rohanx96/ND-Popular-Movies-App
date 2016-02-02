package com.rohanx96.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rose on 2/2/16.
 * Provides several methods to help in network related tasks performed in the application such as generating , building and fetching urls
 */
public class NetworkUtility {
    public static final String MAIN_URL = "http://api.themoviedb.org/3";
    public static final String IMAGE_MAIN_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";
    public static final String DISCOVER_POPULAR = "/discover/movie?sort_by=popularity.desc";

    public static URL generateUrlForImage(String path) throws MalformedURLException {
        Uri builtUri = Uri.parse(IMAGE_MAIN_URL).buildUpon().appendPath(IMAGE_SIZE).appendPath(path).build();
        return new URL(builtUri.toString());
    }

    public static class LoadURL extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                return downloadUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                //Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}