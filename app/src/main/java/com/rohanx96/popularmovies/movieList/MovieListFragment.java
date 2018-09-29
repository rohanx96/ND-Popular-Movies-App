/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohanx96.popularmovies.network.AsyncTaskCallback;
import com.rohanx96.popularmovies.network.AsyncTasks;
import com.rohanx96.popularmovies.data.models.MovieItem;
import com.rohanx96.popularmovies.movieDetails.MovieReviewsRecyclerAdapter;
import com.rohanx96.popularmovies.movieDetails.MovieTrailersRecyclerAdapter;
import com.rohanx96.popularmovies.network.NetworkUtility;
import com.rohanx96.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rose on 2/2/16.
 * This fragment is used in the main activity and displays the list of movies which can be sorted in different ways by using
 * the navigation view in the activity
 */
public class MovieListFragment extends Fragment implements AsyncTaskCallback.FetchMovieTaskCallback {
    boolean isTabletView; // Stores if the currently loaded view is for a tablet

    @BindView(R.id.movie_list_recycler_view)
    RecyclerView mMovieList;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.error_text)
    TextView errorText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (NetworkUtility.isInternetAvailable(getActivity())) {
            NetworkUtility.fetchDataAndUpdateDataList(NetworkUtility.getUrlForCurrentSort(((MainActivity) getActivity()).getmCurrentSort()),
                    getActivity(), this);
        } else {
            TextView errorText = getActivity().findViewById(R.id.error_text);
            errorText.setVisibility(View.VISIBLE);
        }
        isTabletView = getActivity().findViewById(R.id.movie_detail_fragment) != null;

        /* We get the screen orientation and if the orientation is landscape then we display 3 items in single row of list */
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            gridLayoutManager.setSpanCount(3);
        mMovieList.setLayoutManager(gridLayoutManager);

        mMovieList.setAdapter(new MovieListRecyclerAdapter(new ArrayList<MovieItem>(), getActivity(), isTabletView));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetworkUtility.fetchDataAndUpdateDataList(NetworkUtility.getUrlForCurrentSort(((MainActivity) getActivity()).getmCurrentSort()),
                        getActivity(), MovieListFragment.this);
            }
        });
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
        setMovieDataList(new ArrayList<MovieItem>());
        mMovieList.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

    /**
     * Sets visibility of error text to gone
     */
    @Override
    public void hideErrorText() {
        mMovieList.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
    }

    /**
     * Hides the progress bar from the screen
     */
    @Override
    public void hideProgressBar() {
        mMovieList.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Displays the progress bar to provide feedback of operation occurring in the background
     */
    @Override
    public void showProgressBar() {
        mMovieList.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
    }

    public void setGridSpan(int span) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), span);
        mMovieList.setLayoutManager(gridLayoutManager);
        mMovieList.getAdapter().notifyDataSetChanged();
    }
}
