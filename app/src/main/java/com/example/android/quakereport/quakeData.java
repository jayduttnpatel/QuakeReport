package com.example.android.quakereport;

import java.text.SimpleDateFormat;

/**
 * Created by jaydutt on 10/02/2018.
 */

public class quakeData {
    private float magnitude;
    private String location;
    long timeMilliSec;
    String url;

    public quakeData(float mag,String loc,long D,String website)
    {
        magnitude=mag;
        location=loc;
        timeMilliSec=D;
        url=website;
    }
    /*
    function to get magnitude of the quake
     */
    public float getMag()
    {
        return magnitude;
    }

    /*
    function to get Location of the quake
     */
    public String getLocation()
    {
        return location;
    }

    /*
    function to get Date of the quake
     */

    public long getTimeMilli()
    {return timeMilliSec;}

    public String getURL()
    {return url;}
}
