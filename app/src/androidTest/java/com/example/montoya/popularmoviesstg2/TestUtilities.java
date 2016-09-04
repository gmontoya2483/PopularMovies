package com.example.montoya.popularmoviesstg2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesDbHelper;

import java.util.ArrayList;

/**
 * Created by montoya on 23.08.2016.
 */
public class TestUtilities extends AndroidTestCase{



    final static String BASE_TITLE="Fake Title";
    final static String BASE_SYSNOPSIS="Fake sysnopsis";
    final static String BASE_USR_RATING="Fake user rating";
    final static String BASE_RELEASE_DATE="Fake release date";
    final static String BASE_IMAGE_THUMNAIL="Fake image thumbnail";


    static ContentValues createFakeMovieValues (Movie movie){


        ContentValues testValues=new ContentValues();
        testValues.put(PopularMoviesContract.MoviesEntry._ID,movie.getId());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE,movie.getTitle());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL,movie.getImageThumbnail());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS,movie.getSysnopsis());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING,movie.getUserRating());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE,movie.getReleaseDate());

        return testValues;

    }


    static void VerifyExpectedQueryResults (Movie movie, Cursor cursor){

        int indexId=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID);
        int indexTitle=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE);
        int indexImageThumbnail=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL);
        int indexRelease=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE);
        int indexSysnopsis=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS);
        int indexUserRating=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING);



        assertEquals("Error: Movie ID doesn´t Match", movie.getId(),cursor.getLong(indexId));
        assertEquals("Error: Movie Title doesn´t Match",movie.getTitle(),cursor.getString(indexTitle));
        assertEquals("Error: Movie Image Thumbnail doesn´t Match",movie.getImageThumbnail(),cursor.getString(indexImageThumbnail));
        assertEquals("Error: Movie Release date doesn´t Match",movie.getReleaseDate(),cursor.getString(indexRelease));
        assertEquals("Error: Movie Sysnopsis doesn´t Match",movie.getSysnopsis(),cursor.getString(indexSysnopsis));
        assertEquals("Error: Movie User Rating doesn´t Match",movie.getUserRating(),cursor.getString(indexUserRating));

    }




    static int deleteAllMoviesRecords(Context context){

        Cursor cursor;
        SQLiteDatabase db=new PopularMoviesDbHelper(context).getWritableDatabase();
        int qtyOfDeletedRecords=db.delete(PopularMoviesContract.MoviesEntry.TABLE_NAME,null,null);

        cursor=db.rawQuery("SELECT "+PopularMoviesContract.MoviesEntry._ID+" FROM "+PopularMoviesContract.MoviesEntry.TABLE_NAME,null);
        assertFalse("Error: DeleteAllMovies - No all movie records were deleted",cursor.moveToFirst());

        db.close();

        return qtyOfDeletedRecords;




    }


    static int deleteAllFavoritesRecords(Context context){

        Cursor cursor;
        SQLiteDatabase db=new PopularMoviesDbHelper(context).getWritableDatabase();
        int qtyOfDeletedRecords=db.delete(PopularMoviesContract.FavoritesEntry.TABLE_NAME,null,null);

        cursor=db.rawQuery("SELECT "+PopularMoviesContract.FavoritesEntry._ID+" FROM "+PopularMoviesContract.FavoritesEntry.TABLE_NAME,null);
        assertFalse("Error: DeleteAllFavorites - No all movie records were deleted",cursor.moveToFirst());

        db.close();

        return qtyOfDeletedRecords;




    }



    static ArrayList<Movie> generateFakeArrayList (int qtyOfMovies){
        ArrayList<Movie> movies=new ArrayList<Movie>();

        for (int i=0;i<qtyOfMovies;i++){
            movies.add(new Movie(
                            ((long) i),
                            BASE_TITLE+" "+i,
                            BASE_IMAGE_THUMNAIL+" "+i,
                            BASE_SYSNOPSIS+" "+i,
                            BASE_USR_RATING+" "+i,
                            BASE_RELEASE_DATE+" "+i
                            )
            );
        }



        assertEquals("Error: ArreyList size doesn´t match with the expected quantity of movies",movies.size(),qtyOfMovies);

        return movies;

    }


    static ContentValues[] generateFakeContentValueArray (int qtyOfMovies){
        ContentValues values []=new ContentValues[qtyOfMovies];



        for (int i=0;i<qtyOfMovies;i++){
            values[i]=new Movie(
                    ((long) i),
                    BASE_TITLE+" "+i,
                    BASE_IMAGE_THUMNAIL+" "+i,
                    BASE_SYSNOPSIS+" "+i,
                    BASE_USR_RATING+" "+i,
                    BASE_RELEASE_DATE+" "+i
            ).getMovieValues();

        }


        return values;


    }





}
