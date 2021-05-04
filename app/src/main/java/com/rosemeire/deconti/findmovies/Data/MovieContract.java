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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.CONTENT_AUTHORITY;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.URI_BASE_CONTENT;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.URI_PATH_MOVIE;
/* TODO RUBRIC POINT:
/* The titles and IDs of the user’s favorite movies are stored in a native SQLite database
/* and exposed via a ContentProvider
/*

/* ********************************************************************************************** **
/* **** Add content provider constants to the Contract
/* **** Provide to the customer how to have access to the movie data. Information provided:
/* **** 1) Content authority,
/* **** 2) Base content URI,
/* **** 3) Path(s) to the tasks directory
/* **** 4) Content URI for data in the MovieEntry class
/* ********************************************************************************************** */
public class MovieContract {

    /* ****************************************************************************************** **
    /* **** MovieEntry Inner class that defines the contents of the movie table
    /* ****************************************************************************************** */
    public static final class MovieEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = URI_BASE_CONTENT.buildUpon().appendPath(URI_PATH_MOVIE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + URI_PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + URI_PATH_MOVIE;

        // ModelMovie table and column names
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_PATH_POSTER = "path_poster";
        public static final String COLUMN_PATH_BACKDROP = "path_backdrop";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);

        }
    }
}
