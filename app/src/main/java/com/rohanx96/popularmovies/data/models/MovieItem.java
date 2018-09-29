/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.data.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.rohanx96.popularmovies.data.FavouritesContract;

/**
 * Created by rose on 2/2/16.
 * This class defines the variables and methods for a movie item. The movie item holds the various details of a particular movie.
 * The class implements Parcelable so that a movie object can be passed easily via intents or in saving and restoring states on
 * configuration changes
 */
public class MovieItem implements Parcelable{
    private int mMovieID;
    private String mMovieName;
    private String mMovieOverview;
    private String mMovieImage;
    private double mMovieRating;
    private double mMoviePopularity;
    private String mMovieDate;

    public MovieItem(){}

    protected MovieItem(Parcel in) {
        mMovieID = in.readInt();
        mMovieName = in.readString();
        mMovieOverview = in.readString();
        mMovieImage = in.readString();
        mMovieRating = in.readDouble();
        mMoviePopularity = in.readDouble();
        mMovieDate = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    public int getID() {
        return mMovieID;
    }

    public void setID(int mMovieID) {
        this.mMovieID = mMovieID;
    }

    public String getName() {
        return mMovieName;
    }

    public void setName(String mMovieName) {
        this.mMovieName = mMovieName;
    }

    public String getOverview() {
        return mMovieOverview;
    }

    public void setOverview(String mMovieOverview) {
        this.mMovieOverview = mMovieOverview;
    }

    public String getImage() {
        return mMovieImage;
    }

    public void setImage(String mMovieImage) {
        this.mMovieImage = mMovieImage;
    }

    public double getRating() {
        return mMovieRating;
    }

    public void setRating(double mMovieRating) {
        this.mMovieRating = mMovieRating;
    }

    public double getPopularity() {
        return mMoviePopularity;
    }

    public void setPopularity(double mMoviePopularity) {
        this.mMoviePopularity = mMoviePopularity;
    }

    public String getDate() {
        return mMovieDate;
    }

    public void setDate(String mMovieDate) {
        this.mMovieDate = mMovieDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMovieID);
        dest.writeString(this.mMovieName);
        dest.writeString(this.mMovieOverview);
        dest.writeString(this.mMovieImage);
        dest.writeDouble(this.mMovieRating);
        dest.writeDouble(this.mMoviePopularity);
        dest.writeString(this.mMovieDate);
    }

    /** This method is used to create a movieItem object from a cursor returned from query to database */
    public static MovieItem createMovieItemFromDatabase(Cursor cursor){
        MovieItem item = new MovieItem();
        item.setID(cursor.getInt(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COL_ID)));
        item.setName(cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COL_NAME)));
        item.setDate(cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COL_DATE)));
        item.setPopularity(cursor.getDouble(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COL_POPULARITY)));
        item.setOverview(cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COL_OVERVIEW)));
        item.setImage(cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COL_IMAGE)));
        item.setRating(cursor.getDouble(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COL_RATING)));
        return item;
    }
}
