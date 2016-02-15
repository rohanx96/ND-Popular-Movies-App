/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Pager Adapter that is responsible for switching between tabs in the movie detail fragment
 * Created by rose on 15/2/16.
 */
public class MovieDetailTabsAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{"Overview","Trailers","Reviews"};
    Bundle arguments;

    public MovieDetailTabsAdapter(FragmentManager fm, Bundle arguments) {
        super(fm);
        this.arguments = arguments;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MovieDetailOverviewFragment overviewFragment = new MovieDetailOverviewFragment();
                overviewFragment.setArguments(arguments);
                return overviewFragment;
            case 1:
                MovieDetailTrailerFragment trailersFragment = new MovieDetailTrailerFragment();
                trailersFragment.setArguments(arguments);
                return trailersFragment;
            case 2:
                return new MovieDetailReviewFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
