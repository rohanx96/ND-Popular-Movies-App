package com.rohanx96.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rose on 2/2/16.
 * Recycler View Adapter implementation to be used for populating movies list in MovieList Fragment
 */
public class MovieListRecyclerAdapter extends RecyclerView.Adapter {
    ArrayList<MovieItem> mDataList;

    public MovieListRecyclerAdapter(ArrayList<MovieItem> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieItemViewHolder viewHolder = (MovieItemViewHolder) holder;
        MovieItem item = mDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView movieImage;
        private TextView movieName;
        public MovieItemViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_item_image);
            movieName = (TextView) itemView.findViewById(R.id.movie_item_name);
        }
    }
}
