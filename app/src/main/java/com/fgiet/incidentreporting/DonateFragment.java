package com.fgiet.incidentreporting;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.Users.DonateFood;
import com.fgiet.incidentreporting.Users.Home;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonateFragment extends Fragment {

    WebView wv1;
    View mView;
    Button payEazy,payTm,donate;
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("JeevaashrayaDetails");

    public DonateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       mView= inflater.inflate(R.layout.fragment_donate, container, false);
        payEazy=(Button)mView.findViewById(R.id.payEazy);
        payTm=(Button)mView.findViewById(R.id.payTm);
        donate=(Button)mView.findViewById(R.id.donate_medicine_and_food);

        mDatabase.child("Phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                payTm.setText("PayTm on "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wv1=(WebView)mView.findViewById(R.id.wv_donate);
        getActivity().setTitle(getString(R.string.donate));
//        WebSettings webSettings = wv1.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        wv1.loadUrl("https://eazypay.icicibank.com/eazypayLink?P1=iKos8iQFEVSCUVRSE6iQJQ%3D%3D#");
//
        wv1.setWebChromeClient(new WebChromeClient());

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DonateFood.class));
            }
        });

        payEazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString="https://eazypay.icicibank.com/eazypayLink?P1=iKos8iQFEVSCUVRSE6iQJQ%3D%3D#";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.setPackage("com.android.chrome");
                try {
                    mView.getContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {

                    Log.v("MainActivity", "NO CHROME APP INSTALLED");
                    intent.setPackage(null);
                    mView.getContext().startActivity(intent);
                }
            }
        });

        payTm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(mView.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Remember")
                        .setContentText("Don't Forget to mention 'For Jeevaashraya' in the description box of PayTm")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                goDonate();
                            }
                        })
                        .show();


            }
        });

    }

    public void goDonate(){

        Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("net.one97.paytm");
        String url="https://paytm.com";
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
        else
        {
            launchIntent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            launchIntent.setPackage("com.android.chrome");
            try {
                mView.getContext().startActivity(launchIntent);
            } catch (ActivityNotFoundException ex) {

                Log.v("MainActivity", "NO Chrome APP INSTALLED");
                launchIntent.setPackage(null);
                mView.getContext().startActivity(launchIntent);
            }
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wv1.canGoBack()) {
                        wv1.goBack();
                    }
                    return true;
            }
        }
        return false;
    }
}
