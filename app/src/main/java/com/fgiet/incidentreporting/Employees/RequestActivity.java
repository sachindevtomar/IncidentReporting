package com.fgiet.incidentreporting.Employees;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.WorkaroundMapFragment;
import com.fgiet.incidentreporting.Workers.Workers;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class RequestActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap requestMap;
    private Marker marker;
    private String requestid,name="",email="",desc="",phone="",locality="",sublocality="",userid="",thoroughfare="",date,employee="",blocked="";
    private String imageUrl[]=new String[5];
    private double lat=0.0,lag=0.0;
    private int imageCount=0;
    private DatabaseReference mDatabase,mDatabase1,mDatabase2,mDatabase3;
    private TextView nametv,emailtv,desctv,phonetv,locationtv,datetv,assign,assigncheck;
    private ImageView image1,image2,image3,image4,image5;
    private ImageButton download1,download2,download3,download4,download5;
    ACProgressFlower mProgressDialog;
    FirebaseAuth mAuth;
    private Button mWorker,mBlocked, mFiledAtr;
    private ScrollView mScrollView;
    private ValueEventListener valueEventListener;
    String assignWorker="";
    int m=0;
    Query query;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mFiledAtr=(Button)findViewById(R.id.bt_filed_atr);
        mScrollView=(ScrollView)findViewById(R.id.scrollView);
        mWorker=(Button)findViewById(R.id.bt_workers);
        mBlocked=(Button)findViewById(R.id.bt_blocked);
        nametv=(TextView)findViewById(R.id.tv_request_name);
        emailtv=(TextView)findViewById(R.id.tv_request_email);
        phonetv=(TextView)findViewById(R.id.tv_request_phone);
        desctv=(TextView)findViewById(R.id.tv_request_desc);
        locationtv=(TextView)findViewById(R.id.tv_request_location);
        datetv=(TextView)findViewById(R.id.tv_request_date);
        assign=(TextView)findViewById(R.id.tv_request_assign);
        assigncheck=(TextView)findViewById(R.id.tv_request_assign_check);
        image1=(ImageView)findViewById(R.id.iv_request_image1);
        image2=(ImageView)findViewById(R.id.iv_request_image2);
        image3=(ImageView)findViewById(R.id.iv_request_image3);
        image4=(ImageView)findViewById(R.id.iv_request_image4);
        image5=(ImageView)findViewById(R.id.iv_request_image5);
        download1=(ImageButton)findViewById(R.id.ib_request_download1);
        download2=(ImageButton)findViewById(R.id.ib_request_download2);
        download3=(ImageButton)findViewById(R.id.ib_request_download3);
        download4=(ImageButton)findViewById(R.id.ib_request_download4);
        download5=(ImageButton)findViewById(R.id.ib_request_download5);
        image1.setVisibility(View.GONE);
        image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);
        image4.setVisibility(View.GONE);
        image5.setVisibility(View.GONE);
        download1.setVisibility(View.GONE);
        download2.setVisibility(View.GONE);
        download3.setVisibility(View.GONE);
        download4.setVisibility(View.GONE);
        download5.setVisibility(View.GONE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.requestmap);
        mapFragment.getMapAsync(this);

        mapFragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });


        Intent intent = getIntent();
        requestid = intent.getStringExtra("requestid");
        mWorker.setVisibility(View.GONE);
        mBlocked.setVisibility(View.GONE);
        mFiledAtr.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        mDatabase1 = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employee=dataSnapshot.child("Employee").getValue().toString();
                if(employee.equals("1")){
                    mWorker.setVisibility(View.VISIBLE);
                    mBlocked.setVisibility(View.VISIBLE);
                    mFiledAtr.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mDatabase2=FirebaseDatabase.getInstance().getReference("Users");
        query=mDatabase2.orderByChild("RequestId").equalTo(requestid);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                m++;
                if(m!=1)
                {
                    assign.setText(assign.getText()+"\n\n");
                }
                assigncheck.setVisibility(View.GONE);
                assign.setText(assign.getText()+""+m+". "+dataSnapshot.child("Name").getValue(String.class)+
                        "\n"+"   "+dataSnapshot.child("Phone").getValue(String.class));
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



        mWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RequestActivity.this,Workers.class);
                intent.putExtra("requestid",requestid);
                startActivity(intent);
            }
        });

        mFiledAtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RequestActivity.this,FiledATR.class);
                intent.putExtra("requestid",requestid);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Request").child(requestid);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name=dataSnapshot.child("name").getValue().toString();
                email=dataSnapshot.child("emailid").getValue().toString();
                phone=dataSnapshot.child("phone").getValue().toString();
                desc=dataSnapshot.child("desc").getValue().toString();
                locality=dataSnapshot.child("locality").getValue().toString();
                sublocality=dataSnapshot.child("sublocality").getValue().toString();
                thoroughfare=dataSnapshot.child("thoroughfare").getValue().toString();
                lat=Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                lag=Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                imageCount=Integer.parseInt(dataSnapshot.child("imagecount").getValue().toString());
                date=dataSnapshot.child("date").getValue().toString();
                userid=dataSnapshot.child("userid").getValue().toString();


                mDatabase3 = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                mDatabase3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        try{
                            blocked=dataSnapshot1.child("Blocked").getValue().toString();
                        }
                        catch (NullPointerException e){
                            blocked="0";
                        }
