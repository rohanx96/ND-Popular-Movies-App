package com.rohanx96.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by rose on 2/2/16.
 */
public class MovieListFragment extends Fragment {
    RecyclerView mMovieList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list,container,false);
        mMovieList = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMovieList.setAdapter(new MovieListRecyclerAdapter(new ArrayList<MovieItem>()));
    }
}