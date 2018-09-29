/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rohanx96.popularmovies.data.models.MovieItem;
import com.rohanx96.popularmovies.R;

/**
 * Created by rose on 3/2/16.
 * Activity that host the Movie Detail Fragment to display the details of the selected movie. This activity is only displayed in phones
 */
public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(((MovieItem) getIntent().getParcelableExtra("movie_item")).getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Parent activity set as Main Activity in Manifest
        }
        Bundle args = new Bundle();
        // Put the parcelable object received from intent into the bundle
        args.putParcelable("movie_item", getIntent().getParcelableExtra("movie_item"));
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_fragment, fragment).commit();
    }
}

