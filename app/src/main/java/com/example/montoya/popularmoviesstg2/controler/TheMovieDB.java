package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by montoya on 07.05.2016.
 */
public class TheMovieDB {

    private final String LOG_TAG=TheMovieDB.class.getSimpleName();

    public static final String API_KEY= "d89a8ed561f05f7943fbddb322045898"; //Constant THEMOVIEDB Application Key.//TODO add the application key before running


    //public static final String IMAGE_BASE_URL="http://api.themoviedb.org/3"; //Constant URL Base to download the images
    public static final String IMAGE_BASE_URL="http://image.tmdb.org/t/p";
    public static final String IMAGE_SIZE_W92="w92";
    public static final String IMAGE_SIZE_W154="w154";
    public static final String IMAGE_SIZE_W185="w185";
    public static final String IMAGE_SIZE_W342="w342";
    public static final String IMAGE_SIZE_W500="w500";
    public static final String IMAGE_SIZE_W780="w780";


    private final String BASE_URL="http://api.themoviedb.org/3";
    private final String MOVIE_PATH="movie";
    private final String APPID_PARAM="api_key";
    private final String POPULAR_END_POINT="popular";
    private final String TOP_RATED_END_POINT="top_rated";
    private final String PAGE_NUM_PARAM="page";
    private final String VIDEOS_PATH="videos";










    // Method to build the URI for getting the  Movie of a particular enPpointer popular or top_rated oder
    public URL BuildUri(String endPointFilter){


        Uri buildUri= Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(endPointFilter)
                .appendQueryParameter(APPID_PARAM,API_KEY)
                .build();


        try {

            URL url=new URL(buildUri.toString());
            return url;
        } catch (MalformedURLException e) {

            Log.e(LOG_TAG,"Error:"+e.getStackTrace());
            return null;
        }


    }









    // Method to build the URI for getting the  Images
    public static String BuildImageUrl (String size, String file_path){
        Uri buildUri= Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(size)
                .build();

        return buildUri.toString()+file_path;




    }


