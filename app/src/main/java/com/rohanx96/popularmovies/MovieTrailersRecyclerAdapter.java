/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter implementation that manages trailers list in movie details
 * Created by rose on 15/2/16.
 */
public class MovieTrailersRecyclerAdapter extends RecyclerView.Adapter<MovieTrailersRecyclerAdapter.TrailerViewHolder> {
    ArrayList<TrailerItem> mDataList;
    Context context;

    public MovieTrailersRecyclerAdapter(ArrayList<TrailerItem> mDataList, Context context) {
        this.mDataList = mDataList;
        this.context = context;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemmovie_detail_trailer,parent,false));
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TrailerItem item = mDataList.get(position);
        Picasso.with(context).load(Uri.parse("http://img.youtube.com/vi/SUXWAEX2jlg/hqdefault.jpg")).into(holder.thumbnail);
        holder.trailerName.setText(item.getTrailerName());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.trailer_thumbnail)
        ImageView thumbnail;
        @Bind(R.id.trailer_name)
        TextView trailerName;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    /** This class defines the attributes for a trailer item */
    public static class TrailerItem {
        String videoId;
        String trailerName;

        public TrailerItem(String videoId, String trailerName) {
            this.videoId = videoId;
            this.trailerName = trailerName;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getTrailerName() {
            return trailerName;
        }
    }
}
