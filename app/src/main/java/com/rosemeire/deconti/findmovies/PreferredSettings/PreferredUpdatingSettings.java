package com.rosemeire.deconti.findmovies.PreferredSettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.rosemeire.deconti.findmovies.R;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbTitleSortBy;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbValueSortBy;


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

@SuppressLint("Registered")
public class PreferredUpdatingSettings extends AppCompatActivity {

    /* ****************************************************************************************** **
    /* **** format the title depending on the value argument / setting favorite movie
    /* ****************************************************************************************** */
    public PreferredUpdatingSettings(Context context, String value) {

        switch (value) {

            case "popular":
                sTheMovieDbTitleSortBy = context.getResources().getString(R.string.preference_classification_label_popularity);
                sTheMovieDbValueSortBy = value;
                break;

            case "top_rated":
                sTheMovieDbTitleSortBy = context.getResources().getString(R.string.preference_classification_label_rating);
                sTheMovieDbValueSortBy = value;
                break;

            case "favorites":
                sTheMovieDbTitleSortBy = context.getResources().getString(R.string.preference_classification_label_favorites);
                sTheMovieDbValueSortBy = value;
                break;
        }
    }
}
