package com.example.paras.imdb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<MyMovieDetails> {
    // object initialization
    private List<MyMovieDetails> movieProperties;
    private Context context;
    private final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";

    private int resource;

    ImageView movieImage;
    TextView tvMovieName;
    TextView tvReleasedOn;
    RatingBar popularityImages;
    TextView tvVoteCoult;

    // constructor of this class is called to set the class variables as the passed on variables.
    public MyListAdapter(Context context, int resource, ArrayList<MyMovieDetails> movieProperties) {
        super(context, resource, movieProperties);
        this.context = context;
        this.movieProperties = movieProperties;
        this.resource = resource;
    }

    // get view overridden method of array adapter
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get the view at the position from the arrayList and inflate the passed resource.
        MyMovieDetails myMovieDetails = movieProperties.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if (inflater != null) {
                convertView = inflater.inflate(resource, null);
            }
        }
        // initialize the views.
        movieImage = (ImageView) convertView.findViewById(R.id.movieImage);
        tvMovieName = (TextView) convertView.findViewById(R.id.tvMovieName);
        tvReleasedOn = (TextView) convertView.findViewById(R.id.tvReleasedOn);
        popularityImages = (RatingBar) convertView.findViewById(R.id.popularityImages);
        tvVoteCoult = (TextView) convertView.findViewById(R.id.tvVoteCoult);

        // setting the data in the respective views
        tvMovieName.setText(myMovieDetails.getName());
        tvReleasedOn.setText(myMovieDetails.getReleaseDate());
        popularityImages.setRating(myMovieDetails.getPopularity());
        String votesAvg = myMovieDetails.getVotesAverage();
        String voteCount = myMovieDetails.getVotesCount();

        if (Float.parseFloat(votesAvg) < 5.0) {
            tvVoteCoult.setTextColor(Color.parseColor("#BF360C"));
        } else if (Float.parseFloat(votesAvg) >= 8.0) {
            tvVoteCoult.setTextColor(Color.parseColor("#2E7D32"));
        } else
            tvVoteCoult.setTextColor(Color.GRAY);
        tvVoteCoult.setText("(" + votesAvg + "/10) voted by " + voteCount + " users.");

        Glide.with(context).load(BASE_IMAGE_URL + myMovieDetails.getImageUrl()).into(movieImage);

        return convertView;
    }
}
