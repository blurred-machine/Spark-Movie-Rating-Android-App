package com.example.paras.imdb;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

// an adapter class that extends the recycler view.
public class MyPostersAdapter extends RecyclerView.Adapter<MyPostersAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    static Context context;
    static Activity activity;
    ArrayList<MyPostersDetails> arrayList = new ArrayList<>();

    // constructor of the adapter.
    MyPostersAdapter(Context context, ArrayList<MyPostersDetails> arrayList) {
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
this.context = context;
        activity = (Activity) context;
    }

    // overridden method of the recycler view that inflates the poster details.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.posters_movie_details, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // overridden method to bind the data to the views initialized.
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MyPostersDetails myPostersDetails = arrayList.get(position);

        Glide.with(context).load(myPostersDetails.getImageUrl())
                .placeholder(R.drawable.default_poster)
                .into(holder.postersMovieDetailsImageView);

        // on clicking the single poster of the detail movies, the poster should be visible in a dialog fragment.
        holder.postersMovieDetailsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int rra = holder.getPosition();
                MyPostersDetails myPostersDetails = arrayList.get(rra);

                MyDialogOfPoster myDialogOfPoster = new MyDialogOfPoster();
                myDialogOfPoster.showMyPosterDialogBox(myPostersDetails.getImageUrl());
                myDialogOfPoster.show(activity.getFragmentManager(), "posterDialog");


            }
        });
    }

    // method to return the arraylist size.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // custom defined view holder class extending the recycler view holder
    // used to initialize the image view inside the dialog fragment.
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView postersMovieDetailsImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            postersMovieDetailsImageView = (ImageView) itemView.findViewById(R.id.postersMovieDetailsImageView);

        }
    }

    // class that extends the dialog fragment class.
    public static class MyDialogOfPoster extends DialogFragment{

        String myPosterUrl;

        public void showMyPosterDialogBox(String image){
            this.myPosterUrl = image;

        }
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.poster_dialog_fragment, container,
                    false);
            // Do something else
            return rootView;
        }
        
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ImageView imageView = (ImageView)view.findViewById(R.id.myPosterDialogImageView);

            // used gilde to place the image inside the image view.
            Glide.with(context).load(myPosterUrl)
                    .placeholder(R.drawable.default_poster)
                    .into(imageView);
        }
    }
}
