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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rosemeire.deconti.findmovies.Model.ModelMovie;
import com.rosemeire.deconti.findmovies.R;
import com.rosemeire.deconti.findmovies.Utilities.UtilitiesNetwork;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_KEY_ARGUMENT;

/* ********************************************************************************************** **
/* **** Generate movie detail information
/* ********************************************************************************************** */
@SuppressWarnings("WeakerAccess")
public class MovieDetail extends AppCompatActivity {

    MovieDetailFragment movieDetailFragment;

    /* ****************************************************************************************** */
    /* **** Android Life Cycle - onCreate: set layouts, fields, initial data load
    /* ****************************************************************************************** */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);

        Intent intent = getIntent();

        ModelMovie modelMovie;

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            modelMovie = (ModelMovie) intent.getSerializableExtra(Intent.EXTRA_TEXT);

            Bundle arguments = new Bundle();
            arguments.putSerializable(THE_MOVIE_DB_KEY_ARGUMENT, modelMovie);

            movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(arguments);

            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_movie_container, movieDetailFragment)
                        .commit();
            }
        }
    }

    /* ****************************************************************************************** **
    /* **** Android life cycle - OnStart: update trailer and review data
    /* ****************************************************************************************** */
    @Override
    protected void onStart() {
        super.onStart();

        if (UtilitiesNetwork.isNetworkAvailable(this) && movieDetailFragment != null) {

            movieDetailFragment.updateTrailers();
            movieDetailFragment.updateReviews();

        }
    }
}
