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
import android.preference.PreferenceManager;

import com.rosemeire.deconti.findmovies.R;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sTheMovieDbValueSortBy;

/* ********************************************************************************************** **
/* **** Get from shared preferences report classification option
/* ********************************************************************************************** */
public class UtilitiesSharedPreferences {

    public static String getDefaultSharedPreferences(Context context) {

        android.content.SharedPreferences defaultSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);

        sTheMovieDbValueSortBy = defaultSharedPreference.
                getString(context.getString(R.string.preference_classification_key),
                        context.getString(R.string.preference_classification_value_popularity));

        return sTheMovieDbValueSortBy;
    }

}
