package com.example.paras.imdb;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.Toast;

// class used to access the device's database to store the list of favourite and watchlist movies.
// this class extends the SQLiteOpenHelper class.
public class MyDatabaseHelper extends SQLiteOpenHelper {

    // final variables that are used in the tables.
    public static final String TAG = "paras database";
    private static final String DATABASE_NAME = "UserDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "FavouriteAndWatchlist";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_PATH = "poster_path";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_AVERAGE = "votes_average";
    public static final String VOTE_COUNT = "vote_count";
    public static final String RAW_DETAILS = "raw_details";
    public static final String IS_FAVOURITE = "is_favourite";
    public static final String IS_WATCHLIST = "is_watchlist";

    // final strings that creates the table and delete teh table.
    public static final String CREATE_TABLE = " create table " + TABLE_NAME + " ("+ID+" integer primary key autoincrement unique, "+TITLE+" varchar(255), "+RELEASE_DATE+" varchar(255), "+POSTER_PATH+" varchar(255), "+POPULARITY+" real, "+VOTE_AVERAGE+" real, "+VOTE_COUNT+" integer, "+RAW_DETAILS+" varchar(255), "+IS_FAVOURITE+" integer, "+IS_WATCHLIST+" integer);";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME+";";
    public static Context dbContext;

    //constructor to set the database name, database version, factory value, and context.
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.dbContext = context;
        Log.i(TAG, " Constructor called !!");
    }

    // overridden method of the SQLiteOpenHelper class that is called on create of the database.
    // here i used to create the table.
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Log.i(TAG, "inside create");
        }catch (SQLException e){
            Log.i(TAG, "ERR");
        }
    }

    // overridden method of the SQLiteOpenHelper class that is called on upgrade of the database.
    // here i deleted the old table and created a new table.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            Log.i(TAG, "inside upgrade");
            onCreate(db);
        }catch (SQLException e){
            Log.i(TAG, "ERR");
        }
    }
}
