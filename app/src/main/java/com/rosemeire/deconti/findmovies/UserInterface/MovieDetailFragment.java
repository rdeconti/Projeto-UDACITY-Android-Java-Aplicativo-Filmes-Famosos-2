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
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rosemeire.deconti.findmovies.Adapter.AdapterReview;
import com.rosemeire.deconti.findmovies.Adapter.AdapterTrailer;
import com.rosemeire.deconti.findmovies.Data.MovieContract;
import com.rosemeire.deconti.findmovies.Model.ModelMovie;
import com.rosemeire.deconti.findmovies.Model.ModelReview;
import com.rosemeire.deconti.findmovies.Model.ModelTrailer;
import com.rosemeire.deconti.findmovies.R;
import com.rosemeire.deconti.findmovies.Service.ServiceReview;
import com.rosemeire.deconti.findmovies.Service.ServiceTrailer;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.rosemeire.deconti.findmovies.Service.ServiceReview.KEY_RECEIVER;
import static com.rosemeire.deconti.findmovies.Service.ServiceTrailer.KEY_RESULT_TRAILERS;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_KEY_ARGUMENT;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.THE_MOVIE_DB_KEY_URL_IMAGE_W500;
import static com.rosemeire.deconti.findmovies.Utilities.UtilitiesConstants.YOUTUBE_URL_PATH;

/* ********************************************************************************************** **
/* **** Generate detail movie data
/* ********************************************************************************************** */
public class MovieDetailFragment extends Fragment {

    // Toolbar MENU STAR ICONS
    private static final int FAVORITE_ON_ID = R.drawable.ic_favorite_on_white_24dp;
    private static final int FAVORITE_OFF_ID = R.drawable.ic_favorite_off_white_24dp;

    // Recycler View Adapters
    @SuppressWarnings("WeakerAccess")
    AdapterTrailer mAdapterTrailer;
    @SuppressWarnings("WeakerAccess")
    AdapterReview mAdapterReview;

    private ModelMovie mModelMovie;
    private Menu nMenu;

    /* ****************************************************************************************** */
    /* **** Android Life Cycle - onCreate: set layouts, fields, initial data load
    /* ****************************************************************************************** */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    /* ****************************************************************************************** **
    /* **** Allow menu creation to fragment screen
    /* ****************************************************************************************** */
    public MovieDetailFragment() {

        setHasOptionsMenu(true);

    }

    /* TODO RUBRIC POINT:
    /* UI contains a screen for displaying the details for a selected movie.
    /*
    /* ****************************************************************************************** **
    /* **** Create screen and set data
    /* ****************************************************************************************** */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        Bundle arguments = getArguments();

        if (arguments != null) {

            mModelMovie = (ModelMovie) arguments.getSerializable(THE_MOVIE_DB_KEY_ARGUMENT);

        }

        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        rootView.setTag(viewHolder);

