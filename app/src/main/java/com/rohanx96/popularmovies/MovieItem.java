/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

/**
 * Created by rose on 2/2/16.
 * This class defines the variables and methods for a movie item. The movie item holds the various details of a particular movie.
 */
public class MovieItem {
    private int mMovieID;
    private String mMovieName;
    private String mMovieOverview;
    private String mMovieImage;
    private double mMovieRating;
    private double mMoviePopularity;
    private String mMovieDate;

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
}
