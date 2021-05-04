package com.rosemeire.deconti.findmovies.UserInterface;

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

/* ************************************************************************************************/
/*  - Practice other structure and tools (differences from  project from Stage 1)
/*    --  Use of fragment
/*    --  Use of relative layout (instead of constraint layout)
/*    --  User different screen sizes
/*    --  Use Splash screen
/*
/*  - Udacity exercises
/*
/*  - GitHub projects
/*    -- https://github.com/eduardomargoto/PopularMovies
/*       -- check work with fragment
/*       -- relative layout
/*       -- Different screen sizes
/*
/*    -- https://github.com/jordiguzman/PopularMoviesStage2
/*       -- Splash screen
/*
/*  - StackOverflow
/*    -- https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
/*    -- https://stackoverflow.com/questions/27627659/android-actionbar-items-as-three-dots
/*    -- https://stackoverflow.com/questions/49040457/how-can-i-list-videos-in-android-app-using-youtube-api-in-android-studio
/*    -- https://stackoverflow.com/questions/9605913/how-do-i-parse-json-in-android
/*    -- https://stackoverflow.com/questions/2808796/what-is-an-android-pendingintent
/*
/*  - Other tips and tricks
/*    -- https://medium.com/@diprochowdhury/developing-a-movies-app-with-picasso-and-themoviedb-org-api-using-fragments-eb1bd19cf572
/*    -- https://www.tutorialspoint.com/android/android_json_parser.htm
/*    -- ...
/*
/* ************************************************************************************************/

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rosemeire.deconti.findmovies.BuildConfig;
import com.rosemeire.deconti.findmovies.Model.ModelMovie;
import com.rosemeire.deconti.findmovies.PreferredSettings.PreferredActivitySettings;
import com.rosemeire.deconti.findmovies.PreferredSettings.PreferredUpdatingSettings;
import com.rosemeire.deconti.findmovies.R;
import com.rosemeire.deconti.findmovies.Utilities.UtilitiesNetwork;
import com.rosemeire.deconti.findmovies.Utilities.UtilitiesSharedPreferences;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_KEY_ARGUMENT;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sContextApplication;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbTitleSortBy;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbValueApiKey;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbValueSortBy;

