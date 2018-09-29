/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rohanx96.popularmovies.R;
import com.rohanx96.popularmovies.movieDetails.MovieDetailFragment;
import com.rohanx96.popularmovies.network.AsyncTaskCallback;
import com.rohanx96.popularmovies.network.NetworkUtility;

import static com.rohanx96.popularmovies.network.NetworkUtility.SORT_FAVOURITES;
import static com.rohanx96.popularmovies.network.NetworkUtility.fetchDataAndUpdateDataList;
import static com.rohanx96.popularmovies.network.NetworkUtility.getURLForPopularMovies;
import static com.rohanx96.popularmovies.network.NetworkUtility.getUrlForCurrentSort;
import static com.rohanx96.popularmovies.network.NetworkUtility.getUrlForLatestMovies;
import static com.rohanx96.popularmovies.network.NetworkUtility.getUrlForTopGrossingMovies;
import static com.rohanx96.popularmovies.network.NetworkUtility.getUrlForTopRatedMovies;

/**
 * This is the activity displayed on application startup. The activity hosts the MovieListFragment to display the list of movies.
 * The activity also implements a navigation view which provides options to users to select the sort order of movies
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    boolean isTabletView; // Stores if the currently loaded view is for a tablet
    String mCurrentSort; // Stores the current sort order displayed
    MovieListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.movies));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mCurrentSort = NetworkUtility.SORT_POPULAR; // Sort order is by popularity when activity is started
        isTabletView = findViewById(R.id.movie_detail_fragment) != null; // For tablets the detail fragment is not null
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragment = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list_fragment_container);
        // This refreshes the favourites list when returning to activity
        if (mCurrentSort.equals(SORT_FAVOURITES))
            fetchDataAndUpdateDataList(getUrlForCurrentSort(mCurrentSort),
                    this,
                    fragment);
    }

    /**
     * Refreshes the movies list based on the option clicked in the navigation drawer. Checks if the current sort order is same as
     * the selected sort order and if not then updates the movie list based on the sort order selected
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (NetworkUtility.isInternetAvailable(this)) {
            switch (item.getItemId()) {
                case R.id.nav_popular:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_POPULAR))
                        fetchDataAndUpdateDataList(getURLForPopularMovies(),
                                this, fragment);
                    mCurrentSort = NetworkUtility.SORT_POPULAR;
                    setTitle(getString(R.string.most_popular));
                    break;
                case R.id.nav_rating:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_RATING))
                        fetchDataAndUpdateDataList(getUrlForTopRatedMovies(),
                                this, fragment);
                    mCurrentSort = NetworkUtility.SORT_RATING;
                    setTitle(getString(R.string.top_rated));
                    break;
                case R.id.nav_latest:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_LATEST))
                        fetchDataAndUpdateDataList(getUrlForLatestMovies(),
                                this, fragment);
                    mCurrentSort = NetworkUtility.SORT_LATEST;
                    setTitle(getString(R.string.latest));
                    break;
                case R.id.nav_revenue:
                    if (!mCurrentSort.equals(NetworkUtility.SORT_REVENUE))
                        fetchDataAndUpdateDataList(getUrlForTopGrossingMovies(),
                                this, fragment);
                    mCurrentSort = NetworkUtility.SORT_REVENUE;
                    setTitle(getString(R.string.top_grossing));
                    break;
                case R.id.nav_favourites:
                    if (!mCurrentSort.equals(SORT_FAVOURITES))
                        fetchDataAndUpdateDataList(SORT_FAVOURITES,
                                this, fragment);
                    mCurrentSort = SORT_FAVOURITES;
                    setTitle(getString(R.string.favourites));
            }
        } else {
            fragment.showErrorText();
        }
        // Remove the detail fragment for previously selected movie for a tablet
        if (isTabletView) {
            MovieDetailFragment fragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_detail_fragment);
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        item.setChecked(true);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragment.setGridSpan(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragment.setGridSpan(2);
        }
    }

    public String getmCurrentSort() {
        return mCurrentSort;
    }
}
