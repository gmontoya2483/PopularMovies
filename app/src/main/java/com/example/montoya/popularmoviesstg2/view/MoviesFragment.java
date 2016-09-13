package com.example.montoya.popularmoviesstg2.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.GridView;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.controler.MovieCursorAdapter;
import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.controler.Utils;
import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

import java.util.ArrayList;


public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private final String LOG_TAG=MoviesFragment.class.getSimpleName();
    private String FAVORITES_SELECTION;


    private MovieCursorAdapter myMovieAdapter;
    private GridView myMovieListView;
    private ArrayList<Movie> myMovieList=new ArrayList<Movie>();
    private static final int MOVIE_LOADER = 0;
    private String mCurrentSelection;



    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FAVORITES_SELECTION= getResources().getString(R.string.pref_sort_by_favorite_collection);
        mCurrentSelection=Utils.getCurrentSelection(getActivity());
        TheMovieDB.updateMovies(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        myMovieAdapter=new MovieCursorAdapter(getContext(),null,0);


        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_movies, container, false);


        myMovieListView=(GridView) rootView.findViewById(R.id.movies_ListView);
        myMovieListView.setAdapter(myMovieAdapter);




        //Set OnItemclickListener on the actual Listview
        myMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri SelectedMovieUri;

                Cursor cursor=(Cursor) parent.getItemAtPosition(position);
                if (cursor!=null){
                    Intent intent=new Intent(getContext(),MovieDetailsActivity.class);

                   if (mCurrentSelection.equals(FAVORITES_SELECTION)){
                        SelectedMovieUri=PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry._ID)));

                    }else{
                        SelectedMovieUri=PopularMoviesContract.MoviesEntry.buildMoviesUri(cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID)));

                    }


                    //TODO ARREGLAR ESTA DESPROLIGIDAD PASAR SOLO EL ID y QUE EL FRAGMENT DESTINO ARME LA URI
                    //TODO AVERIGAU ACA SI LA PELICULA TIENE VIDEOS ya CARGADOS SI NO LOS TIENE LLAMAR LA TASK PARA CARGARLOS
                    intent.putExtra("MOVIE_ID",cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry._ID)));
                    intent.setData(SelectedMovieUri);


                    startActivity(intent);


                }




            }
        });


        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //init the Loadaer manager always in the OnActivity created
        getLoaderManager().initLoader(MOVIE_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //TheMovieDB.updateMovies(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        String selection=Utils.getCurrentSelection(getActivity());

        if (!selection.equals(mCurrentSelection)){
            mCurrentSelection=selection;
            onSelectionChanged();


        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Uri allMoviesUri;

        //Get the selected sortby from the Shared Preferences

        String endPointFilter= Utils.getCurrentSelection(getActivity());





        if(endPointFilter.equals(FAVORITES_SELECTION)){
            allMoviesUri=PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri();



        }else{
            allMoviesUri= PopularMoviesContract.MoviesEntry.buildAllMoviesUri();

        }





        return new CursorLoader(getActivity(),allMoviesUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myMovieAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        myMovieAdapter.swapCursor(null);

    }



    public void onSelectionChanged(){
        TheMovieDB.updateMovies(getActivity());
        getLoaderManager().restartLoader(MOVIE_LOADER,null,this);

    }








}
