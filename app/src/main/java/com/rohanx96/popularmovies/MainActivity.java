/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * This is the activity displayed on application startup. The activity hosts the MovieListFragment to display the list of movies.
 * The activity also implements a navigation view which provides options to users to select the sort order of movies
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    String mCurrentSort; // Stores the current sort order displayed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.movies));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mCurrentSort = NetworkUtility.SORT_POPULAR; // Sort order is by popularity when activity is started
    }

    /**
     * Refreshes the movies list based on the option clicked in the navigation drawer. Checks if the current sort order is same as
     * the selected sort order and if not then updates the movie list based on the sort order selected
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(NetworkUtility.isInternetAvailable(this)) {
            switch (item.getItemId()) {
                case R.id.nav_popular:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_POPULAR))
                        updateDataList(NetworkUtility.getURLForPopularMovies());
                    mCurrentSort = NetworkUtility.SORT_POPULAR;
                    setTitle(getString(R.string.most_popular));
                    break;
                case R.id.nav_rating:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_RATING))
                        updateDataList(NetworkUtility.getUrlForTopRatedMovies());
                    mCurrentSort = NetworkUtility.SORT_RATING;
                    setTitle(getString(R.string.top_rated));
                    break;
                case R.id.nav_latest:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_LATEST))
                        updateDataList(NetworkUtility.getUrlForLatestMovies());
                    mCurrentSort = NetworkUtility.SORT_LATEST;
                    setTitle(getString(R.string.latest));
                    break;
                case R.id.nav_revenue:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_REVENUE))
                        updateDataList(NetworkUtility.getUrlForTopGrossingMovies());
                    mCurrentSort = NetworkUtility.SORT_REVENUE;
                    setTitle(getString(R.string.top_grossing));
                    break;
            }
        }
        else {
            TextView errorText = (TextView) findViewById(R.id.error_text);
            errorText.setVisibility(View.VISIBLE);
        }
        item.setChecked(true);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /** Refreshes data list when a new sort order is selected */
    public void updateDataList(String sortOrder){
        NetworkUtility.LoadURL task = new NetworkUtility.LoadURL
                ((MovieListFragment)getSupportFragmentManager().findFragmentById(R.id.movie_list_fragment_container));
        task.execute(sortOrder);
    }
}
