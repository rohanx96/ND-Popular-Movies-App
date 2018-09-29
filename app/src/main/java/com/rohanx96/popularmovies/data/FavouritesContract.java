/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines Table names and columns for PopularMovies database. Also defines URI that the content provider can parse
 * Created by rose on 11/2/16.
 */
public class FavouritesContract {
    public static final String CONTENT_AUTHORITY = "com.rohanx96.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVOURITES = "favourites";

    /** This class defines columns and Uri for the favourites table */
    public static final class FavouritesEntry implements BaseColumns{
        // Defines the URI for Favourites Table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        /** Define the type of cursors that can be returned by various queries */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

        /* Column names for the Favourites table */
        public static final String TABLE_NAME = "Favourites";
        public static final String COL_NAME = "Name";
        public static final String COL_ID = "ID";
        public static final String COL_RATING = "Rating";
        public static final String COL_POPULARITY = "Popularity";
        public static final String COL_OVERVIEW = "Overview";
        public static final String COL_IMAGE = "ImageURL";
        public static final String COL_DATE = "Date";

        /** Gets movie id from an uri query for a particular ID */
        public static String getIDFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

        /** Builds Uri for newly inserted element in the database */
        public static Uri buildUriForId(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
