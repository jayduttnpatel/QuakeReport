/**
 * Created by jaydutt on 11/02/2018.
 */

package com.example.android.quakereport;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**

 * Helper methods related to requesting and receiving earthquake data from USGS.

 */

public final class QueryUtils
{



    /** Sample JSON response for a USGS query */

    private static String JSON_RESPONSE = null;
    private static final String USGSlink="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private QueryUtils() {

    }

    public static ArrayList<quakeData> extractEarthquakes()
    {
        URL usgsURL= makeURL(USGSlink);
        JSON_RESPONSE=makeNetworkCall(usgsURL);
        return quakeDataParsing(JSON_RESPONSE);
    }

    private static ArrayList<quakeData> quakeDataParsing(String Queryrespons)
    {
        ArrayList<quakeData> earthquakes = new ArrayList<>();
        try
        {
            JSONObject rootObj = new JSONObject(Queryrespons);
            JSONArray features = rootObj.getJSONArray("features");
            for(int i=0;i<features.length();i++)
            {
                JSONObject q_data=features.getJSONObject(i);
                JSONObject properties= q_data.getJSONObject("properties");
                float m=(float)properties.getDouble("mag");
                String l=properties.getString("place");
                long d=properties.getLong("time");
                String urlString=properties.getString("url");
                earthquakes.add(new quakeData(m,l,d,urlString));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }
        // Return the list of earthquakes
        return earthquakes;

    }

    /*
        method to get appropriate url object for given input string link
     */
    private static URL makeURL(String link)
    {
        if(link==null)
            return null; //return null because string is empty
        else
        {
            try {
                return new URL(link); //return url object
            }
            catch(MalformedURLException e)
            {
                return null; //some error occured during url creation
            }
        }
    }

    /*
    This function make network request to usgs website and get JSON responce from it which is then parsed to get information about each earthquake
    and then displayed on the screen
     */
    private static String makeNetworkCall(URL url)
    {
        if(url==null)    //no network call can be made
            return null;

        InputStream dataStream=null; //input stream of data
        HttpURLConnection url_connection=null; //utl connection object
        String inputstreamdata=null;
        try
        {
            url_connection=(HttpURLConnection)url.openConnection(); //make http connection to usgs surver
            url_connection.setRequestMethod("GET");
            url_connection.setConnectTimeout(1500);
            url_connection.setReadTimeout(1500);
            url_connection.connect();
            if(url_connection.getResponseCode()==200) //check if connection was made or not using responce code
            {
                dataStream=url_connection.getInputStream();
                inputstreamdata=readDataFromStream(dataStream);
            }
        }
        catch(IOException e)
        {
            Log.e("QueryUtils", "makeNetworkCall: "+e.getMessage());
        }
        finally {
            //release resources
            try {
                if (dataStream != null)
                    dataStream.close();
            }
            catch(IOException e){
                Log.e("IOException", "makeNetworkCall: "+"Exception during closing input stream" );
            }
            if (url_connection != null)
                url_connection.disconnect();
        }
        return inputstreamdata;
    }

    private static String readDataFromStream(InputStream inp)
    {
        if(inp==null)
            return null;

        StringBuilder responce=new StringBuilder();
        InputStreamReader inputStreamReader=new InputStreamReader(inp, Charset.forName("UTF-8"));//UTF-8 is used to converts bits from input stream to data
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        String line=null;
        try
        {
            do {
                line=bufferedReader.readLine();
                responce.append(line);
            }while(line!=null);
        }
        catch(IOException e)
        {
            Log.e("QueryUtils", "readDataFromStream: "+"error in reading data from input stream" );
        }
        return responce.toString();
    }


}
