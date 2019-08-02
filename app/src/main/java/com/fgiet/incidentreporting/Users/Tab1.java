package com.fgiet.incidentreporting.Users;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.fgiet.incidentreporting.Employees.EmpSignUp;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.ZoomableImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static android.app.Activity.RESULT_OK;
import static com.fgiet.incidentreporting.Users.Home.locationEnabledIntend;
import static java.lang.Double.compare;


/**
 * Created by This Pc on 7/10/2017.
 */

public class Tab1 extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;

    MapView mapView;
    View mView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double lat = 0.0, lag = 0.0;

    //Image Variables
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private static final int VOICE_SPEECH = 3;

    private Button btn_choose_image;
    int imageArray[] = new int[5];
    private Button send;
    private ProgressDialog progressDialog;
    private Uri selectedImage = null;
    private byte[] databaos1 = null, databaos2 = null, databaos3 = null, databaos4 = null, databaos5 = null;
    private ImageView mImageView1, mImageView2, mImageView3, mImageView4, mImageView5;
    private ImageButton close1, close2, close3, close4, close5;
    private FrameLayout frame1, frame2, frame3, frame4, frame5;
    int check = 0, count = 0, temp = 0, temp1 = 0, temp2 = 0;
    Bitmap bitmap = null, bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null, bitmap5 = null;
    int k = 0,value;
    String imageUrl[] = new String[5], phonestr="", namestr="", subLocality = "", thoRoughFare = "", locality = "", emailstr="",blocked,descstr="";
    private Uri downloadUrl = null;
    private EditText desc;
    private FirebaseAuth mAuth;
    DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date date;
    Marker marker = null;
    Snackbar snackbar;
    View sbView;
    private AlertDialog dialog;
    private ValueEventListener valueEventListenerUser1,valueEventListener1,valueEventListenerPhone;

    Spinner spinnerType;
    ArrayAdapter<CharSequence> adapter;
    String type="Select Case type";

    //Database Variables
    private DatabaseReference mDatabase, newPost,mDatabaseUser,mDatabaseUser1;
    private DatabaseReference mDatabaseRequestCounter=FirebaseDatabase.getInstance().getReference("RequestCounter");

    public Tab1() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab1, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Request");

        spinnerType=(Spinner)mView.findViewById(R.id.spinnerType1);
        adapter=ArrayAdapter.createFromResource(getActivity(),R.array.type_category,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type=adapterView.getItemAtPosition(position).toString();
                try {
                    if (type.equals("Select Case type"))
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.rgb(255, 0, 0));
                    else
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.rgb(0, 105, 92));
                }
                catch(Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


           return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mapView = (MapView) mView.findViewById(R.id.map);
        imageArray[0] = imageArray[1] = imageArray[2] = imageArray[3] = imageArray[4] = 0;
        mImageView1 = (ImageView) mView.findViewById(R.id.customView1);
        mImageView2 = (ImageView) mView.findViewById(R.id.customView2);
        mImageView3 = (ImageView) mView.findViewById(R.id.customView3);
        mImageView4 = (ImageView) mView.findViewById(R.id.customView4);
        mImageView5 = (ImageView) mView.findViewById(R.id.customView5);
        close1 = (ImageButton) mView.findViewById(R.id.button1);
        close2 = (ImageButton) mView.findViewById(R.id.button2);
        close3 = (ImageButton) mView.findViewById(R.id.button3);
        close4 = (ImageButton) mView.findViewById(R.id.button4);
        close5 = (ImageButton) mView.findViewById(R.id.button5);
        frame1 = (FrameLayout) mView.findViewById(R.id.frame1);
        frame2 = (FrameLayout) mView.findViewById(R.id.frame2);
        frame3 = (FrameLayout) mView.findViewById(R.id.frame3);
        frame4 = (FrameLayout) mView.findViewById(R.id.frame4);
        frame5 = (FrameLayout) mView.findViewById(R.id.frame5);
        btn_choose_image = (Button) mView.findViewById(R.id.btn_choose_image);
        FloatingActionButton send = (FloatingActionButton) mView.findViewById(R.id.button);
        desc = (EditText) mView.findViewById(R.id.desc);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mDatabaseUser1 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        valueEventListenerUser1= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Blocked")) {
                    blocked=dataSnapshot.child("Blocked").getValue(String.class);
                }
                else{
                    blocked="0";
                    mDatabaseUser1.child("Blocked").setValue("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseUser1.addValueEventListener(valueEventListenerUser1);


        //picture
        progressDialog = new ProgressDialog(getActivity());

        frame1.setVisibility(View.GONE);
        frame2.setVisibility(View.GONE);
        frame3.setVisibility(View.GONE);
        frame4.setVisibility(View.GONE);
        frame5.setVisibility(View.GONE);

        final String[] items = new String[]{getString(R.string.from_camera), getString(R.string.from_gallery)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);

        //For Dialog Appearance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_image));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                    dialog.cancel();
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete Action Using"), PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();

        //To choose Image
        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
                if (count != 5) {
                    dialog.show();
                } else {
                    snackbar=Snackbar.make(mView, "Select Maximum 5 images4", Snackbar.LENGTH_LONG);
                    sbView=snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTypeface(null, Typeface.BOLD);
                    snackbar.show();
                }
            }
        });

        //ImageView in Dialog
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap1);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap2);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap3);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap4);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();
                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap5);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        //Close Button in Overlay
        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[0] = 0;
                count = 0;
                frame1.setVisibility(View.GONE);
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[1] = 0;
                count = 0;
                frame2.setVisibility(View.GONE);
            }
        });

        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[2] = 0;
                count = 0;
                frame3.setVisibility(View.GONE);
            }
        });

        close4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[3] = 0;
                count = 0;
                frame4.setVisibility(View.GONE);
            }
        });

        close5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[4] = 0;
                count = 0;
                frame5.setVisibility(View.GONE);
            }
        });

        //Send Button for firebase
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable())
                    checkPhone();
                else {
                    snackbar=Snackbar.make(mView, "No Internet connection", Snackbar.LENGTH_LONG);
                    sbView=snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTypeface(null, Typeface.BOLD);
                    snackbar.show();
                }
            }
        });

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        mapView.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lag = location.getLongitude();
                onMapReady(mGoogleMap);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(getActivity(), "Please Turn On your Location", Toast.LENGTH_LONG).show();
                if(locationEnabledIntend==0){
                    locationEnabledIntend++;
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        };


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //configureButton();
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
        } else {
            //configureButton();
        }

        desc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (v.getId() ==R.id.desc) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (desc.getRight() - desc.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        speechMethod();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    //picture
    public void addImage() {
        int i = 0;
        for (; i < 5; i++) {
            if (imageArray[i] == 0) {
                temp = i + 1;
                break;
            }
        }
        if (i == 5)
            count = i;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PICK_FROM_CAMERA) {
            //get the camera image
            check = 1;
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageSet(baos, bitmap);

        } else if(requestCode==PICK_FROM_FILE) {
            //get the gallery image
            check = 2;
            if (resultCode == RESULT_OK) {

                selectedImage = data.getData();

                try {
                    bitmap = decodeBitmap(selectedImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    imageSet(baos, bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            if(data!=null)
            {
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String tempString=result.get(0);
                if(!tempString.equals("clear all"))
                    desc.setText(desc.getText()+" "+tempString);
                else{
                    desc.setText("");
                    Toast.makeText(getActivity(), "Cleared", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void speechMethod(){
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something and for clearing text in the box say 'clear all'");

        try{
            startActivityForResult(i,VOICE_SPEECH);
        }
        catch(ActivityNotFoundException e){
            Toast.makeText(getActivity(), "Your device does not support this feature", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public void imageSet(ByteArrayOutputStream baos, Bitmap bitmapTemp) {
        if (temp == 1) {
            databaos1 = baos.toByteArray();
            bitmap1 = bitmapTemp;
            mImageView1.setImageBitmap(bitmap);
            imageArray[0] = 1;
            frame1.setVisibility(View.VISIBLE);
        } else if (temp == 2) {
            databaos2 = baos.toByteArray();
            bitmap2 = bitmapTemp;
            mImageView2.setImageBitmap(bitmap);
            imageArray[1] = 1;
            frame2.setVisibility(View.VISIBLE);
        } else if (temp == 3) {
            bitmap3 = bitmapTemp;
            databaos3 = baos.toByteArray();
            mImageView3.setImageBitmap(bitmap);
            imageArray[2] = 1;
            frame3.setVisibility(View.VISIBLE);
        } else if (temp == 4) {
            bitmap4 = bitmapTemp;
            databaos4 = baos.toByteArray();
            mImageView4.setImageBitmap(bitmap);
            imageArray[3] = 1;
            frame4.setVisibility(View.VISIBLE);
        } else {
            bitmap5 = bitmapTemp;
            databaos5 = baos.toByteArray();
            mImageView5.setImageBitmap(bitmap);
            imageArray[4] = 1;
            frame5.setVisibility(View.VISIBLE);
        }
    }

    public void checkPhone(){
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
        View mmView=getActivity().getLayoutInflater().inflate(R.layout.user_phone,null);
        final EditText mKey=(EditText) mmView.findViewById(R.id.etphone);
        Button mOk=(Button) mmView.findViewById(R.id.okbtn);
        mBuilder.setView(mmView);
        dialog=mBuilder.create();

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonestr=mKey.getText().toString();
                if(phonestr.length()!=10)
                {
                    Toast.makeText(getActivity(), "Enter valid Phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    mDatabaseUser.child("Phone").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mDatabaseUser.child("Phone").setValue(phonestr);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    sendData();
                }
                dialog.dismiss();
            }
        });

        valueEventListenerPhone=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Phone")) {
                    phonestr = dataSnapshot.child("Phone").getValue(String.class);
                    sendData();
                }
                else{
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseUser.addListenerForSingleValueEvent(valueEventListenerPhone);
    }


    public void sendData() {
        descstr = desc.getText().toString().trim();

        if(type.equals("Select Case type")){
            Toast.makeText(getActivity(), "Select any Case type", Toast.LENGTH_SHORT).show();
            return;
        }

        if(blocked.equals("1")){
            Toast.makeText(getActivity(), "You are blocked. For getting Unblocked go to ContactUs section and send us a email", Toast.LENGTH_LONG).show();
            temp2=0;
            temp1=0;
            return;
        }

        for (k = 0; k < 5 && imageArray[k] == 1; k++) {
            temp1++;
        }

        if (temp1 != 0) {
            if (compare(lat, 0.0) != 0) {
                progressDialog.setMessage("Uploding image...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                //Firebase storage folder where you want to put the images


                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                for (k = 0; k < 5 && imageArray[k] == 1; k++) { //name of the image file (add time to have different files to avoid rewrite on the same file)
                    StorageReference imagesRef = storageRef.child("Picker").child("Camera_" + k + "_" + new Date().getTime());
//            //send this name to database
//            //upload image

                    UploadTask uploadTask = null;
                    if (k == 0) {
                        uploadTask = imagesRef.putBytes(databaos1);
                        storeImage(bitmap1, k);
                    } else if (k == 1) {
                        uploadTask = imagesRef.putBytes(databaos2);
                        storeImage(bitmap2, k);
                    } else if (k == 2) {
                        uploadTask = imagesRef.putBytes(databaos3);
                        storeImage(bitmap3, k);
                    } else if (k == 3) {
                        uploadTask = imagesRef.putBytes(databaos4);
                        storeImage(bitmap4, k);
                    } else {
                        uploadTask = imagesRef.putBytes(databaos5);
                        storeImage(bitmap5, k);
                    }
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            snackbar=Snackbar.make(mView, "Uploading Failed", Snackbar.LENGTH_LONG);
                            sbView=snackbar.getView();
                            sbView.setBackgroundColor(Color.RED);
                            TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTypeface(null, Typeface.BOLD);
                            snackbar.show();
                            temp1=0;
                            temp2=0;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            downloadUrl = taskSnapshot.getDownloadUrl();
                            imageUrl[temp2] = downloadUrl.toString();
                            temp2++;
                            if (temp1 == temp2) {
                                progressDialog.dismiss();

                                updateCounter(true);

                                Toast.makeText(getActivity(), "Upload done", Toast.LENGTH_SHORT).show();
                                locationManager.removeUpdates(locationListener);
                                startActivity(new Intent(getActivity(), Home.class));
                                getActivity().finish();
                            }
                        }
                    });
                }

            }
            else {

                snackbar=Snackbar.make(mView, "Wait For Location", Snackbar.LENGTH_LONG);
                sbView=snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                tv.setTypeface(null, Typeface.BOLD);
                snackbar.show();
                temp1 = 0;
            }
        } else {
            snackbar=Snackbar.make(mView, "Select atleast one image", Snackbar.LENGTH_LONG);
            sbView=snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
            tv.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
    }

    private void updateCounter(final boolean increment) {
        mDatabaseRequestCounter.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() != null) {
                    value = mutableData.getValue(Integer.class);
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
                 mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child((String) mAuth.getCurrentUser().getUid());
                 valueEventListener1=new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        date=new Date();
                        namestr = dataSnapshot.child("Name").getValue(String.class);
                        emailstr = dataSnapshot.child("EmailId").getValue(String.class);
                        phonestr= dataSnapshot.child("Phone").getValue(String.class);
                        newPost = mDatabase.push();
                        newPost.child("name").setValue(namestr);
                        newPost.child("desc").setValue(descstr);
                        newPost.child("phone").setValue(phonestr);
                        newPost.child("latitude").setValue(lat);
                        newPost.child("longitude").setValue(lag);
                        newPost.child("imagecount").setValue(temp1);
                        newPost.child("locality").setValue(locality);
                        newPost.child("sublocality").setValue(subLocality);
                        newPost.child("userid").setValue(mAuth.getCurrentUser().getUid());
                        newPost.child("thoroughfare").setValue(thoRoughFare);
                        newPost.child("emailid").setValue(emailstr);
                        newPost.child("date").setValue(sdf.format(date).toString());
                        newPost.child("requestno").setValue(newPost.getKey());
                        newPost.child("status").setValue("0");
                        newPost.child("serialno").setValue(value);
                        newPost.child("type").setValue(type);
                        for (k = 0; k < temp2; k++) {
                            newPost.child("image_" + k).setValue(imageUrl[k]);
                        }
                    }
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                mDatabaseUser.addValueEventListener(valueEventListener1);
            }
        });
    }


    private void storeImage(Bitmap image, int k) {
        File pictureFile = getOutputMediaFile(k);
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
    private File getOutputMediaFile(int k) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Jeevaashraya"
                + "/Images");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "image_" + k + "_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureButton();
                }
                return;
        }
    }


    //Configur button
    public void configureButton() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
    }


    //Map Function

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
        LatLng latLng = new LatLng(lat,lag);
        if(marker!=null)
        {
            marker.remove();
        }
        try {
            Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lat, lag, 1);

            if (addresses.isEmpty()) {
            }
            else {
                if (addresses.size() > 0) {
                    if(addresses.get(0).getThoroughfare()!=null&&addresses.get(0).getSubLocality()!=null){
                        thoRoughFare=addresses.get(0).getThoroughfare();
                        subLocality=addresses.get(0).getSubLocality();
                        locality=addresses.get(0).getLocality();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }

        marker=mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(subLocality));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lag), 12.0f));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try{
            mDatabaseUser1.removeEventListener(valueEventListenerUser1);
            mDatabaseUser.removeEventListener(valueEventListener1);
            mDatabaseUser.removeEventListener(valueEventListenerPhone);
        }
        catch (Exception e){

        }
        super.onDestroy();
    }
}