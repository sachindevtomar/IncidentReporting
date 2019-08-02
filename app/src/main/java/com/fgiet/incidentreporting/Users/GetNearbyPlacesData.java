package com.fgiet.incidentreporting.Users;

/**
 * Created by Admin on 3/30/2018.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 3/23/2018.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];
        DownloadUrl downloadUrl=new DownloadUrl();
        try {
            Log.d("Finding Error","Got an error");
            googlePlacesData=downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String ,String >> nearbyPlaceList=null;
        DataParser parser=new DataParser();
        nearbyPlaceList=parser.parse(s);
        if(nearbyPlaceList!=null)
            Log.d("Finding Error","Got an error6" + nearbyPlaceList.size());

        showNearbyPlaces(nearbyPlaceList);
        Log.d("Finding Error","Got an error7");
    }

    private void showNearbyPlaces(List<HashMap<String ,String>> nearbyPlaceList){
        try {
            Log.d("Finding Error", "hello");
        }
        catch(Exception e) {

            Log.d("Finding Error",e.getMessage());
        }
        for(int i=0;i<nearbyPlaceList.size();i++){
            MarkerOptions markerOptions =new MarkerOptions();

            Log.d("Finding Error","Got an error9");
            HashMap<String ,String >googlePlace=nearbyPlaceList.get(i);

            String placeName=googlePlace.get("place_name");
            String vicinity=googlePlace.get(("vicinity"));
            double lat=Double.parseDouble(googlePlace.get("lat"));
            double lng=Double.parseDouble(googlePlace.get("lng"));

            Log.d("Finding Error","Got an error11");
            LatLng latLng=new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName+" : "+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            Log.d("Finding Error","Got an error10");
        }
    }
}
