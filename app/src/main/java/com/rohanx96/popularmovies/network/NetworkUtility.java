/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.network;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.rohanx96.popularmovies.R;
import com.rohanx96.popularmovies.data.FavouritesContract;
import com.rohanx96.popularmovies.movieDetails.MovieReviewsRecyclerAdapter;
import com.rohanx96.popularmovies.movieDetails.MovieTrailersRecyclerAdapter;
import com.rohanx96.popularmovies.data.models.MovieItem;
import com.rohanx96.popularmovies.movieList.MovieListFragment;

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
    public static final String MAIN_URL_MOVIE = "http://api.themoviedb.org/3/movie";
    public static final String MAIN_URL_DISCOVER = "http://api.themoviedb.org/3/discover/movie?";
    public static final String IMAGE_MAIN_URL = "http://image.tmdb.org/t/p/";
    public static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/";
    public static final String IMAGE_SIZE = "w185";
    public static final String SORT_KEY = "sort_by";
    public static final String SORT_POPULAR = "popularity.desc";
    public static final String SORT_RATING = "vote_average.desc";
    public static final String SORT_LATEST = "primary_release_date.desc";
    public static final String SORT_REVENUE = "revenue.desc";
    public final static String SORT_FAVOURITES = "favourites";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_REVIEWS = "reviews";
    public static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY = "0d31f70134d991904eadf56c2f2237da";

    /**
     * Generates URL string query for fetching movies sorted by popularity
     */
    public static String getURLForPopularMovies() {
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY, SORT_POPULAR).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    /**
     * Generates URL string query for fetching movies sorted by rating
     */
    public static String getUrlForTopRatedMovies() {
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY, SORT_RATING)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    /**
     * Generates URL string query for fetching movies sorted by release date
     */
    public static String getUrlForLatestMovies() {
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY, SORT_LATEST)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    /**
     * Generates URL string query for fetching movies sorted by revenue
     */
    public static String getUrlForTopGrossingMovies() {
        Uri builtUri = Uri.parse(MAIN_URL_DISCOVER).buildUpon().appendQueryParameter(SORT_KEY, SORT_REVENUE)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return builtUri.toString();
    }

    /**
     * Generates URL string query for fetching movie poster according to file path received
     */
    public static Uri generateUrlForImage(String path) throws MalformedURLException {
        Uri builtUri = Uri.parse(IMAGE_MAIN_URL).buildUpon().appendPath(IMAGE_SIZE).appendPath(path).build();
        //Log.i("imageUri",builtUri.toString());
        return builtUri;
    }

    public static String getUrlForCurrentSort(String sortOrder) {
        switch (sortOrder) {
            case SORT_POPULAR:
                return getURLForPopularMovies();
            case SORT_LATEST:
                return getUrlForLatestMovies();
            case SORT_RATING:
                return getUrlForTopRatedMovies();
            case SORT_REVENUE:
                return getUrlForTopGrossingMovies();
            case SORT_FAVOURITES:
                return SORT_FAVOURITES;
            default:
                return getURLForPopularMovies();
        }
    }

    /**
     * Refreshes data list when a new sort order is selected
     */
    public static void fetchDataAndUpdateDataList(String sortOrder, Context context, AsyncTaskCallback.FetchMovieTaskCallback callback) {

        // If the user chooses to view favourites load the datalist from database accordingly
        if (sortOrder.equals(SORT_FAVOURITES)) {
            Cursor cursor = context.getContentResolver().query(FavouritesContract.FavouritesEntry.CONTENT_URI,
                    null, null, null, null);
            ArrayList<MovieItem> dataList = new ArrayList<>();
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            dataList.add(MovieItem.createMovieItemFromDatabase(cursor));
                        } while (cursor.moveToNext());
                    }
                } finally {
                    cursor.close();
                    callback.setMovieDataList(dataList);
                }
            }
        } else {
            AsyncTasks.FetchMovieList task = new AsyncTasks.FetchMovieList(callback);
            task.execute(sortOrder);
        }
    }

    /**
     * Generates uri for videos based on ID received for the movie
     */
    public static Uri generateUriForVideos(String key) {
        return Uri.parse(MAIN_URL_MOVIE).buildUpon().appendPath(key).appendPath(PATH_VIDEOS).appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
    }

    /**
     * Generates uri for reviews based on ID received for the movie
     */
    public static Uri generateUriForReviews(String key) {
        return Uri.parse(MAIN_URL_MOVIE).buildUpon().appendPath(key).appendPath(PATH_REVIEWS).appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
    }

    /**
     * Generates uri to get video thumbnail from video id
     */
    public static Uri generateUriForThumbnail(String key) {
        return Uri.parse(YOUTUBE_THUMBNAIL_URL).buildUpon().appendPath(key).appendPath("hqdefault.jpg").build();
    }

    /**
     * Checks if internet is available
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves the web page content as a InputStream, which it returns as
     * a string.
     */
    public static String downloadUrl(String myurl) throws IOException {
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
            Log.i("receivedString", buffer.toString());
            return buffer.toString();
            // Makes sure that the InputStream is closed after the app is finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Parses the JSON string received to generate an ArrayList of MovieItem objects
     */
    public static ArrayList<MovieItem> parseJson(String jsonString) {
        ArrayList<MovieItem> movieList = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(jsonString);
            JSONArray moviesArray = rootObject.getJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++) {
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

    /**
     * Parses the json string for reviews to generate an array list of review items
     */
    public static ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> parseReviewJson(String json) {
        ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> reviewItems = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray reviewArray = rootObject.getJSONArray("results");
            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject reviewItem = reviewArray.getJSONObject(i);
                MovieReviewsRecyclerAdapter.ReviewItem item = new MovieReviewsRecyclerAdapter.ReviewItem(
                        reviewItem.getString("author"), reviewItem.getString("content"));
                reviewItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewItems;
    }

    /**
     * Parses the json string for trailers to generate an array list of review items
     */
    public static ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> parseTrailersJson(String json) {
        ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> trailerItems = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray reviewArray = rootObject.getJSONArray("results");
            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject reviewItem = reviewArray.getJSONObject(i);
                MovieTrailersRecyclerAdapter.TrailerItem item = new MovieTrailersRecyclerAdapter.TrailerItem(
                        reviewItem.getString("key"), reviewItem.getString("name"));
                trailerItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerItems;
    }
}