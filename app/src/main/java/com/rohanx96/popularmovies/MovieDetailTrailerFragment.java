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
public class MovieDetailTrailerFragment extends Fragment {
    RecyclerView trailerList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail_trailer,container,false);
        trailerList = (RecyclerView) rootView.findViewById(R.id.movie_detail_trailer_list);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<MovieTrailersRecyclerAdapter.TrailerItem> trailerItems = new ArrayList<>();
        trailerItems.add(new MovieTrailersRecyclerAdapter.TrailerItem("sadasd","Trailer 2"));
        trailerList.setAdapter(new MovieTrailersRecyclerAdapter(trailerItems,getActivity()));
    }
}
