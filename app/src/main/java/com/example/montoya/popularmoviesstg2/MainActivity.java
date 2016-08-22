package com.example.montoya.popularmoviesstg2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.model.Movie;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG=MainActivity.class.getSimpleName();

    private CustomGridArrayAdapter myMovieAdapter;
    private GridView myMovieListView;
    private ArrayList<Movie> myMovieList=new ArrayList<Movie>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        myMovieAdapter=new CustomGridArrayAdapter(this,myMovieList);
        myMovieListView=(GridView) findViewById(R.id.movies_ListView);
        myMovieListView.setAdapter(myMovieAdapter);




        //Set OnItemclickListener on the actual Listview
        myMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the selected movie
                Movie movie=myMovieList.get(position);

                //Toast.makeText(MainActivity.this, "Item clicked at the position: "+position+" - Title: "+movie.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(MainActivity.this,MovieDetailsActivity.class);
                intent.putExtra("ID",movie.getId());
                intent.putExtra("TITLE",movie.getTitle());
                intent.putExtra("RELEASE_DATE", movie.getReleaseDate());
                intent.putExtra("USER_RATING", movie.getUserRating());
                intent.putExtra("SYNOPSIS", movie.getSysnopsis());
                intent.putExtra("IMAGE", movie.getImageThumbnail());

                startActivity(intent);


            }
        });


        //PopularMoviesSyncAdapter.initializeSyncAdapter(this);
















    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                startActivity (new Intent(getApplicationContext(),SettingsActivity.class));

                break;

            default:
                break;

        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateMovies();

    }



    private void updateMovies(){
        //Check if there is internet connection
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Execute the Async task
            FetchMoviesTask moviesTask=new FetchMoviesTask();
            moviesTask.execute();


        } else {

            Toast.makeText(MainActivity.this, R.string.err_no_netwaork_connection, Toast.LENGTH_LONG).show();
        }



    }







    public class FetchMoviesTask extends AsyncTask<Void,Void,ArrayList<Movie>>{

        private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();

        private TheMovieDB theMDB = new TheMovieDB();




        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            //Get the selected sorby from the Shared Preferences
            SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
                //TODO Please review why if I would have not include this line to set the once again the adater with the emulator the grid was refreshed sometimes and on the other hand it never runned in the cellphone. - Thanks
                myMovieListView.setAdapter(myMovieAdapter);
            }
        }
    }












}

