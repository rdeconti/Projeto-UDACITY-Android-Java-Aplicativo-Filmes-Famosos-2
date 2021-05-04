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


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rosemeire.deconti.findmovies.Model.ModelReview;
import com.rosemeire.deconti.findmovies.R;

import java.util.ArrayList;
import java.util.Objects;

/* ********************************************************************************************** **
/* **** Adapter to fill up recycler view
/* ********************************************************************************************** */
@SuppressWarnings("WeakerAccess")
public class AdapterReview extends RecyclerView.Adapter<AdapterReview.MyViewHolder> {

    final ArrayList<ModelReview> mModelReviews;

    final Context mContext;

    final LayoutInflater mLayoutInflater;

    /* ****************************************************************************************** **
    /* **** Initialization
    /* ****************************************************************************************** */
    public AdapterReview(Context context, ArrayList<ModelReview> modelReviews) {

        this.mModelReviews = modelReviews;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /* ****************************************************************************************** **
    /* **** This gets called when each new ViewHolder is created. This happens when the RecyclerView
    /* **** is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling
    /* ****************************************************************************************** */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.review, parent, false);
        return new MyViewHolder(view);

    }

    /* ****************************************************************************************** **
    /* **** OnBindViewHolder is called by the RecyclerView to display the data at the specified position
    /* ****************************************************************************************** */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_author.setText(mModelReviews.get(position).getAuthor());
        holder.tv_content.setText(mModelReviews.get(position).getContent());

    }

    /* ****************************************************************************************** **
    /* **** This method simply returns the size of items to display. It is used behind the scenes
    /* **** to help layout our Views and for animations
    /* ****************************************************************************************** */
    @Override
    public int getItemCount() {

        return mModelReviews.size();

    }

    /* ****************************************************************************************** **
    /* **** Clear list of movies and notify changes
    /* ****************************************************************************************** */
    public void clear() {

        mModelReviews.clear();
        notifyDataSetChanged();

    }

    /* ****************************************************************************************** **
    /* **** Add movie in the list and notify change
    /* ****************************************************************************************** */
    public void add(ModelReview modelReview) {

        if (modelReview != null) {

            mModelReviews.add(modelReview);
            notifyItemInserted(mModelReviews.size() - 1);

        }
    }

    /* ****************************************************************************************** **
    /* **** A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
    /* **** a cache of the child views for a forecast item. It's also a convenient place to set an
    /* **** OnClickListener, since it has access to the adapter and the views
    /* ****************************************************************************************** */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tv_author;
        final TextView tv_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.review_content);
            tv_author = itemView.findViewById(R.id.review_author);
        }
    }
}
