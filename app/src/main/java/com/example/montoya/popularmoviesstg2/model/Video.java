package com.example.montoya.popularmoviesstg2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

import java.util.ArrayList;

/**
 * Created by montoya on 06.09.2016.
 */
public class Video {

    private Long id;
    private Long movieId;
    private String key;
    private String name;
    private String site;
    private String type;


    //Valid video Site
    public static final String VALID_SITE="YouTube";


    public  Video(Long movieId,String key, String name,String site, String type){
        this.movieId=movieId;
        this.key=key;
        this.name=name;
        this.site=site;
        this.type=type;

    }

    public  Video(Long _id,Long movieId,String key, String name,String site, String type){
        this.id=-_id;
        this.movieId=movieId;
        this.key=key;
        this.name=name;
        this.site=site;
        this.type=type;

    }




    public Long getId() {
        return id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }




    public ContentValues getVideoValues (){


        ContentValues videoValues=new ContentValues();
        videoValues.put(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID,this.getMovieId());
        videoValues.put(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY,this.getKey());
        videoValues.put(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME,this.getName());
        videoValues.put(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_SITE,this.getSite());
        videoValues.put(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE,this.getType());


        return videoValues;

    }




    public static int bulkInsertVideos (Context context, ArrayList<Video> videos){
        int quantityOfInsertedMovies=0;
        int quantityOfVideos=videos.size();
        Uri allVideosUri=PopularMoviesContract.VideosEntry.buildAllVideosUri();

        ContentValues values[]=new ContentValues[quantityOfVideos];

        //generate the Content values Array
        int i=0;
        for (Video video: videos) {
            values[i]=video.getVideoValues();
            i++;
        }

        quantityOfInsertedMovies=context.getContentResolver().bulkInsert(allVideosUri,values);

        return quantityOfInsertedMovies;

    }




    public static ArrayList<Video> getMovieVideosArrayList(Context context,Long movieId){
        ArrayList<Video> videos=new ArrayList<Video>();
        Cursor cursor;
        Uri allVideosByMovie=PopularMoviesContract.VideosEntry.buildVideosByMovieIdUri(movieId);
        cursor=context.getContentResolver().query(allVideosByMovie,null,null,null,null);

        Long id, movie_id;
        String key, name,site,type;



        if (cursor.moveToFirst()){
            do{
                id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.VideosEntry._ID));
                movie_id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID));
                key=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY));
                name=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME));
                site=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_SITE));
                type=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE));

                videos.add(new Video(id,movie_id,key,name,site,type));

            }while(cursor.moveToNext());

        }else{
            videos=null;
        }
        return videos;




    }



    public static int deleteAllVides(Context context){
        int qtyOfDeletedVideos=0;
        Uri allVideosUri=PopularMoviesContract.VideosEntry.buildAllVideosUri();
        qtyOfDeletedVideos=context.getContentResolver().delete(allVideosUri,null,null);

        return qtyOfDeletedVideos;

    }



}