//                        Toast.makeText(RequestActivity.this,"it is "+blocked, Toast.LENGTH_SHORT).show();
                        if(blocked.equals("0")){
                            mBlocked.setText(" Block");
                        }
                        else{
                            mBlocked.setText(" Unblock");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError1) {

                    }
                });

                for(int i=0;i<imageCount;i++){
                    imageUrl[i]=dataSnapshot.child("image_"+i).getValue().toString();
                    switch(i){
                        case 0:{
                            image1.setVisibility(View.VISIBLE);
                            download1.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(imageUrl[0]).into(image1);
                        }
                        break;
                        case 1:{
                            image2.setVisibility(View.VISIBLE);
                            download2.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(imageUrl[1]).into(image2);
                        }
                        break;
                        case 2:{
                            image3.setVisibility(View.VISIBLE);
                            download3.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(imageUrl[2]).into(image3);
                        }
                        break;
                        case 3:{
                            image4.setVisibility(View.VISIBLE);
                            download4.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(imageUrl[3]).into(image4);
                        }
                        break;
                        case 4:{
                            image5.setVisibility(View.VISIBLE);
                            download5.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(imageUrl[4]).into(image5);
                        }
                        break;
                        default:
                            Toast.makeText(RequestActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                    }
                }
                if(name.equals(""))
                {
                    nametv.setTextColor(Color.parseColor("#FF0000"));
                    nametv.setText("Not Given");
                }
                else
                    nametv.setText(name);

                if(email.equals(""))
                {
                    emailtv.setTextColor(Color.parseColor("#FF0000"));
                    emailtv.setText("Not Given");
                }
                else
                    emailtv.setText(email);

                if(phone.equals(""))
                    phonetv.setVisibility(View.GONE);
                else
                    phonetv.setText(phone);

                if(desc.equals(""))
                {
                    desctv.setTextColor(Color.parseColor("#FF0000"));
                    desctv.setText("Not Given");
                }
                else
                    desctv.setText(desc);
                locationtv.setText(thoroughfare+", "+sublocality+", "+locality);
                datetv.setText(date);

                onMapReady(requestMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        download1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute DownloadImage AsyncTask
                new DownloadImage().execute(imageUrl[0]);
            }
        });
        download2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute DownloadImage AsyncTask
                new DownloadImage().execute(imageUrl[1]);
            }
        });
        download3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute DownloadImage AsyncTask
                new DownloadImage().execute(imageUrl[2]);
            }
        });
        download4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute DownloadImage AsyncTask
                new DownloadImage().execute(imageUrl[3]);
            }
        });
        download5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute DownloadImage AsyncTask
                new DownloadImage().execute(imageUrl[4]);
            }
        });

        mBlocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blocked.equals("0")) {
                    new AlertDialog.Builder(RequestActivity.this)
                            .setIcon(R.drawable.ic_block_red_24dp)
                            .setTitle("Report")
                            .setMessage("Block "+name.toUpperCase()+" ? Blocked users will no longer be able to report incidents.")
                            .setPositiveButton("Block", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    blockUser();
                                }

                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                else{
                    new AlertDialog.Builder(RequestActivity.this)
                            .setIcon(R.drawable.ic_block_red_24dp)
                            .setTitle("Report")
                            .setMessage("Unblock "+name.toUpperCase()+" ?")
                            .setPositiveButton("Unblock", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    blockUser();
                                }

                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });

    }

    public void blockUser(){
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(blocked.equals("0")){
                    mDatabase3.child("Blocked").setValue("1");
                    Toast.makeText(RequestActivity.this,name+" is now Blocked", Toast.LENGTH_SHORT).show();
                }
                else if(blocked.equals("1")){
                    mDatabase3.child("Blocked").setValue("0");
                    Toast.makeText(RequestActivity.this,name+" is now Unblocked", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase3.addListenerForSingleValueEvent(valueEventListener);
    }

    //map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        requestMap=googleMap;
        if(marker!=null){
            marker.remove();
        }
        LatLng latLng = new LatLng(lat,lag);
        requestMap.getUiSettings().setZoomControlsEnabled(true);
        marker=requestMap.addMarker(new MarkerOptions().position(latLng).title("India"));
        requestMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lag), 17.0f));
    }


    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ACProgressFlower.Builder(RequestActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Downloading")
                    .fadeColor(Color.DKGRAY).build();
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            storeImage(result);
            // Close progressdialog
            Toast.makeText(RequestActivity.this,"Saved in InternalStorage/Jeevaashraya/Downloads", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Jeevaashraya"
                + "/Downloads");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName="image_"+timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
