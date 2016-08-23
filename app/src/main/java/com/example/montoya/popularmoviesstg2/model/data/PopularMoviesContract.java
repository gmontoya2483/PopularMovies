package com.example.montoya.popularmoviesstg2.model.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by montoya on 17.08.2016.
 */
public class PopularMoviesContract {



    //Content provider fields
    public static final String CONTENT_AUTHORITY = "com.example.montoya.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    //public static final String PATH_FAVORITES = "favorites";






    public static final class MoviesEntry implements BaseColumns {


        // Table name
        public static final String TABLE_NAME = "movies";

        // Columns
        public static final String COULUMN_MOVIE_TITLE = "mov_title";
        public static final String COULUMN_MOVIE_IMAGE_THUMBNAIL = "mov_imageThumbnail";
        public static final String COULUMN_MOVIE_SYSNOPSIS = "mov_mov_sysnopsis";
        public static final String COULUMN_MOVIE_USER_RATING = "mov_userRating";
        public static final String COULUMN_MOVIE_RELEASE_DATE = "mov_releaseDate";




        // Create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        // create cursor of base type directory for multiples entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;


        //for building URIs on insertion
        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildAllMoviesUri() {
            return CONTENT_URI;
        }


        public static Uri buildMoviebyIdUri(long id){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();


        }


    }



}
