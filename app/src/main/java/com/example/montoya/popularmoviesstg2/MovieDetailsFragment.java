package com.example.montoya.popularmoviesstg2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by montoya on 30.08.2016.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private Long id;
    private String title;
    private String synopsis;
    private String image;
    private String releaseDate;
    private String userRating;
    private Uri movieUri;




    private ImageView movieImage;
    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieSynopsis;
    private TextView movieRating;
    private TextView movieId;


    private static final int MOVIE_DETAILS_LOADER = 1;


    View mRootView;



    public MovieDetailsFragment(){

    }







    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         mRootView= inflater.inflate(R.layout.fragment_movie_details, container, false);

        setLayoutObjects();


        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(MOVIE_DETAILS_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }















    private void getIntentParameter(){
        Intent intent=getActivity().getIntent();
        if (intent != null) {

            movieUri=intent.getData();


        } else{
            movieUri=null;

        }

    }

    private void setLayoutValues(Cursor cursor){

        if (cursor.moveToFirst()){



            this.id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID));
            this.title = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE));
            this.synopsis=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS));
            this.image=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL));
            this.releaseDate=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE));
            this.userRating=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING));




            //Set the image
            String imagePath= TheMovieDB.BuildImageUrl(TheMovieDB.IMAGE_SIZE_W500,this.image);
            Picasso.with(getActivity()).load(imagePath).into(movieImage);

            //Set the movie title

            movieTitle.setText(this.title);


            //Set the movie release Date

            movieReleaseDate.setText(this.releaseDate);


            //Set the movie Rating

            movieRating.setText(this.userRating);


            //Set the movie id

            movieId.setText(Long.toString(this.id));




            //Set the movie Synopsis

            movieSynopsis.setText(this.synopsis);





        }






    }


    private void setLayoutObjects(){

        movieImage=(ImageView) mRootView.findViewById(R.id.details_image);

        //Set the movie title
        movieTitle=(TextView) mRootView.findViewById(R.id.details_title);

        //Set the movie release Date
        movieReleaseDate=(TextView) mRootView.findViewById(R.id.details_release_date);

        //Set the movie Rating
        movieRating=(TextView) mRootView.findViewById(R.id.details_user_rating);

        //Set the movie id
        movieId=(TextView) mRootView.findViewById(R.id.details_id);

        //Set the movie Synopsis
        movieSynopsis=(TextView) mRootView.findViewById(R.id.details_synopsys);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        getIntentParameter();
        return new CursorLoader(getActivity(),movieUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setLayoutValues(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
