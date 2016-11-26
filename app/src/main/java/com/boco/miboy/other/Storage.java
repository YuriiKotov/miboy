package com.boco.miboy.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.crash.FirebaseCrash;

import java.util.Set;

public final class Storage {
    private final String QUERY_REQUIRED = "QUERY_REQUIRED";

    private static Storage storage = null;
    private static SharedPreferences preferences;

    public static Storage getInstance(Context context) {
        if (storage == null) {
            storage = new Storage();
            preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
        return storage;
    }


    private SharedPreferences getPreferences() {
        return preferences;
    }


    public boolean isQueryRequired() {
        return getBoolean(QUERY_REQUIRED, true);
    }

    public void setQueryRequired(boolean value) {
        setBoolean(QUERY_REQUIRED, value);
    }

    ////////////////////////////////////   LOGIC   /////////////////////////////////////////////////
    private String getString(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    private void setString(String key, String value) {
        SharedPreferences.Editor prefEditor = getPreferences().edit();
        prefEditor.putString(key, value);
        prefEditor.apply();
    }

    private void setBoolean(String key, boolean value) {
        SharedPreferences.Editor prefEditor = getPreferences().edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    private void setInt(String key, int value) {
        SharedPreferences.Editor prefEditor = getPreferences().edit();
        prefEditor.putInt(key, value);
        prefEditor.apply();
    }

    private long getLong(String key, long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    private int getInteger(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    private void setLong(String key, long value) {
        SharedPreferences.Editor prefEditor = getPreferences().edit();
        prefEditor.putLong(key, value);
        prefEditor.apply();
    }

    private void setStringSet(String key, Set<String> set) {
        SharedPreferences.Editor prefEditor = getPreferences().edit();
        prefEditor.putStringSet(key, set);
        prefEditor.apply();
    }

    private Set<String> getStringSet(String key, Set<String> set) {
        Set<String> stringSet = set;
        try {
            stringSet = getPreferences().getStringSet(key, set);
        } catch (ClassCastException e) {
            FirebaseCrash.report(new Exception("Storage" + ": !!! CRASHED !!! = " + e.getMessage()));
        }
        return stringSet;
    }

}
