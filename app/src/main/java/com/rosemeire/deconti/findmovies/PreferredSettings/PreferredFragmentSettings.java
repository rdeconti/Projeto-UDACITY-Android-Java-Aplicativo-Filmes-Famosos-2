package com.rosemeire.deconti.findmovies.PreferredSettings;

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
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.rosemeire.deconti.findmovies.R;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.sContextApplication;

public class PreferredFragmentSettings extends PreferenceFragmentCompat implements
        OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    /* ****************************************************************************************** **
    /* **** Format screen with definitions done in XML file and shared preferences
    /* ****************************************************************************************** */
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.preferred_settings);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        PreferenceScreen prefScreen = getPreferenceScreen();

        int count = prefScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {

            Preference p = prefScreen.getPreference(i);

            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
    }

    /* ****************************************************************************************** **
    /* **** Figure out which preference was changed
    /* ****************************************************************************************** */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);

        if (null != preference) {

            // Updates the summary for the preference
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);

                // Update TheMovieDb parameters depending on user settings options
                new PreferredUpdatingSettings(sContextApplication, value);

            }
        }
    }

    /* ****************************************************************************************** **
    /* **** Updates the summary for the preference
    /* ****************************************************************************************** */
    private void setPreferenceSummary(Preference preference, String value) {

        // Update TheMovieDb parameters depending on user settings options
        Context context = getContext();
        new PreferredUpdatingSettings(context, value);

        if (preference instanceof ListPreference) {

            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;

            int prefIndex = listPreference.findIndexOfValue(value);

            if (prefIndex >= 0) {

                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);

            }
        } else if (preference instanceof EditTextPreference) {

            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);

            // Update TheMovieDb parameters depending on user settings options
            new PreferredUpdatingSettings(sContextApplication, value);

        }
    }

    /* ****************************************************************************************** **
    /* **** In this context, we're using the onPreferenceChange listener for checking whether the
    /* **** size setting was set to a valid value.
    /* ****************************************************************************************** */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}