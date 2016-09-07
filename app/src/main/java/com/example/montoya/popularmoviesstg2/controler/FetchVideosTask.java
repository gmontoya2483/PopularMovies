package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by montoya on 05.09.2016.
 */
public class FetchVideosTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG=FetchVideosTask.class.getSimpleName();
    private TheMovieDB theMDB = new TheMovieDB();
    private final Context mContext;




    public FetchVideosTask(Context context){

        mContext=context;



    }


    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
