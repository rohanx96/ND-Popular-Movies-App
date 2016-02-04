/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rose on 2/2/16.
 * Recycler View Adapter implementation which is used for populating movies list in MovieList Fragment
 */
public class MovieListRecyclerAdapter extends RecyclerView.Adapter {
    ArrayList<MovieItem> mDataList;
    Context context;
    public MovieListRecyclerAdapter(ArrayList<MovieItem> mDataList, Context context) {
        this.mDataList = mDataList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieItemViewHolder viewHolder = (MovieItemViewHolder) holder;
        MovieItem item = mDataList.get(position);
        try {
            // error() sets the drawable when there is problem loading url or some error occurs. It also prevents null exceptions caused due to
            // errors. It will retry three times before setting the error image
           Picasso.with(context).load(NetworkUtility.generateUrlForImage(item.getImage())).placeholder(R.drawable.default_movie_poster)
                   .error(R.drawable.default_movie_poster).into(viewHolder.movieImage);
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
        @Bind(R.id.movie_item_image) ImageView movieImage;
        @Bind(R.id.movie_item_name) TextView movieName;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.movie_item_card)
        public void startDetailActivity(View v){
            MovieItem item = mDataList.get(getAdapterPosition());
            Intent details = new Intent(context,MovieDetailActivity.class);
            // Pass the movie item object to the intent so they can be fetched in the new activity
            details.putExtra("movie_item", item);
            context.startActivity(details);
        }
    }
}
