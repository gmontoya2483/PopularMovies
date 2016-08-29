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
    public Movie(Context context, long id){

        Cursor cursor;
        Uri uriMovieByID=PopularMoviesContract.MoviesEntry.buildMoviebyIdUri(id);
        cursor=context.getContentResolver().query(uriMovieByID,null,null,null,null);

        if (cursor.getCount()!=1){
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

        cursor.close();

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



    public ContentValues getMovieValues (){

        ContentValues testValues=new ContentValues();
        testValues.put(PopularMoviesContract.MoviesEntry._ID,this.getId());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE,this.getTitle());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL,this.getImageThumbnail());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS,this.getSysnopsis());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING,this.getUserRating());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE,this.getReleaseDate());

        return testValues;

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


    public static int deleteAllMovies(Context context){
        int quantityOfDeletedMovies=0;
        Uri allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();


        quantityOfDeletedMovies=context.getContentResolver().delete(allMoviesUri,null,null);




        return quantityOfDeletedMovies;
    }



    public static ArrayList<Movie> getAllMoviesArrayList (Context context){
        ArrayList<Movie> movieArrayList=new ArrayList<Movie>();
        Cursor cursor;


        cursor=getAllMovies(context);

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


        return movieArrayList;
    }






}
