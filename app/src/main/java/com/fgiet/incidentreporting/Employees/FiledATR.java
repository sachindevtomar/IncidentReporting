package com.fgiet.incidentreporting.Employees;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fgiet.incidentreporting.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FiledATR extends AppCompatActivity {

    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query recent;
    String requestid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filed_atr);

        requestid=getIntent().getStringExtra("requestid");

        mBlogList = (RecyclerView)findViewById(R.id.blog_list3);
        mBlogList.setHasFixedSize(true);

        setTitle("Filed ATR");
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        mBlogList.setLayoutManager(linearLayout);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ATR").child(requestid);
        recent=myRef.limitToLast(10);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelClassATR, ATRViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClassATR, ATRViewHolder>(
                        ModelClassATR.class,
                        R.layout.design_row_filed_atr,
                        ATRViewHolder.class,
                        recent
                ) {
            @Override
            protected void populateViewHolder(ATRViewHolder viewHolder, ModelClassATR model, int position) {

                viewHolder.setDesc(model.getDesc());
                viewHolder.setWorkername(model.getWorkername());
                viewHolder.setImagecount(model.getImagecount(),model.getAtrno(),model.getRequestid());
               // viewHolder.setRequestid(model.getRequestid());

            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }
}