    //Method to get the Json Strring for the movies
    public String getDataFromInternet(String endPointFilter){
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String moviesJsonStr=null;
        URL url=this.BuildUri(endPointFilter);


        //Create the request to the MovieDB and Open the connection
        try {
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            //Read the input stream into a String
            InputStream inputStream=urlConnection.getInputStream();
            StringBuffer buffer=new StringBuffer();
            if(inputStream!=null){
                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line=reader.readLine())!=null){
                    buffer.append (line+"\n");
                }

                if (buffer.length()!=0){
                    moviesJsonStr=buffer.toString();
                }


            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error: "+e.getStackTrace(),e);
            return null;

        }finally {
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            if (reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG,"Error Closing: "+e.getStackTrace(),e);
                }
            }
        }


        return moviesJsonStr;


    }



    //Parser mothod which gets a JSon String and return an ArrayList of Movies
    public ArrayList<Movie> JSonParser (String JsonMessage)  {



        ArrayList<Movie> myMovieList = new ArrayList<Movie>();


        final String OWM_RESULTS="results";
        final String POSTER_PATH="poster_path";
        final String OVERVIEW="overview";
        final String RELEASE_DATE="release_date";
        final String VOTE_AVERAGE="vote_average";
        final String ORIGINAL_TITLE="original_title";
        final String ID="id";



        JSONObject movieJsonList= null;
        JSONObject movieJson=null;
        try {
            movieJsonList = new JSONObject(JsonMessage);
            JSONArray moviesJsonArray=movieJsonList.getJSONArray(OWM_RESULTS);


            for (int i=0;i <moviesJsonArray.length();i++){
                String id="";
                String title="";
                String synopsis="";
                String releaseDate="";
                String imagePath="";
                String rating="";

                movieJson=moviesJsonArray.getJSONObject(i);
                id=movieJson.getString(ID);
                title=movieJson.getString(ORIGINAL_TITLE);
                synopsis=movieJson.getString(OVERVIEW);
                releaseDate=movieJson.getString(RELEASE_DATE);
                imagePath=movieJson.getString(POSTER_PATH);
                rating=movieJson.getString(VOTE_AVERAGE);


                myMovieList.add(new Movie(Long.parseLong(id),title,imagePath,synopsis,rating,releaseDate));


            }

            return myMovieList;



        } catch (JSONException e) {
           Log.e (LOG_TAG,"Error: "+ e.getStackTrace(),e);
            return null;
        }


    }



    //Method which trigger the Asynctask for getting the Movies from Internet
    public static void updateMovies(Context context){
        //Check if there is internet connection
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Execute the Async task
            //FetchMoviesTask moviesTask=new FetchMoviesTask(getActivity(),myMovieAdapter);
            FetchMoviesTask moviesTask=new FetchMoviesTask(context);
            moviesTask.execute();


        } else {

            Toast.makeText(context, R.string.err_no_netwaork_connection, Toast.LENGTH_LONG).show();
        }



    }



    public static void updateVideos(Context context, Long movieID){
        //Check if there is internet connection
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Execute the Async task
            //FetchMoviesTask moviesTask=new FetchMoviesTask(getActivity(),myMovieAdapter);
            FetchVideosTask videosTask=new FetchVideosTask(context, movieID);
            videosTask.execute();


        } else {

            Toast.makeText(context, R.string.err_no_netwaork_connection, Toast.LENGTH_LONG).show();
        }



    }




    /*
    * This section applies to the method to manage the Videos.
    * http://api.themoviedb.org/3/movie/{id}/videos?api_key=?
    *
    */


    // Method to build the URI for getting the Videos of a particular Movie
    public URL BuildVideosUri(Long id){


        Uri buildUri= Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(id))
                .appendPath(VIDEOS_PATH)
                .appendQueryParameter(APPID_PARAM,API_KEY)
                .build();


        try {

            URL url=new URL(buildUri.toString());
            return url;
        } catch (MalformedURLException e) {

            Log.e(LOG_TAG,"Error:"+e.getStackTrace());
            return null;
        }


    }


    //Method to get the Json Strring for the movies
    public String getVideosFromInternet(Long movieId){
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String videosJsonStr=null;
        URL url=this.BuildVideosUri(movieId);


        //Create the request to the MovieDB and Open the connection
        try {
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            //Read the input stream into a String
            InputStream inputStream=urlConnection.getInputStream();
            StringBuffer buffer=new StringBuffer();
            if(inputStream!=null){
                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line=reader.readLine())!=null){
                    buffer.append (line+"\n");
                }

                if (buffer.length()!=0){
                    videosJsonStr=buffer.toString();
                }


            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error: "+e.getStackTrace(),e);
            return null;

        }finally {
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            if (reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG,"Error Closing: "+e.getStackTrace(),e);
                }
            }
        }


        return videosJsonStr;


    }


    //Parser method which gets a JSon String and return an ArrayList of Movies
    public ArrayList<Video> JSonVideoParser (String JsonMessage)  {



        ArrayList<Video> myVideoList = new ArrayList<Video>();


        final String OWM_RESULTS="results";
        final String KEY="key";
        final String NAME="name";
        final String SITE="site";
        final String TYPE="type";
        final String MOVIE_ID="id";


        String movieID;




        JSONObject videoJsonList= null;
        JSONObject videoJson=null;
        try {
            videoJsonList = new JSONObject(JsonMessage);
            movieID=videoJsonList.getString(MOVIE_ID);
            JSONArray videoJsonArray=videoJsonList.getJSONArray(OWM_RESULTS);


            for (int i=0;i <videoJsonArray.length();i++){


                String key="";
                String name="";
                String site="";
                String type="";




                videoJson=videoJsonArray.getJSONObject(i);
                key=videoJson.getString(KEY);
                name=videoJson.getString(NAME);
                site=videoJson.getString(SITE);
                type=videoJson.getString(TYPE);


                myVideoList.add(new Video(Long.parseLong(movieID),key,name,site,type));


            }

            return myVideoList;



        } catch (JSONException e) {
            Log.e (LOG_TAG,"Error: "+ e.getStackTrace(),e);
            return null;
        }


    }









}
