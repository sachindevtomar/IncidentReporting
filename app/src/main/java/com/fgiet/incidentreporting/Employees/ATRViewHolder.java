package com.fgiet.incidentreporting.Employees;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fgiet.incidentreporting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by This Pc on 3/30/2018.
 */

public class ATRViewHolder extends RecyclerView.ViewHolder {

   View mView;

    FrameLayout f1,f2,f3,f4,f5;
    ImageView v1,v2,v3,v4,v5;
    ImageButton more;
    TextView postDesc,workerName;
    String imageUrl[]=new String[5],atrNo;

    int imageCount;
    String description,workername1;
    int temp;
    String requestId;

    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("ATR");


    public ATRViewHolder(View itemView) {
        super(itemView);

        mView=itemView;


        v1=(ImageView)mView.findViewById(R.id.customView1);
        v2=(ImageView)mView.findViewById(R.id.customView2);
        v3=(ImageView)mView.findViewById(R.id.customView3);
        v4=(ImageView)mView.findViewById(R.id.customView4);
        v5=(ImageView)mView.findViewById(R.id.customView5);
        f1=(FrameLayout)mView.findViewById(R.id.frame1);
        f2=(FrameLayout)mView.findViewById(R.id.frame2);
        f3=(FrameLayout)mView.findViewById(R.id.frame3);
        f4=(FrameLayout)mView.findViewById(R.id.frame4);
        f5=(FrameLayout)mView.findViewById(R.id.frame5);
        more=(ImageButton)mView.findViewById(R.id.bt_post_more);
        postDesc=(TextView)mView.findViewById(R.id.tv_post_desc);
        workerName=(TextView)mView.findViewById(R.id.tv_worker_name);
        postDesc.setVisibility(View.GONE);

        //ImageView in Dialog
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(mView.getContext());
                LayoutInflater inflater = (LayoutInflater)mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView_overlay=inflater.inflate(R.layout.overlay_post,null);
                final ImageView customView_overlay=(ImageView)mView_overlay.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay=(ImageButton)mView_overlay.findViewById(R.id.close_overlay) ;
                Picasso.with(mView_overlay.getContext()).load(imageUrl[0]).into(customView_overlay);
                mBuilder.setView(mView_overlay);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();
                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(mView.getContext());
                LayoutInflater inflater = (LayoutInflater)mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView_overlay=inflater.inflate(R.layout.overlay_post,null);
                final ImageView customView_overlay=(ImageView)mView_overlay.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay=(ImageButton)mView_overlay.findViewById(R.id.close_overlay) ;
                Picasso.with(mView_overlay.getContext()).load(imageUrl[1]).into(customView_overlay);
                mBuilder.setView(mView_overlay);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(mView.getContext());
                LayoutInflater inflater = (LayoutInflater)mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView_overlay=inflater.inflate(R.layout.overlay_post,null);
                final ImageView customView_overlay=(ImageView)mView_overlay.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay=(ImageButton)mView_overlay.findViewById(R.id.close_overlay) ;
                Picasso.with(mView_overlay.getContext()).load(imageUrl[2]).into(customView_overlay);
                mBuilder.setView(mView_overlay);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(mView.getContext());
                LayoutInflater inflater = (LayoutInflater)mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView_overlay=inflater.inflate(R.layout.overlay_post,null);
                final ImageView customView_overlay=(ImageView)mView_overlay.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay=(ImageButton)mView_overlay.findViewById(R.id.close_overlay) ;
                Picasso.with(mView_overlay.getContext()).load(imageUrl[3]).into(customView_overlay);
                mBuilder.setView(mView_overlay);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(mView.getContext());
                LayoutInflater inflater = (LayoutInflater)mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView_overlay=inflater.inflate(R.layout.overlay_post,null);
                final ImageView customView_overlay=(ImageView)mView_overlay.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay=(ImageButton)mView_overlay.findViewById(R.id.close_overlay) ;
                Picasso.with(mView_overlay.getContext()).load(imageUrl[4]).into(customView_overlay);
                mBuilder.setView(mView_overlay);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

    }


    public void setDesc(String desc) {
        description=desc;
        postDesc.setText(desc);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp==0)
                {
                    postDesc.setVisibility(View.VISIBLE);
                    more.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    temp=1;
                }
                else
                {
                    postDesc.setVisibility(View.GONE);
                    more.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    temp=0;
                }
            }
        });
    }


    public void setWorkername(String workername) {
       workername1=workername;
        workerName.setText(workername1);

    }


    public void setImagecount(int imagecount, String atrno,String requestid) {
        f1.setVisibility(View.GONE);
        f2.setVisibility(View.GONE);
        f3.setVisibility(View.GONE);
        f4.setVisibility(View.GONE);
        f5.setVisibility(View.GONE);
        imageCount=imagecount;
        atrNo=atrno;
        requestId=requestid;
        if(atrNo!=null) {
            mDatabase.child(requestId).child(atrNo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (int i = 0; i < imageCount; i++) {
                            imageUrl[i] = dataSnapshot.child("image_" + i).getValue().toString();
                            switch (i) {
                                case 0: {
                                    f1.setVisibility(View.VISIBLE);
//                            download1.setVisibility(View.VISIBLE);
                                    Picasso.with(mView.getContext()).load(imageUrl[0]).into(v1);
                                }
                                break;
                                case 1: {
                                    f2.setVisibility(View.VISIBLE);
//                            download2.setVisibility(View.VISIBLE);
                                    Picasso.with(mView.getContext()).load(imageUrl[1]).into(v2);
                                }
                                break;
                                case 2: {
                                    f3.setVisibility(View.VISIBLE);
//                            download3.setVisibility(View.VISIBLE);
                                    Picasso.with(mView.getContext()).load(imageUrl[2]).into(v3);
                                }
                                break;
                                case 3: {
                                    f4.setVisibility(View.VISIBLE);
//                            download4.setVisibility(View.VISIBLE);
                                    Picasso.with(mView.getContext()).load(imageUrl[3]).into(v4);
                                }
                                break;
                                case 4: {
                                    f5.setVisibility(View.VISIBLE);
//                            download5.setVisibility(View.VISIBLE);
                                    Picasso.with(mView.getContext()).load(imageUrl[4]).into(v5);
                                }
                                break;
                                default:
                                    Toast.makeText(mView.getContext(), "Check Your Network", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(mView.getContext(), e.getMessage() + imageCount, Toast.LENGTH_SHORT).show();
                        Toast.makeText(mView.getContext(), imageCount+ " imge " + atrNo , Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
            Toast.makeText(mView.getContext(), "Error postno", Toast.LENGTH_SHORT).show();
    }


}
