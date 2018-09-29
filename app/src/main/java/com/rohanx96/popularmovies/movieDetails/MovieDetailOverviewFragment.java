/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieDetails;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohanx96.popularmovies.data.FavouritesContract;
import com.rohanx96.popularmovies.data.models.MovieItem;
import com.rohanx96.popularmovies.network.NetworkUtility;
import com.rohanx96.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rose on 3/2/16.
 * Fragment that displays the selected movie's details
 */
public class MovieDetailOverviewFragment extends Fragment {
    @BindView(R.id.movie_detail_name)
    TextView mName;
    @BindView(R.id.movie_detail_overview)
    TextView mOverview;
    @BindView(R.id.movie_detail_rating)
    TextView mRating;
    @BindView(R.id.movie_detail_popularity)
    TextView mPopularity;
    @BindView(R.id.movie_detail_image)
    ImageView mImage;
    @BindView(R.id.movie_detail_date)
    TextView mDate;
    @BindView(R.id.movie_detail_add_favorite)
    ImageButton mAddFavourite;
    private MovieItem mItem;
    private boolean isFavourite; // Stores if the movie is added to favourites

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail_overview, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        mItem = args.getParcelable("movie_item");
        // Movie details updated according to the arguments received
        mName.setText(mItem.getName());
        mRating.setText(String.format(getString(R.string.rating_format), mItem.getRating()));
        mPopularity.setText(String.format(getString(R.string.popularity_format), mItem.getPopularity()));
        mOverview.setText(mItem.getOverview());
        if (mItem.getOverview().isEmpty()) {
            mOverview.setText("Plot Unknown");
        }
        try {
            // error() sets the drawable when there is problem loading url or some error occurs. It also prevents null exceptions caused due to
            // errors. It will retry three times before setting the error image
            Picasso.get().load(NetworkUtility.generateUrlForImage(mItem.getImage())).placeholder(R.drawable.default_movie_poster)
                    .error(R.drawable.default_movie_poster).into(mImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(mItem.getDate());
            java.text.DateFormat localFormat = DateFormat.getDateFormat(getActivity());
            mDate.setText(String.format(getString(R.string.date_format), localFormat.format(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Uri query to find movie by ID
        Uri query = FavouritesContract.FavouritesEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(mItem.getID())).build();
        Cursor cursor = getContext().getContentResolver().query(query, new String[]{FavouritesContract.FavouritesEntry.COL_ID}, null, null
                , null);
        try {
            // if cursor count != 1 then no movie returned for the queried id and hence movie not a favourite
            if (cursor.moveToFirst() && cursor.getCount() == 1) {
                isFavourite = true;
                mAddFavourite.setImageResource(R.drawable.ic_heart_checked);
            } else
                isFavourite = false;
        } finally {
            cursor.close();
        }

    }

    @OnClick(R.id.movie_detail_add_favorite)
    /** This method adds movie to favourite or removes movie from favourite depending on whether the movie has been previously added*/
    public void addFavourite() {
        if (isFavourite) {
            /* Delete movie from favourites database */
            getContext().getContentResolver().delete(FavouritesContract.FavouritesEntry.CONTENT_URI,
                    FavouritesContract.FavouritesEntry.COL_ID + " = ?", new String[]{Integer.toString(mItem.getID())});
            isFavourite = false;
            mAddFavourite.setImageResource(R.drawable.ic_heart_unchecked);
        } else {
            /* Insert the movie item into the database */
            ContentResolver contentResolver = getContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavouritesContract.FavouritesEntry.COL_ID, mItem.getID());
            contentValues.put(FavouritesContract.FavouritesEntry.COL_IMAGE, mItem.getImage());
            contentValues.put(FavouritesContract.FavouritesEntry.COL_NAME, mItem.getName());
            contentValues.put(FavouritesContract.FavouritesEntry.COL_OVERVIEW, mItem.getOverview());
            contentValues.put(FavouritesContract.FavouritesEntry.COL_POPULARITY, mItem.getPopularity());
            contentValues.put(FavouritesContract.FavouritesEntry.COL_RATING, mItem.getRating());
            contentValues.put(FavouritesContract.FavouritesEntry.COL_DATE, mItem.getDate());
            contentResolver.insert(FavouritesContract.FavouritesEntry.CONTENT_URI, contentValues);
            isFavourite = true;
            mAddFavourite.setImageResource(R.drawable.ic_heart_checked);
        }
    }

    @OnClick(R.id.movie_detail_share)
    /** Creates a chooser to Share the trailer link. If no trailer is found displays a dialog */
    public void shareTrailer() {
        RecyclerView trailerList = (RecyclerView) getActivity().findViewById(R.id.movie_detail_trailer_list);
        // no trailers
        if (((MovieTrailersRecyclerAdapter) trailerList.getAdapter()).getmDataList().size() == 0) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setMessage(getString(R.string.no_trailers))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }
        //Share the link
        else {
            String id = ((MovieTrailersRecyclerAdapter) trailerList.getAdapter()).getmDataList().get(0).getVideoId();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + id);
            startActivity(Intent.createChooser(share, getString(R.string.share_link)));
        }
    }
}
