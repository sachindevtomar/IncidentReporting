package com.fgiet.incidentreporting.Employees;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.Workers.WorkerRequestActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 1401480 on 7/18/2017.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {
    View mView,mView1;
    List<String> likers_list;
    BottomSheetDialog dialog;
    ArrayAdapter<String> arrayAdapter;
    String imageUrl[]=new String[5],postNo;
    FrameLayout f1,f2,f3,f4,f5;
    ListView likersList;
    ImageButton mLikeBtn,share,more;
    TextView postDesc,likesCount;
    int imageCount;
    ImageView v1,v2,v3,v4,v5;
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Post");
    DatabaseReference mDatabase1= FirebaseDatabase.getInstance().getReference("LikesCounter");
    DatabaseReference mDatabaseLikes=FirebaseDatabase.getInstance().getReference("Likes");
    DatabaseReference mDatabaseUser=FirebaseDatabase.getInstance().getReference("Users");
    int temp=0;
    Bitmap image;
    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String description;
    int like=0;
    private boolean mProcessLikes=false;

   public PostViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

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
       share=(ImageButton)mView.findViewById(R.id.share);
       postDesc=(TextView)mView.findViewById(R.id.tv_post_desc);
       mLikeBtn=(ImageButton)mView.findViewById(R.id.likes_btn);
       likesCount=(TextView)mView.findViewById(R.id.likescount);

       postDesc.setVisibility(View.GONE);

       share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               new DownloadImage().execute(new Share(imageUrl,imageCount,description));
           }
       });
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

       mLikeBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mProcessLikes=true;
                   mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if(mProcessLikes) {
                               if (dataSnapshot.child(postNo).hasChild(mAuth.getCurrentUser().getUid())) {
                                   mDatabaseLikes.child(postNo).child(mAuth.getCurrentUser().getUid()).removeValue();
                                   updateCounter(false);
                                   mProcessLikes = false;
                               } else {

                                   mDatabaseUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           mDatabaseLikes.child(postNo).child(mAuth.getCurrentUser().getUid()).setValue(dataSnapshot.child("Name").getValue().toString());
                                           updateCounter(true);
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {

                                       }
                                   });

                                   mProcessLikes = false;
                               }
                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

               if(like==1){
                   mLikeBtn.setImageResource(R.drawable.selector_ic_thumb_down_red_24dp);
                   like=0;
               }
               else{
                   mLikeBtn.setImageResource(R.drawable.selector_ic_thumb_up_green_24dp);
                   like=1;
               }
               }

       });

       mView1=LayoutInflater.from(mView.getContext()).inflate(R.layout.overlay_likers,null);
       dialog = new BottomSheetDialog(mView.getContext());
       dialog.setContentView(mView1);
       likersList=(ListView)mView1.findViewById(R.id.likersList);

       likesCount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               likers_list = new ArrayList<String>();
               arrayAdapter = new ArrayAdapter<String>
                       (mView1.getContext(), android.R.layout.simple_list_item_1, likers_list);
               likersList.setAdapter(arrayAdapter);
               dialog.show();
               mDatabaseLikes.child(postNo).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                           likers_list.add(childDataSnapshot.getValue().toString());
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
               arrayAdapter.notifyDataSetChanged();
           }
       });
   }

    private void updateCounter(final boolean increment) {
        mDatabase1.child(postNo).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() != null) {
                    int value = mutableData.getValue(Integer.class);
                    if(increment) {
                        value++;
                    } else {
                        value--;
                    }
                    mutableData.setValue(value);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
            }
        });
    }

    private static class Share{
        String shareImage[]=new String[5];
        int shareCount;
        String shareDesc;

        public Share(String[] shareImage, int shareCount, String shareDesc) {
            this.shareImage = shareImage;
            this.shareCount = shareCount;
            this.shareDesc = shareDesc;
        }
    }

    private static class ShareBit{
        Bitmap shareBitmap[]=new Bitmap[5];
        int shareBitCount;
        String shareBitDesc;

        public ShareBit(Bitmap[] shareBitmap, int shareBitCount, String shareBitDesc) {
            this.shareBitmap = shareBitmap;
            this.shareBitCount = shareBitCount;
            this.shareBitDesc = shareBitDesc;
        }
    }

    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<Share, Void, ShareBit> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(mView.getContext());
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected ShareBit doInBackground(Share... shareData) {
            Bitmap bitmap[] = new Bitmap[5];
            try {
                // Download Image from URL
                for(int i=0;i<shareData[0].shareCount;i++) {
                    InputStream input = new java.net.URL(shareData[0].shareImage[i]).openStream();
                    // Decode Bitmap
                    bitmap[i] = BitmapFactory.decodeStream(input);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ShareBit(bitmap,shareData[0].shareCount,shareData[0].shareDesc);
        }

        @Override
        protected void onPostExecute(ShareBit results) {
            // Close progressdialog
            mProgressDialog.dismiss();
            Uri bitBit[]=new Uri[5];
            ArrayList<Uri> imageUris=new ArrayList<Uri>();
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_TEXT, results.shareBitDesc);
//       String path = MediaStore.Images.Media.insertImage(getContentResolver(), loadedImage, "", null);
//       Uri screenshotUri = Uri.parse(path);
            for(int i=0;i<results.shareBitCount;i++){
                bitBit[i]=getImageUri(mView.getContext(),results.shareBitmap[i]);
                imageUris.add(bitBit[i]);
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            intent.setType("image/*");
            mView.getContext().startActivity(Intent.createChooser(intent, "Share image via..."));

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void setLikeButton(final String postid){

        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    likesCount.setText(" Likes: "+dataSnapshot.child(postid).getValue().toString());
                }
                catch(Exception e){
                    Toast.makeText(mView.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseLikes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postid).hasChild(mAuth.getCurrentUser().getUid())){
                    mLikeBtn.setImageResource(R.drawable.selector_ic_thumb_down_red_24dp);
                    like=0;
                }
                else{
                    mLikeBtn.setImageResource(R.drawable.selector_ic_thumb_up_green_24dp);
                    like=1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setImagecount(int imagecount,String postno) {
        f1.setVisibility(View.GONE);
        f2.setVisibility(View.GONE);
        f3.setVisibility(View.GONE);
        f4.setVisibility(View.GONE);
        f5.setVisibility(View.GONE);
        imageCount=imagecount;
        postNo=postno;
        if(postNo!=null) {
            mDatabase.child(postNo).addValueEventListener(new ValueEventListener() {
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
                        Toast.makeText(mView.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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


    public void setDate(String date1) {
        DateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date d1=null;
        Date d2=new Date();
        Calendar cal=Calendar.getInstance();
        String dateprint="",atTime="";
        boolean onlyday=false;
        DecimalFormat formatter = new DecimalFormat("00");
        try{
            d1=sdf.parse(date1);
            d2=sdf.parse(sdf.format(d2));
            long diff=d2.getTime()- d1.getTime();
            long diffsecs=diff/1000%60;
            long diffmins=diff/(60*1000)%60;
            long diffhours=diff/(60*60*1000)%24;
            long diffdays=diff/(24*60*60*1000);
            if(diffdays>0){
                if(d1.getHours()<12)
                    atTime=d1.getHours()+":"+formatter.format(d1.getMinutes())+"am";
                else{
                    if(d1.getHours()==12)
                        atTime=d1.getHours()+":"+formatter.format(d1.getMinutes())+"pm";
                    else
                        atTime=(d1.getHours()-12)+":"+formatter.format(d1.getMinutes())+"pm";
                }

                    cal.setTime(d1);
                    int month=cal.get(Calendar.MONTH);
                    dateprint=monthInString(month)+" "+d1.getDate()+" at "+atTime;

                onlyday=true;
            }
            if((diffhours>0||diffdays>0)&&!onlyday){
                dateprint=dateprint+" "+diffhours+" hours";
            }
            if((diffmins>0||diffhours>0||diffdays>0)&&!onlyday){
                dateprint=dateprint+" "+diffmins+" mins ago";
            }
            TextView date=(TextView) mView.findViewById(R.id.tv_post_time);
            date.setText(dateprint);
        }
        catch(Exception e){

        }
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

    public String monthInString(int monthInt){
        switch (monthInt){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Month";
        }
    }
}
