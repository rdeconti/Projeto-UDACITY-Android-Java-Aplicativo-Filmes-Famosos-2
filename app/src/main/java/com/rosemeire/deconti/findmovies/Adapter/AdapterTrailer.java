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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rosemeire.deconti.findmovies.Model.ModelTrailer;
import com.rosemeire.deconti.findmovies.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.YOUTUBE_URL_BASE_VND;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.YOUTUBE_URL_PATH;

/* ********************************************************************************************** **
/* **** Adapter to fill up recycler view
/* ********************************************************************************************** */
@SuppressWarnings("WeakerAccess")
public class AdapterTrailer extends RecyclerView.Adapter<AdapterTrailer.MyViewHolder> {

    final ArrayList<ModelTrailer> mModelTrailers;

    final Context mContext;

    final LayoutInflater mLayoutInflater;

    /* ****************************************************************************************** **
    /* **** Initialization
    /* ****************************************************************************************** */
    public AdapterTrailer(Context context, ArrayList<ModelTrailer> modelTrailers) {

        this.mModelTrailers = modelTrailers;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /* ****************************************************************************************** **
    /* **** This gets called when each new ViewHolder is created. This happens when the RecyclerView
    /* **** is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling
    /* ****************************************************************************************** */
    @Override
    public AdapterTrailer.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.trailer, parent, false);
        return new MyViewHolder(view);

    }

    /* ****************************************************************************************** **
    /* **** OnBindViewHolder is called by the RecyclerView to display the data at the specified position
    /* ****************************************************************************************** */
    @Override
    public void onBindViewHolder(final AdapterTrailer.MyViewHolder holder, final int position) {

        holder.title.setText(mModelTrailers.get(position).getName());

        final int positionAdapter = holder.getAdapterPosition();

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YOUTUBE_URL_BASE_VND +
                                    mModelTrailers.get(positionAdapter).getKey())));

                } catch (ActivityNotFoundException ex) {

                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YOUTUBE_URL_PATH +
                                    mModelTrailers.get(positionAdapter).getKey())));
                }
            }
        });
    }

    /* ****************************************************************************************** **
    /* **** Get first trailer of the list
    /* ****************************************************************************************** */
    public ModelTrailer getFirstTrailer() {

        if (mModelTrailers != null && !mModelTrailers.isEmpty())

            return mModelTrailers.get(0);

        else

            return null;

    }

    /* ****************************************************************************************** **
    /* **** This method simply returns the size of items to display. It is used behind the scenes
    /* **** to help layout our Views and for animations
    /* ****************************************************************************************** */
    @Override
    public int getItemCount() {

        return mModelTrailers.size();

    }

    /* ****************************************************************************************** **
    /* **** Clear list of trailers and notify
    /* ****************************************************************************************** */
    public void clear() {

        mModelTrailers.clear();
        notifyDataSetChanged();
    }

    /* ****************************************************************************************** **
    /* **** Add trailer in the list and notify
    /* ****************************************************************************************** */
    public void add(ModelTrailer modelTrailer) {

        if (modelTrailer != null) {

            mModelTrailers.add(modelTrailer);
            notifyItemInserted(mModelTrailers.size() - 1);

        }
    }

    /* ****************************************************************************************** **
    /* **** A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
    /* **** a cache of the child views for a forecast item. It's also a convenient place to set an
    /* **** OnClickListener, since it has access to the adapter and the views
    /* ****************************************************************************************** */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final ImageView imageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.iv_video);

        }
    }
}
