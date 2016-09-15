package com.example.montoya.popularmoviesstg2.view;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.controler.Utils;
import com.example.montoya.popularmoviesstg2.controler.VideoArrayAdapter;
import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by montoya on 30.08.2016.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String FAVORITES_SELECTION;
    private final String LOG_TAG=MoviesFragment.class.getSimpleName();

    private Movie mMovie;
    private Uri movieUri;
    private boolean inFavorites;
    private static Long mMovieID;
    private ArrayList<Video> mVideosList;
    private VideoArrayAdapter mVideoAdapter;




    private ImageView movieImage;
    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieSynopsis;
    private TextView movieRating;
    private TextView movieId;
    private Button butonFavorites;
    private ListView videoListView;


    private static final int MOVIE_DETAILS_LOADER = 1;



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

        getIntentParameter();

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
            mMovieID= ContentUris.parseId(movieUri);
            //mMovieID=intent.getLongExtra("MOVIE_ID",-1L);


        } else{
            movieUri=null;
            mMovieID=-1L;


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



        //Set the
        mVideosList=mMovie.getVideosList(getActivity());
        if (mVideosList!=null){
            //Set the adapter
            mVideoAdapter=new VideoArrayAdapter(getActivity(),mVideosList);
            videoListView.setAdapter(mVideoAdapter);



        }else{
            getVideosFromInternet();

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



        //Set the List View
       videoListView =(ListView) mRootView.findViewById(R.id.videos_ListView);
       //Add the listener
       videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Video video= (Video) videoListView.getItemAtPosition(position);
                Video.watchVideo(getActivity(),video.getKey());

            }
        });



    }



    private void getVideosFromInternet(){

        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Execute the Async task
            FetchVideosTask videoTask=new FetchVideosTask(getActivity(),mMovie.getId());
            videoTask.execute();


        } else {

            Toast.makeText(getActivity(), R.string.err_no_netwaork_connection, Toast.LENGTH_LONG).show();
        }

    }






    //Inner Class to fetch the videos from intermet
    public class FetchVideosTask extends AsyncTask<Void,Void,ArrayList<Video>> {

        private final String LOG_TAG=FetchVideosTask.class.getSimpleName();
        private TheMovieDB mTheMovieDB = new TheMovieDB();
        private final Context mContext;
        private Long mMovieId;



        public FetchVideosTask(Context context,Long movieID){
            this.mContext=context;
            this.mMovieId=movieID;

        }


        @Override
        protected ArrayList<Video> doInBackground(Void... voids) {
            ArrayList<Video> mVideosList;

            String JsonString=mTheMovieDB.getVideosFromInternet(mMovieId);
            if (JsonString!=null){
                mVideosList=new ArrayList<Video>(mTheMovieDB.JSonVideoParser(JsonString));
                Video.bulkInsertVideos(mContext,mVideosList);

            }else{
                mVideosList=null;
            }




            return mVideosList;
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videos) {

            if (mVideosList!=null){
                mVideosList.clear();
            }else{
                mVideosList=new ArrayList<Video>();
            }


            if(videos!=null){
                for (Video videoItem: videos){
                    mVideosList.add(videoItem);
                }
                mVideoAdapter=new VideoArrayAdapter(getActivity(),mVideosList);
                videoListView.setAdapter(mVideoAdapter);
            }






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




}
