/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import java.util.ArrayList;

/**
 * Created by rose on 2/2/16.
 * Interface to be implemented by the calling activity/fragment when calling the async task defined in the AsyncTasks  class
 */
public interface AsyncTaskCallback {
    void setMovieDataList(ArrayList<MovieItem> dataList);
    void setReviewDataList(ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> dataList);
    void setTrailerDataList(ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> dataList);
    void showErrorText();
    void hideErrorText();
    void hideProgressBar();
    void showProgressBar();
}
