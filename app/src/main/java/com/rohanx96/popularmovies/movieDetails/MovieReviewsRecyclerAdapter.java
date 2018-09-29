/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.movieDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohanx96.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter that manages the items in recycler view for the reviews list in movie details
 * Created by rose on 15/2/16.
 */
public class MovieReviewsRecyclerAdapter extends RecyclerView.Adapter {
    ArrayList<ReviewItem> mDataList;

    public MovieReviewsRecyclerAdapter(ArrayList<ReviewItem> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_detail_review,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReviewItem item = mDataList.get(position);
        ReviewViewHolder viewHolder = (ReviewViewHolder) holder;
        viewHolder.userName.setText(item.getUserName());
        viewHolder.reviewText.setText(item.getReview());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_user_name)
        TextView userName;
        @BindView(R.id.review_detail) TextView reviewText;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    /** Defines the attributes of movie review item */
    public static class ReviewItem{
        String userName;
        String review;

        public ReviewItem(String userName, String review) {
            this.userName = userName;
            this.review = review;
        }

        public String getUserName() {
            return userName;
        }

        public String getReview() {
            return review;
        }
    }

    public void setmDataList(ArrayList<ReviewItem> mDataList) {
        this.mDataList = mDataList;
    }
}
