package com.example.paras.imdb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// an adapter class that contains the movie details.
public class MyMovieDetailActivity extends AppCompatActivity {

    String capturedId;
    Context mainContext;
    ProgressBar myDetailProgressRound;
    RecyclerView mainRecycleView;
    Toolbar toolbar;
    CoordinatorLayout myHomeCoordinatorLayout;
    LinearLayout noDataLinearLayout;
    View noDataFoundView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movie_detail);

        myDetailProgressRound = (ProgressBar) findViewById(R.id.myDetailProgressRound);
        mainRecycleView = (RecyclerView) findViewById(R.id.mainRecycleView);
        myHomeCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.myHomeCoordinatorLayout2);
        toolbar = (Toolbar) findViewById(R.id.app_bar2);

        // setting the action bar for the app as the custom defied toolbar
        setSupportActionBar(toolbar);
        // setting the setHomeButtonEnabled as true.
        getSupportActionBar().setHomeButtonEnabled(true);
        // display the upEnabled to display.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // inflating the required layout views.
        LayoutInflater inflater = LayoutInflater.from(this);
        noDataLinearLayout = (LinearLayout) findViewById(R.id.noDataImageView2);
        noDataFoundView = inflater.inflate(R.layout.no_internet_layout, null, true);
        noDataLinearLayout.addView(noDataFoundView);

        mainContext = this;
        // getting the id of the specific movie that i passed earlier from the intent.
        capturedId = getIntent().getStringExtra("Key123");
        // creating the poster url for getting the images of the movie.
        String posterUrl = "http://api.themoviedb.org/3/movie/" + capturedId + "/images/?api_key=8496be0b2149805afa458ab8ec27560c";


        // creating the objects of all the detail activities of the specific movie whose classes i have made below.
        MovieDetails movieDetails = new MovieDetails();
        MoviePoster moviePoster = new MoviePoster();
        MovieTrailers movieTrailers = new MovieTrailers();
        MovieCast movieCast = new MovieCast();
        MovieCrew movieCrew = new MovieCrew();

    }//on create ends

