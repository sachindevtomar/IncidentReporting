package com.fgiet.incidentreporting.Workers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.MainActivity;
import com.fgiet.incidentreporting.Rough1;

/**
 * Created by 1401480 on 7/19/2017.
 */

public class CheckWorkerRequest extends AppCompatActivity {

    String empUid=null;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    String value,request;
    DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                finish();
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(CheckWorkerRequest.this, MainActivity.class));
                }
            }
        };
        empUid = (String) mAuth.getCurrentUser().getUid();
        final DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(empUid);
        mDatabase1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("Engaged").getValue(String.class);
                request = dataSnapshot.child("RequestId").getValue(String.class);
                if (mAuth.getCurrentUser() != null) {
                    mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Employee = dataSnapshot.child("Employee").getValue(String.class);
                            if (Employee.equals("2")) {
                                if (value.equals("1")) {
                                    finish();
                                    Intent intent = new Intent(CheckWorkerRequest.this, WorkerRequestActivity.class);
                                    intent.putExtra("requestid", request);
                                    startActivity(intent);
                                } else {
                                    finish();
                                    startActivity(new Intent(CheckWorkerRequest.this, Rough1.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "yolo", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