/* ********************************************************************************************** **
/* **** Generated movie report (General or Favorite report depending on user selected option)
/* ********************************************************************************************** */
public class MovieReport extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final String TAG = "*** REPORT *** ";

    private String mCriteriaOrder;

    private boolean mScreenControl;

    /* ****************************************************************************************** */
    /* **** Android Life Cycle - onCreate: set layouts, fields, initial data load
    /* ****************************************************************************************** */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sContextApplication = getApplicationContext();

        setContentView(R.layout.movie_report);

        // Check if TheMovieDb key was informed (not possible execute application without this KEY)
        //noinspection ConstantConditions
        if (BuildConfig.THE_MOVIE_DB_API_KEY_FROM_USER.isEmpty()) {
            sendToastMessage(R.string.msg_error_0);
            finish();
            return;
        }

        sTheMovieDbValueApiKey = BuildConfig.THE_MOVIE_DB_API_KEY_FROM_USER;

        // Check if network connectivity is ok (not possible execute application without network)
        if (!UtilitiesNetwork.isNetworkAvailable(sContextApplication)) {
            sendToastMessage(R.string.msg_application_network_connection_fail);
            finish();
            return;
        }

        setupSharedPreferences();

        mCriteriaOrder = UtilitiesSharedPreferences.getDefaultSharedPreferences(this);

        setActivityTitle();

        if (findViewById(R.id.detail_movie_container) != null) {

            mScreenControl = true;

            if (savedInstanceState == null) {

                Bundle arguments = new Bundle();

                MovieReportFragment movieReportFragment = (MovieReportFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie);

                assert movieReportFragment != null;
                if (movieReportFragment.getMovieAdapter().getFirstMovie() != null) {

                    arguments.putSerializable(THE_MOVIE_DB_KEY_ARGUMENT, movieReportFragment.getMovieAdapter().getFirstMovie());

                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

                    movieDetailFragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction().add(R.id.detail_movie_container, movieDetailFragment, THE_MOVIE_DB_KEY_ARGUMENT)
                            .commit();
                }
            }

        } else {

            mScreenControl = false;

        }

    }

    /* ****************************************************************************************** **
    /* **** setupSharedPreferences (classification parameters)
    /* ****************************************************************************************** */
    private void setupSharedPreferences() {

        SharedPreferences sharedPreferences =
                android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);

        // Update TheMovieDb parameters depending on parameters saved on Shared Preferences
        sTheMovieDbValueSortBy = sharedPreferences.
                getString(getString(R.string.preference_classification_key),
                        getString(R.string.preference_classification_label));

        new PreferredUpdatingSettings(sContextApplication, sTheMovieDbValueSortBy);

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    /* ****************************************************************************************** **
    /* **** Format message and send to user
    /* ****************************************************************************************** */
    private void sendToastMessage(int message) {

        Toast toast = Toast.makeText(sContextApplication, getString(message), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }

    /* ****************************************************************************************** **
    /* **** Format and set activity title
    /* ****************************************************************************************** */
    private void setActivityTitle() {

       if (sTheMovieDbValueSortBy.equals(sContextApplication.getString(R.string.preference_classification_value_popularity)))
       {
            sTheMovieDbTitleSortBy = sContextApplication.getResources().getString(R.string.preference_classification_label_popularity);
            setTitle(getString(R.string.app_name) + " " + sTheMovieDbTitleSortBy);
            return;
       }

        if (sTheMovieDbValueSortBy.equals(sContextApplication.getString(R.string.preference_classification_value_rating)))
        {
            sTheMovieDbTitleSortBy = sContextApplication.getResources().getString(R.string.preference_classification_label_rating);
            setTitle(getString(R.string.app_name) + " " + sTheMovieDbTitleSortBy);
            return;
        }

        if (sTheMovieDbValueSortBy.equals(sContextApplication.getString(R.string.preference_classification_value_favorites)))
        {
            sTheMovieDbTitleSortBy = sContextApplication.getResources().getString(R.string.preference_classification_label_favorites);
            setTitle(getString(R.string.app_name) + " " + sTheMovieDbTitleSortBy);
        }

    }

    /* TODO RUBRIC POINT:
    /* When a movie poster thumbnail is selected, the movie details screen is launched.
    /*
    /* ****************************************************************************************** **
    /* **** Start movie detail report
    /* ****************************************************************************************** */
    public void onMovieChanged(ModelMovie modelMovie) {

        if (modelMovie != null) {

            Bundle arguments = new Bundle();

            arguments.putSerializable(THE_MOVIE_DB_KEY_ARGUMENT, modelMovie);

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

            movieDetailFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.detail_movie_container, movieDetailFragment, THE_MOVIE_DB_KEY_ARGUMENT).commit();

        } else {

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.detail_movie_container, new MovieDetailFragment(), THE_MOVIE_DB_KEY_ARGUMENT).commit();

        }
    }

    /* ****************************************************************************************** **
    /* **** Check screen
    /* ****************************************************************************************** */
    public boolean isSecondScreen() {

        return mScreenControl;

    }

    /* ****************************************************************************************** **
    /* **** Android Life Cycle - OnResume: Activity restart
    /* ****************************************************************************************** */
    @Override
    protected void onResume() {
         super.onResume();

        formatMovieReport();

    }

    /* ****************************************************************************************** **
    /* **** Create menu options
    /* ****************************************************************************************** */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_movie_report_fragment, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /* TODO RUBRIC POINT:
    /* UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the
    /* movies by: most popular, highest rated.
    /*
    /* ****************************************************************************************** **
    /* **** Treat menu options
    /* ****************************************************************************************** */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_settings:
                Intent myIntent = new Intent(sContextApplication, PreferredActivitySettings.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sContextApplication.startActivity(myIntent);
                break;

        }

        setActivityTitle();

        return super.onOptionsItemSelected(item);
    }

    /* ****************************************************************************************** **
    /* **** Get all of the values from shared preferences to set up application
    /* ****************************************************************************************** */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    /* ****************************************************************************************** **
    /* **** Get data and format movie report
    /* ****************************************************************************************** */
    private void formatMovieReport() {

        String criteria = UtilitiesSharedPreferences.getDefaultSharedPreferences(this);

        setActivityTitle();

        if (!mCriteriaOrder.equals(criteria)) {

            MovieReportFragment movieReportFragment = (MovieReportFragment) getSupportFragmentManager().
                    findFragmentById(R.id.fragment_movie);

            if (movieReportFragment != null) {

                movieReportFragment.updateMoviesTask();

                if (mScreenControl)

                    onMovieChanged(movieReportFragment.getMovieAdapter().getFirstMovie());

            }
        }

        mCriteriaOrder = criteria;

    }
}
