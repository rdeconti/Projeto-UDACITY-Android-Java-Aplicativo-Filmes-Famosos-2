package com.rosemeire.deconti.findmovies.Data;

/* *************************************************************************************************
/* UDACITY Android Developer NanoDegree Program
/* Created by Rosemeire Deconti on 01/10/2018
/* *************************************************************************************************
/* Copyright (C) 2016 The Android Open Source Project
/*
/* Licensed under the Apache License, Version 2.0 (the "License");
/* you may not use this file except in compliance with the License.
/* You may obtain a copy of the License at
/*
/*     http://www.apache.org/licenses/LICENSE-2.0
/*
/* Unless required by applicable law or agreed to in writing, software
/* distributed under the License is distributed on an "AS IS" BASIS,
/* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/* See the License for the specific language governing permissions and
/* limitations under the License.
/* ************************************************************************************************/

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.rosemeire.deconti.findmovies.R;

import java.util.Objects;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.CONTENT_AUTHORITY;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.PROVIDER_FAVORITE;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.PROVIDER_MOVIES;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.URI_PATH_MOVIE;

/* TODO RUBRIC POINT:
/* The titles and IDs of the user’s favorite movies are stored in a native SQLite database
/* and exposed via a ContentProvider
/*
/* ********************************************************************************************** **
/* **** Content provider manager access to a central repository of data
/* ********************************************************************************************** */
@SuppressWarnings("WeakerAccess")
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    /* ****************************************************************************************** **
    /* **** buildUriMatcher method that associates URI's with their int match by user
    /* **** Initialize a new matcher object without any matches
    /* **** then use .addURI(String authority, String path, int match) to add matches
    /* ****************************************************************************************** */
    static UriMatcher buildUriMatcher() {

        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        sURIMatcher.addURI(CONTENT_AUTHORITY, URI_PATH_MOVIE, PROVIDER_MOVIES);
        sURIMatcher.addURI(CONTENT_AUTHORITY, URI_PATH_MOVIE + "/*", PROVIDER_FAVORITE);

        // 3) Return the new matcher!
        return sURIMatcher;
    }

    /* ****************************************************************************************** **
    /* **** onCreate() to initialize and setup data source (DbHelper to gain access to it)
    /* ****************************************************************************************** */
    @Override
    public boolean onCreate() {

        mOpenHelper = new MovieDbHelper(getContext());
        return true;

    }

    /* ****************************************************************************************** **
    /* **** Implement query to handle requests for data by URI
    /* ****************************************************************************************** */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case PROVIDER_MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PROVIDER_FAVORITE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return retCursor;
    }

    /* ****************************************************************************************** **
    /* **** Get movies
    /* ****************************************************************************************** */
    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case PROVIDER_MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;

            case PROVIDER_FAVORITE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException(Objects.requireNonNull(getContext()).getString(R.string.msg_error_10) + uri);
        }
    }

    /* ****************************************************************************************** **
    /* **** Implement insert to handle requests to insert a single new row of data
    /* ****************************************************************************************** */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case PROVIDER_MOVIES: {

                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

                if (_id > 0)

                    returnUri = MovieContract.MovieEntry.buildUri(_id);

                else

                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        db.close();

        return returnUri;
    }

    /* ****************************************************************************************** **
    /* **** Implement delete to delete a single row of data
    /* ****************************************************************************************** */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";

        switch (match) {

            case PROVIDER_MOVIES:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0)

            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        db.close();
        return rowsDeleted;
    }

    /* ****************************************************************************************** **
    /* **** Implement update to delete a single row of data
    /* ****************************************************************************************** */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {

            case PROVIDER_MOVIES:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0)

            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        db.close();
        return rowsUpdated;
    }


    /* ****************************************************************************************** **
    /* **** Mass insertion of data
    /* ****************************************************************************************** */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case PROVIDER_MOVIES:

                db.beginTransaction();

                int returnCount = 0;

                try {

                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);

                        if (_id != -1) {
                            returnCount++;
                        }
                    }

                    db.setTransactionSuccessful();

                } finally {

                    db.endTransaction();

                }

                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
