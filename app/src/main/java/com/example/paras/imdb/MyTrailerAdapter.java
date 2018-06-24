package com.example.paras.imdb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

// an adapter class that extends the recycler view.
public class MyTrailerAdapter extends RecyclerView.Adapter<MyTrailerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    Context context;
    ArrayList<MyTrailersDetails> arrayList = new ArrayList<>();

    // constructor.
    MyTrailerAdapter(Context context, ArrayList<MyTrailersDetails> arrayList) {
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.context = context;
    }

    // overridden method of the recycler view that inflates the poster details.
    @NonNull
    @Override
    public MyTrailerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.trailers_movie_details, parent, false);
        MyTrailerAdapter.MyViewHolder myViewHolder = new MyTrailerAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    // overridden method to bind the data to the views initialized.
    @Override
    public void onBindViewHolder(@NonNull MyTrailerAdapter.MyViewHolder holder, int position) {
        MyTrailersDetails myTrailersDetails = arrayList.get(position);

        String text = myTrailersDetails.getTrailerType() + "(" + myTrailersDetails.getTrailerSize() + ")";
        final String url = myTrailersDetails.getTrailerUrl();

        holder.trailerTextView.setText(text);
        holder.trailerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    // method to return the arraylist size.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // custom defined view holder class extending the recycler view holder
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView trailerTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            trailerTextView = (TextView) itemView.findViewById(R.id.trailerTextView);
        }
    }
}
