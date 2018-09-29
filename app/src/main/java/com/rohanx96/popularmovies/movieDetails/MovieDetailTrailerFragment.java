/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rohanx96.popularmovies.R;
import com.rohanx96.popularmovies.data.models.MovieItem;
import com.rohanx96.popularmovies.network.AsyncTaskCallback;
import com.rohanx96.popularmovies.network.AsyncTasks;
import com.rohanx96.popularmovies.network.NetworkUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment to display the trailers of a particular movie
 * Created by rose on 15/2/16.
 */
public class MovieDetailTrailerFragment extends Fragment implements AsyncTaskCallback.FetchTrailersTaskCallback {
    @BindView(R.id.movie_detail_trailer_list)
    RecyclerView trailerList;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.error_text)
    TextView errorText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail_trailer, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (NetworkUtility.isInternetAvailable(getActivity())) {
            AsyncTasks.FetchTrailers task = new AsyncTasks.FetchTrailers(this);
            String id = Integer.toString(((MovieItem) getArguments().getParcelable("movie_item")).getID());
            task.execute(NetworkUtility.generateUriForVideos(id).toString());
        } else {
            errorText.setVisibility(View.VISIBLE);
        }
        trailerList.setAdapter(new MovieTrailersRecyclerAdapter(new ArrayList<MovieTrailersRecyclerAdapter.TrailerItem>(), getActivity()));
    }

    /**
     * Updates the recycler view's datalist as received from the async task
     */
    @Override
    public void setTrailerDataList(ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> dataList) {
        ((MovieTrailersRecyclerAdapter) trailerList.getAdapter()).setmDataList(dataList);
        trailerList.getAdapter().notifyDataSetChanged();
        if (dataList.size() == 0) {
            TextView noTrailers = (TextView) getActivity().findViewById(R.id.no_trailer_text);
            noTrailers.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Display error text if internet not available or error received from async task
     */
    @Override
    public void showErrorText() {
        errorText.setVisibility(View.VISIBLE);
    }

    /**
     * Sets visibility of error text to gone
     */
    @Override
    public void hideErrorText() {
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
}
