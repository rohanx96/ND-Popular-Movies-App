/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rohanx96.popularmovies.FavouritesContract.FavouritesEntry;

/**
 * Created by rose on 11/2/16.
 */
public class FavoritesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PopularMovies.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public FavoritesDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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