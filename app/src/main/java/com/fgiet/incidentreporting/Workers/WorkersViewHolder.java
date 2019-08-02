package com.fgiet.incidentreporting.Workers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.R;

import org.w3c.dom.Text;

/**
 * Created by This Pc on 7/16/2017.
 */

public class WorkersViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    private String requestid1, engaged1, workerid1, phone1;
    private TextView tvname, tvphone, tvworkerid;
    private DatabaseReference mDatabase, mDatabase1;


    public void setWorkerPhone(String phone) {
        tvphone = (TextView) mView.findViewById(R.id.tv_available_workers_phone);
        tvphone.setText(phone);
        phone1 = phone;
    }

    public void setWorkerName(String name) {
        tvname = (TextView) mView.findViewById(R.id.tv_available_workers_name);
        tvname.setText(name);
    }

    public void setWorkerId(String workerid) {
        tvworkerid = (TextView) mView.findViewById(R.id.tv_available_workers_serialno);
        tvworkerid.setText(workerid);
        workerid1 = workerid;
    }

    public void setRequestId(String requestid) {
        requestid1 = requestid;
    }

    public void setWorkerFlag(String engaged) {
        engaged1 = engaged;
    }

    public WorkersViewHolder(final View itemView) {
        super(itemView);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Request");
        Button call = (Button) itemView.findViewById(R.id.worker_call);
        mView = itemView;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase1.child(requestid1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String pastStatus = dataSnapshot.child("status").getValue(String.class);
                        if (pastStatus.equals("2")) {
                            Toast.makeText(mView.getContext(), "Already Resolved", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(mView.getContext(), "Updating request", Toast.LENGTH_SHORT).show();
                            mDatabase.child(workerid1).child("RequestId").setValue(requestid1);
                            mDatabase.child(workerid1).child("Engaged").setValue("1");
                            mDatabase1.child(requestid1).child("status").setValue("1");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

           }
}
