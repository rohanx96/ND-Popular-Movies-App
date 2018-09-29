/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.network;

import com.rohanx96.popularmovies.movieDetails.MovieReviewsRecyclerAdapter;
import com.rohanx96.popularmovies.movieDetails.MovieTrailersRecyclerAdapter;
import com.rohanx96.popularmovies.data.models.MovieItem;

import java.util.ArrayList;

/**
 * Created by rose on 2/2/16.
 * Interface to be implemented by the calling activity/fragment when calling the async task defined in the AsyncTasks  class
 */

public class AsyncTaskCallback {
    public interface BaseCallback {
        void showErrorText();

        void hideErrorText();

        void hideProgressBar();

        void showProgressBar();
    }


    public interface FetchMovieTaskCallback extends BaseCallback {
        void setMovieDataList(ArrayList<MovieItem> dataList);
    }

    public interface FetchReviewsTaskCallback extends BaseCallback {
        void setReviewDataList(ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> dataList);
    }

    public interface FetchTrailersTaskCallback extends BaseCallback {
        void setTrailerDataList(ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> dataList);
    }
}


