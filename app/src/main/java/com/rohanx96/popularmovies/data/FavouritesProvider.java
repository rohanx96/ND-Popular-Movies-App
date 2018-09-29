/*
 * Copyright (c) 2016. Rohan Agarwal (rOhanX96)
 */

package com.rohanx96.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Content provider for the application. This content provider provides access to the favourites table of popular movies database.
 * It supports two basic query uris along with insert and delete uris for favourites table
 * Created by rose on 14/2/16.
 */
public class FavouritesProvider extends ContentProvider {
    private FavoritesDatabaseHelper mDatabaseHelper;

    // The Uri matcher for the content provider
    private static UriMatcher sUriMatcher = buildUriMatcher();

    /* Uri matcher codes for various supported URIs */
    private static final int FAVOURITES = 100;
    private static final int FAVOURITES_ID = 101;

    @Override
    public boolean onCreate() {
        // Hold a reference to the databaseOpener
        mDatabaseHelper = new FavoritesDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case FAVOURITES:
                retCursor = mDatabaseHelper.getReadableDatabase().query(FavouritesContract.FavouritesEntry.TABLE_NAME,projection
                        ,selection,selectionArgs,null,null,sortOrder);
                break;
            case FAVOURITES_ID:
                String selectionClause = FavouritesContract.FavouritesEntry.COL_ID + "= ?";
                retCursor = mDatabaseHelper.getReadableDatabase().query(FavouritesContract.FavouritesEntry.TABLE_NAME,projection,
                        selectionClause,new String[]{FavouritesContract.FavouritesEntry.getIDFromUri(uri)},null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("unsupported Uri: " + uri);
        }
        // Notify updates in data to the cursor
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case FAVOURITES:
                return FavouritesContract.FavouritesEntry.CONTENT_TYPE;
            case FAVOURITES_ID:
                return FavouritesContract.FavouritesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unsupported uri; " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        Uri retUri = null;
        switch (sUriMatcher.match(uri)){
            case FAVOURITES:
                long id = database.insert(FavouritesContract.FavouritesEntry.TABLE_NAME,null,values);
                if (id > 0){
                    retUri =  FavouritesContract.FavouritesEntry.buildUriForId(id);
                }
                else Log.e("Insert Error "," Failed");
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)){
            case FAVOURITES:
                rowsDeleted = database.delete(FavouritesContract.FavouritesEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri");
        }
        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavouritesContract.CONTENT_AUTHORITY,FavouritesContract.PATH_FAVOURITES,FAVOURITES);
        matcher.addURI(FavouritesContract.CONTENT_AUTHORITY,FavouritesContract.PATH_FAVOURITES + "/*",FAVOURITES_ID);
        return matcher;
    }
}
