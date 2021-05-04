package com.rosemeire.deconti.findmovies.Model;

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

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;

import com.rosemeire.deconti.findmovies.Data.MovieContract;

import java.io.Serializable;
import java.util.Date;

/* ********************************************************************************************** **
/* **** Java interface
/* ********************************************************************************************** */
public class ModelMovie implements Serializable {

    private long movie_id;
    private String title;
    private String path_poster;
    private String backdrop_path;
    private String overview;
    private float rating;
    private int vote_count;
    private Date releaseDate;
    private boolean favorite;


    public ModelMovie(Cursor cursor) {
        movie_id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
        title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
        overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
        vote_count = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT));
        rating = cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
        path_poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PATH_POSTER));
        backdrop_path = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PATH_BACKDROP));
        int julianDay = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
        releaseDate = new Date(julianDay);
        favorite = true;
    }

    public ModelMovie() {
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getMovieId() {
        return movie_id;
    }

    public void setMovieId(long id) {
        this.movie_id = id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPath_poster() {
        return path_poster;
    }

    public void setPath_poster(String path_poster) {
        this.path_poster = path_poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ContentValues getContentValues() {
        Time dayTime = new Time();
        dayTime.set(releaseDate.getTime());
        int julianDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, movie_id);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        values.put(MovieContract.MovieEntry.COLUMN_PATH_BACKDROP, backdrop_path);
        values.put(MovieContract.MovieEntry.COLUMN_PATH_POSTER, path_poster);
        values.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, vote_count);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, julianDay);
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);

        return values;
    }
}
