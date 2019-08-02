package com.fgiet.incidentreporting.Workers;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.fgiet.incidentreporting.R;

public class EngagedWorkers extends Fragment {

    private View mView;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase,mDatabase1;
    private String requestid;
    Query recent,recent1;
    Button statusUpdate;
    private TextView text;
    ModelClassWorkers mo;
    int i=0;
    String worker[]=new String[100];

    public EngagedWorkers(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.activity_engaged_workers,container,false);
        return mView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recycler View
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.engaged_workers_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase1= FirebaseDatabase.getInstance().getReference().child("Request");
        requestid=getActivity().getIntent().getStringExtra("requestid");
        recent1=mDatabase.orderByChild("RequestId").equalTo(requestid);
        statusUpdate=(Button)mView.findViewById(R.id.bt_status_update);

        recent1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                worker[i]=dataSnapshot.child("UserId").getValue().toString();
                i++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        text=(TextView)mView.findViewById(R.id.no_engaged);
        FirebaseRecyclerAdapter<ModelClassWorkers, WorkersEngagedViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassWorkers, WorkersEngagedViewHolder>(
                        ModelClassWorkers.class,
                        R.layout.design_row_worker_engaged,
                        WorkersEngagedViewHolder.class,recent1) {
                    @Override
                    protected void populateViewHolder(WorkersEngagedViewHolder viewHolder, ModelClassWorkers model, int position) {
//                        viewHolder.setName(model.getName()+" "+position+" "+model.getEmailid()+" "+model.getImage_1());
//                        viewHolder.setImage_0(getActivity().getApplicationContext(), model.getImage_0());
                        viewHolder.setWorkerName(model.getName());
                        viewHolder.setWorkerPhone(model.getPhone());
                        viewHolder.setWorkerId(model.getUserId());
                        viewHolder.setRequestId(requestid);
                        viewHolder.setWorkerFlag(model.getEngaged());
                        if(model!=null){
                            text.setVisibility(View.GONE);
                        }

                        statusUpdate.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mView.getContext(), "Resolved", Toast.LENGTH_SHORT).show();
                                mDatabase1.child(requestid).child("status").setValue("2");
                                for(int j=0;j<i;j++){
                                    mDatabase.child(worker[j]).child("RequestId").setValue("");
                                    mDatabase.child(worker[j]).child("Engaged").setValue("0");
                                }
                            }
                        });
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }



}
