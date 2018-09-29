/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rohanx96.popularmovies.data.FavouritesContract.FavouritesEntry;

/**
 * The database helper class to get readable or writable database for our application
 * Created by rose on 11/2/16.
 */
public class FavoritesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PopularMovies.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Create the favourites table */
        String query = "CREATE TABLE " + FavouritesEntry.TABLE_NAME + "(" + FavouritesEntry._ID + " INTEGER PRIMARY KEY, " +
                FavouritesEntry.COL_ID + " INTEGER UNIQUE, " + FavouritesEntry.COL_NAME + " TEXT, " + FavouritesEntry.COL_IMAGE + " TEXT, "
                + FavouritesEntry.COL_OVERVIEW + " TEXT, " + FavouritesEntry.COL_POPULARITY + " REAL, " +
                FavouritesEntry.COL_RATING + " REAL, " + FavouritesEntry.COL_DATE + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}