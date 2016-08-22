package com.example.montoya.popularmoviesstg2.controler;

import android.net.Uri;
import android.util.Log;

import com.example.montoya.popularmoviesstg2.model.Movie;

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










    public static String BuildImageUrl (String size, String file_path){
        Uri buildUri= Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(size)
                .build();

        return buildUri.toString()+file_path;




    }



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



    /*
    public void BulkInsertMovies (Context context, ArrayList<Movie> movies){


        Vector<ContentValues> cvVector=new Vector<ContentValues>(movies.size());

        for (Movie currentMovie: movies)
        {
            ContentValues movieValues=new ContentValues();

            movieValues.put(PopularMoviesContract.MoviesEntry._ID,currentMovie.getId());
            movieValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE,currentMovie.getTitle());
            movieValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL,currentMovie.getImageThumbnail());
            movieValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS,currentMovie.getSysnopsis());
            movieValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING,currentMovie.getUserRating());
            movieValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE,currentMovie.getReleaseDate());

            cvVector.add(movieValues);

        }

        if (cvVector.size()>0){
            ContentValues[] cvArray=new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(PopularMoviesContract.MoviesEntry.CONTENT_URI,cvArray);

        }

    }


    public void DeleteAllMovies(Context context){

        context.getContentResolver().delete(PopularMoviesContract.MoviesEntry.CONTENT_URI,null,null);

    }

*/




}
