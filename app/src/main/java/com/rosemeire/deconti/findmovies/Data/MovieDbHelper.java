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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.DATABASE_NAME;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.DATABASE_VERSION;

/* TODO RUBRIC POINT:
/* The titles and IDs of the user’s favorite movies are stored in a native SQLite database
/* and exposed via a ContentProvider
/*
/* ********************************************************************************************** **
/* **** Treat storage SQLite
/* ********************************************************************************************** */
@SuppressWarnings("WeakerAccess")
public class MovieDbHelper extends SQLiteOpenHelper {

    /* ****************************************************************************************** **
    /* **** Constructor
    /* ****************************************************************************************** */
    public MovieDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /* ****************************************************************************************** **
    /* **** Called when the tasks database is created for the first time
    /* ****************************************************************************************** */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = getSQLCreateMovieTable();
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    /* ****************************************************************************************** **
    /* **** Create table into database
    /* ****************************************************************************************** */
    private String getSQLCreateMovieTable() {

        return "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_PATH_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_PATH_POSTER + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL );";
    }

    /* ****************************************************************************************** **
    /* **** This method discards the old table of data and calls onCreate to recreate a new one
    /* **** his occurs when the version number for this database (DATABASE_VERSION) is incremented
    /* ****************************************************************************************** */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
