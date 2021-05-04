package com.rosemeire.deconti.findmovies.Service;

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

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.rosemeire.deconti.findmovies.Data.MovieContract;
import com.rosemeire.deconti.findmovies.Model.ModelMovie;
import com.rosemeire.deconti.findmovies.R;
import com.rosemeire.deconti.findmovies.Utilities.UtilitiesNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_API_KEY;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_KEY_MOVIES_POPULAR;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_KEY_MOVIES_TOP_RATED;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_URL_DEFAULT;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbValueApiKey;

/* TODO RUBRIC POINT:
/* In a background thread, app queries the /movie/popular or /movie/top_rated API
/* for the sort criteria specified in the settings menu.
/*
/* ********************************************************************************************** **
/* **** Get Movies from TheMovieDB by API query (Json Structure)
/* ********************************************************************************************** */
public class ServiceMovie extends IntentService {

    public static final String PREFERENCES_ORDER_QUERY_EXTRA = "pref_order";
    public static final String KEY_RESULT_QUERY_EXTRA = "result";
    public static final String KEY_RECEIVER = "receiver";

    private ArrayList<ModelMovie> mModelMovies = new ArrayList<>();

    public ServiceMovie() {
        super(ServiceMovie.class.getSimpleName());

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(Intent intent) {

        String preferencesOrder = intent.getStringExtra(PREFERENCES_ORDER_QUERY_EXTRA);

        if (!preferencesOrder.equals(getString(R.string.preference_classification_value_favorites)) && UtilitiesNetwork.isNetworkAvailable(this)) {

            mModelMovies = getMovies(preferencesOrder);

        } else {

            Cursor cursor = this.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                mModelMovies.add(new ModelMovie(cursor));
            }

        }

        Bundle resultBundle = new Bundle();
        resultBundle.putSerializable(KEY_RESULT_QUERY_EXTRA, mModelMovies);
        ResultReceiver resRec = intent.getParcelableExtra(KEY_RECEIVER);

        resRec.send(101, resultBundle);
    }

    /* TODO RUBRIC POINT:
    /* In a background thread, app queries the /movie/popular or /movie/top_rated API
    /* for the sort criteria specified in the settings menu.
    /*
    /* ****************************************************************************************** **
    /* **** Get movie list depending on classification argument
    /* ****************************************************************************************** */
    private ArrayList<ModelMovie> getMovies(String path_key) {

        String path;

        if (path_key.equals(getString(R.string.preference_classification_value_popularity)))

            path = THE_MOVIE_DB_KEY_MOVIES_POPULAR;

        else

            path = THE_MOVIE_DB_KEY_MOVIES_TOP_RATED;

            HttpURLConnection urlConnection = null;

            BufferedReader reader = null;

            String resultJsonStr;

            try {

                String strURL = THE_MOVIE_DB_URL_DEFAULT + path + "?" + THE_MOVIE_DB_API_KEY + "=" + sTheMovieDbValueApiKey;

                URL url = new URL(strURL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                resultJsonStr = buffer.toString();

                return parseJsonMovies(resultJsonStr);

            } catch (IOException e) {
                e.printStackTrace();
                return null;

            } catch (JSONException e) {
                e.printStackTrace();

            } catch (ParseException e) {
                e.printStackTrace();

            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {

                    try {
                        reader.close();

                    } catch (final IOException e) {
                        e.printStackTrace();

                    }
                }
            }

            return null;

    }

    private ArrayList<ModelMovie> parseJsonMovies(String json) throws JSONException, ParseException {

        final String MOVIE_RESULTS = "results";

        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_BACKDROP_PATH = "backdrop_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_VOTE_COUNT = "vote_count";
        final String MOVIE_RELEASEDATE = "release_date";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<ModelMovie> modelMovies = new ArrayList<>();

        JSONObject jsonData = new JSONObject(json);
        JSONArray results = jsonData.getJSONArray(MOVIE_RESULTS);

        for (int i = 0; i < results.length(); i++) {
            ModelMovie modelMovie = new ModelMovie();
            JSONObject jsonMovie = results.getJSONObject(i);

            modelMovie.setMovieId(jsonMovie.getLong(MOVIE_ID));
            modelMovie.setTitle(jsonMovie.getString(MOVIE_TITLE));
            modelMovie.setOverview(jsonMovie.getString(MOVIE_OVERVIEW));
            modelMovie.setRating((float) jsonMovie.getDouble(MOVIE_RATING));
            modelMovie.setVote_count(jsonMovie.getInt(MOVIE_VOTE_COUNT));
            modelMovie.setPath_poster(jsonMovie.getString(MOVIE_POSTER_PATH));
            modelMovie.setBackdrop_path(jsonMovie.getString(MOVIE_BACKDROP_PATH));
            modelMovie.setReleaseDate(sdf.parse(jsonMovie.getString(MOVIE_RELEASEDATE)));

            modelMovies.add(modelMovie);
        }

        return modelMovies;
    }
}
