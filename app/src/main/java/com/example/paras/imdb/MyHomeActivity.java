package com.example.paras.imdb;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// the main activity called after the main splash screen is shown.
// this class implements the DataTransferInterface and overrides the method that recieves the data in string type.
public class MyHomeActivity extends AppCompatActivity implements DataTransferInterface {

    // setting the different parts of the url and later joining them as per requirements.
    public final String BASE_URL = "http://api.themoviedb.org/";
    public final String API_VERSION = "3/";
    public final String QUESTION_MARK = "?";
    public final String API = "api_key=";
    public String API_KEY = "8496be0b2149805afa458ab8ec27560c";

    // creating the appropriate urls of the types of movies.
    public String mostPopularMovieUrl = BASE_URL + API_VERSION + "movie/popular" + QUESTION_MARK + API + API_KEY;
    public String upcomingMovieUrl = BASE_URL + API_VERSION + "movie/upcoming" + QUESTION_MARK + API + API_KEY;
    public String latestMovieUrl = BASE_URL + API_VERSION + "movie/latest" + QUESTION_MARK + API + API_KEY;
    public String nowPlayingMovieUrl = BASE_URL + API_VERSION + "movie/now_playing" + QUESTION_MARK + API + API_KEY;
    public String topRatedMovieUrl = BASE_URL + API_VERSION + "movie/top_rated" + QUESTION_MARK + API + API_KEY;


    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar myProgressRound;
    String idOfRowClicked;
    ArrayList<MyMovieDetails> myArrayList;
    String currentSearchedUrl;
    LinearLayout noDataLinearLayout;
    LinearLayout noMoviesLinearLayout;
    View noDataFoundView;
    View noMovieFoundView;
    Toolbar myToolbar;
    FrameLayout toolbarFrameLayout;
    CoordinatorLayout myHomeCoordinatorLayout;
    Context context = this;
    private long back_pressed;
    public static int watchlistSnackbarVisited = 0;
    public static int favouriteSnackbarVisited = 0;

    int prevId = 0;
    int flag = 0;

    ///////////////////////////////////////////////////////////////////////////////////// onCreate starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);

