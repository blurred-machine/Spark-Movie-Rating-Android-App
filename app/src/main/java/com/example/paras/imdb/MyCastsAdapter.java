package com.example.paras.imdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

// adapter class for the cast which extends the recycler view.
public class MyCastsAdapter extends RecyclerView.Adapter<MyCastsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    Context context;
    ArrayList<MyCastsDetails> arrayList = new ArrayList<>();

    // constructor of the class.
    MyCastsAdapter(Context context, ArrayList<MyCastsDetails> arrayList) {
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.context = context;
    }

    //method used to inflate the layout of the recycler view
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.casts_and_crew_movie_details, parent, false);
        MyCastsAdapter.MyViewHolder myViewHolder = new MyCastsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    // method used to bind the data with the viewHolder of the inflated data.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyCastsDetails myCastsDetails = arrayList.get(position);

        // used the glide class to place the image in the imageView.
        Glide.with(context).load(myCastsDetails.getCastUrl())
                .placeholder(R.drawable.default_poster)
                .into(holder.imageViewCaste);

        holder.textViewRealName.setText(myCastsDetails.getRealName());
        holder.textViewCharacter.setText(myCastsDetails.getCharacterName());


    }

    // method that returns the size of the arraylist.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // custom class that extends the viewHolder of the recycler view.
    // this class initialises the views inside the inflated recycler view object.
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewCaste;
        TextView textViewCharacter;
        TextView textViewRealName;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageViewCaste = itemView.findViewById(R.id.castImageView);
            textViewRealName = itemView.findViewById(R.id.tvRealName);
            textViewCharacter = itemView.findViewById(R.id.tvCharacterName);

        }
    }
}
