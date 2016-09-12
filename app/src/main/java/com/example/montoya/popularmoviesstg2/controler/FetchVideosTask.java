package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.os.AsyncTask;

import com.example.montoya.popularmoviesstg2.model.Video;

import java.util.ArrayList;

/**
 * Created by montoya on 12.09.2016.
 */
public class FetchVideosTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG=FetchVideosTask.class.getSimpleName();
    private TheMovieDB mTheMovieDB = new TheMovieDB();
    private final Context mContext;
    private final Long mVideoId;


    public FetchVideosTask(Context context,Long movieID){
        this.mContext=context;
        this.mVideoId=movieID;

    }


    @Override
    protected Void doInBackground(Void... voids) {


        String JsonString=mTheMovieDB.getVideosFromInternet(mVideoId);
        ArrayList<Video> mVideosList=new ArrayList<Video>();
        mVideosList=mTheMovieDB.JSonVideoParser(JsonString);
        Video.bulkInsertVideos(mContext,mVideosList);



        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);




        /*
        //sacar luego del test

        Log.i(LOG_TAG,"FINALIZO LA CARGA DE VIDEOS DESDE INTERNET");
        Uri AllVideosUri= PopularMoviesContract.VideosEntry.buildAllVideosUri();
        Cursor cursor= mContext.getContentResolver().query(AllVideosUri,null,null,null,null);

        while (cursor.moveToNext()){
            System.out.println("VIDEO INFO:   _ID:"+cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.VideosEntry._ID))
            +" Key:"+cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY))
            +" name:" + cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME))
            +" site:" + cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_SITE))
            +" type:" + cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE))
            );

        }
        */



    }
}








