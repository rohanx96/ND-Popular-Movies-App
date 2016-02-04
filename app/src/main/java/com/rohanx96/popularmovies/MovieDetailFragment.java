/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rose on 3/2/16.
 * Fragment that displays the selected movie's details
 */
public class MovieDetailFragment extends Fragment {
    @Bind(R.id.movie_detail_name) TextView mName;
    @Bind(R.id.movie_detail_overview)TextView mOverview;
    @Bind(R.id.movie_detail_rating)TextView mRating;
    @Bind(R.id.movie_detail_popularity)TextView mPopularity;
    @Bind(R.id.movie_detail_image)ImageView mImage;
    @Bind(R.id.movie_detail_date)TextView mDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail,container,false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        MovieItem item = args.getParcelable("movie_item");
        // Movie details updated according to the arguments received
        mName.setText(item.getName());
        mRating.setText(String.format(getString(R.string.rating_format),item.getRating()));
        mPopularity.setText(String.format(getString(R.string.popularity_format), item.getPopularity()));
        mOverview.setText(item.getOverview());
        try {
            // error() sets the drawable when there is problem loading url or some error occurs. It also prevents null exceptions caused due to
            // errors. It will retry three times before setting the error image
            Picasso.with(getActivity()).load(NetworkUtility.generateUrlForImage(item.getImage())).placeholder(R.drawable.default_movie_poster)
                    .error(R.drawable.default_movie_poster).into(mImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(item.getDate());
            java.text.DateFormat localFormat = DateFormat.getDateFormat(getActivity());
            mDate.setText(String.format(getString(R.string.date_format),localFormat.format(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
