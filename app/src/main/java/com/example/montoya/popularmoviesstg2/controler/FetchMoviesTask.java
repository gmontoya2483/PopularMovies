package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.os.AsyncTask;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.Video;

import java.util.ArrayList;

/**
 * Created by montoya on 29.08.2016.
 */
//public class FetchMoviesTask extends AsyncTask<Void,Void,ArrayList<Movie>>
public class FetchMoviesTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();
    private TheMovieDB theMDB = new TheMovieDB();
    private final Context mContext;
    private ArrayList<Movie> myMovieList=new ArrayList<Movie>();






    public FetchMoviesTask (Context context){

        mContext=context;



    }




    @Override
    protected Void doInBackground(Void... params) {
    //protected ArrayList<Movie> doInBackground(Void... params){


        //Get the selected sortby from the Shared Preferences
        String endPointFilter=Utils.getCurrentSelection(mContext);




        //TODO Ver como reemplazar esto por los valores del file String
        if(!endPointFilter.equals("favorite_collection")){



            String JsonMessage=theMDB.getDataFromInternet(endPointFilter);
            ArrayList<Movie> myMovieList;

            if (JsonMessage!=null){
                myMovieList=new ArrayList<Movie>(theMDB.JSonParser(JsonMessage));

                //Empty the movie table
                Movie.deleteAllMovies(mContext);

                //Bulk Insert the movies
                Movie.bulkInsertMovies(mContext,myMovieList);




            }

        }


        return null;





    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //delete all Videos which are not associated to any Movie or eny Favorite
        Video.deleteAllVides(mContext);
    }


    /*
    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {

        if (movies!=null && mMovieAdapter!=null){
            mMovieAdapter.clear();
            for (Movie movie: movies){
                mMovieAdapter.add(movie);
            }
        }


    }
    */
}
