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
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.rosemeire.deconti.findmovies.Model.ModelTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_API_KEY;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_URL_DEFAULT;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbValueApiKey;

/* TODO RUBRIC POINT:
/* App requests for related videos for a selected movie via the /movie/{id}/videos endpoint
/* in a background thread and displays those details when the user selects a movie.
/*
/* ********************************************************************************************** **
/* **** Get Trailers from TheMovieDB by API query (Json Structure)
/* ********************************************************************************************** */
@SuppressWarnings("WeakerAccess")
public class ServiceTrailer extends IntentService {

    public static final String MOVIE_ID_QUERY_EXTRA = "movie_id";
    public static final String KEY_RESULT_TRAILERS = "result";
    public static final String KEY_RECEIVER = "receiver";

    ArrayList<ModelTrailer> mModelTrailers = new ArrayList<>();

    public ServiceTrailer() {

        super(ServiceTrailer.class.getSimpleName());

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(Intent intent) {

        String movie_id = intent.getStringExtra(MOVIE_ID_QUERY_EXTRA);

        mModelTrailers = getTrailers(movie_id);

        Bundle resultBundle = new Bundle();
        resultBundle.putSerializable(KEY_RESULT_TRAILERS, mModelTrailers);
        ResultReceiver resRec = intent.getParcelableExtra(KEY_RECEIVER);

        resRec.send(101, resultBundle);
    }

    ArrayList<ModelTrailer> getTrailers(String movie_id) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String resultJsonStr;

        try {

            String TRAILERS_MOVIE = "/movie/{movie_id}/videos";
            String strURL = THE_MOVIE_DB_URL_DEFAULT + TRAILERS_MOVIE.
                    replace("{movie_id}", movie_id + "") +
                    "?" + THE_MOVIE_DB_API_KEY + "=" + sTheMovieDbValueApiKey;

            URL url = new URL(strURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                // Nothing to do.
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

            return parseJsonTrailers(resultJsonStr);

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } catch (JSONException e) {
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

    private ArrayList<ModelTrailer> parseJsonTrailers(String json) throws JSONException {

        final String TRAILERS_RESULTS = "results";
        final String TRAILER_NAME = "name";
        final String TRAILER_SITE = "site";
        final String TRAILER_KEY = "key";

        ArrayList<ModelTrailer> modelTrailers = new ArrayList<>();

        JSONObject jsonData = new JSONObject(json);
        JSONArray results = jsonData.getJSONArray(TRAILERS_RESULTS);

        for (int i = 0; i < results.length(); i++) {

            ModelTrailer modelTrailer = new ModelTrailer();

            JSONObject jsonTrailer = results.getJSONObject(i);

            modelTrailer.setName(jsonTrailer.getString(TRAILER_NAME));
            modelTrailer.setSite(jsonTrailer.getString(TRAILER_SITE));
            modelTrailer.setKey(jsonTrailer.getString(TRAILER_KEY));

            modelTrailers.add(modelTrailer);
        }

        return modelTrailers;
    }
}
