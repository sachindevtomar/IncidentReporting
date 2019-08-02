package com.fgiet.incidentreporting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by This Pc on 7/18/2017.
 */
public class ContactTab1 extends Fragment implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    MapView mapView;
    View rootView;
    TextView address,email,weblink,fblink,phone;
    DatabaseReference mDatabaseContact= FirebaseDatabase.getInstance().getReference().child("JeevaashrayaDetails");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact_tab1, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        address=(TextView)view.findViewById(R.id.contact_address);
        email=(TextView)view.findViewById(R.id.contact_email);
        weblink=(TextView)view.findViewById(R.id.contact_weblink);
        fblink=(TextView)view.findViewById(R.id.contact_fblink);
        phone=(TextView)view.findViewById(R.id.contact_phone);

        mapView = (MapView) rootView.findViewById(R.id.map1);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        mDatabaseContact.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                address.setText(dataSnapshot.child("Address").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                weblink.setText(dataSnapshot.child("Weblink").getValue().toString());
                fblink.setText(dataSnapshot.child("Fblink").getValue().toString());
                phone.setText(dataSnapshot.child("ContactNo").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(26.770653, 80.857965)).title("Jeev Aashraya"));
        CameraPosition aashraya = CameraPosition.builder().target(new LatLng(26.770653, 80.857965)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(aashraya));
    }

}

