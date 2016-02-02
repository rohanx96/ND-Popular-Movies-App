package com.rohanx96.popularmovies;

import java.util.ArrayList;

/**
 * Created by rose on 2/2/16.
 */
public interface AsyncTaskCallback {
    void setString(String s);
    void setDataList(ArrayList<MovieItem> dataList);
}
