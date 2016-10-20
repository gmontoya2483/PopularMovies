package com.example.montoya.popularmoviesstg2.controler.Service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.controler.Utils;
import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.Video;

import java.util.ArrayList;

/**
 * Created by Gabriel on 19/10/2016.
 */

public class PopularService extends IntentService{

    private static final String LOG_TAG = PopularService.class.getSimpleName();
    public static final String TRIGGER_METHOD = "TRIGGER";
    private String mMethod="NotDefined";

    private TheMovieDB theMDB = new TheMovieDB();
    private  Context mContext;
    private ArrayList<Movie> myMovieList=new ArrayList<Movie>();


    public PopularService() {
        super("PopularService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mContext=getApplicationContext();

        String endPointFilter= Utils.getCurrentSelection(mContext);





        if(!endPointFilter.equals("favorite_collection")){



            String JsonMessage=theMDB.getDataFromInternet(endPointFilter);
            ArrayList<Movie> myMovieList;

            if (JsonMessage!=null){
                myMovieList=new ArrayList<Movie>(theMDB.JSonParser(JsonMessage));

                //Empty the movie table
                Movie.deleteAllMovies(mContext);

                //Bulk Insert the movies
                Movie.bulkInsertMovies(mContext,myMovieList);

                //delete all Videos which are not associated to any Movie or eny Favorite
                Video.deleteAllVideos(mContext);

            }

        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMethod=intent.getStringExtra(TRIGGER_METHOD);
        Log.i (LOG_TAG,"TRIGGER METHOD: "+mMethod+" - Service Started ...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i (LOG_TAG,"Service Stopped ...");
    }


    public static class AlarmReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent=new Intent (context, PopularService.class);
            sendIntent.putExtra("METHOD","AlarmReceiver");
            context.startService(sendIntent);

        }
    }

}
