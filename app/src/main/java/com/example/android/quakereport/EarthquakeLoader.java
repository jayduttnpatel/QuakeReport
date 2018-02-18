package com.example.android.quakereport;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by jaydutt on 18/02/2018.
 */

public class EarthquakeLoader extends android.content.AsyncTaskLoader<ArrayList<quakeData>>
{
    public EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<quakeData> loadInBackground()
    {
        return QueryUtils.extractEarthquakes();
    }

    @Override
    protected void onStartLoading() {
        // super.onStartLoading();
        forceLoad();
    }
}
