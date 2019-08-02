package com.fgiet.incidentreporting;

/**
 * Created by 1401480 on 7/17/2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgiet.incidentreporting.Employees.RequestActivity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrackViewHolder extends RecyclerView.ViewHolder {
    View mView;
    String request=null;
    public TrackViewHolder(final View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(itemView.getContext(),RequestActivity.class);
                intent.putExtra("requestid",request);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    public void setRequest(String request1){
        request=request1;
    }

    public void setName(String name) {
        TextView post_name = (TextView) mView.findViewById(R.id.tv_track_name);
        post_name.setText(name);
    }

    public void setEmailid(String emailid){
        TextView email_id=(TextView) mView.findViewById(R.id.tv_track_email);
        email_id.setText(emailid);
    }

    public void setDate(String date1) {
        DateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date d1=null;
        Date d2=new Date();
        Calendar cal=Calendar.getInstance();
        String dateprint="",atTime="";
        boolean onlyday=false;
        DecimalFormat formatter = new DecimalFormat("00");
        try{
            d1=sdf.parse(date1);
            d2=sdf.parse(sdf.format(d2));
            long diff=d2.getTime()- d1.getTime();
            long diffsecs=diff/1000%60;
            long diffmins=diff/(60*1000)%60;
            long diffhours=diff/(60*60*1000)%24;
            long diffdays=diff/(24*60*60*1000);
            if(diffdays>0){
                if(d1.getHours()<12)
                    atTime=d1.getHours()+":"+formatter.format(d1.getMinutes())+"am";
                else{
                    if(d1.getHours()==12)
                        atTime=d1.getHours()+":"+formatter.format(d1.getMinutes())+"pm";
                    else
                        atTime=(d1.getHours()-12)+":"+formatter.format(d1.getMinutes())+"pm";
                }

                cal.setTime(d1);
                int month=cal.get(Calendar.MONTH);
                dateprint=monthInString(month)+" "+d1.getDate()+" at "+atTime;

                onlyday=true;
            }
            if((diffhours>0||diffdays>0)&&!onlyday){
                dateprint=dateprint+" "+diffhours+" hours";
            }
            if((diffmins>0||diffhours>0||diffdays>0)&&!onlyday){
                dateprint=dateprint+" "+diffmins+" mins ago";
            }
            TextView date=(TextView) mView.findViewById(R.id.tv_track_time);
            date.setText(dateprint);
        }
        catch(Exception e){

        }
    }

    public String monthInString(int monthInt){
        switch (monthInt){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Month";
        }
    }

    public void setPosition(int position){
        TextView serial_no=(TextView)mView.findViewById(R.id.tv_track_serialno);
        serial_no.setText("Incident No- "+position);
    }

    public void setStatus(String status){
        ImageView statusRed=(ImageView)mView.findViewById(R.id.status_red);
        ImageView statusYellow=(ImageView)mView.findViewById(R.id.status_yellow);
        ImageView statusGreen=(ImageView)mView.findViewById(R.id.status_green);
        if(status!=null)
        {
            if(status.equals("0")){
                statusRed.setBackgroundResource(R.drawable.selector_ic_status_00_red_24dp);
                statusYellow.setBackgroundResource(R.drawable.selector_ic_status_11_yellow_24dp);
                statusGreen.setBackgroundResource(R.drawable.selector_ic_status_21_green_24dp);
            }
            else if(status.equals("1")){
                statusRed.setBackgroundResource(R.drawable.selector_ic_status_01_red_24dp);
                statusYellow.setBackgroundResource(R.drawable.selector_ic_status_10_yellow_24dp);
                statusGreen.setBackgroundResource(R.drawable.selector_ic_status_21_green_24dp);
            }
            else{
                statusRed.setBackgroundResource(R.drawable.selector_ic_status_01_red_24dp);
                statusYellow.setBackgroundResource(R.drawable.selector_ic_status_11_yellow_24dp);
                statusGreen.setBackgroundResource(R.drawable.selector_ic_status_20_green_24dp);
            }
        }
    }
}