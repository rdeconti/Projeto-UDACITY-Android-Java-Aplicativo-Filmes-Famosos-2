package com.rosemeire.deconti.findmovies.Utilities;

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
import android.net.Uri;

public abstract class UtilitiesConstants {

    // Save application context
    public static Context sContextApplication;

    // TheMovieDb key (that it will be initialized with value from application build gradle file)
    public static String sTheMovieDbValueApiKey = "";

    // Titles to movie report (default options ara "by popularity" and "English")
    public static String sTheMovieDbValueSortBy = "most_popular";
    public static String sTheMovieDbTitleSortBy = "by popularity";

    // TheMovieDb definitions
    public static final String THE_MOVIE_DB_KEY_ARGUMENT = "MARG";
    public static final String THE_MOVIE_DB_URL_DEFAULT = "https://api.themoviedb.org/3";
    public static final String THE_MOVIE_DB_KEY_MOVIES_POPULAR = "/movie/popular";
    public static final String THE_MOVIE_DB_KEY_MOVIES_TOP_RATED = "/movie/top_rated";
    public static final String THE_MOVIE_DB_API_KEY = "api_key";
    public static final String THE_MOVIE_DB_KEY_URL_IMAGE_W500 = "https://image.tmdb.org/t/p/w500";
    public static final String THE_MOVIE_DB_KEY_URL_IMAGE_W342 = "http://image.tmdb.org/t/p/w342";
    public static final String THE_MOVIE_DB_KEY_URL_IMAGE_W780 = "http://image.tmdb.org/t/p/w780";

    // Contract definitions
    public static final String URI_PATH_MOVIE = "movie";
    public static final String CONTENT_AUTHORITY = "com.rosemeire.deconti.findmovies";
    public static final Uri URI_BASE_CONTENT = Uri.parse("content://" + CONTENT_AUTHORITY);

    // SQLite definitions
    public static final String DATABASE_NAME = "FindMovies.db";
    public static final int DATABASE_VERSION = 1;

    // Provider definitions
    public static final int PROVIDER_MOVIES = 100;
    public static final int PROVIDER_FAVORITE = 101;

    // Youtube address
    public static final String YOUTUBE_URL_PATH = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_URL_BASE_VND = "vnd.youtube://";
}
