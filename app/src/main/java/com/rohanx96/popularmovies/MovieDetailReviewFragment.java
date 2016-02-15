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

import java.util.ArrayList;

/**
 * Created by rose on 15/2/16.
 */
public class MovieDetailReviewFragment extends Fragment {
    RecyclerView reviewList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail_review,container,false);
        reviewList = (RecyclerView) rootView.findViewById(R.id.movie_detail_review_list);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reviewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<MovieReviewsRecyclerAdapter.ReviewItem> itemArrayList = new ArrayList<>();
        itemArrayList.add(new MovieReviewsRecyclerAdapter.ReviewItem("Rohan","Lorem ipsum asdwnsa\ndjsakdbwkbdnasdmahbsh shadjkvbawjhd"));
        itemArrayList.add(new MovieReviewsRecyclerAdapter.ReviewItem("Gaurav","Lorem ipsum asdwnsa\ndjsakdbwkbdnasdmahbsh shadjkvbawjhd"));
        reviewList.setAdapter(new MovieReviewsRecyclerAdapter(itemArrayList));
    }
}
