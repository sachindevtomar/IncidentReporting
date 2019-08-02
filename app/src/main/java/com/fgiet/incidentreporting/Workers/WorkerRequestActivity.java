package com.fgiet.incidentreporting.Workers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.DirectionsJSONParser;
import com.fgiet.incidentreporting.Employees.RequestActivity;
import com.fgiet.incidentreporting.MainActivity;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.Rough1;
import com.fgiet.incidentreporting.WorkaroundMapFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.Double.compare;

public class WorkerRequestActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap requestMap;
    PolylineOptions lineOptions = null;
    private Marker markerdes, markercurrent;
    private String requestid, name = "", email = "", desc = "", phone = "", locality = "", sublocality = "", thoroughfare = "", date, employee = "", incidentno = "";
    private String imageUrl[] = new String[5];
    private double lat1 = 0.0, lag1 = 0.0;
    private double lat = 0.0, lag = 0.0;
    private String distance = "", duration = "";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private int imageCount = 0;
    private DatabaseReference mDatabase, mDatabase1;
    private Button refresh,fileATR;
    private ScrollView mScrollView;
    private TextView nametv, emailtv, desctv, phonetv, locationtv, datetv, distancetv, incidenttv;
    private ImageView image1, image2, image3, image4, image5;
    private ImageButton download1, download2, download3, download4, download5;
    ACProgressFlower mProgressDialog;
    FirebaseAuth mAuth;
    Button btnNews;
    private FirebaseAuth.AuthStateListener mAuthListener;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.requesttoolbar);

        fileATR=(Button)findViewById(R.id.btn_worker_atr);
        nametv = (TextView) findViewById(R.id.tv_request_name);
        emailtv = (TextView) findViewById(R.id.tv_request_email);
        phonetv = (TextView) findViewById(R.id.tv_request_phone);
        desctv = (TextView) findViewById(R.id.tv_request_desc);
        distancetv = (TextView) findViewById(R.id.tv_distance);
        locationtv = (TextView) findViewById(R.id.tv_request_location);
        incidenttv = (TextView) findViewById(R.id.tv_request_incident);
        refresh = (Button) findViewById(R.id.refresh);
        datetv = (TextView) findViewById(R.id.tv_request_date);
        image1 = (ImageView) findViewById(R.id.iv_request_image1);
        image2 = (ImageView) findViewById(R.id.iv_request_image2);
        image3 = (ImageView) findViewById(R.id.iv_request_image3);
        image4 = (ImageView) findViewById(R.id.iv_request_image4);
        image5 = (ImageView) findViewById(R.id.iv_request_image5);
        download1 = (ImageButton) findViewById(R.id.ib_request_download1);
        download2 = (ImageButton) findViewById(R.id.ib_request_download2);
        download3 = (ImageButton) findViewById(R.id.ib_request_download3);
        download4 = (ImageButton) findViewById(R.id.ib_request_download4);
        download5 = (ImageButton) findViewById(R.id.ib_request_download5);
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
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        btnNews = (Button) findViewById(R.id.btn_news);
        setSupportActionBar(toolbar);
        refresh.setEnabled(false);




        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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


        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkerRequestActivity.this, WorkerPost.class));
            }
        });

        Intent intent = getIntent();
        requestid = intent.getStringExtra("requestid");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                    startActivity(new Intent(WorkerRequestActivity.this, MainActivity.class));
                }
            }
        };

        fileATR.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WorkerRequestActivity.this,FileATR.class);
                intent.putExtra("requestid", requestid);
                startActivity(intent);
            }
        });

        if (!requestid.equals("")) {
            mDatabase = FirebaseDatabase.getInstance().getReference("Request").child(requestid);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    name = dataSnapshot.child("name").getValue().toString();
                    email = dataSnapshot.child("emailid").getValue().toString();
                    phone = dataSnapshot.child("phone").getValue().toString();
                    desc = dataSnapshot.child("desc").getValue().toString();
                    locality = dataSnapshot.child("locality").getValue().toString();
                    sublocality = dataSnapshot.child("sublocality").getValue().toString();
                    thoroughfare = dataSnapshot.child("thoroughfare").getValue().toString();
                    lat1 = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                    lag1 = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                    imageCount = Integer.parseInt(dataSnapshot.child("imagecount").getValue().toString());
                    date = dataSnapshot.child("date").getValue().toString();
                    incidentno = "Incident No- " + dataSnapshot.child("serialno").getValue().toString();
                    for (int i = 0; i < imageCount; i++) {
                        imageUrl[i] = dataSnapshot.child("image_" + i).getValue().toString();
                        switch (i) {
                            case 0: {
                                image1.setVisibility(View.VISIBLE);
                                download1.setVisibility(View.VISIBLE);
                                Picasso.with(getApplicationContext()).load(imageUrl[0]).into(image1);
                            }
                            break;
                            case 1: {
                                image2.setVisibility(View.VISIBLE);
                                download2.setVisibility(View.VISIBLE);
                                Picasso.with(getApplicationContext()).load(imageUrl[1]).into(image2);
                            }
                            break;
                            case 2: {
                                image3.setVisibility(View.VISIBLE);
                                download3.setVisibility(View.VISIBLE);
                                Picasso.with(getApplicationContext()).load(imageUrl[2]).into(image3);
                            }
                            break;
                            case 3: {
                                image4.setVisibility(View.VISIBLE);
                                download4.setVisibility(View.VISIBLE);
                                Picasso.with(getApplicationContext()).load(imageUrl[3]).into(image4);
                            }
                            break;
                            case 4: {
                                image5.setVisibility(View.VISIBLE);
                                download5.setVisibility(View.VISIBLE);
                                Picasso.with(getApplicationContext()).load(imageUrl[4]).into(image5);
                            }
                            break;
                            default:
                                Toast.makeText(WorkerRequestActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (name.equals("")) {
                        nametv.setTextColor(Color.parseColor("#FF0000"));
                        nametv.setText("Not Given");
                    } else
                        nametv.setText(name);

                    if (email.equals("")) {
                        emailtv.setTextColor(Color.parseColor("#FF0000"));
                        emailtv.setText("Not Given");
                    } else
                        emailtv.setText(email);

                    if (phone.equals(""))
                        phonetv.setVisibility(View.GONE);
                    else
                        phonetv.setText(phone);

                    if (incidentno.equals(""))
                        incidenttv.setVisibility(View.GONE);
                    else
                        incidenttv.setText(incidentno);

                    if (desc.equals("")) {
                        desctv.setTextColor(Color.parseColor("#FF0000"));
                        desctv.setText("Not Given");
                    } else
                        desctv.setText(desc);
                    locationtv.setText(thoroughfare + ", " + sublocality + ", " + locality);
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
        } else {
            finish();
            startActivity(new Intent(WorkerRequestActivity.this, Rough1.class));
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lag = location.getLongitude();
                if (compare(lat, 0.0) != 0) {
                    refresh.setEnabled(true);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(WorkerRequestActivity.this, "Please Turn On your Location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }


        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(WorkerRequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                configureButton();
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
            }
        } else {
            configureButton();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onMapReady(requestMap);
            }
        });

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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        requestMap = googleMap;
        LatLng latLng1 = new LatLng(lat1, lag1);
        if (markercurrent != null) {
            requestMap.clear();
        }
        markerdes = requestMap.addMarker(new MarkerOptions().position(latLng1).title("Destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_location)));
        requestMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat1, lag1), 15.0f));
        requestMap.getUiSettings().setZoomControlsEnabled(true);

        if (compare(lat1, 0.0) != 0 && compare(lat, 0.0) != 0) {
            LatLng latLng = new LatLng(lat, lag);
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(latLng, latLng1);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

            markercurrent = requestMap.addMarker(new MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to Log Out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ACProgressFlower.Builder(WorkerRequestActivity.this)
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
            Toast.makeText(WorkerRequestActivity.this, "Saved in InternalStorage/Jeevaashraya/Downloads", Toast.LENGTH_SHORT).show();
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

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Jeevaashraya"
                + "/Downloads");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "image_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {

                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                Log.d("Error Occured Here", e.getMessage());
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
                lineOptions.startCap(new RoundCap());
                lineOptions.endCap(new RoundCap());
//                lineOptions.geodesic(true);
            }
            // Drawing polyline in the Google Map for the i-th route
            requestMap.addPolyline(lineOptions);

            distancetv.setText("Distance: " + distance + "\nDuration: " + duration +result.toString());
        }
    }


    @Override
    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Log Out")
//                .setMessage("Are you sure you want to Log Out?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mAuth.signOut();
//                    }
//
//                })
//                .setNegativeButton("No", null)
//                .show();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Log Out")
                .setContentText("Are you sure you want to Log Out?")
                .setConfirmText("Yes, log Out!")
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        mAuth.signOut();
                    }
                })
                .show();
    }
}
