package com.rosemeire.deconti.findmovies.Adapter;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rosemeire.deconti.findmovies.Model.ModelMovie;
import com.rosemeire.deconti.findmovies.R;
import com.rosemeire.deconti.findmovies.UserInterface.MovieDetail;
import com.rosemeire.deconti.findmovies.UserInterface.MovieReport;
import com.rosemeire.deconti.findmovies.UserInterface.MovieReportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_KEY_URL_IMAGE_W500;

/* ********************************************************************************************** **
/* **** Adapter to fill up recycler view
/* ********************************************************************************************** */
public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.MyViewHolder> {

    private final ArrayList<ModelMovie> mModelMovies;
    private final MovieReport activity;

    /* ****************************************************************************************** **
    /* **** Initialization
    /* ****************************************************************************************** */
    public AdapterMovie(Activity activity, ArrayList<ModelMovie> modelMovies) {

        this.mModelMovies = modelMovies;
        this.activity = (MovieReport) activity;

    }

    /* ****************************************************************************************** **
    /* **** This gets called when each new ViewHolder is created. This happens when the RecyclerView
    /* **** is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling
    /* ****************************************************************************************** */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (layoutInflater).inflate(R.layout.movie, parent, false);
        return new MyViewHolder(view);
    }

    /* ****************************************************************************************** **
    /* **** OnBindViewHolder is called by the RecyclerView to display the data at the specified position
    /* ****************************************************************************************** */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ModelMovie modelMovie = mModelMovies.get(position);

        Picasso.get()
                .load(THE_MOVIE_DB_KEY_URL_IMAGE_W500
                        + modelMovie.getPath_poster())
                .into(holder.image_banner);

        final int positionAdapter = holder.getAdapterPosition();

        holder.image_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MovieReportFragment fm = (MovieReportFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_movie);

                (fm).setPosition(positionAdapter);

                if (!activity.isSecondScreen()) {

                    Intent intent = new Intent(activity, MovieDetail.class);
                    intent.putExtra(Intent.EXTRA_TEXT, modelMovie);
                    activity.startActivity(intent);

                } else {

                    activity.onMovieChanged(modelMovie);

                }
            }
        });
    }

    /* ****************************************************************************************** **
    /* **** This method simply returns the size of items to display. It is used behind the scenes
    /* **** to help layout our Views and for animations
    /* ****************************************************************************************** */
    @Override
    public int getItemCount() {

        return mModelMovies.size();

    }

    /* ****************************************************************************************** **
    /* **** Get first movie of the list
    /* ****************************************************************************************** */
    public ModelMovie getFirstMovie() {

        try {

            return mModelMovies.get(0);

        } catch (IndexOutOfBoundsException e) {

            return null;

        }
    }

    /* ****************************************************************************************** **
    /* **** Clear items and notify changes
    /* ****************************************************************************************** */
    public void clear() {

        mModelMovies.clear();
        notifyDataSetChanged();

    }

    /* ****************************************************************************************** **
    /* **** Include items and notify changes
    /* ****************************************************************************************** */
    public void add(ModelMovie m) {

        if (m != null) {

            mModelMovies.add(m);
            notifyItemInserted(mModelMovies.size() - 1);

        }
    }

    /* ****************************************************************************************** **
    /* **** A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
    /* **** a cache of the child views for a forecast item. It's also a convenient place to set an
    /* **** OnClickListener, since it has access to the adapter and the views
    /* ****************************************************************************************** */
    @SuppressWarnings("WeakerAccess")
    public class MyViewHolder extends RecyclerView.ViewHolder {

        final ImageView image_banner;

        public MyViewHolder(View itemView) {

            super(itemView);
            image_banner = itemView.findViewById(R.id.poster_movie);

        }
    }
}
