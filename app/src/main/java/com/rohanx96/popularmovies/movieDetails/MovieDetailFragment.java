/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohanx96.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This fragment hosts the tabs for viewing various movie details like overview and reviews
 * Created by rose on 15/2/16.
 */
public class MovieDetailFragment extends Fragment {
    @BindView(R.id.movie_detail_tabs) TabLayout tabLayout;
    @BindView(R.id.movie_detail_view_pager) ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail,container,false);
        ButterKnife.bind(this,rootView);
        // The arguments received from intent are passed to the adapter so these arguments can be passed to respective tab fragments
        viewPager.setAdapter(new MovieDetailTabsAdapter(getChildFragmentManager(),getArguments()));
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }
}
