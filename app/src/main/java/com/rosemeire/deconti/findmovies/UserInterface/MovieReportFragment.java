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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rosemeire.deconti.findmovies.Adapter.AdapterMovie;
import com.rosemeire.deconti.findmovies.Model.ModelMovie;
import com.rosemeire.deconti.findmovies.R;
import com.rosemeire.deconti.findmovies.Service.ServiceMovie;
import com.rosemeire.deconti.findmovies.Utilities.UtilitiesNetwork;
import com.rosemeire.deconti.findmovies.Utilities.UtilitiesSharedPreferences;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.rosemeire.deconti.findmovies.Service.ServiceMovie.KEY_RECEIVER;
import static com.rosemeire.deconti.findmovies.Service.ServiceMovie.KEY_RESULT_QUERY_EXTRA;

/* ********************************************************************************************** **
/* **** Generated movie report (General or Favorite report depending on user selected option)
/* ********************************************************************************************** */
@SuppressWarnings("WeakerAccess")
public class MovieReportFragment extends Fragment {

    private final String TAG = "*** FRAGMENT *** ";

    @SuppressWarnings("WeakerAccess")
    public static final String SELECTED_KEY = "selected_position";
    public static int SELECTED_POSITION;

    public static int LAST_FIRST_VISIBLE_POSITION;

    private AdapterMovie mAdapterMovie;

    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mRecyclerView;

    /* TODO RUBRIC POINT:
    /* Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
    /*
    /* ****************************************************************************************** */
    /* **** Android Life Cycle - onCreate: set layouts, fields, initial data load
    /* ****************************************************************************************** */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.movie_report_fragment, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getContext(), 2);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapterMovie = new AdapterMovie(getActivity(), new ArrayList<ModelMovie>());

        mRecyclerView.setAdapter(mAdapterMovie);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            SELECTED_POSITION = savedInstanceState.getInt(SELECTED_KEY);
            mRecyclerView.scrollToPosition(SELECTED_POSITION);
        }

        return rootView;
    }

    /* ****************************************************************************************** **
    /* **** Update movie report
    /* ****************************************************************************************** */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        updateMoviesTask();

        mRecyclerView.getLayoutManager().scrollToPosition(LAST_FIRST_VISIBLE_POSITION);

        super.onViewCreated(view, savedInstanceState);

    }

    /* ****************************************************************************************** **
    /* **** Set list position
    /* ****************************************************************************************** */
    public void setPosition(int mPosition) {

        this.SELECTED_POSITION = mPosition;

    }

    /* ****************************************************************************************** **
    /* **** Save data do
    /* ****************************************************************************************** */
    public AdapterMovie getMovieAdapter() {

        return mAdapterMovie;
    }

    /* ****************************************************************************************** **
    /* **** Save data to onSaveInstanceState
    /* ****************************************************************************************** */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (SELECTED_POSITION != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, SELECTED_POSITION);
        }

        super.onSaveInstanceState(outState);
    }

    /* ****************************************************************************************** **
    /* **** Update recyclerView with last visible position
    /* ****************************************************************************************** */
    public void onResume () {

        mRecyclerView.getLayoutManager().scrollToPosition(LAST_FIRST_VISIBLE_POSITION);

        super.onResume();
    }

    /* ****************************************************************************************** **
    /* **** Get last visible position
    /* ****************************************************************************************** */

    public void onPause () {

        LAST_FIRST_VISIBLE_POSITION = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

        super.onPause();
    }

    /* ****************************************************************************************** **
    /* **** Update recyclerView with movies do a background UI for a internet connection
    /* ****************************************************************************************** */
    public void updateMoviesTask() {

        MyReceiverResult receiverResult = new MyReceiverResult(new Handler());

        Intent intent = new Intent(getActivity(), ServiceMovie.class);

        intent.putExtra(ServiceMovie.PREFERENCES_ORDER_QUERY_EXTRA,
                UtilitiesSharedPreferences.getDefaultSharedPreferences(getActivity()));

        intent.putExtra(KEY_RECEIVER, receiverResult);

        (getActivity()).startService(intent);

    }

    /* ****************************************************************************************** **
    /* **** Create a new ResultReceive to receive results
    /* ****************************************************************************************** */
    @SuppressWarnings("unchecked")
    class MyReceiverResult extends ResultReceiver {

        @SuppressLint("RestrictedApi")
        public MyReceiverResult(Handler handler) {
            super(handler);

        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            mAdapterMovie.clear();

            ArrayList<ModelMovie> modelMovies = (ArrayList<ModelMovie>) resultData.getSerializable(KEY_RESULT_QUERY_EXTRA);

            for (ModelMovie m : (modelMovies))
                mAdapterMovie.add(m);

            if (((MovieReport) (getActivity())).isSecondScreen())
                ((MovieReport) getActivity()).onMovieChanged(mAdapterMovie.getFirstMovie());

            mRecyclerView.getLayoutManager().scrollToPosition(LAST_FIRST_VISIBLE_POSITION);

            setEmptyMessage(modelMovies);

        }

        // Get from shared preferences report classification option
        private void setEmptyMessage(ArrayList<ModelMovie> modelMovies) {

            if (UtilitiesSharedPreferences.getDefaultSharedPreferences(getContext()).
                    equals(getString(R.string.preference_classification_value_favorites)) && !modelMovies.isEmpty())
            {
                (getView()).findViewById(R.id.empty_view).setVisibility(GONE);

            } else if (UtilitiesSharedPreferences.getDefaultSharedPreferences(getContext()).equals(getString(R.string.preference_classification_value_favorites)))
            {
                TextView textView = (getView()).findViewById(R.id.empty_view);
                textView.setText(getString(R.string.msg_without_favorites));
                textView.setVisibility(View.VISIBLE);

            } else if (UtilitiesNetwork.isNetworkAvailable((getContext())) && (getView()).findViewById(R.id.empty_view) != null)
            {
                getView().findViewById(R.id.empty_view).setVisibility(GONE);

            } else {

                getView().findViewById(R.id.empty_view).setVisibility(View.INVISIBLE);
            }
        }
    }
}