        return rootView;
    }

    /* TODO RUBRIC POINT:
    /* Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
    /*
    /* ****************************************************************************************** **
    /* **** Get data, image and load data
    /* ****************************************************************************************** */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (mModelMovie == null)
            return;

        final ViewHolder holder = (ViewHolder) view.getTag();

        Cursor cursor = (getContext()).getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI, null, MovieContract.MovieEntry._ID
                        + " = ?", new String[]{String.valueOf(mModelMovie.getMovieId())}, null);

        if (cursor != null && cursor.moveToNext()) {
            mModelMovie.setFavorite(true);
            cursor.close();
        }

        Picasso.get()
                .load(THE_MOVIE_DB_KEY_URL_IMAGE_W500 + mModelMovie.getBackdrop_path())
                .into(holder.iv_backdrop);

        holder.tv_title_movie.setText(mModelMovie.getTitle());
        holder.tv_overview.setText(mModelMovie.getOverview());
        holder.tv_release_date.setText(
                DateFormat.getDateInstance(DateFormat.YEAR_FIELD, Locale.getDefault()).format(mModelMovie.getReleaseDate())
        );

        holder.tv_rating.setText(getString(R.string.rating_movie, mModelMovie.getRating(), mModelMovie.getVote_count()));

        if (!mModelMovie.isFavorite()) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            holder.rv_trailers.setLayoutManager(layoutManager);
            holder.rv_reviews.setLayoutManager(layoutManager2);

            mAdapterTrailer = new AdapterTrailer(getContext(), new ArrayList<ModelTrailer>());
            mAdapterReview = new AdapterReview(getContext(), new ArrayList<ModelReview>());

            holder.rv_trailers.setAdapter(mAdapterTrailer);
            holder.rv_reviews.setAdapter(mAdapterReview);

            if (savedInstanceState != null) {
                updateTrailers();
                updateReviews();
            }

        }

        super.onViewCreated(view, savedInstanceState);

    }

    /* TODO RUBRIC POINT:
    /* Movie Details layout contains a section for displaying trailer videos and user reviews.
    /*
    /* ****************************************************************************************** **
    /* **** Upload trailer and review data
    /* ****************************************************************************************** */
    @Override
    public void onStart() {
        super.onStart();

        // Update trailers from movie
        updateTrailers();

        // Update review from movie
        updateReviews();
    }

    /* ****************************************************************************************** **
    /* **** Create menu options
    /* ****************************************************************************************** */
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {

        // Save menu data
        this.nMenu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail_fragment, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    /* ****************************************************************************************** */
    /* **** Set STAR MENU ICON like ON or OFF depending on movie status
    /* ****************************************************************************************** */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if (mModelMovie.isFavorite()) {

            // Set Toolbar Menu Icon like STAR OFF
            nMenu.getItem(0).setIcon(ContextCompat.getDrawable((getContext()), FAVORITE_OFF_ID));

        } else {

            // Set Toolbar Menu Icon like STAR ON
            nMenu.getItem(0).setIcon(ContextCompat.getDrawable((getContext()), FAVORITE_ON_ID));

        }
    }

    /* ****************************************************************************************** */
    /* **** Treat menu option chosen by user
    /* ****************************************************************************************** */
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Get menu item chosen by user
        int id = item.getItemId();

        // Treat option selected by user
        switch (id) {

            case R.id.menu_detail_favorite:
                treat_movie_favorite();
                break;

            case R.id.menu_detail_share:
                treat_movie_sharing();
                break;

        }

        // Return item selected
        return super.onOptionsItemSelected(item);

    }

    /* TODO RUBRIC POINT:
    /* In the movies detail screen, a user can tap a button (for example, a star) to mark it as
    /* a Favorite. Tap the button on a favorite movie will unfavorite it.
    /*
    /* ****************************************************************************************** **
    /* **** Treat movies (favorite or not favorite) ant set STAR icon
    /* ****************************************************************************************** */
    private void treat_movie_favorite() {

        // In case that MOVIE is already FAVORITE it is set to NO FAVORITE and deleted from favorite report
        // In case that MOVIE is already NO FAVORITE it is set to FAVORITE and inserted into favorite report

        if (mModelMovie.isFavorite()) {

            // Delete movie from favorite report
            mModelMovie.setFavorite(false);
            Objects.requireNonNull(getContext()).getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry._ID + " = " + mModelMovie.getMovieId(), null);

            if (Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.fragment_movie) != null)
            {
                MovieReport activity = ((MovieReport) getActivity());

                MovieReportFragment movieReportFragment = (MovieReportFragment)
                        getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_movie);

                if (movieReportFragment != null) {

                    movieReportFragment.updateMoviesTask();
                    activity.onMovieChanged(movieReportFragment.getMovieAdapter().getFirstMovie());

                }
            }

            // Set Toolbar Menu Icon like STAR ON
            nMenu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), FAVORITE_ON_ID));

            // Send message TOAST to user about movie has been deleted from favorite report movie
            sendToastMessage(R.string.msg_movie_favorite_deleted);

        } else {

            // Insert movie to favorite report
            mModelMovie.setFavorite(true);
            Objects.requireNonNull(getContext()).getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, mModelMovie.getContentValues());

            // Set Toolbar Menu Icon like STAR OFF
            nMenu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), FAVORITE_OFF_ID));

            // Send message TOAST to user about movie has been deleted from favorite report movie
            sendToastMessage(R.string.msg_movie_favorite_included);

        }
    }

    /* ****************************************************************************************** **
    /* **** Send toast message to user
    /* ****************************************************************************************** */
    private void sendToastMessage(int message) {

        // Send TOAST message to user
        Toast toast = Toast.makeText(getContext(), getString(message), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }

    /* TODO RUBRIC POINT:
    /* Implement sharing functionality to allow the user to share the first trailer’s YouTube
    /* URL from the movie details screen.
    /*
    /* ****************************************************************************************** **
    /* **** Treat share movie
    /* ****************************************************************************************** */
    private void treat_movie_sharing() {

        if (mAdapterTrailer != null) {

            ModelTrailer modelTrailer = mAdapterTrailer.getFirstTrailer();

            if (modelTrailer != null) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, modelTrailer.getName()
                        + ": " + YOUTUBE_URL_PATH + modelTrailer.getKey());

                startActivity(Intent.createChooser(shareIntent, getString(R.string.label_share_link)));

                sendToastMessage(R.string.msg_movie_shared);

            }
        }
    }

     /* ****************************************************************************************** **
    /* **** Update review data
    /* ****************************************************************************************** */
    public void updateReviews() {

        try {

            MyReviewReceiver reviewReceiver = new MyReviewReceiver(new Handler());

            Intent intent = new Intent(getActivity(), ServiceReview.class);
            intent.putExtra(ServiceReview.MOVIE_ID_QUERY_EXTRA, String.valueOf(mModelMovie.getMovieId()));
            intent.putExtra(KEY_RECEIVER, reviewReceiver);

            Objects.requireNonNull(getActivity()).startService(intent);

        } catch (NullPointerException e) {

            e.printStackTrace();

        }
    }

    /* ****************************************************************************************** **
    /* **** Update trailer data
    /* ****************************************************************************************** */
    public void updateTrailers() {

        try {

            MyTrailerReceiver trailerReceiver = new MyTrailerReceiver(new Handler());

            Intent intent = new Intent(getActivity(), ServiceTrailer.class);
            intent.putExtra(ServiceTrailer.MOVIE_ID_QUERY_EXTRA, String.valueOf(mModelMovie.getMovieId()));
            intent.putExtra(KEY_RECEIVER, trailerReceiver);

            Objects.requireNonNull(getActivity()).startService(intent);

        } catch (NullPointerException e) {

            e.printStackTrace();

        }
    }

    /* ****************************************************************************************** **
    /* **** Create a new ResultReceive to receive results - TRAILERS
    /* ****************************************************************************************** */
    @SuppressWarnings("WeakerAccess")
    class MyTrailerReceiver extends ResultReceiver {

        @SuppressLint("RestrictedApi")
        public MyTrailerReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (mAdapterTrailer != null) {

                mAdapterTrailer.clear();

                @SuppressWarnings("unchecked") ArrayList<ModelTrailer> modelTrailers = (ArrayList<ModelTrailer>) resultData.getSerializable(KEY_RESULT_TRAILERS);

                if ((getView() != null && getView().findViewById(R.id.empty_trailers) != null) &&
                        (modelTrailers != null && modelTrailers.isEmpty())) {
                    getView().findViewById(R.id.empty_trailers).setVisibility(View.VISIBLE);
                }

                for (ModelTrailer t : Objects.requireNonNull(modelTrailers)) {
                    mAdapterTrailer.add(t);
                }
            }
        }
    }

    /* ****************************************************************************************** **
    /* **** Create a new ResultReceive to receive results - REVIEWS
    /* ****************************************************************************************** */
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    class MyReviewReceiver extends ResultReceiver {

        @SuppressWarnings("WeakerAccess")
        @SuppressLint("RestrictedApi")
        public MyReviewReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (mAdapterReview != null) {
                mAdapterReview.clear();

                ArrayList<ModelReview> modelReviews = (ArrayList<ModelReview>) resultData.getSerializable(ServiceReview.KEY_RESULT_REVIEWS);

                if ((getView() != null && getView().findViewById(R.id.empty_review) != null) &&
                        (modelReviews != null && modelReviews.isEmpty())) {
                    getView().findViewById(R.id.empty_review).setVisibility(View.VISIBLE);
                }

                for (ModelReview r : Objects.requireNonNull(modelReviews)) {
                    mAdapterReview.add(r);

                }
            }
        }
    }

    /* ****************************************************************************************** **
    /* **** Load recycler view data
    /* ****************************************************************************************** */
    @SuppressWarnings("WeakerAccess")
    class ViewHolder {

        final ImageView iv_backdrop;

        final TextView tv_title_movie;
        final TextView tv_overview;
        final TextView tv_release_date;
        final TextView tv_rating;

        final RecyclerView rv_trailers;
        final RecyclerView rv_reviews;

        public ViewHolder(View view) {

            this.iv_backdrop = view.findViewById(R.id.iv_backdrop);
            this.tv_title_movie = view.findViewById(R.id.tv_movie_name);
            this.tv_overview = view.findViewById(R.id.tv_overview);
            this.tv_release_date = view.findViewById(R.id.tv_release_date);
            this.tv_rating = view.findViewById(R.id.tv_rating);
            this.rv_trailers = view.findViewById(R.id.rv_trailers);
            this.rv_reviews = view.findViewById(R.id.rv_reviews);

        }
    }
}
