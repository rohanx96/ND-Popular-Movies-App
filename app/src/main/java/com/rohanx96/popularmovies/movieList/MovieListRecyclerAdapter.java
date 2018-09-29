/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieList;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohanx96.popularmovies.data.models.MovieItem;
import com.rohanx96.popularmovies.network.NetworkUtility;
import com.rohanx96.popularmovies.R;
import com.rohanx96.popularmovies.movieDetails.MovieDetailActivity;
import com.rohanx96.popularmovies.movieDetails.MovieDetailFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rose on 2/2/16.
 * Recycler View Adapter implementation which is used for populating movies list in MovieList Fragment
 */
public class MovieListRecyclerAdapter extends RecyclerView.Adapter {
    ArrayList<MovieItem> mDataList;
    boolean isTablet;
    FragmentActivity context;

    public MovieListRecyclerAdapter(ArrayList<MovieItem> mDataList, FragmentActivity context, boolean isTablet) {
        this.mDataList = mDataList;
        this.context = context;
        this.isTablet = isTablet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MovieItemViewHolder viewHolder = (MovieItemViewHolder) holder;
        MovieItem item = mDataList.get(position);
        try {
            // error() sets the drawable when there is problem loading url or some error occurs. It also prevents null exceptions caused due to
            // errors. It will retry three times before setting the error image
            Picasso.get().load(NetworkUtility.generateUrlForImage(item.getImage())).placeholder(R.drawable.default_movie_poster)
                    .error(R.drawable.default_movie_poster).into(viewHolder.movieImage, new Callback() {
                @Override
                public void onSuccess() {
                    Palette p = Palette.from(((BitmapDrawable) viewHolder.movieImage.getDrawable()).getBitmap()).generate();
                    Palette.Swatch color = p.getDominantSwatch();
                    if (color != null) {
                        viewHolder.movieName.setBackgroundColor(color.getRgb());
                        viewHolder.movieName.setTextColor(color.getTitleTextColor());
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        viewHolder.movieName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public ArrayList<MovieItem> getmDataList() {
        return mDataList;
    }

    public void setmDataList(ArrayList<MovieItem> mDataList) {
        this.mDataList = mDataList;
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_item_image)
        ImageView movieImage;
        @BindView(R.id.movie_item_name)
        TextView movieName;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.movie_item_card)
        /** Starts the detail activity. For tablets replaces the corresponding fragment */
        public void startDetailActivity(View v) {
            MovieItem item = mDataList.get(getAdapterPosition());
            if (isTablet) {
                Bundle args = new Bundle();
                // Put the parcelable object received from intent into the bundle
                args.putParcelable("movie_item", item);
                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(args);
                context.getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_fragment, fragment).commit();
            } else {
                Intent details = new Intent(context, MovieDetailActivity.class);
                // Pass the movie item object to the intent so they can be fetched in the new activity
                details.putExtra("movie_item", item);
                context.startActivity(details);
            }
        }
    }
}
