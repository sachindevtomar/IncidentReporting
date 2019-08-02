package com.fgiet.incidentreporting.Users;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.fgiet.incidentreporting.Employees.PostViewHolder;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.Workers.WorkerRequestActivity;


public class Post extends Fragment {
    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query recent;
    View mView;
    public Post(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.activity_post,container,false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recycler View
        mBlogList = (RecyclerView) mView.findViewById(R.id.blog_list2);
        mBlogList.setHasFixedSize(true);
        getActivity().setTitle(getString(R.string.newsfeed));
        LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        mBlogList.setLayoutManager(linearLayout);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Post");
        recent = myRef.limitToLast(50);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ModelClassPost, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassPost, PostViewHolder>(
                        ModelClassPost.class,
                        R.layout.design_row_post,
                        PostViewHolder.class,
                        recent) {
                    @Override
                    protected void populateViewHolder(PostViewHolder viewHolder, ModelClassPost model, int position) {
//                        viewHolder.setName(model.getName()+" "+position+" "+model.getEmailid()+" "+model.getImage_1());
//                        viewHolder.setImage_0(getActivity().getApplicationContext(), model.getImage_0());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setImagecount(model.getImagecount(),model.getPostno());
                        viewHolder.setLikeButton(model.getPostno());
                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }


}
