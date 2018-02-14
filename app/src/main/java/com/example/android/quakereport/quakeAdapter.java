package com.example.android.quakereport;

import java.text.DecimalFormat;
import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

/**
 * Created by jaydutt on 10/02/2018.
 */

public class quakeAdapter extends ArrayAdapter {

    quakeData qD;
    public quakeAdapter(ArrayList<quakeData> data, Activity context)
    {
        super(context,0,data);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        qD=(quakeData)getItem(position);
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.quakelistview, parent, false);
        }

        //show only one decimal pace
        DecimalFormat deci_format=new DecimalFormat("0.0");
        TextView txt1=(TextView) convertView.findViewById(R.id.Mag_text_view);
        String magnitude=deci_format.format(qD.getMag());
        txt1.setText(magnitude);
        //set color
        GradientDrawable magnitudeCircle = (GradientDrawable) txt1.getBackground();
        int magnitudeColor = getMagnitudeColor((int)qD.getMag());
        magnitudeCircle.setColor(ContextCompat.getColor(getContext(), magnitudeColor));

        Date date_object=new Date(qD.getTimeMilli());
        SimpleDateFormat df_date=new SimpleDateFormat("LLL dd, yyyy");
        SimpleDateFormat df_time=new SimpleDateFormat("h:mm a");
        String date=df_date.format(date_object);
        String time=df_time.format(date_object);
        TextView txt2=(TextView) convertView.findViewById(R.id.Date_text_view);
        TextView txt3=(TextView) convertView.findViewById(R.id.Time_text_view);

        txt2.setText(date);
        txt3.setText(time);

        //split into two parts one is primary location and other is the offset

        String str=qD.getLocation();
        int t=str.indexOf("of ");
        if(t==-1)
            t=str.indexOf("Of ");
        String mainLocation,offSet;
        if(t==-1) //no "of" string in the location use "Near the"
        {
            offSet="Near the";
            mainLocation=str;
        }
        else
        {
            offSet=str.substring(0,t+2);
            mainLocation=str.substring(t+3);
        }
        TextView txt4=(TextView) convertView.findViewById(R.id.main_location_text_view);
        txt4.setText(mainLocation);

        TextView txt5=(TextView) convertView.findViewById(R.id.offset_text_view);
        txt5.setText(offSet);


        return convertView;
    }
    private int getMagnitudeColor(int t)
    {
        int color;
        switch(t)
        {
            case 0:
            case 1:
                color=R.color.magnitude1;
                break;
            case 2:
                color=R.color.magnitude2;
                break;
            case 3:
                color= R.color.magnitude3;
                break;
            case 4:
                color= R.color.magnitude4;
                break;
            case 5:
                color= R.color.magnitude5;
                break;
            case 6:
                color= R.color.magnitude6;
                break;
            case 7:
                color= R.color.magnitude7;
                break;
            case 8:
                color= R.color.magnitude8;
                break;
            case 9:
                color=R.color.magnitude9;
                break;
            default:
                color= R.color.magnitude10plus;
                break;
        }
        return color;
    }
}
