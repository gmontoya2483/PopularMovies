package com.example.montoya.popularmoviesstg2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.controler.Utils;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG=MainActivity.class.getSimpleName();
    private final String MOVIESFRAGMENT_TAG = "MFTAG";
    public String mSelection;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSelection= Utils.getCurrentSelection(this);




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


}

