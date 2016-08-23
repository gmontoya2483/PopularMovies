package com.example.montoya.popularmoviesstg2.model.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by montoya on 17.08.2016.
 */
public class PopularMoviesProvider extends ContentProvider {

    private static final String LOG_TAG=PopularMoviesProvider.class.getSimpleName();
    private static final UriMatcher mUriMatcher=buildUriMatcher();
    private PopularMoviesDbHelper mPopularMoviesDbHelper;


    //Codes for the UriMatcher
    private static final int MOVIE=100;
    private static final int MOVIE_WITH_ID=110;




    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIES+"/#", MOVIE_WITH_ID);


        return matcher;

    }



    @Override
    public boolean onCreate() {

        mPopularMoviesDbHelper=new PopularMoviesDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (mUriMatcher.match(uri)){
            case MOVIE:
            {
                retCursor= mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            }
            case MOVIE_WITH_ID:
            {
                retCursor= mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        PopularMoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);

                //TODO probar un SQL Statment directmante.



                break;
            }




            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        return retCursor;
    }




    @Override
    public String getType(Uri uri) {

        final int match = mUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE_WITH_ID:
                return PopularMoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return PopularMoviesContract.MoviesEntry.CONTENT_DIR_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=mPopularMoviesDbHelper.getWritableDatabase();
        final int match=mUriMatcher.match(uri);
        int rowsDeleted=0;

        switch (match){
            case MOVIE:
                rowsDeleted=db.delete(PopularMoviesContract.MoviesEntry.TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsDeleted;

    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsInserted = 0;

        switch (match) {
            case MOVIE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id=db.insert(PopularMoviesContract.MoviesEntry.TABLE_NAME,null,value);
                        if (_id!=-1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsInserted;


    }






}
