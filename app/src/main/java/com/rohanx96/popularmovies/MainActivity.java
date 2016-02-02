package com.rohanx96.popularmovies;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    String mCurrentSort;
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
        mCurrentSort = NetworkUtility.SORT_POPULAR;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_popular:
                if(!mCurrentSort.equals(NetworkUtility.SORT_POPULAR))
                    updateDataList(NetworkUtility.getURLForPopularMovies());
                mCurrentSort = NetworkUtility.SORT_POPULAR;
                break;
            case R.id.nav_rating:
                if (!mCurrentSort.equals(NetworkUtility.SORT_RATING))
                    updateDataList(NetworkUtility.getUrlForTopRatedMovies());
                mCurrentSort = NetworkUtility.SORT_RATING;
                break;
            case R.id.nav_latest:
                if (!mCurrentSort.equals(NetworkUtility.SORT_LATEST))
                    updateDataList(NetworkUtility.getUrlForLatestMovies());
                mCurrentSort = NetworkUtility.SORT_LATEST;
                break;
            case R.id.nav_revenue:
                if (!mCurrentSort.equals(NetworkUtility.SORT_REVENUE))
                    updateDataList(NetworkUtility.getUrlForTopGrossingMovies());
                mCurrentSort = NetworkUtility.SORT_REVENUE;
                break;
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateDataList(String sortOrder){
        NetworkUtility.LoadURL task = new NetworkUtility.LoadURL
                ((MovieListFragment)getSupportFragmentManager().findFragmentById(R.id.movie_list_fragment_container));
        task.execute(sortOrder);
    }
}
