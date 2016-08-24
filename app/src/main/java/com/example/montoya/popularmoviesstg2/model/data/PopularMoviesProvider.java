package com.example.montoya.popularmoviesstg2.model.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by montoya on 17.08.2016.
 */
public class PopularMoviesProvider extends ContentProvider {

    private static final String LOG_TAG=PopularMoviesProvider.class.getSimpleName();
    private static final UriMatcher mUriMatcher=buildUriMatcher();
    private PopularMoviesDbHelper mPopularMoviesDbHelper;


    //Codes for the UriMatcher
    public static final int MOVIE=100;
    public static final int MOVIE_WITH_ID=110;




    public static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIES + "/*", MOVIE_WITH_ID);
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIES, MOVIE);








        return matcher;

    }



    @Override
    public boolean onCreate() {

        mPopularMoviesDbHelper=new PopularMoviesDbHelper(getContext());
        return true;
    }



    @Override
    public String getType(Uri uri) {

        final int match = mUriMatcher.match(uri);

        switch (match) {

            case MOVIE_WITH_ID:
                return PopularMoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return PopularMoviesContract.MoviesEntry.CONTENT_DIR_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri + " - match value: "+match);
        }


    }




    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        final int match=mUriMatcher.match(uri);

        switch (match){
            case MOVIE:
            {
                String id=String.valueOf(ContentUris.parseId(uri));
                retCursor=queryMovieFromDbById(id);
                break;

            }
            case MOVIE_WITH_ID:
            {

                String id=String.valueOf(ContentUris.parseId(uri));
                retCursor=queryMovieFromDbById(id);

                break;
            }




            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);



        }
        return retCursor;
    }






    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri insertedUri=null;
        final int match=mUriMatcher.match(uri);
        Log.e(LOG_TAG, "Expected uri:"+ match);

        switch (match){
            case MOVIE:
                insertedUri=insertMovieIntoDb(values);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);

        }




        return insertedUri;
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



    private Uri insertMovieIntoDb(ContentValues values){

        Uri insertedMovieUri=null;
        Long insertedMovieId;


        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        if (db.isOpen()){
            insertedMovieId=db.insert(PopularMoviesContract.MoviesEntry.TABLE_NAME,null,values);
            if(insertedMovieId!=-1){

                insertedMovieUri=PopularMoviesContract.MoviesEntry.buildMoviebyIdUri(insertedMovieId);


            }else{
                insertedMovieUri=null;
                Log.e(LOG_TAG,"Movie could not be inserted");
            }
            db.close();

        }else{
            insertedMovieUri=null;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return insertedMovieUri;


    }




    private Cursor queryMovieFromDbById(String id){

        Cursor cursor;

        SQLiteDatabase db=mPopularMoviesDbHelper.getReadableDatabase();
        if (db.isOpen()){
            cursor=db.query(PopularMoviesContract.MoviesEntry.TABLE_NAME,
                    null,
                    PopularMoviesContract.MoviesEntry._ID + " = ?",
                    new String[]{id},
                    null,null,null);
            db.close();
        }else{
            cursor=null;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return cursor;


    }








}
