/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import java.util.ArrayList;

/**
 * Created by rose on 2/2/16.
 * Interface to be implemented by the calling activity/fragment when calling the LoadURL async task defined in the NetworkUtility class
 */
public interface AsyncTaskCallback {
    void setDataList(ArrayList<MovieItem> dataList);
    void showErrorText();
    void hideErrorText();
    void hideProgressBar();
    void showProgressBar();
}
