/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rose on 2/2/16.
 * This fragment is used in the main activity and displays the list of movies which can be sorted in different ways by using
 * the navigation view in the activity
 */
public class MovieListFragment extends Fragment implements AsyncTaskCallback {
    boolean isTabletView; // Stores if the currently loaded view is for a tablet
    RecyclerView mMovieList;
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mMovieList = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (NetworkUtility.isInternetAvailable(getActivity())) {
            AsyncTasks.FetchMovieList task = new AsyncTasks.FetchMovieList(this);
            task.execute(NetworkUtility.getURLForPopularMovies());
        } else {
            TextView errorText = (TextView) getActivity().findViewById(R.id.error_text);
            errorText.setVisibility(View.VISIBLE);
        }
        isTabletView = getActivity().findViewById(R.id.movie_detail_fragment) != null;

        /* We get the screen orientation and if the orientation is landscape then we display 3 items in single row of list */
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            mMovieList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        else mMovieList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mMovieList.setAdapter(new MovieListRecyclerAdapter(new ArrayList<MovieItem>(), getActivity(), isTabletView));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Updates the recycler view's datalist as received from the async task
     */
    @Override
    public void setMovieDataList(ArrayList<MovieItem> dataList) {
        ((MovieListRecyclerAdapter) mMovieList.getAdapter()).setmDataList(dataList);
        mMovieList.getAdapter().notifyDataSetChanged();
    }

    /**
     * Display error text if internet not available or error received from async task
     */
    @Override
    public void showErrorText() {
        TextView errorText = (TextView) getActivity().findViewById(R.id.error_text);
        errorText.setVisibility(View.VISIBLE);
    }

    /**
     * Sets visibility of error text to gone
     */
    @Override
    public void hideErrorText() {
        TextView errorText = (TextView) getActivity().findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);
    }

    /**
     * Hides the progress bar from the screen
     */
    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Displays the progress bar to provide feedback of operation occurring in the background
     */
    @Override
    public void showProgressBar() {
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * These callback method is not called for this fragment so these may not be implemented
     */
    @Override
    public void setTrailerDataList(ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> dataList) {
    }

    @Override
    public void setReviewDataList(ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> dataList) {
    }
}
