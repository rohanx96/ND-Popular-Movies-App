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
           Picasso.with(context).load(NetworkUtility.generateUrlForImage(item.getImage())).into(viewHolder.movieImage);
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
        private ImageView movieImage;
        private TextView movieName;
        private CardView container;
        public MovieItemViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_item_image);
            movieName = (TextView) itemView.findViewById(R.id.movie_item_name);
            container = (CardView) itemView.findViewById(R.id.movie_item_card);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieItem item = mDataList.get(getAdapterPosition());
                    Intent details = new Intent(context,MovieDetailActivity.class);
                    // Pass the movie item details to the intent so they can be fetched in the new activity
                    Bundle args = new Bundle();
                    args.putString("name",item.getName());
                    args.putString("overview",item.getOverview());
                    args.putDouble("rating",item.getRating());
                    args.putDouble("pop",item.getPopularity());
                    args.putString("image",item.getImage());
                    args.putString("date",item.getDate());
                    details.putExtras(args);
                    context.startActivity(details);
                }
            });
        }
    }
}
