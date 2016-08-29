package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.montoya.popularmoviesstg2.CustomGridArrayAdapter;
import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.model.Movie;

import java.util.ArrayList;

/**
 * Created by montoya on 29.08.2016.
 */
public class FetchMoviesTask extends AsyncTask<Void,Void,ArrayList<Movie>> {

    private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();
    private TheMovieDB theMDB = new TheMovieDB();
    private final Context mContext;
    private ArrayList<Movie> myMovieList=new ArrayList<Movie>();
    //private ArrayAdapter<Movie> mMovieAdapter;

    private CustomGridArrayAdapter  mMovieAdapter;



    public FetchMoviesTask (Context context, CustomGridArrayAdapter movieAdapter){

        mContext=context;
        mMovieAdapter=movieAdapter;


    }




    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {



        //Get the selected sortby from the Shared Preferences
        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences( mContext);
        String endPointFilter=sharedPrefs.getString(
                mContext.getString(R.string.pref_sort_by_key),
                mContext.getString(R.string.pref_sort_by_most_popular));



        String JsonMessage=theMDB.getDataFromInternet(endPointFilter);
        ArrayList<Movie> myMovieList;

        if (JsonMessage!=null){
            myMovieList=new ArrayList<Movie>(theMDB.JSonParser(JsonMessage));

            //Empty the movie table
            Movie.deleteAllMovies(mContext);

            //Bulk Insert the movies
            Movie.bulkInsertMovies(mContext,myMovieList);


            // get data from the Database
            //TODO Verify if this step makes sence
            myMovieList=null;
            myMovieList=Movie.getAllMoviesArrayList(mContext);



            return myMovieList;


        }else{


            return null;
        }

    }


    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {

        if (movies!=null && mMovieAdapter!=null){
            mMovieAdapter.clear();
            for (Movie movie: movies){
                mMovieAdapter.add(movie);
            }
        }

//        myMovieList.clear();
//
//        if(movies!=null){
//
//            for (Movie movieItem: movies){
//                myMovieList.add(movieItem);
//            }
//            myMovieListView.setAdapter(myMovieAdapter);
//        }
    }
}
