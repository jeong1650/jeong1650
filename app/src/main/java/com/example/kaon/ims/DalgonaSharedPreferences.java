package com.example.kaon.ims;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class DalgonaSharedPreferences {

    public static final String KEY_COOKIE = "ims.key.cookie" ;



    private static DalgonaSharedPreferences dsp = null;


    public static DalgonaSharedPreferences getInstanceOf(Context c) {

        if (dsp == null) {

            dsp = new DalgonaSharedPreferences(c);

        }
        return dsp;
    }


    private Context mContext;

    private SharedPreferences pref;


    public DalgonaSharedPreferences(Context c) {

        mContext = c;

        String PREF_NAME = c.getPackageName();

        pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

    }


    public void putHashSet(String key, HashSet<String> set) {

        SharedPreferences.Editor editor = pref.edit();

        editor.putStringSet(key, set);

        editor.commit();

    }


    public HashSet<String> getHashSet(String key, HashSet<String> dftValue) {

        try {

            return (HashSet<String>) pref.getStringSet(key, dftValue);

        } catch (Exception e) {

            e.printStackTrace();

            return dftValue;

        }

    }

}
