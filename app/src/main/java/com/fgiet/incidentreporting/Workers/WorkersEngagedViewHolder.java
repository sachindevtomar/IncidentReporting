package com.fgiet.incidentreporting.Workers;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.fgiet.incidentreporting.R;

/**
 * Created by This Pc on 7/16/2017.
 */

public class WorkersEngagedViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    private String requestid1,engaged1,workerid1,phone1;
    private TextView tvname,tvphone;
    private DatabaseReference mDatabase,mDatabase1;
    Button call = (Button) itemView.findViewById(R.id.worker_call);
    Button msg = (Button) itemView.findViewById(R.id.worker_msg);

    public void setWorkerPhone(String phone) {
        tvphone=(TextView)mView.findViewById(R.id.tv_engaged_workers_phone);
        tvphone.setText(phone);
        phone1=phone;
    }

    public void setWorkerName(String name) {
        tvname=(TextView)mView.findViewById(R.id.tv_engaged_workers_name);
        tvname.setText(name);
    }

    public void setWorkerId(String workerid) {
        workerid1=workerid;
    }

    public void setRequestId(String requestid) {
        requestid1=requestid;
    }

    public void setWorkerFlag(String engaged) {
        engaged1=engaged;
    }

    public WorkersEngagedViewHolder(final View itemView) {
        super(itemView);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mView = itemView;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(workerid1).child("RequestId").setValue("");
                mDatabase.child(workerid1).child("Engaged").setValue("0");
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone1));
                if (ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mView.getContext().startActivity(intent);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(mView.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Send Message")
                        .setMessage("Are you sure you want to sent message to "+tvname.getText().toString().toUpperCase() +" ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendMsg();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

    }

    public void sendMsg(){
        String message = "An incident is assigned to you, Check it on Accident Reporting App";
        String phoneNumber = "+91"+phone1;
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(mView.getContext(), 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(mView.getContext(), 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        mView.getContext().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(mView.getContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(mView.getContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(mView.getContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(mView.getContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(mView.getContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        mView.getContext().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(mView.getContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(mView.getContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

}
