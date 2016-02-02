package com.rohanx96.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by rose on 2/2/16.
 * Provides several methods to help in network related tasks performed in the application such as generating , building and fetching urls
 */
public class NetworkUtility {
    public static final String MAIN_URL_DISCOVER = "http://api.themoviedb.org/3/discover/movie?";
    public static final String IMAGE_MAIN_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";
    public static final String SORT_KEY = "sort_by";
    public static final String SORT_POPULAR = "popularity.desc";
    public static final String SORT_RATING = "vote_average.desc";
    public static final String SORT_LATEST = "primary_release_date.desc";
    public static final String SORT_REVENUE = "revenue.desc";
    public static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY = "";

    public static String getURLForPopularMovies() {
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_POPULAR).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    public static String getUrlForTopRatedMovies(){
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_RATING)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    public static String getUrlForLatestMovies(){
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_LATEST)
                .appendQueryParameter(API_KEY_PARAM,API_KEY).build();
        return builtUri.toString();
    }

    public static String getUrlForTopGrossingMovies(){
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_REVENUE)
                .appendQueryParameter(API_KEY_PARAM,API_KEY).build();
        return builtUri.toString();
    }
    public static Uri generateUrlForImage(String path) throws MalformedURLException {
        Uri builtUri = Uri.parse(IMAGE_MAIN_URL).buildUpon().appendPath(IMAGE_SIZE).appendPath(path).build();
        //Log.i("imageUri",builtUri.toString());
        return builtUri;
    }

    public static class LoadURL extends AsyncTask<String,Void,ArrayList<MovieItem>>{
        AsyncTaskCallback callback;

        public LoadURL(AsyncTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected ArrayList<MovieItem> doInBackground(String... params) {
            try {
                String json = downloadUrl(params[0]);
                return parseJson(json);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> data) {
            callback.setDataList(data);
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            //int len = 5000;

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
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }
                Log.i("receivedString",buffer.toString());
                return buffer.toString();
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
            Log.i("receivedString",new String(buffer));
            return new String(buffer);
        }
    }

    public static ArrayList<MovieItem> parseJson(String jsonString){
        ArrayList<MovieItem> movieList = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(jsonString);
            JSONArray moviesArray = rootObject.getJSONArray("results");
            for(int i = 0;i<moviesArray.length();i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                MovieItem item = new MovieItem();
                item.setID(movieObject.getInt("id"));
                item.setImage(movieObject.getString("poster_path").substring(1));
                item.setName(movieObject.getString("title"));
                item.setOverview(movieObject.getString("overview"));
                item.setRating(movieObject.getDouble("vote_average"));
                item.setPopularity(movieObject.getDouble("popularity"));
                movieList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}