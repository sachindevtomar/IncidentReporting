package com.fgiet.incidentreporting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.fgiet.incidentreporting.Employees.BlogViewHolder;
import com.fgiet.incidentreporting.Employees.ModelClass;

/**
 * Created by 1401480 on 7/17/2017.
 */

public class TrackRequest extends Fragment {
    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query recent;
    FirebaseAuth mAuth;
    private TextView text;
    View mView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_track_request,container,false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recycler View
        mBlogList = (RecyclerView) mView.findViewById(R.id.blog_list1);
        mBlogList.setHasFixedSize(true);
        mAuth=FirebaseAuth.getInstance();
        LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        mBlogList.setLayoutManager(linearLayout);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Request");
        if(mAuth.getCurrentUser()!=null)
        {
            recent = myRef.limitToLast(1000).orderByChild("userid").equalTo(mAuth.getCurrentUser().getUid());
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        text=(TextView)mView.findViewById(R.id.no_incident);
        FirebaseRecyclerAdapter<ModelClass, TrackViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClass, TrackViewHolder>(
                        ModelClass.class,
                        R.layout.design_row_track,
                        TrackViewHolder.class,
                        recent) {
                    @Override
                    protected void populateViewHolder(TrackViewHolder viewHolder, ModelClass model, int position) {
//                        viewHolder.setName(model.getName()+" "+position+" "+model.getEmailid()+" "+model.getImage_1());
//                        viewHolder.setImage_0(getActivity().getApplicationContext(), model.getImage_0());
                        viewHolder.setName(model.getName());
                        viewHolder.setEmailid(model.getEmailid());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setPosition(model.getSerialno());
                        viewHolder.setRequest(model.getRequestno());
                        viewHolder.setStatus(model.getStatus());
                        if(model!=null){
                            text.setVisibility(View.GONE);
                        }
                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
}
