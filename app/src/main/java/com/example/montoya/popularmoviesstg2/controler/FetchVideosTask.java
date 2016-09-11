package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.os.AsyncTask;

import com.example.montoya.popularmoviesstg2.model.Video;

import java.util.ArrayList;

/**
 * Created by montoya on 05.09.2016.
 */
public class FetchVideosTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG=FetchVideosTask.class.getSimpleName();
    private TheMovieDB mTheMovieDB = new TheMovieDB();
    private final Context mContext;
    Long mMovieId;




    public FetchVideosTask(Context context, Long movie_id){

        this.mContext=context;
        this.mMovieId=movie_id;





    }


    @Override
    protected Void doInBackground(Void... voids) {

        String JsonString=mTheMovieDB.getVideosFromInternet(mMovieId);
        ArrayList<Video> mVideosList=new ArrayList<Video>();
        mVideosList=mTheMovieDB.JSonVideoParser(JsonString);

        Video.bulkInsertVideos(mContext,mVideosList);


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
