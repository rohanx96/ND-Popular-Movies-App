/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

    /** Generates URL string query for fetching movies sorted by popularity */
    public static String getURLForPopularMovies() {
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_POPULAR).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    /** Generates URL string query for fetching movies sorted by rating */
    public static String getUrlForTopRatedMovies(){
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_RATING)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    /** Generates URL string query for fetching movies sorted by release date */
    public static String getUrlForLatestMovies(){
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_LATEST)
                .appendQueryParameter(API_KEY_PARAM,API_KEY).build();
        return builtUri.toString();
    }

    /** Generates URL string query for fetching movies sorted by revenue */
    public static String getUrlForTopGrossingMovies(){
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY,SORT_REVENUE)
                .appendQueryParameter(API_KEY_PARAM,API_KEY).build();
        return builtUri.toString();
    }

    /** Generates URL string query for fetching movie poster according to file path received */
    public static Uri generateUrlForImage(String path) throws MalformedURLException {
        Uri builtUri = Uri.parse(IMAGE_MAIN_URL).buildUpon().appendPath(IMAGE_SIZE).appendPath(path).build();
        //Log.i("imageUri",builtUri.toString());
        return builtUri;
    }

    /** Checks if internet is avilable */
    public static boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * Async Task that fetches movie list according to sort order string received as parameter.
     * It uses AsyncTaskCallback to report the progress to the parent of the task
     */
    public static class LoadURL extends AsyncTask<String,Void,ArrayList<MovieItem>>{
        AsyncTaskCallback callback;

        public LoadURL(AsyncTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.showProgressBar();
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
            if(data!=null) {
                callback.setDataList(data);
                callback.hideErrorText();
            }
            else callback.showErrorText();
            callback.hideProgressBar();
        }

        /**
         * Given a URL, establishes an HttpUrlConnection and retrieves the web page content as a InputStream, which it returns as
         * a string.
         */
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                //if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                //Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                Log.i("receivedString",buffer.toString());
                return buffer.toString();
                // Makes sure that the InputStream is closed after the app is finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    /** Parses the JSON string received to generate an ArrayList of MovieItem objects */
    public static ArrayList<MovieItem> parseJson(String jsonString){
        ArrayList<MovieItem> movieList = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(jsonString);
            JSONArray moviesArray = rootObject.getJSONArray("results");
            for(int i = 0;i<moviesArray.length();i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                MovieItem item = new MovieItem();
                // The keys are set according to the default format provided by TheMovieDb API for discover query
                item.setID(movieObject.getInt("id"));
                item.setImage(movieObject.getString("poster_path").substring(1));
                item.setName(movieObject.getString("title"));
                item.setOverview(movieObject.getString("overview"));
                item.setRating(movieObject.getDouble("vote_average"));
                item.setPopularity(movieObject.getDouble("popularity"));
                item.setDate(movieObject.getString("release_date"));
                movieList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (movieList.isEmpty())
            return null;
        else return movieList;
    }
}