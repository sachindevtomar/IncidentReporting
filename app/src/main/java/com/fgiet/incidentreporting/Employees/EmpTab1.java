package com.fgiet.incidentreporting.Employees;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.fgiet.incidentreporting.R;
import com.google.firebase.database.ValueEventListener;


public class EmpTab1 extends Fragment {
    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();;
    Query recent;
    int count=0;
    String userType;
    View mView;
    private DatabaseReference mDataBase;

    private ChildEventListener requestChildEventListener  = new ChildEventListener() {
        @Override
        public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
            count++;
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    FirebaseRecyclerAdapter<ModelClass, BlogViewHolder> firebaseRecyclerAdapter;
    public EmpTab1(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.activity_emp_tab1,container,false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recycler View
        mBlogList = (RecyclerView) mView.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        mBlogList.setLayoutManager(linearLayout);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Request");

        mDataBase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userType=dataSnapshot.child("CaseType").getValue().toString();
                Toast.makeText(getActivity(), ""+userType, Toast.LENGTH_SHORT).show();
                //for Query make a Query object
                myRef.addChildEventListener(requestChildEventListener);
                recent = myRef.orderByChild("type").equalTo(userType).limitToLast(100);

                firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<ModelClass, BlogViewHolder>(
                                ModelClass.class,
                                R.layout.design_row,
                                BlogViewHolder.class,
                                recent) {
                            @Override
                            protected void populateViewHolder(BlogViewHolder viewHolder, ModelClass model, int position) {
//                        viewHolder.setName(model.getName()+" "+position+" "+model.getEmailid()+" "+model.getImage_1());
//                        viewHolder.setImage_0(getActivity().getApplicationContext(), model.getImage_0());
                                try {
                                    viewHolder.setName(model.getName());
                                    viewHolder.setEmailid(model.getEmailid());
                                    viewHolder.setUserid(model.getUserid());
                                    viewHolder.setDate(model.getDate());
                                    viewHolder.setPosition(model.getSerialno());
                                    viewHolder.setRequest(model.getRequestno());
                                    viewHolder.setStatus(model.getStatus());
                                }
                                catch (Exception e){
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                mBlogList.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        myRef.removeEventListener(requestChildEventListener);
    }
}
