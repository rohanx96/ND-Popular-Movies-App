/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieDetails;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohanx96.popularmovies.R;
import com.rohanx96.popularmovies.network.NetworkUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_detail_trailer,parent,false));
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TrailerItem item = mDataList.get(position);
        Picasso.get().load(NetworkUtility.generateUriForThumbnail(item.getVideoId())).into(holder.thumbnail);
        holder.trailerName.setText(item.getTrailerName());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_thumbnail)
        ImageView thumbnail;
        @BindView(R.id.trailer_name)
        TextView trailerName;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.trailer_card)
        /** This method opens the video in the youtube app. If app is not installed then it falls to opening the video link */
        public void playVideo(){
            String id = mDataList.get(getAdapterPosition()).getVideoId();
            try{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                context.startActivity(intent);
            }catch (ActivityNotFoundException ex){
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + id));
                context.startActivity(intent);
            }
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

    public ArrayList<TrailerItem> getmDataList() {
        return mDataList;
    }

    public void setmDataList(ArrayList<TrailerItem> mDataList) {
        this.mDataList = mDataList;
    }
}
