package com.example.montoya.popularmoviesstg2.view;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.controler.Utils;
import com.example.montoya.popularmoviesstg2.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by montoya on 30.08.2016.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String FAVORITES_SELECTION;
    private final String LOG_TAG=MoviesFragment.class.getSimpleName();

    Movie mMovie;
    private Uri movieUri;
    private boolean inFavorites;




    private ImageView movieImage;
    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieSynopsis;
    private TextView movieRating;
    private TextView movieId;
    private Button butonFavorites;


    private static final int MOVIE_DETAILS_LOADER = 1;
    private static final int MOVIE_VIDEOS=2;


    View mRootView;



    public MovieDetailsFragment(){

    }




    /**
     * Fragment overrided methods
     */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FAVORITES_SELECTION= getResources().getString(R.string.pref_sort_by_favorite_collection);
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


    /**
     * Loader Manager overrided methods
     */


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        getIntentParameter();
        return new CursorLoader(getActivity(),movieUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mMovie=new Movie(data);

        setLayoutValues();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovie=null;

    }


    /*
    *  Helper Methods
    */




    private void getIntentParameter(){
        Intent intent=getActivity().getIntent();
        if (intent != null) {

            movieUri=intent.getData();


        } else{
            movieUri=null;

        }

    }


    private void setLayoutValues(){


            //Set the image
            String imagePath= TheMovieDB.BuildImageUrl(TheMovieDB.IMAGE_SIZE_W500,mMovie.getImageThumbnail());
            Picasso.with(getActivity()).load(imagePath).into(movieImage);

            //Set the movie title
            movieTitle.setText(mMovie.getTitle());

            //Set the movie release Date
            movieReleaseDate.setText(Utils.getYear(mMovie.getReleaseDate()));

            //Set the movie Rating
            movieRating.setText(mMovie.getUserRating());

            //Set the movie id
            movieId.setText(Long.toString(mMovie.getId()));

            //Set the movie Synopsis
            movieSynopsis.setText(mMovie.getSysnopsis());


        //Set the favorite button text
        inFavorites=mMovie.getIsFavorite(getActivity());

        if(!inFavorites){
            butonFavorites.setText (R.string.btn_MarkAsFavorites);

        }else{
            butonFavorites.setText(R.string.btn_RemoveFavorites);

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

        //Set the Favorites Button
        butonFavorites=(Button) mRootView.findViewById(R.id.button_favorites);
        butonFavorites.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if (Utils.getCurrentSelection(getActivity()).equals(FAVORITES_SELECTION)){
                    mMovie.removeFromFavorite(getActivity());
                    butonFavorites.setVisibility(View.INVISIBLE);
                    getLoaderManager().destroyLoader(MOVIE_DETAILS_LOADER);

                }else{

                    if (inFavorites){
                        mMovie.removeFromFavorite(getActivity());
                        butonFavorites.setText(R.string.btn_MarkAsFavorites);

                    }else{
                        mMovie.setToFavorite(getActivity());
                        butonFavorites.setText(R.string.btn_RemoveFavorites);

                    }

                    inFavorites=mMovie.getIsFavorite(getActivity());

                }



            }
        });




    }





    //Inner class for the videos Fragment
    public  static class VideosFragment extends Fragment {

        View mVideoView;


        public VideosFragment(){

        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            mVideoView= inflater.inflate(R.layout.fragment_movie_details_videos, container, false);
            return mVideoView;
        }
    }






}
