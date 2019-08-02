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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.fgiet.incidentreporting.R;

public class AvailableWorkers extends Fragment {

    private View mView;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private String requestid;
    private TextView text;
    Query recent;

        public AvailableWorkers(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.activity_available_workers,container,false);
        return mView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recycler View
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.available_workers_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        recent=mDatabase.orderByChild("Engaged").equalTo("0");
        requestid=getActivity().getIntent().getStringExtra("requestid");
    }

    @Override
    public void onStart() {
        super.onStart();
        text=(TextView)mView.findViewById(R.id.no_available);
        FirebaseRecyclerAdapter<ModelClassWorkers, WorkersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassWorkers, WorkersViewHolder>(
                        ModelClassWorkers.class,
                        R.layout.design_row_worker,
                        WorkersViewHolder.class,recent) {
                    @Override
                    protected void populateViewHolder(WorkersViewHolder viewHolder, ModelClassWorkers model, int position) {
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

                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }





}
