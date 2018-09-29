/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.network;

import android.os.AsyncTask;

import com.rohanx96.popularmovies.movieDetails.MovieReviewsRecyclerAdapter;
import com.rohanx96.popularmovies.movieDetails.MovieTrailersRecyclerAdapter;
import com.rohanx96.popularmovies.data.models.MovieItem;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class contains the various AsyncTasks for fetching movies, trailers and reviews
 * Created by rose on 16/2/16.
 */
public class AsyncTasks {
    /**
     * Async Task that fetches movie list according to sort order string received as parameter.
     * It uses AsyncTaskCallback to report the progress to the parent of the task
     */
    public static class FetchMovieList extends AsyncTask<String, Void, ArrayList<MovieItem>> {
        AsyncTaskCallback.FetchMovieTaskCallback callback;

        public FetchMovieList(AsyncTaskCallback.FetchMovieTaskCallback callback) {
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
                String json = NetworkUtility.downloadUrl(params[0]);
                return NetworkUtility.parseJson(json);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> data) {
            if (data != null) {
                callback.setMovieDataList(data);
                callback.hideErrorText();
            } else callback.showErrorText();
            callback.hideProgressBar();
        }
    }

    /**
     * Async Task that fetches trailers for given movie id
     * It uses AsyncTaskCallback to report the progress to the parent of the task
     */
    public static class FetchTrailers extends AsyncTask<String, Void, ArrayList<MovieTrailersRecyclerAdapter.TrailerItem>> {
        AsyncTaskCallback.FetchTrailersTaskCallback callback;

        public FetchTrailers(AsyncTaskCallback.FetchTrailersTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.showProgressBar();
        }

        @Override
        protected ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> doInBackground(String... params) {
            try {
                String json = NetworkUtility.downloadUrl(params[0]);
                return NetworkUtility.parseTrailersJson(json);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> data) {
            if (data != null) {
                callback.setTrailerDataList(data);
                callback.hideErrorText();
            } else callback.showErrorText();
            callback.hideProgressBar();
        }
    }

    /**
     * Async Task that fetches reviews for given movie id
     * It uses AsyncTaskCallback to report the progress to the parent of the task
     */
    public static class FetchReviews extends AsyncTask<String, Void, ArrayList<MovieReviewsRecyclerAdapter.ReviewItem>> {
        AsyncTaskCallback.FetchReviewsTaskCallback callback;

        public FetchReviews(AsyncTaskCallback.FetchReviewsTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.showProgressBar();
        }

        @Override
        protected ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> doInBackground(String... params) {
            try {
                String json = NetworkUtility.downloadUrl(params[0]);
                return NetworkUtility.parseReviewJson(json);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> data) {
            if (data != null) {
                callback.setReviewDataList(data);
                callback.hideErrorText();
            } else callback.showErrorText();
            callback.hideProgressBar();
        }
    }
}
