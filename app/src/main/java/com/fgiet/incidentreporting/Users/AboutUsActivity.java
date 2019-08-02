package com.fgiet.incidentreporting.Users;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.R;


public class AboutUsActivity extends AppCompatActivity {
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("JeevaashrayaDetails");
    String adoption="",abc="",rescues="",shelter="";
    int temp,temp1,temp2,temp3;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setTitle(R.string.about_us);
        final TextView tv=(TextView)findViewById(R.id.adoption);
        final TextView tv1=(TextView)findViewById(R.id.abc);
        final TextView tv2=(TextView)findViewById(R.id.rescue);
        final TextView tv3=(TextView)findViewById(R.id.shelter);
        TextView headText=(TextView)findViewById(R.id.textView4);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Courgette-Regular.ttf");
        headText.setTypeface(face);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp=Integer.parseInt(dataSnapshot.child("Adoption").getValue().toString());
                animateTextView(1,temp,tv);
                temp1=Integer.parseInt(dataSnapshot.child("ABC").getValue().toString());
                animateTextView(1,temp1,tv1);
                temp2=Integer.parseInt(dataSnapshot.child("Rescues").getValue().toString());
                animateTextView(1,temp2,tv2);
                temp3=Integer.parseInt(dataSnapshot.child("Shelter").getValue().toString());
                animateTextView(1,temp3,tv3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();
    }
}