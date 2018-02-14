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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    //usgs query url return top 10 resent earth quakes happen in the world
    private String USGS_QUrl="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    quakeAdapter mAdapter;
    ArrayList<quakeData> earthquakes=new ArrayList<quakeData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter=new quakeAdapter(earthquakes,this);

        new EarthquakeAsynkTask().execute();

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
    }

    private class EarthquakeAsynkTask extends AsyncTask<Void , Void ,ArrayList<quakeData>>
    {

        @Override
        protected ArrayList<quakeData> doInBackground(Void... voids) {
            return QueryUtils.extractEarthquakes();
        }

        @Override
        protected void onPostExecute(ArrayList<quakeData> quakeData) {
            mAdapter.clear();

            if(quakeData!=null && !quakeData.isEmpty())
            {
                mAdapter.addAll(quakeData);
            }
        }
    }
}
