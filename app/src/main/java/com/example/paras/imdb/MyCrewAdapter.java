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

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.ArrayList;

// a custom adapter class that extends the RecyclerView.
public class MyCrewAdapter extends RecyclerView.Adapter<MyCrewAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    Context context;
    ArrayList<MyCrewDetails> arrayList = new ArrayList<>();

    // constructor.
    MyCrewAdapter(Context context, ArrayList<MyCrewDetails> arrayList) {
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.context = context;
    }

    //method used to inflate the layout of the recycler view
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.casts_and_crew_movie_details, parent, false);
        MyCrewAdapter.MyViewHolder myViewHolder = new MyCrewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    // method used to bind the data with the viewHolder of the inflated data.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyCrewDetails myCrewDetails = arrayList.get(position);

        // used the glide class to place the image in the imageView.
        Glide.with(context).load(myCrewDetails.getCrewImage())
                .placeholder(R.drawable.default_poster)
                .into(holder.imageViewCrew);

        // setting the values of the text views to the value extracted.
        holder.crewName.setText(myCrewDetails.getCrewName());
        holder.crewJob.setText(myCrewDetails.getCrewJob());

    }

    // method that returns the size of the arraylist.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // custom class that extends the viewHolder of the recycler view.
    // this class initialises the views inside the inflated recycler view object.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCrew;
        TextView crewName;
        TextView crewJob;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageViewCrew = itemView.findViewById(R.id.castImageView);
            crewName = (TextView) itemView.findViewById(R.id.tvRealName);
            crewJob = (TextView) itemView.findViewById(R.id.tvCharacterName);
        }
    }
}