        // initializing the view elements.
        listView = (ListView) findViewById(R.id.mainListView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainListViewSwipeRefresh);
        myProgressRound = (ProgressBar) findViewById(R.id.myProgressRound);
        myToolbar = (Toolbar) findViewById(R.id.app_bar);
        myHomeCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.myHomeCoordinatorLayout);

        // setting the toolbar's name and it's margin.
        myToolbar.setTitle("Spark");
        myToolbar.setTitleMargin(100, 0, 0, 0);
        // registering the action bar as the main toolbar.
        setSupportActionBar(myToolbar);

        // inflating the noDataImage to show when internet connection is not there.
        // and no movies found image when movies are not found in the list.
        LayoutInflater inflater = LayoutInflater.from(this);
        noDataLinearLayout = (LinearLayout) findViewById(R.id.noDataImageView);
        noMoviesLinearLayout = (LinearLayout) findViewById(R.id.noMovieImageView);

        // creating the views by inflating the respective layouts.
        noDataFoundView = inflater.inflate(R.layout.no_internet_layout, null, true);
        noMovieFoundView = inflater.inflate(R.layout.no_movie_layout, null, true);

        // attaching the corresponding views to the layouts.
        noDataLinearLayout.addView(noDataFoundView);
        noMoviesLinearLayout.addView(noMovieFoundView);

        // initially on app launch the list of upcoming movies is to be shown
        // so the url to be searched is upcomingMovieUrl.
        currentSearchedUrl = upcomingMovieUrl;
        // method called to show the list of upcoming movies.
        // and a message that welcomes user.
        showSelectedDataOnScreen(upcomingMovieUrl, "Welcome");

        // setting the listener on the click of an item in the list view.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // on item clicked.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // getting the id of the item clicked at the position.
                idOfRowClicked = myArrayList.get(position).getId();
                // intent to movie detail activity and passing the key value pair along with the intent.
                Intent intent = new Intent(MyHomeActivity.this, MyMovieDetailActivity.class);
                intent.putExtra("Key123", idOfRowClicked);
                intent.putExtra("Key321", currentSearchedUrl);
                startActivity(intent);
            }
        });

        // feature the swipe vertical to refresh the page.
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // logic to refresh the page.
                        if (prevId != 0)
                            myMethod(prevId);
                        else {
                            myMethod(R.id.upcomingMovies);
                        }
                        // after executing the task of refresh set the refreshing to false.
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    ///////////////////////////////////////////////////////////////////////////////////// onCreate ends
///////////////////////////////////////////////////////////////////////////////////// connection check starts
// method to check whether the connection is established or not.
    private boolean connectionIsEstablished() {
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
///////////////////////////////////////////////////////////////////////////////////// menu starts
    // method to inflate the menu using the menuLayout created in the menu file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // method called on the selection of any item from the menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // getting the id of the item selected.
        int displayedMovieId = item.getItemId();

        // logic to handle the items selected.
        if (flag == 0) {
            if (displayedMovieId == R.id.refresh) {
                displayedMovieId = R.id.upcomingMovies;
            }
            prevId = displayedMovieId;
        }

        // switch case to do something on selection of respective items.
        switch (displayedMovieId) {

            case (R.id.myFavourite):
                //todo when myFavourite is clicked in menu item.
                Intent favouriteIntent = new Intent(MyHomeActivity.this, ShowFavouriteMoviesActivity.class);
                startActivity(favouriteIntent);
                Toast.makeText(this, "My Favourite", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.myWatchList):
                //todo when addToWatchlist is clicked in menu item.
                Intent watchlistIntent = new Intent(MyHomeActivity.this, ShowWatchlistMoviesActivity.class);
                startActivity(watchlistIntent);
                Toast.makeText(this, "My WatchList", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.refresh):
                //todo: refresh the fetching data from api not the activity on menu item click.
                //Toast.makeText(this, "Refresing...", Toast.LENGTH_SHORT).show();
                myMethod(prevId);

                break;

            default:
                prevId = displayedMovieId;
                flag = 1;
                myMethod(displayedMovieId);
                break;
        }

        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected ends.

    ///////////////////////////////////////////////////////////////////////////////////////////menu ends
///////////////////////////////////////////////////////////////////////////////////////////my method
    public void myMethod(int displayedMovieId) {

        switch (displayedMovieId) {

            case (R.id.mostPopular):
                currentSearchedUrl = mostPopularMovieUrl;
                showSelectedDataOnScreen(mostPopularMovieUrl, "Most Popular Movies");
                break;

            case (R.id.upcomingMovies):
                currentSearchedUrl = upcomingMovieUrl;
                showSelectedDataOnScreen(upcomingMovieUrl, "Upcoming Movies");
                break;

            case (R.id.latestMovies):
                currentSearchedUrl = latestMovieUrl;
                showSelectedDataOnScreen(latestMovieUrl, "Latest Movies");
                break;

            case (R.id.nowPlaying):
                currentSearchedUrl = nowPlayingMovieUrl;
                showSelectedDataOnScreen(nowPlayingMovieUrl, "Now Playing Movies");
                break;

            case (R.id.topRated):
                currentSearchedUrl = topRatedMovieUrl;
                showSelectedDataOnScreen(topRatedMovieUrl, "Top Rated Movies");
                break;

        }// switch case ends.
    }
///////////////////////////////////////////////////////////////////////////////////////////my method ends
///////////////////////////////////////////////////////////////////////////////////////////open selected url

    // method that show the data on screen.
    // if internet is connected then fetch data else show connectivity error message.
    public void showSelectedDataOnScreen(String URL, String message) {
        if (connectionIsEstablished()) {
            noDataLinearLayout.setVisibility(View.INVISIBLE);


            FetchData fetchData = new FetchData(MyHomeActivity.this, URL, this, listView, myProgressRound);
            fetchData.execute();
            Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
        } else {
            //todo: 15/6/2018
            final Snackbar snackbar = Snackbar.make(myHomeCoordinatorLayout, "Internet Connection Needed", Snackbar.LENGTH_LONG);
            snackbar.setAction("CONNECT WIFI", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo: connect to wifi here.
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    Snackbar.make(myHomeCoordinatorLayout, "Wifi Enabled.\nPull down to Refresh.", Snackbar.LENGTH_SHORT).show();
                   // Snackbar.make(myHomeCoordinatorLayout, "Wifi Enabled.", Snackbar.LENGTH_SHORT).show();

                }
            });
            snackbar.show();

            myProgressRound.setVisibility(View.INVISIBLE);
            noDataLinearLayout.setVisibility(View.VISIBLE);
            noMoviesLinearLayout.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            //Toast.makeText(this, "Check Internet Connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////open selected url ends
//////////////////////////////////////////////////////////////////////////////// data recieved from async task
    // the overridden method of the dataTransfer interface where the data is kept after getting from the api.
    @Override
    public void dataHere(String data) {

        try {

            myArrayList = new ArrayList<>();
            JSONObject parentObject = new JSONObject(data);

            // if the data of results is not there in the string extracted then
            //  no data image should display.
            // else the json array from the data is parsed.
            if (!parentObject.has("results")) {
                myArrayList.clear();

                listView.setVisibility(View.GONE);
                noDataLinearLayout.setVisibility(View.INVISIBLE);
                noMoviesLinearLayout.setVisibility(View.VISIBLE);


                Toast.makeText(this, "Sorry!! No Available movies", Toast.LENGTH_SHORT).show();
            } else {

                listView.setVisibility(View.VISIBLE);
                noDataLinearLayout.setVisibility(View.GONE);
                noMoviesLinearLayout.setVisibility(View.GONE);

                JSONArray array = parentObject.getJSONArray("results");

                // for each array element the details will be extracted in strings
                // add the data in the array list.
                // use the array adapter to show the data in the list view.
                for (int i = 0; i < array.length(); i++) {
                    JSONObject childObject = array.getJSONObject(i);

                    String id = childObject.getString("id");
                    String poster_path = childObject.getString("poster_path");
                    String title = childObject.getString("title");
                    String release_date = childObject.getString("release_date");

                    float popRating = 0;
                    popRating = Float.parseFloat(childObject.getString("popularity")) / 100;
                    if (popRating > 5) {
                        popRating = 5;
                    }
                    String vote_average = childObject.getString("vote_average");
                    String vote_count = childObject.getString("vote_count");

                    myArrayList.add(new MyMovieDetails(id, poster_path, title, release_date, popRating, vote_average, vote_count));
                }//for loop ends.
                ArrayAdapter<MyMovieDetails> adapter = new MyListAdapter(this, R.layout.list_view_layout, myArrayList);
                listView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }// data here ends

    ///////////////////////////////////////////////////////////////////////////////////// data ends
    // method for on Back press override, user need to press twice back to exit.
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {     // wait for 2 seconds until the back button is pressed again.
            super.onBackPressed();
            Toast.makeText(getBaseContext(), "Thank you for using my App!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}//class ends
