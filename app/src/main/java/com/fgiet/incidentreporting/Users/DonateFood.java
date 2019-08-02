package com.fgiet.incidentreporting.Users;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.fgiet.incidentreporting.Employees.PostViewHolder;
import com.fgiet.incidentreporting.R;

public class DonateFood extends AppCompatActivity {

    private RecyclerView mBlogList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query recent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_food);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Recycler View
        mBlogList = (RecyclerView)findViewById(R.id.blog_list_donate);
        mBlogList.setHasFixedSize(true);
        setTitle(getString(R.string.donate));
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
//        linearLayout.setReverseLayout(true);
//        linearLayout.setStackFromEnd(true);
        mBlogList.setLayoutManager(linearLayout);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Donate");
        recent = myRef.limitToLast(500);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

               finish();
                return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ModelDonateFood, DonateViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelDonateFood, DonateViewHolder>(
                        ModelDonateFood.class,
                        R.layout.design_row_donatefood,
                        DonateViewHolder.class,
                        recent) {
                    @Override
                    protected void populateViewHolder(DonateViewHolder viewHolder, ModelDonateFood model, int position) {
//                        viewHolder.setName(model.getName()+" "+position+" "+model.getEmailid()+" "+model.getImage_1());
//                        viewHolder.setImage_0(getActivity().getApplicationContext(), model.getImage_0());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setName(model.getName());
                        viewHolder.setType(model.getType());
                        viewHolder.setPrice(model.getPrice());
                        viewHolder.setImage(model.getImageurl());
                    }
                };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }
}
