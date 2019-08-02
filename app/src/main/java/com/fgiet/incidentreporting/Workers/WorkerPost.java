package com.fgiet.incidentreporting.Workers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.fgiet.incidentreporting.Employees.PostViewHolder;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.Users.ModelClassPost;


public class WorkerPost extends AppCompatActivity {
    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query recent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_post);
        //Recycler View
        mBlogList = (RecyclerView)findViewById(R.id.blog_list2);
        mBlogList.setHasFixedSize(true);
        setTitle("News Feed");
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
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
    }
}
