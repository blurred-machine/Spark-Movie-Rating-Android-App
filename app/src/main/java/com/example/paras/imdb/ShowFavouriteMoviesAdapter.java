package com.example.paras.imdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

// a custom adapter class for the favourite movies.
public class ShowFavouriteMoviesAdapter extends ArrayAdapter<MyMovieDetails> {

    private List<MyMovieDetails> listViewFavouritesList;
    private Context context;
    private int resource;

    // constructor.
    public ShowFavouriteMoviesAdapter(Context context, int resource, List<MyMovieDetails> listViewFavouritesList) {
        super(context, resource, listViewFavouritesList);
        this.context = context;
        this.resource = resource;
        this.listViewFavouritesList = listViewFavouritesList;
    }

    // initializing the views and adding data to them.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        ImageView ivPoster = view.findViewById(R.id.movieImage);
        TextView tvTitle = view.findViewById(R.id.tvMovieName);
        TextView tvReleaseDate = view.findViewById(R.id.tvReleasedOn);
        RatingBar rbPopularity = view.findViewById(R.id.popularityImages);
        TextView tvVoteCount = view.findViewById(R.id.tvVoteCoult);

        MyMovieDetails myMovieDetails = listViewFavouritesList.get(position);

        tvTitle.setText(myMovieDetails.getName());
        tvReleaseDate.setText(myMovieDetails.getReleaseDate());
        rbPopularity.setRating(myMovieDetails.getPopularity());
        tvVoteCount.setText("("+myMovieDetails.getVotesAverage()+"/10) voted by "+myMovieDetails.getVotesCount()+" users.");

        Glide.with(context).load(myMovieDetails.getImageUrl())
                .placeholder(R.drawable.default_poster)
                .into(ivPoster);

        return view;
    }
}
