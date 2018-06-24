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

// an activity that shows the list of movies that the user has added to his watchlist.
public class ShowWatchlistMoviesActivity extends AppCompatActivity {


    public static MyDatabaseHelper myDatabaseHelper;
    List<MyMovieDetails> myWatchList;
    ShowWatchlistMoviesAdapter myAdapter;
    ListView myListView;
    SQLiteDatabase db;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watchlist_movies);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.watchListCoordinatorLatyout);
        if (MyHomeActivity.watchlistSnackbarVisited == 0){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Long press to add/remove", Snackbar.LENGTH_LONG);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(coordinatorLayout, "Movie can be removed from watchlist\nMovies can be added to favourites.", Snackbar.LENGTH_LONG).show();
                    MyHomeActivity.watchlistSnackbarVisited = 1;
                }
            });
            snackbar.show();
        }

        myDatabaseHelper = new MyDatabaseHelper(this);
        myListView = (ListView) findViewById(R.id.myListViewWatchList);
        showDataOfWatchlist();
        registerForContextMenu(myListView);

        toolbar = (Toolbar)findViewById(R.id.app_bar_watchlist);
        toolbar.setTitle("My Watchlist");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void showDataOfWatchlist() {


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
                MyDatabaseHelper.IS_WATCHLIST};

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, column, null, null, null, null, null);
        myWatchList = new ArrayList<>();

        int i1 = cursor.getColumnIndex(MyDatabaseHelper.ID);
        int i2 = cursor.getColumnIndex(MyDatabaseHelper.TITLE);
        int i3 = cursor.getColumnIndex(MyDatabaseHelper.RELEASE_DATE);
        int i4 = cursor.getColumnIndex(MyDatabaseHelper.POSTER_PATH);
        int i5 = cursor.getColumnIndex(MyDatabaseHelper.POPULARITY);
        int i6 = cursor.getColumnIndex(MyDatabaseHelper.VOTE_AVERAGE);
        int i7 = cursor.getColumnIndex(MyDatabaseHelper.VOTE_COUNT);
        int i8 = cursor.getColumnIndex(MyDatabaseHelper.RAW_DETAILS);
        int i9 = cursor.getColumnIndex(MyDatabaseHelper.IS_WATCHLIST);


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
                String raw_details = cursor.getString(i8);

                myWatchList.add(new MyMovieDetails(id, poster_path, title, release_date, Float.parseFloat(popularity), votes_avg, votes_count));
            }
        }

        if (myWatchList.isEmpty()) {
            Toast.makeText(this, "Empty WatchList !!", Toast.LENGTH_SHORT).show();
        }
        myAdapter = new ShowWatchlistMoviesAdapter(MyDatabaseHelper.dbContext, R.layout.list_view_layout, myWatchList);
        myListView.setAdapter(myAdapter);
        db.close();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.myListViewWatchList) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list_context_menu_watchlist, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int index = info.position;
        int itemId = item.getItemId();
        if (itemId == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        MyMovieDetails myMovieDetails2 = myWatchList.get(index);
        String movieId = myMovieDetails2.getId();

        switch (item.getItemId()) {
            // switch case when the id of the button to remove from watchlist is clicked.
            case R.id.removeWatchList:
                // add stuff here
                SQLiteDatabase myDb = myDatabaseHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(MyDatabaseHelper.IS_WATCHLIST, 0);
                String[] whereArgs = {movieId};
                myDb.update(MyDatabaseHelper.TABLE_NAME, cv, MyDatabaseHelper.ID + " =? ", whereArgs);
                showDataOfWatchlist();
                return true;

            // switch case when the id of the button to add to favourite is clicked.
            case R.id.addToFav:
                SQLiteDatabase myDb2 = myDatabaseHelper.getWritableDatabase();
                ContentValues cv2 = new ContentValues();
                cv2.put(MyDatabaseHelper.IS_FAVOURITE, 1);
                String[] whereArgs2 = {movieId};
                myDb2.update(MyDatabaseHelper.TABLE_NAME, cv2, MyDatabaseHelper.ID + " =? ", whereArgs2);
                showDataOfWatchlist();
                myDb2.close();
                return true;



            default:
                return super.onContextItemSelected(item);
        }
    }
}
