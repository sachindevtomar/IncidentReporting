package com.fgiet.incidentreporting;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

import static java.lang.Double.compare;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Map Variables
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double lat = 0.0, lag = 0.0;

    //Image Variables
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private static final int VOICE_SPEECH = 3;
    private Button btn_choose_image;
    int imageArray[] = new int[5];
    private ProgressDialog progressDialog;
    private Uri selectedImage = null;
    private byte[] databaos1 = null, databaos2 = null, databaos3 = null, databaos4 = null, databaos5 = null;
    private ImageView mImageView1, mImageView2, mImageView3, mImageView4, mImageView5;
    private ImageButton close1, close2, close3, close4, close5;
    private FrameLayout frame1, frame2, frame3, frame4, frame5;
    int check = 0, count = 0, temp = 0, temp1 = 0, temp2 = 0;
    Bitmap bitmap = null, bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null, bitmap5 = null;
    int k = 0;
    String imageUrl[] = new String[5], subLocality = "", thoRoughFare = "", locality = "",namestr,phonestr,descstr;
    private Uri downloadUrl = null;
    private EditText name, phone, desc;
    DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date date;
    Marker marker = null;


    //Database Variables
    private DatabaseReference mDatabase, newPost;
    private DatabaseReference mDatabaseRequestCounter=FirebaseDatabase.getInstance().getReference("RequestCounter");

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        imageArray[0] = imageArray[1] = imageArray[2] = imageArray[3] = imageArray[4] = 0;
        mImageView1 = (ImageView) findViewById(R.id.customView1);
        mImageView2 = (ImageView) findViewById(R.id.customView2);
        mImageView3 = (ImageView) findViewById(R.id.customView3);
        mImageView4 = (ImageView) findViewById(R.id.customView4);
        mImageView5 = (ImageView) findViewById(R.id.customView5);
        close1 = (ImageButton) findViewById(R.id.button1);
        close2 = (ImageButton) findViewById(R.id.button2);
        close3 = (ImageButton) findViewById(R.id.button3);
        close4 = (ImageButton) findViewById(R.id.button4);
        close5 = (ImageButton) findViewById(R.id.button5);
        frame1 = (FrameLayout) findViewById(R.id.frame1);
        frame2 = (FrameLayout) findViewById(R.id.frame2);
        frame3 = (FrameLayout) findViewById(R.id.frame3);
        frame4 = (FrameLayout) findViewById(R.id.frame4);
        frame5 = (FrameLayout) findViewById(R.id.frame5);
        btn_choose_image = (Button) findViewById(R.id.btn_choose_image);
        FloatingActionButton send = (FloatingActionButton) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        desc = (EditText) findViewById(R.id.desc);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Request");

        setTitle(getString(R.string.incident_reporting));
        //picture
        progressDialog = new ProgressDialog(this);

        frame1.setVisibility(View.GONE);
        frame2.setVisibility(View.GONE);
        frame3.setVisibility(View.GONE);
        frame4.setVisibility(View.GONE);
        frame5.setVisibility(View.GONE);

        final String[] items = new String[]{getString(R.string.from_camera), getString(R.string.from_gallery)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);

        //For Dialog Appearance
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
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
        final android.support.v7.app.AlertDialog dialog = builder.create();

        //To choose Image
        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
                if (count != 5) {
                    dialog.show();
                } else {
                    Crouton.showText(MapsActivity.this, R.string.select_maximum_5_images, Style.ALERT);
                }
            }
        });

        //ImageView in Dialog
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
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
                    sendData();
                else {
                    Crouton.showText(MapsActivity.this,R.string.no_internet_connection, Style.ALERT);
                }
            }

        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lag = location.getLongitude();
                onMapReady(mMap);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(MapsActivity.this, R.string.please_turn_on_your_location, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }


        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                configureButton();
            }
            else {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
            }
        }
        else {
            configureButton();
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


    public void speechMethod(){
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.say_something_and_for_clearing_text));

        try{
            startActivityForResult(i,VOICE_SPEECH);
        }
        catch(ActivityNotFoundException e){
            Toast.makeText(this, "Your device does not support this feature", Toast.LENGTH_SHORT).show();
        }
    }

    //map
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

    //map
    public void configureButton() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
    }

    //map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(lat, lag);
        if (marker != null) {
            marker.remove();
        }
        try {

            Geocoder geo = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lat, lag, 1);

            if (addresses.isEmpty()) {
                Crouton.showText(MapsActivity.this, getString(R.string.wait_for_location), Style.INFO);
            } else {
                if (addresses.size() > 0) {
                    if (addresses.get(0).getThoroughfare() != null && addresses.get(0).getSubLocality() != null) {
                        thoRoughFare = addresses.get(0).getThoroughfare();
                        subLocality = addresses.get(0).getSubLocality();
                        locality = addresses.get(0).getLocality();
                    }
                }
            }
        } catch (Exception e) {
//            Toast.makeText(this, e.getMessage()+" "+lat+" "+lag, Toast.LENGTH_LONG).show(); // getFromLocation() may sometimes fail
        }
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(subLocality + "," + thoRoughFare));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lag), 12.0f));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        }
        else if(requestCode==PICK_FROM_FILE){
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
            if(data!=null){
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String tempString=result.get(0);
                if(!tempString.equals("clear all"))
                    desc.setText(desc.getText()+" "+tempString);
                else{
                    desc.setText("");
                    Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

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
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
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

    public void sendData() {
        namestr = name.getText().toString().trim();
        phonestr = phone.getText().toString().trim();
        descstr = desc.getText().toString().trim();
        if (phonestr.length() != 10 && phonestr.length() != 0) {
            Crouton.showText(MapsActivity.this, R.string.enter_valid_mobile_number, Style.ALERT);
            return;
        }

        for (k = 0; k < 5 && imageArray[k] == 1; k++) {
            temp1++;
        }

        if(TextUtils.isEmpty(phonestr)){
            Crouton.showText(MapsActivity.this,getString(R.string.enter_phone_number),Style.ALERT);
            return;
        }


        if (temp != 0) {
            if (compare(lat, 0.0) != 0) {
                progressDialog.setMessage(getString(R.string.uploading_image));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                //Firebase storage folder where you want to put the images

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                for (k = 0; k < 5 && imageArray[k] == 1; k++) {
                    //name of the image file (add time to have different files to avoid rewrite on the same file)
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
                            Crouton.showText(MapsActivity.this, getString(R.string.uploading_failed), Style.ALERT);
                            temp1=0;
                            temp2=0;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            downloadUrl = taskSnapshot.getDownloadUrl();
                            imageUrl[temp2] = downloadUrl.toString();
                            temp2++;
                            if (temp1 == temp2) {
                                updateCounter(true);
                                Intent intent=new Intent(MapsActivity.this,MainActivity.class);
                                //intent.putExtra("cro","2");
                                Toast.makeText(MapsActivity.this, R.string.upload_done, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                locationManager.removeUpdates(locationListener);
                                finish();
                            }

                        }
                    });
                }
            } else {
                Crouton.showText(MapsActivity.this, R.string.wait_for_location, Style.INFO);
            }
        }
        else

        {
            Crouton.showText(MapsActivity.this, getString(R.string.select_atleast_one_image), Style.INFO);
        }

    }

    private void updateCounter(final boolean increment) {
        mDatabaseRequestCounter.runTransaction(new Transaction.Handler() {
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
                    date=new Date();
                    newPost = mDatabase.push();
                    newPost.child("name").setValue(namestr);
                    newPost.child("desc").setValue(descstr);
                    newPost.child("phone").setValue(phonestr);
                    newPost.child("latitude").setValue(lat);
                    newPost.child("longitude").setValue(lag);
                    newPost.child("imagecount").setValue(temp1);
                    newPost.child("locality").setValue(locality);
                    newPost.child("sublocality").setValue(subLocality);
                    newPost.child("thoroughfare").setValue(thoRoughFare);
                    newPost.child("userid").setValue("");
                    newPost.child("emailid").setValue("");
                    newPost.child("date").setValue(sdf.format(date).toString());
                    newPost.child("requestno").setValue(newPost.getKey());
                    newPost.child("status").setValue("0");
                    newPost.child("serialno").setValue(value);
                    for (k = 0; k < temp2; k++) {
                        newPost.child("image_" + k).setValue(imageUrl[k]);
                    }


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

    private void storeImage(Bitmap image,int k) {
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
    private  File getOutputMediaFile(int k){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Jeevaashraya"
                + "/Images");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="image_"+k+"_"+timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        locationManager.removeUpdates(locationListener);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