////////////////////////////////////////////////////////////////////////////////////////////////////rate movie dilaog box


    // method to create the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // adding the functionality to the up navigation button for back to main activity.
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////////////////////////////////////////////////// connection check starts
// method to check whether the connection is established or not.
    public boolean connectionIsEstablished() {
        // initially we assume that the connection is not established.
        boolean isConnected = false;
        // getting the connectivityManager to check the connection.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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

    ///////////////////////////////////////////////////////////////////////////////////// connection check ends
    /// ///////////////////////////////////////////////////////////////////////////////////////////////Movie details
    // method to show the details of the movie.
    public class MovieDetails implements DataTransferInterface {

        private MyListAdapterSpecificOne specificAdapter;
        ArrayList<MyMovieDetailSpecificOne> arrayList;
        RecyclerView recyclerViewDetails;

        String detailUrl = "http://api.themoviedb.org/3/movie/" + capturedId + "?api_key=8496be0b2149805afa458ab8ec27560c";

        // if connection is there then the data corresponding to the movie detail url is fetched from the async task class.
        public MovieDetails() {
            if (connectionIsEstablished()) {
                FetchData fetchData = new FetchData(MyMovieDetailActivity.this, detailUrl, this, mainRecycleView, myDetailProgressRound);
                fetchData.execute();
                noDataLinearLayout.setVisibility(View.INVISIBLE);
            } else {
                // if connection is not there then the snackbar will ask to connect to wifi.
                final Snackbar snackbar = Snackbar.make(myHomeCoordinatorLayout, "Internet Connection Needed", Snackbar.LENGTH_LONG);
                // the action to connect wifi will create an object of the wifi manager and set it enabled.
                snackbar.setAction("CONNECT WIFI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // todo: connect to wifi here.
                        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(true);
                        Snackbar.make(myHomeCoordinatorLayout, "Wifi Enabled.\nPress back button.", Snackbar.LENGTH_SHORT).show();
                    }
                });
                snackbar.show();

                noDataLinearLayout.setVisibility(View.VISIBLE);
                myDetailProgressRound.setVisibility(View.INVISIBLE);
                toolbar.setTitle("Movie Details");
                //Toast.makeText(mainContext, "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }

        // implemented method of the interface that gives the sting of the json data fetched.
        @Override
        public void dataHere(String detailData) {
            try {
                // creating the instance of arraylist and parsing the data from the json object.
                arrayList = new ArrayList<>();
                JSONObject object = new JSONObject(detailData);
                // parsing the data and storing it in respective string variables.
                String id = object.getString("id");
                String imageCode = object.getString("poster_path");
                String title = object.getString("original_title");
                float popRating = 0;
                popRating = Float.parseFloat(object.getString("popularity")) / 100;
                if (popRating > 5) {
                    popRating = 5;
                }
                String desc = object.getString("tagline");
                String releaseDate = object.getString("release_date");
                String budget = object.getString("budget");
                String revenue = object.getString("revenue");
                String status = object.getString("status");
                String vote_average = object.getString("vote_average");
                String vote_count = object.getString("vote_count");
                String descriptionBig = object.getString("overview");
                // making the string url.
                String imageUrl = "http://image.tmdb.org/t/p/w500" + imageCode;

                // adding the movie name to the toolbar.
                toolbar.setTitle(title);
                // adding the data in the arraylist.
                arrayList.add(new MyMovieDetailSpecificOne(id, imageUrl, title, popRating, desc, releaseDate, budget,
                        revenue, status, vote_average, vote_count, descriptionBig));
                //todo paras today continue from here.
                //  ArrayAdapter<MyMovieDetailSpecificOne> adapter = new MyListAdapterSpecificOne(this,R.layout.list_view_layout, arrayList);
                // attaching the data to the custom adapter and displaying it to the recycler view
                recyclerViewDetails = findViewById(R.id.mainRecycleView);
                specificAdapter = new MyListAdapterSpecificOne(mainContext, arrayList, capturedId);
                recyclerViewDetails.setAdapter(specificAdapter);
                recyclerViewDetails.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////Movie details ends
/// ///////////////////////////////////////////////////////////////////////////////////////////////Movie posters
    public class MoviePoster implements DataTransferInterface {

        RecyclerView recyclerViewPoster;
        private MyPostersAdapter specificPosterAdapter;
        ArrayList<MyPostersDetails> arrayPosterList = new ArrayList<>();
        String posterUrl = "http://api.themoviedb.org/3/movie/" + capturedId + "/images?api_key=8496be0b2149805afa458ab8ec27560c";

        public MoviePoster() {
            if (connectionIsEstablished()) {
                FetchData fetchData = new FetchData(mainContext, posterUrl, this, mainRecycleView, myDetailProgressRound);
                fetchData.execute();
            } else {
                Toast.makeText(mainContext, "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void dataHere(String detailData) {
            try {
                // parsing the data from the json object and array inside it.
                JSONObject object = new JSONObject(detailData);
                JSONArray jsonArray = object.getJSONArray("posters");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObject = jsonArray.getJSONObject(i);
                    String myPosterUrl = innerObject.getString("file_path");
                    String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + myPosterUrl;

                    arrayPosterList.add(new MyPostersDetails(fullPosterUrl));
                }
                // attaching the data to the custom adapter and displaying it to the recycler view
                recyclerViewPoster = findViewById(R.id.postersRecyclerView);
                specificPosterAdapter = new MyPostersAdapter(MyMovieDetailActivity.this, arrayPosterList);
                recyclerViewPoster.setAdapter(specificPosterAdapter);
                recyclerViewPoster.setLayoutManager(new LinearLayoutManager(mainContext, LinearLayoutManager.HORIZONTAL, false));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////Movie posters ends
/// ///////////////////////////////////////////////////////////////////////////////////////////////Movie trailers
    public class MovieTrailers implements DataTransferInterface {

        RecyclerView recyclerViewTrailer;
        private MyTrailerAdapter specificTrailerAdapter;
        ArrayList<MyTrailersDetails> arrayTrailerList = new ArrayList<>();
        String trailerUrl = "http://api.themoviedb.org/3/movie/" + capturedId + "/videos?api_key=8496be0b2149805afa458ab8ec27560c";

        public MovieTrailers() {
            if (connectionIsEstablished()) {
                FetchData fetchData = new FetchData(mainContext, trailerUrl, this, mainRecycleView, myDetailProgressRound);
                fetchData.execute();
            } else {
                Toast.makeText(mainContext, "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void dataHere(String detailData) {
            try {
                JSONObject object = new JSONObject(detailData);
                JSONArray jsonArray = object.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObject = jsonArray.getJSONObject(i);
                    String key = innerObject.getString("key");
                    String site = innerObject.getString("site");
                    String type = innerObject.getString("type");
                    String size = innerObject.getString("size");

                    String fullTrailerUrl = "https://www." + site + ".com/watch?v=" + key;

                    arrayTrailerList.add(new MyTrailersDetails(fullTrailerUrl, size, type));
                }
                recyclerViewTrailer = (RecyclerView) findViewById(R.id.trailersRecyclerView);
                specificTrailerAdapter = new MyTrailerAdapter(mainContext, arrayTrailerList);
                recyclerViewTrailer.setAdapter(specificTrailerAdapter);
                recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(mainContext, LinearLayoutManager.HORIZONTAL, false));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////Movie trailers ends
/// ///////////////////////////////////////////////////////////////////////////////////////////////Movie cast
    public class MovieCast implements DataTransferInterface {

        RecyclerView recyclerViewCast;
        private MyCastsAdapter specificCastAdapter;
        ArrayList<MyCastsDetails> arrayCastList = new ArrayList<>();
        String castUrl = "http://api.themoviedb.org/3/movie/" + capturedId + "/credits?api_key=8496be0b2149805afa458ab8ec27560c";

        public MovieCast() {
            if (connectionIsEstablished()) {
                FetchData fetchData = new FetchData(mainContext, castUrl, this, mainRecycleView, myDetailProgressRound);
                fetchData.execute();
            } else {
                Toast.makeText(mainContext, "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void dataHere(String detailData) {
            try {
                JSONObject object = new JSONObject(detailData);
                JSONArray jsonArray = object.getJSONArray("cast");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObject = jsonArray.getJSONObject(i);
                    String myCastUrl = innerObject.getString("profile_path");
                    String characterName = innerObject.getString("character");
                    String realName = innerObject.getString("name");

                    String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + myCastUrl;

                    arrayCastList.add(new MyCastsDetails(characterName, realName, fullPosterUrl));
                }
                recyclerViewCast = findViewById(R.id.castsRecyclerView);
                specificCastAdapter = new MyCastsAdapter(mainContext, arrayCastList);
                recyclerViewCast.setAdapter(specificCastAdapter);
                recyclerViewCast.setLayoutManager(new LinearLayoutManager(mainContext, LinearLayoutManager.HORIZONTAL, false));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////Movie cast ends
/// ///////////////////////////////////////////////////////////////////////////////////////////////Movie cast
    public class MovieCrew implements DataTransferInterface {

        RecyclerView recyclerViewCrew;
        private MyCrewAdapter specificCrewAdapter;
        ArrayList<MyCrewDetails> arrayCrewList = new ArrayList<>();
        String crewUrl = "http://api.themoviedb.org/3/movie/" + capturedId + "/credits?api_key=8496be0b2149805afa458ab8ec27560c";

        public MovieCrew() {
            if (connectionIsEstablished()) {
                FetchData fetchData = new FetchData(mainContext, crewUrl, this, mainRecycleView, myDetailProgressRound);
                fetchData.execute();
            } else {
                Toast.makeText(mainContext, "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void dataHere(String detailData) {
            try {
                JSONObject object = new JSONObject(detailData);
                JSONArray jsonArray = object.getJSONArray("crew");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObject = jsonArray.getJSONObject(i);
                    String myCastUrl = innerObject.getString("profile_path");
                    String job = innerObject.getString("job");
                    String realName = innerObject.getString("name");

                    String fullPosterUrl = "http://image.tmdb.org/t/p/w500" + myCastUrl;

                    arrayCrewList.add(new MyCrewDetails(fullPosterUrl, realName, job));
                }
                recyclerViewCrew = findViewById(R.id.crewRecyclerView);
                specificCrewAdapter = new MyCrewAdapter(mainContext, arrayCrewList);
                recyclerViewCrew.setAdapter(specificCrewAdapter);
                recyclerViewCrew.setLayoutManager(new LinearLayoutManager(mainContext, LinearLayoutManager.HORIZONTAL, false));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
/// ///////////////////////////////////////////////////////////////////////////////////////////////Movie cast ends
/// ///////////////////////////////////////////////////////////////////////////////////////////////rating button ends

} // main class ends.
