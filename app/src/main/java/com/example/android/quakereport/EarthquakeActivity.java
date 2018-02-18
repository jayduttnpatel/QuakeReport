/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<ArrayList<quakeData>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    //usgs query url return top 10 resent earth quakes happen in the world
    private String USGS_QUrl="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    quakeAdapter mAdapter;
    ArrayList<quakeData> earthquakes=new ArrayList<quakeData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        //check internet connection
        ConnectivityManager cm =(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if(!isConnected)
        {
            ((ProgressBar)findViewById(R.id.progress_bar)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.Empty_text_view)).setText("No Internet Connection!!");
            return;
        }


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter=new quakeAdapter(earthquakes,this);

        getLoaderManager().initLoader(1,null,this);

        earthquakeListView.setAdapter(mAdapter);

        ListView listView= (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(earthquakes!=null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String url = earthquakes.get(i).getURL();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });

        listView.setEmptyView((TextView)findViewById(R.id.Empty_text_view));



    }


    @Override
    public android.content.Loader<ArrayList<quakeData>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this);
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<quakeData>> loader, ArrayList<quakeData> data) {
        mAdapter.clear();
        ProgressBar loading=(ProgressBar)findViewById(R.id.progress_bar);
        loading.setVisibility(View.GONE);

        if(data!=null && !data.isEmpty())
        {
            mAdapter.addAll(data);
        }
        else
        {
            TextView txt=(TextView)findViewById(R.id.Empty_text_view);
            txt.setText("Earthquake Data not found");
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<quakeData>> loader) {
        mAdapter.clear();
    }
}
