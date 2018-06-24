package com.example.paras.imdb;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MyListAdapterSpecificOne extends RecyclerView.Adapter<MyListAdapterSpecificOne.MyViewHolder> implements View.OnClickListener {

    private LayoutInflater inflater;
    static Context context;
    String capturedId;
    MyMovieDetailSpecificOne specificOne;
    ContentValues cv, cv2, cv3;
    MyDatabaseHelper databaseHelper;
    SQLiteDatabase db;

    ArrayList<MyMovieDetailSpecificOne> arrayList = new ArrayList<>();

    String userRating;
    String guestSessionId;

    View view;

    // the custom request codes.
    private static final int FAV_CODE = 123;
    private static final int WAT_CODE = 321;

    // the constructor of the class.
    MyListAdapterSpecificOne(Context context, ArrayList<MyMovieDetailSpecificOne> arrayList, String capturedId) {
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.context = context;
        this.capturedId = capturedId;
    }

    //method used to inflate the layout of the recycler view
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.detail_layout_item_1, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // method that returns the size of the ArrayList.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // method used to bind the data with the viewHolder of the inflated data.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        specificOne = arrayList.get(position);
        databaseHelper = new MyDatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        Glide.with(context).load(specificOne.getImageUrl())
                .thumbnail(0.5f)
                .crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieImage);

        holder.tvMovieName.setText(specificOne.getTitle());
        holder.ratingPopularityBig.setRating(specificOne.getPopularity());
        holder.tvDescription.setText(specificOne.getDescription());
        holder.tvReleaseDate.setText("Release Date: " + specificOne.getReleaseDate());
        holder.tvBudget.setText("Budget: " + specificOne.getBudget());
        holder.tvRevenue.setText("Revenue: " + specificOne.getRevenue());
        holder.tvStatus.setText("Status: " + specificOne.getStatus());
        holder.tvAvgVote.setText("(" + specificOne.getVotesAverage() + "/10)");
        holder.tvTotalVotes.setText(specificOne.getVotesCount() + " Users.");
        holder.tvDescriptionBig.setText(specificOne.getDescriptionBig());

        holder.imgMyFavourite.setOnClickListener(this);
        holder.imgWatchList.setOnClickListener(this);
        holder.btnRateIt.setOnClickListener(this);

        insertIntoDatabase();
        checkFavouriteButtonColor(holder.imgMyFavourite, FAV_CODE);
        checkFavouriteButtonColor(holder.imgWatchList, WAT_CODE);
    }


    public void insertIntoDatabase() {
        int myFlag = 0;
        String[] column = {MyDatabaseHelper.ID};

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, column, null, null,
                null, null, null);

        int i1 = cursor.getColumnIndex(MyDatabaseHelper.ID);

        cursor.moveToFirst();
        cursor.moveToPrevious();
        while (cursor.moveToNext()) {
            int idd = cursor.getInt(i1);
            if (idd == Integer.parseInt(capturedId)) {
                myFlag = 1;
                break;
            }
        }

        if (myFlag == 0) {
            cv = new ContentValues();
            cv.put(MyDatabaseHelper.ID, specificOne.getId());
            cv.put(MyDatabaseHelper.TITLE, specificOne.getTitle());
            cv.put(MyDatabaseHelper.RELEASE_DATE, specificOne.getReleaseDate());
            cv.put(MyDatabaseHelper.POSTER_PATH, specificOne.getImageUrl());
            cv.put(MyDatabaseHelper.POPULARITY, specificOne.getPopularity());
            cv.put(MyDatabaseHelper.VOTE_AVERAGE, specificOne.getVotesAverage());
            cv.put(MyDatabaseHelper.VOTE_COUNT, specificOne.getVotesCount());
            cv.put(MyDatabaseHelper.RAW_DETAILS, specificOne.getDescription());
            cv.put(MyDatabaseHelper.TITLE, specificOne.getTitle());
            cv.put(MyDatabaseHelper.IS_FAVOURITE, 0);
            cv.put(MyDatabaseHelper.IS_WATCHLIST, 0);
            db.insert(MyDatabaseHelper.TABLE_NAME, null, cv);
        }

    }

    @Override
    public void onClick(View v) {
        String[] whereArgs = {specificOne.getId()};
        if (v.getId() == R.id.imgMyFavourite) {
            cv2 = new ContentValues();
            cv2.put(MyDatabaseHelper.IS_FAVOURITE, 1);
            db.update(MyDatabaseHelper.TABLE_NAME, cv2, MyDatabaseHelper.ID + " =? ", whereArgs);

            checkFavouriteButtonColor(v, FAV_CODE);

        }
        if (v.getId() == R.id.imgWatchList) {
            cv3 = new ContentValues();
            cv3.put(MyDatabaseHelper.IS_WATCHLIST, 1);
            db.update(MyDatabaseHelper.TABLE_NAME, cv3, MyDatabaseHelper.ID + " =? ", whereArgs);

            checkFavouriteButtonColor(v, WAT_CODE);

        }
        if (v.getId() == R.id.btnRateThisMovie) {
            GetGuestSessionId getGuestSessionId = new GetGuestSessionId();
            rateItOne();
        }
    }

    private void checkFavouriteButtonColor(View imageView, int whichBtn) {
        Cursor data = db.query(MyDatabaseHelper.TABLE_NAME,
                new String[]{MyDatabaseHelper.IS_FAVOURITE, MyDatabaseHelper.IS_WATCHLIST},
                MyDatabaseHelper.ID + "=?", new String[]{specificOne.getId()},
                null, null, null);
        int fav = 404;
        int wat = 404;
        data.moveToFirst();
        data.moveToPrevious();
        while (data.moveToNext()) {
            fav = data.getInt(data.getColumnIndex(MyDatabaseHelper.IS_FAVOURITE));
            wat = data.getInt(data.getColumnIndex(MyDatabaseHelper.IS_WATCHLIST));
        }


        if (whichBtn == FAV_CODE) {
            if (fav == 1) {
                ((ImageButton) imageView).setImageResource(R.drawable.favourite_1_icon);
            } else {
                ((ImageButton) imageView).setImageResource(R.drawable.favourite_0_icon);
            }
        } else if (whichBtn == WAT_CODE) {
            if (wat == 1) {
                ((ImageButton) imageView).setImageResource(R.drawable.watchlist_1_icon);
            } else {
                ((ImageButton) imageView).setImageResource(R.drawable.watchlist_0_icon);
            }
        }
    }

    public void rateItOne() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        final View view = inflater.inflate(R.layout.rate_movie_dialog_box, null);   // inflating the layout for the activity.
        builder.setView(view);
        builder.setPositiveButton("rate", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                RatingBar ratingBarRateIt = (RatingBar) view.findViewById(R.id.rateMovieDialogBar);
                float ratings;
                ratings = ratingBarRateIt.getRating();
                userRating = String.valueOf(ratings);
                Log.i("User Ratings: ", "" + ratings);
                RatePostRequestClass requestClass = new RatePostRequestClass();
                requestClass.execute(userRating);

                if (ratings == 0.0){
                    Toast.makeText(context, "Sorry!! Minimum rating is 0.5", Toast.LENGTH_SHORT).show();

                }
                else if (ratings >= 3.0)
                    Toast.makeText(context, "Thank you for rating " + userRating, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Thank you for your rating.", Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    // custom class that extends the viewHolder of the recycler view.
    // this class initialises the views inside the inflated recycler view object.
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        TextView tvMovieName;
        RatingBar ratingPopularityBig;
        TextView tvDescription;
        TextView tvReleaseDate;
        TextView tvBudget;
        TextView tvRevenue;
        TextView tvStatus;
        TextView tvAvgVote;
        TextView tvTotalVotes;
        TextView tvDescriptionBig;
        ImageButton imgMyFavourite;
        ImageButton imgWatchList;
        Button btnRateIt;


        public MyViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageMovie);
            tvMovieName = itemView.findViewById(R.id.tvMovieName);
            ratingPopularityBig = itemView.findViewById(R.id.ratingPopularityBig);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            tvRevenue = itemView.findViewById(R.id.tvRevenue);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAvgVote = itemView.findViewById(R.id.tvAvgVote);
            tvTotalVotes = itemView.findViewById(R.id.tvTotalVotes);
            tvDescriptionBig = itemView.findViewById(R.id.tvDescriptionBig);
            imgMyFavourite = (ImageButton) itemView.findViewById(R.id.imgMyFavourite);
            imgWatchList = (ImageButton) itemView.findViewById(R.id.imgWatchList);
            btnRateIt = (Button) itemView.findViewById(R.id.btnRateThisMovie);
        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////Rate movie class
    public class GetGuestSessionId implements DataTransferInterface {

        View prog = view.findViewById(R.id.myInsideProgressRound);
        View listee = view.findViewById(R.id.insideListDetails1);
        String API_KEY = "8496be0b2149805afa458ab8ec27560c";
        public String guestSessionIdUrl = "http://api.themoviedb.org/3/authentication/guest_session/new?api_key=" + API_KEY;


        public GetGuestSessionId() {
            if (connectionIsEstablished2()) {
                FetchData fetchData = new FetchData(context, guestSessionIdUrl, this, listee, prog);
                fetchData.execute();
            } else {
                Toast.makeText(context, "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void dataHere(String detailData) {
            try {
                JSONObject object = new JSONObject(detailData);
                if (object.has("guest_session_id")) {
                    if (object.getString("success") == "true") {
                        guestSessionId = object.getString("guest_session_id");
                        API_KEY = guestSessionId;
                        Log.i("Guest Session Id:", "" + API_KEY);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        boolean connectionIsEstablished2() {
            // initially we assume that the connection is not established.
            boolean isConnected = false;
            // getting the connectivityManager to check the connection.
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            // getting the information about the network that is established.
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // if networkInfo has some information about the connectivity then,
            if (networkInfo != null) {
                // set the connection as true.
                isConnected = true;
            }
            // return the status of connectivity.
            return isConnected;
        }
    }
    /// ///////////////////////////////////////////////////////////////////////////////////////////////Rate movie class ends

    // class extending AsyncTask
    public class RatePostRequestClass extends AsyncTask<String, Void, String> {

        URL ratingUrl;

        {
            try {
                // create the url of the user rating api
                ratingUrl = new URL("https://api.themoviedb.org/3/movie/" + capturedId + "/rating?api_key=8496be0b2149805afa458ab8ec27560c&guest_session_id=" + guestSessionId);
                Log.i("RatePostRequestClass: ", ratingUrl.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }


        // the overridden method of the async task class
        //this method is executed before the execution of any other code in the async task.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // the overridden method of the async task class
        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("value", strings[0]);
                Log.e("params", postDataParams.toString());

                // using the HttpURLConnection to set the connection to the rating url
                // then set it's time out and request method as post.
                HttpURLConnection conn = (HttpURLConnection) ratingUrl.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // output stream and buffer writer to write the data as post request to the api.
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                // getting the response of the post request using buffer reader.
                // and appending the string variable line with the data extracted line by line.
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        // menthod that gets the data after the post request.
        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }

        // the overridden method of the async task class
        // the method executed after the execution the main do in background method of async task.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(context, ""+s, Toast.LENGTH_LONG).show();        }

        }
    }
    /// ///////////////////////////////////////////////////////////////////////////////////////////////RatePostRequestClass ends


}
