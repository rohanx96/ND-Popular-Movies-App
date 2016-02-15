/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to display the trailers of a particular movie
 * Created by rose on 15/2/16.
 */
public class MovieDetailTrailerFragment extends Fragment implements AsyncTaskCallback{
    @Bind(R.id.movie_detail_trailer_list)RecyclerView trailerList;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.error_text) TextView errorText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail_trailer,container,false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> trailerItems = new ArrayList<>();
        trailerItems.add(new MovieTrailersRecyclerAdapter.TrailerItem("sadasd","Trailer 2"));*/
        if(NetworkUtility.isInternetAvailable(getActivity())) {
            AsyncTasks.FetchTrailers task = new AsyncTasks.FetchTrailers(this);
            String id = Integer.toString(((MovieItem)getArguments().getParcelable("movie_item")).getID());
            task.execute(NetworkUtility.generateUriForVideos(id).toString());
        }
        else {
            errorText.setVisibility(View.VISIBLE);
        }
        trailerList.setAdapter(new MovieTrailersRecyclerAdapter(new ArrayList<MovieTrailersRecyclerAdapter.TrailerItem>(),getActivity()));
    }

    /** Updates the recycler view's datalist as received from the async task */
    @Override
    public void setTrailerDataList(ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> dataList) {
        ((MovieTrailersRecyclerAdapter) trailerList.getAdapter()).setmDataList(dataList);
        trailerList.getAdapter().notifyDataSetChanged();
        if (dataList.size() == 0){
            TextView noTrailers = (TextView) getActivity().findViewById(R.id.no_trailer_text);
            noTrailers.setVisibility(View.VISIBLE);
        }
    }

    /** Display error text if internet not available or error received from async task */
    @Override
    public void showErrorText() {
        errorText.setVisibility(View.VISIBLE);
    }

    /** Sets visibility of error text to gone */
    @Override
    public void hideErrorText() {
        errorText.setVisibility(View.GONE);
    }

    /** Hides the progress bar from the screen */
    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    /** Displays the progress bar to provide feedback of operation occurring in the background */
    @Override
    public void showProgressBar() {
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /** These callback method is not called for this fragment so these may not be implemented */
    @Override
    public void setMovieDataList(ArrayList<MovieItem> dataList) {}
    @Override
    public void setReviewDataList(ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> dataList) {}
}
