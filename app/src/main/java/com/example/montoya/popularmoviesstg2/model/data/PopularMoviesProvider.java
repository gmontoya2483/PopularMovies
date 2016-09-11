package com.example.montoya.popularmoviesstg2.model.data;

import android.annotation.TargetApi;
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
    public static final int FAVORITE=200;
    public static final int FAVORITE_WITH_ID=210;
    public static final int VIDEO=300;
    public static final int VIDEO_WITH_MOVIE_ID=310;
    public static final int VIDEO_WITH_KEY=320;





    public static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIES, MOVIE);

        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITES + "/#",FAVORITE_WITH_ID);
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITES, FAVORITE);

        matcher.addURI(authority, PopularMoviesContract.PATH_VIDEOS + "/*",VIDEO_WITH_KEY);
        matcher.addURI(authority, PopularMoviesContract.PATH_VIDEOS, VIDEO);
        matcher.addURI(authority, PopularMoviesContract.PATH_VIDEOS +"_"+PopularMoviesContract.PATH_MOVIES+"/#",VIDEO_WITH_MOVIE_ID);



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

            case FAVORITE_WITH_ID:
                return PopularMoviesContract.FavoritesEntry.CONTENT_ITEM_TYPE;
            case FAVORITE:
                return PopularMoviesContract.FavoritesEntry.CONTENT_DIR_TYPE;
            case VIDEO:
                return PopularMoviesContract.VideosEntry.CONTENT_DIR_TYPE;
            case VIDEO_WITH_KEY:
                return PopularMoviesContract.VideosEntry.CONTENT_ITEM_TYPE;
            case VIDEO_WITH_MOVIE_ID:
                return PopularMoviesContract.VideosEntry.CONTENT_DIR_TYPE;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


    }




    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor=null;
        final int match=mUriMatcher.match(uri);

        switch (match){
            case MOVIE:
            {
                retCursor=mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            }
            case MOVIE_WITH_ID:
            {
               String movie_id= String.valueOf(ContentUris.parseId(uri));
                retCursor=queryMovieById(movie_id);
                break;
            }

            case FAVORITE:
                retCursor=mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FAVORITE_WITH_ID:
            {
                String movie_id= String.valueOf(ContentUris.parseId(uri));
                retCursor=queryFavoriteById(movie_id);
                break;
            }



            case VIDEO:
                retCursor=mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.VideosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;


            case VIDEO_WITH_KEY:
                selectionArgs= new String[] {uri.getLastPathSegment().toString()};
                selection=PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY+"=?";

                retCursor=mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.VideosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;


            case VIDEO_WITH_MOVIE_ID:
                String movie_id=String.valueOf(ContentUris.parseId(uri));
                selection=PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID+"=?";
                selectionArgs= new String[] {String.valueOf(ContentUris.parseId(uri))};
                retCursor=mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.VideosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;



            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //Set the notification URI for our Cursor and register a content observer to watch changes to the URI
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }






    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri insertedUri=null;
        final int match=mUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                insertedUri=insertMovie(values);
                break;


            case FAVORITE:
                insertedUri=insertFavorite(values);
                break;

            case VIDEO:
                insertedUri=insertVideo(values);
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);

        }

        //Notify changes to the registered observers !!
        if (insertedUri!=null){

            getContext().getContentResolver().notifyChange(uri, null);
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

            case FAVORITE_WITH_ID:
                String movie_id= String.valueOf(ContentUris.parseId(uri));
                rowsDeleted=deleteFavorite(movie_id);
                break;

            case VIDEO:
                rowsDeleted=deleteVideos();
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted>0){
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return rowsDeleted;

    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {


        final int match = mUriMatcher.match(uri);
        int rowsInserted = 0;

        switch (match) {
            case MOVIE:
                rowsInserted=bulkInsertMovies(values);

                break;
            case VIDEO:
                rowsInserted=bulkInsertVideos(values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        if (rowsInserted>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;


    }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mPopularMoviesDbHelper.close();
        super.shutdown();
    }


    //Helper methods stars here
    //
    //

    private Uri insertMovie(ContentValues values){

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


        }else{
            insertedMovieUri=null;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return insertedMovieUri;


    }


    private Cursor queryMovieById(String id){

        Cursor cursor;

        SQLiteDatabase db=mPopularMoviesDbHelper.getReadableDatabase();
        if (db.isOpen()){

            String SQLStatment="SELECT * from "+PopularMoviesContract.MoviesEntry.TABLE_NAME+" WHERE _id="+id;

            cursor=db.rawQuery(SQLStatment,null);

        }else{
            cursor=null;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return cursor;


    }


    private int bulkInsertMovies(ContentValues[] values){

        int rowsInserted = 0;
        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
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

        return rowsInserted;


    }



    /**
     * Helper Method for Managen the FAVORITES TABLE
     *
    * */


    private Cursor queryFavoriteById (String id){

        Cursor cursor;

        SQLiteDatabase db=mPopularMoviesDbHelper.getReadableDatabase();
        if (db.isOpen()){

            String SQLStatment="SELECT * from "+PopularMoviesContract.FavoritesEntry.TABLE_NAME+" WHERE _id="+id;

            cursor=db.rawQuery(SQLStatment,null);

        }else{
            cursor=null;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return cursor;


    }



    private Uri insertFavorite(ContentValues values){

        Uri insertedMovieUri=null;
        Long insertedMovieId;


        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        if (db.isOpen()){
            insertedMovieId=db.insert(PopularMoviesContract.FavoritesEntry.TABLE_NAME,null,values);
            if(insertedMovieId!=-1){

                insertedMovieUri=PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(insertedMovieId);


            }else{
                insertedMovieUri=null;
                Log.e(LOG_TAG,"Movie could not be inserted");
            }


        }else{
            insertedMovieUri=null;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return insertedMovieUri;


    }


    private int deleteFavorite(String id){
        int deletedRecords=-1;

        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        if (db.isOpen()){

            deletedRecords=db.delete(PopularMoviesContract.FavoritesEntry.TABLE_NAME,PopularMoviesContract.FavoritesEntry._ID+"=?",new String[]{id});


            if(deletedRecords!=1){

                Log.e(LOG_TAG,"There was a problem deleting the Movie from the favorite list.");


            }
        }else{
            Log.e(LOG_TAG,"database could not be opened");
        }



        return  deletedRecords;

    }


/**
 * Helper Method for Manage the VIDEO TABLE
 *
 * */


    private Uri insertVideo(ContentValues values){

        Uri insertedVideoUri=null;
        Long insertedVideoId;


        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        if (db.isOpen()){
            insertedVideoId=db.insert(PopularMoviesContract.VideosEntry.TABLE_NAME,null,values);
            if(insertedVideoId!=-1){

                String key=values.getAsString(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY);
                insertedVideoUri=PopularMoviesContract.VideosEntry.buildVideosByKeyUri(key);


            }else{
                insertedVideoUri=null;
                Log.e(LOG_TAG,"Movie could not be inserted");
            }


        }else{
            insertedVideoUri=null;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return insertedVideoUri;


    }





    private int bulkInsertVideos(ContentValues[] values){

        int rowsInserted = 0;
        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues value : values) {

                long _id=db.insert(PopularMoviesContract.VideosEntry.TABLE_NAME,null,value);
                if (_id!=-1){
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return rowsInserted;


    }



    private int deleteVideos(){
        int deletedVideos;

        //We ensure that movie is not part of the favorite List
        String sqlWhere= PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID
                + " NOT IN (SELECT "+PopularMoviesContract.FavoritesEntry._ID
                                +" FROM "+PopularMoviesContract.FavoritesEntry.TABLE_NAME+")";

        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        if (db.isOpen()){
            deletedVideos=db.delete(PopularMoviesContract.VideosEntry.TABLE_NAME,sqlWhere,null);


        }else{
            deletedVideos=-1;
            Log.e(LOG_TAG,"database could not be opened");
        }


        return deletedVideos;


    }







}
