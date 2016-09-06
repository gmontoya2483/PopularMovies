package com.example.montoya.popularmoviesstg2.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by montoya on 17.08.2016.
 */
public class PopularMoviesDbHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG=PopularMoviesDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="p_movies.db";


    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_MOVIES_TABLE=
                "CREATE TABLE "+ PopularMoviesContract.MoviesEntry.TABLE_NAME +
                " ("+
                        PopularMoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY, "+
                        PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE + " TEXT NOT NULL, "+
                        PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL + " TEXT NOT NULL, "+
                        PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS + " TEXT NOT NULL, "+
                        PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING + " TEXT NOT NULL, "+
                        PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL "+
                ") ";


        final String SQL_CREATE_FAVORITES_TABLE=
                "CREATE TABLE "+ PopularMoviesContract.FavoritesEntry.TABLE_NAME +
                        " ("+
                        PopularMoviesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY, "+
                        PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_TITLE + " TEXT NOT NULL, "+
                        PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL + " TEXT NOT NULL, "+
                        PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_SYSNOPSIS + " TEXT NOT NULL, "+
                        PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_USER_RATING + " TEXT NOT NULL, "+
                        PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL "+
                        ") ";

        final String SQL_CREATE_VIDEOS_TABLE=
                "CREATE TABLE "+ PopularMoviesContract.VideosEntry.TABLE_NAME +
                        " ("+
                        PopularMoviesContract.VideosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                        PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID+" INTEGER NOT NULL, "+
                        PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY + " TEXT UNIQUE NOT NULL, "+
                        PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME + " TEXT NOT NULL, "+
                        PopularMoviesContract.VideosEntry.COLUMN_VIDEO_SITE+" TEXT NOT NULL,"+
                        PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE + " TEXT NOT NULL "+
                        ") ";







        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_VIDEOS_TABLE);
        //db.execSQL("CREATE TABLE sqlite_sequence(name,seq)");








    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(LOG_TAG, "Upgrading database from version "+ oldVersion + " to "+newVersion+". OLD DATA WILL BE DESTROYED");


        db.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.FavoritesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.VideosEntry.TABLE_NAME);


        //RESET VIDEO COUNTER
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"+PopularMoviesContract.VideosEntry.TABLE_NAME+"'");




        onCreate(db);

    }

}
