package com.example.montoya.popularmoviesstg2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

import java.util.ArrayList;

/**
 * Created by montoya on 05.05.2016.
 */
public class Movie {
    private long id;
    private String title;
    private String imageThumbnail;
    private String sysnopsis;
    private String userRating;
    private String releaseDate;



    public Movie(long id, String title, String imageThumbnail, String sysnopsis, String userRating, String releaseDate) {
        this.id=id;
        this.title = title;
        this.imageThumbnail = imageThumbnail;
        this.sysnopsis = sysnopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }


    // This constructor receives a Movie ID andthe context and creates a Movie Objects from the database
    // If the user ID is not found in the database it set id= -1L
    public Movie(Context context, long id,String path){

        Cursor cursor;
        Uri uriMovieByID;

        switch (path){
            case PopularMoviesContract.PATH_MOVIES:
                uriMovieByID=PopularMoviesContract.MoviesEntry.buildMoviebyIdUri(id);
                cursor=context.getContentResolver().query(uriMovieByID,null,null,null,null);
                break;
            case PopularMoviesContract.PATH_FAVORITES:
                uriMovieByID=PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(id);
                cursor=context.getContentResolver().query(uriMovieByID,null,null,null,null);
                break;
            default:
                cursor=null;
                break;


        }



        if (cursor==null ||cursor.getCount()!=1){
            this.id=-1L;
            this.title=null;
            this.imageThumbnail=null;
            this.sysnopsis=null;
            this.userRating=null;
            this.releaseDate=null;

        }else{
            cursor.moveToFirst();
            this.id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID));
            this.title=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE));
            this.imageThumbnail=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL));
            this.sysnopsis=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS));
            this.userRating=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING));
            this.releaseDate=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE));

        }

        if (cursor!=null){
            cursor.close();
        }



    }


    //this methods creates a Movie Object from a Cursor
    // If the cursor has more than 1 records or the cursor is empty its returns a Movie id=-1L.
    public Movie (Cursor cursor) {

        this.id=-1L;
        this.title=null;
        this.imageThumbnail=null;
        this.sysnopsis=null;
        this.userRating=null;
        this.releaseDate=null;

        if (cursor.getCount() == 1 && cursor.moveToFirst()) {

            this.id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID));
            this.title=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE));
            this.imageThumbnail=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL));
            this.sysnopsis=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS));
            this.userRating=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING));
            this.releaseDate=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE));


        }



    }




    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public String getSysnopsis() {
        return sysnopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Boolean getIsFavorite(Context context){
        boolean isFavorite=false;
        Uri movieUri=PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(this.id);
        Cursor cursor;

        cursor=context.getContentResolver().query(movieUri,null,null,null,null);
        if (cursor!=null && cursor.getCount()==1){
            isFavorite=true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return isFavorite;
    }


    public void setToFavorite(Context context){

        Uri InsertFavoriteUri=PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri();
        if (!getIsFavorite(context)){
            Uri inserted=context.getContentResolver().insert(InsertFavoriteUri, this.getMovieValues());

        }else{
            PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(this.getId());
        }



    }


    public void removeFromFavorite(Context context){
        Uri movieUri=PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(this.id);

        if(getIsFavorite(context)){
            int delete = context.getContentResolver().delete(movieUri, null, null);
        }

    }




    public ContentValues getMovieValues (){

        ContentValues values=new ContentValues();
        values.put(PopularMoviesContract.MoviesEntry._ID,this.getId());
        values.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE,this.getTitle());
        values.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL,this.getImageThumbnail());
        values.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS,this.getSysnopsis());
        values.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING,this.getUserRating());
        values.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE,this.getReleaseDate());

        return values;

    }


    public Uri insertMovie(Context context){

        Uri insertedMovieUri;
        Uri allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();

        if (verifyAttributes()){
            insertedMovieUri=context.getContentResolver().insert(allMoviesUri,this.getMovieValues());
        }else{

            insertedMovieUri=null;
        }

        return insertedMovieUri;

    }



    private boolean verifyAttributes(){

        boolean isOK=true;

        if (this.getTitle()== null){
            isOK=false;
        }

        if (this.getImageThumbnail()== null){
            isOK=false;
        }

        if (this.getReleaseDate()== null){
            isOK=false;
        }

        if (this.getUserRating()== null){
            isOK=false;
        }

        if (this.getSysnopsis()== null){
            isOK=false;
        }


        return isOK;
    }


    public boolean isEqual(Movie movieToCompare){
        boolean areEqual=true;

        if(this.getId()!=movieToCompare.getId()){
            areEqual=false;
        }

        if (this.getTitle()== movieToCompare.getTitle()){
            areEqual=false;
        }

        if (this.getImageThumbnail()== movieToCompare.getImageThumbnail()){
            areEqual=false;
        }

        if (this.getReleaseDate()== movieToCompare.getReleaseDate()){
            areEqual=false;
        }

        if (this.getUserRating()== movieToCompare.getUserRating()){
            areEqual=false;
        }

        if (this.getSysnopsis()== movieToCompare.getSysnopsis()){
            areEqual=false;
        }


        return areEqual;
    }



    public static int bulkInsertMovies (Context context,ArrayList<Movie> movies){
        int quantityOfInsertedMovies=0;
        int quantityOfMovies=movies.size();
        Uri allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();

        ContentValues values[]=new ContentValues[quantityOfMovies];

        //generate the Content values Array
        int i=0;
        for (Movie movie: movies) {
            values[i]=movie.getMovieValues();
            i++;
        }

        quantityOfInsertedMovies=context.getContentResolver().bulkInsert(allMoviesUri,values);

        return quantityOfInsertedMovies;

    }


    public static Cursor getAllMovies (Context context){
        Cursor cursor;
        Uri allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();

        cursor=context.getContentResolver().query(allMoviesUri,null,null,null,null);

        return cursor;
    }


    public static Cursor getAllFavorites (Context context){
        Cursor cursor;
        Uri allFavoritesUri=PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri();
        cursor=context.getContentResolver().query(allFavoritesUri,null,null,null,null);

        return cursor;
    }




    public static int deleteAllMovies(Context context){
        int quantityOfDeletedMovies=0;
        Uri allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();


        quantityOfDeletedMovies=context.getContentResolver().delete(allMoviesUri,null,null);




        return quantityOfDeletedMovies;
    }



    public static ArrayList<Movie> getAllMoviesArrayList (Context context,String path){
        ArrayList<Movie> movieArrayList=new ArrayList<Movie>();
        Cursor cursor;


        switch (path){
            case PopularMoviesContract.PATH_MOVIES:
                cursor=getAllMovies(context);
                break;
            case PopularMoviesContract.PATH_FAVORITES:
                cursor=getAllFavorites(context);
                break;
            default:
                cursor=null;
                break;

        }


        if (cursor!=null){
            while (cursor.moveToNext()){
                movieArrayList.add(new Movie(
                        cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL)),
                        cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS)),
                        cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING)),
                        cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE))

                ));


            }

        }else{
            movieArrayList=null;
        }

        return movieArrayList;

    }
















}
