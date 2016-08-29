package com.example.montoya.popularmoviesstg2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.montoya.popularmoviesstg2.controler.FetchMoviesTask;
import com.example.montoya.popularmoviesstg2.model.Movie;

import java.util.ArrayList;


public class MoviesFragment extends Fragment {


    private final String LOG_TAG=MoviesFragment.class.getSimpleName();

    private CustomGridArrayAdapter myMovieAdapter;
    private GridView myMovieListView;
    private ArrayList<Movie> myMovieList=new ArrayList<Movie>();


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_movies, container, false);


        myMovieAdapter=new CustomGridArrayAdapter(getContext(),myMovieList);
        myMovieListView=(GridView) rootView.findViewById(R.id.movies_ListView);
        myMovieListView.setAdapter(myMovieAdapter);




        //Set OnItemclickListener on the actual Listview
        myMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the selected movie
                Movie movie=myMovieList.get(position);

                //Toast.makeText(MainActivity.this, "Item clicked at the position: "+position+" - Title: "+movie.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(getContext(),MovieDetailsActivity.class);
                intent.putExtra("ID",movie.getId());
                intent.putExtra("TITLE",movie.getTitle());
                intent.putExtra("RELEASE_DATE", movie.getReleaseDate());
                intent.putExtra("USER_RATING", movie.getUserRating());
                intent.putExtra("SYNOPSIS", movie.getSysnopsis());
                intent.putExtra("IMAGE", movie.getImageThumbnail());

                startActivity(intent);


            }
        });

        return rootView;

    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovies();

    }



    private void updateMovies(){
        //Check if there is internet connection
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Execute the Async task
            FetchMoviesTask moviesTask=new FetchMoviesTask(getActivity(),myMovieAdapter);
            moviesTask.execute();


        } else {

            Toast.makeText(getContext(), R.string.err_no_netwaork_connection, Toast.LENGTH_LONG).show();
        }



    }





/*
    public class FetchMoviesTask extends AsyncTask<Void,Void,ArrayList<Movie>> {

        private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();

        private TheMovieDB theMDB = new TheMovieDB();




        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            //Get the selected sorby from the Shared Preferences
            SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(getContext());
            String endPointFilter=sharedPrefs.getString(
                    getString(R.string.pref_sort_by_key),
                    getString(R.string.pref_sort_by_most_popular));



            String JsonMessage=theMDB.getDataFromInternet(endPointFilter);
            ArrayList<Movie> myMovieList;

            if (JsonMessage!=null){
                myMovieList=new ArrayList<Movie>(theMDB.JSonParser(JsonMessage));



                return myMovieList;

            }else{


                return null;
            }

        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            myMovieList.clear();

            if(movies!=null){

                for (Movie movieItem: movies){
                    myMovieList.add(movieItem);
                }
                myMovieListView.setAdapter(myMovieAdapter);
            }
        }
    }


*/



}
