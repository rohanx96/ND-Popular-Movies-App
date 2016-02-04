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

/**
 * Created by rose on 3/2/16.
 * Fragment that displays the selected movie's details
 */
public class MovieDetailFragment extends Fragment {
    TextView mName;
    TextView mOverview;
    TextView mRating;
    TextView mPopularity;
    ImageView mImage;
    TextView mDate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail,container,false);
        mImage = (ImageView) rootView.findViewById(R.id.movie_detail_image);
        mName = (TextView) rootView.findViewById(R.id.movie_detail_name);
        mRating = (TextView) rootView.findViewById(R.id.movie_detail_rating);
        mPopularity = (TextView) rootView.findViewById(R.id.movie_detail_popularity);
        mOverview = (TextView) rootView.findViewById(R.id.movie_detail_overview);
        mDate = (TextView) rootView.findViewById(R.id.movie_detail_date);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        // Movie details updated according to the arguments received
        mName.setText(args.getString("name"));
        mRating.setText(String.format(getString(R.string.rating_format),args.getDouble("rating")));
        mPopularity.setText(String.format(getString(R.string.popularity_format), args.getDouble("pop")));
        mOverview.setText(args.getString("overview"));
        try {
            Picasso.with(getActivity()).load(NetworkUtility.generateUrlForImage(args.getString("image"))).into(mImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(args.getString("date"));
            java.text.DateFormat localFormat = DateFormat.getDateFormat(getActivity());
            mDate.setText(String.format(getString(R.string.date_format),localFormat.format(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
