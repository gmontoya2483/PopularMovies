package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.montoya.popularmoviesstg2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Gabriel on 04/09/2016.
 */
public class Utils {


    public static String getYear(String date){
        String year="";
        Date fecha=null;
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat df=new SimpleDateFormat("yyyy");

        try{

            fecha=formatoDeFecha.parse(date);
            year=df.format(fecha);






        }catch(Exception e){
            e.printStackTrace();
        }

        return year;

    }


    public static String getCurrentSelection(Context context){
        String selection;
        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences( context);
        selection=sharedPrefs.getString(
                context.getString(R.string.pref_sort_by_key),
                context.getString(R.string.pref_sort_by_most_popular));


        return selection;


    }

}
