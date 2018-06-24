package com.example.paras.imdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

// an activity that shows the list of movies that the user has added to his favourites.
public class ShowFavouriteMoviesActivity extends AppCompatActivity {

    public static MyDatabaseHelper myDatabaseHelper;
    List<MyMovieDetails> myFavouriteList;
    ShowWatchlistMoviesAdapter myAdapter;
    ListView myListView;
    SQLiteDatabase db;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favourite_movies);
        // initializing the coordinator layout mainly used for the snackbar.
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.favListCoordinatorLatyout);
        // logic to avoid showing the snackbar multiple times in one application launch.
        if (MyHomeActivity.favouriteSnackbarVisited == 0) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Long press to add/remove", Snackbar.LENGTH_LONG);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(coordinatorLayout, "Movie can be removed from favourites\nMovies can be added to watchlist.", Snackbar.LENGTH_LONG).show();
                    MyHomeActivity.favouriteSnackbarVisited = 1;
                }
            });
            snackbar.show();
        }


        myDatabaseHelper = new MyDatabaseHelper(this);
        myListView = (ListView) findViewById(R.id.myListViewFavourite);
        showDataOfFavouriteList();
        // reagistering the listview created to show the data for context menu
        // that is triggered when the list item is clicked for long press.
        registerForContextMenu(myListView);

        // setting the custom toolbar for the favourite activity and setting its title and upNavigation to home activity.
        toolbar = (Toolbar) findViewById(R.id.app_bar_favourite);
        toolbar.setTitle("My Favourite");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    // method created to show the details of the movie that is added earlier to the database.
    public void showDataOfFavouriteList() {

        db = myDatabaseHelper.getWritableDatabase();
        String[] column = {
                MyDatabaseHelper.ID,
                MyDatabaseHelper.TITLE,
                MyDatabaseHelper.RELEASE_DATE,
                MyDatabaseHelper.POSTER_PATH,
                MyDatabaseHelper.POPULARITY,
                MyDatabaseHelper.VOTE_AVERAGE,
                MyDatabaseHelper.VOTE_COUNT,
                MyDatabaseHelper.RAW_DETAILS,
                MyDatabaseHelper.IS_FAVOURITE
        };

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, column, null, null, null, null, null);
        myFavouriteList = new ArrayList<>();

        int i1 = cursor.getColumnIndex(MyDatabaseHelper.ID);
        int i2 = cursor.getColumnIndex(MyDatabaseHelper.TITLE);
        int i3 = cursor.getColumnIndex(MyDatabaseHelper.RELEASE_DATE);
        int i4 = cursor.getColumnIndex(MyDatabaseHelper.POSTER_PATH);
        int i5 = cursor.getColumnIndex(MyDatabaseHelper.POPULARITY);
        int i6 = cursor.getColumnIndex(MyDatabaseHelper.VOTE_AVERAGE);
        int i7 = cursor.getColumnIndex(MyDatabaseHelper.VOTE_COUNT);
        int i8 = cursor.getColumnIndex(MyDatabaseHelper.RAW_DETAILS);
        int i9 = cursor.getColumnIndex(MyDatabaseHelper.IS_FAVOURITE);


        cursor.moveToFirst();
        cursor.moveToPrevious();
        while (cursor.moveToNext()) {
            if (cursor.getInt(i9) == 1) {
                String id = cursor.getString(i1);
                String title = cursor.getString(i2);
                String release_date = cursor.getString(i3);
                String poster_path = cursor.getString(i4);
                String popularity = cursor.getString(i5);
                String votes_avg = cursor.getString(i6);
                String votes_count = cursor.getString(i7);

                // adding the details of the movie from the database to the arraylist.
                myFavouriteList.add(new MyMovieDetails(id, poster_path, title, release_date, Float.parseFloat(popularity), votes_avg, votes_count));
            }
        }

        // if arraylist is empty then prompt the user that the list is empty.
        if (myFavouriteList.isEmpty()) {
            Toast.makeText(this, "Empty Favourites!!", Toast.LENGTH_SHORT).show();
        }
        // creating the instance of the adapter by inflating the layout.
        myAdapter = new ShowWatchlistMoviesAdapter(MyDatabaseHelper.dbContext, R.layout.list_view_layout, myFavouriteList);
        // attaching the adapter to the arraylist.
        myListView.setAdapter(myAdapter);
        db.close();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.myListViewFavourite) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list_context_menu_favourite, menu);
        }
    }

    // overridden method called when the item of the context menu is clicked.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int index = info.position;
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        MyMovieDetails myMovieDetails2 = myFavouriteList.get(index);
        String movieId = myMovieDetails2.getId();

        switch (item.getItemId()) {
            // switch case when the id of the button to remove from favourite is clicked.
            case R.id.removeFavourite:
                // add stuff here
                SQLiteDatabase myDb = myDatabaseHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(MyDatabaseHelper.IS_FAVOURITE, 0);
                String[] whereArgs = {movieId};
                myDb.update(MyDatabaseHelper.TABLE_NAME, cv, MyDatabaseHelper.ID + " =? ", whereArgs);
                showDataOfFavouriteList();
                myDb.close();
                return true;
            // switch case when the id of the button to add to watchlist is clicked.
            case R.id.addToWat:
                SQLiteDatabase myDb2 = myDatabaseHelper.getWritableDatabase();
                ContentValues cv2 = new ContentValues();
                cv2.put(MyDatabaseHelper.IS_WATCHLIST, 1);
                String[] whereArgs2 = {movieId};
                myDb2.update(MyDatabaseHelper.TABLE_NAME, cv2, MyDatabaseHelper.ID + " =? ", whereArgs2);
                showDataOfFavouriteList();
                myDb2.close();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
