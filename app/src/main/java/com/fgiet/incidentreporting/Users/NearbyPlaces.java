package com.fgiet.incidentreporting.Users;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import com.fgiet.incidentreporting.R;
import com.google.android.gms.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyPlaces extends Fragment implements OnMapReadyCallback
        {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public static final int REQUEST_LOCATION_CODE=99;
    int PROXIMITY_RADIUS=10000;
    double latitude,longitude;
    MapView mapView;
    View mView;
    Button hospitalBtn;
    Object dataTransfer[]=new Object[2];
    String url;
    GetNearbyPlacesData getNearbyPlaceData=new GetNearbyPlacesData();
    int flagMap=1;

    public NearbyPlaces() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_nearby_places, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Nearby Police Stations");

        if(!CheckGooglePlayServices()){
            Log.d("OnCreate","Google Play Services are not available");
            getActivity().finish();
        }

        mapView = (MapView)mView.findViewById(R.id.mapV);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        Log.d("SeriesChecking","1");
        mapView.getMapAsync(this);

        Log.d("SeriesChecking","2");




    }

    private boolean CheckGooglePlayServices(){
        GoogleApiAvailability googleAPI=GoogleApiAvailability.getInstance();
        int result=googleAPI.isGooglePlayServicesAvailable(getContext());

        Log.d("SeriesChecking","4");
        if(result!= ConnectionResult.SUCCESS){
            if(googleAPI.isUserResolvableError(result)){
                googleAPI.getErrorDialog(getActivity(),result,0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if(flagMap==1) {
            flagMap=0;
            String hospital = "police";
            url = getUrl(26.2345, 81.2409, hospital);
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            getNearbyPlaceData.execute(dataTransfer);
        }

    }



    private String getUrl(double latitude, double longitude, String nearbyPlace){
        StringBuilder googlePlaceUrl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        Log.d("finding error",""+latitude+",,"+longitude);
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCLmk9s-MUSHi6dHM3Ugv6j-71n4AYIL-o");

        Log.d("SeriesChecking","10");
        return googlePlaceUrl.toString();
    }

}
