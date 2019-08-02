package com.fgiet.incidentreporting.Users;


import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {

    View mView;
    DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference().child("JeevaashrayaDetails");
    String adoption="",abc="",rescues="",shelter="";
    int temp,temp1,temp2,temp3;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_about_us, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.about_us));
        final TextView tv=(TextView)mView.findViewById(R.id.adoption);
        final TextView tv1=(TextView)mView.findViewById(R.id.abc);
        final TextView tv2=(TextView)mView.findViewById(R.id.rescue);
        final TextView tv3=(TextView)mView.findViewById(R.id.shelter);
        TextView headText=(TextView)mView.findViewById(R.id.textView4);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Courgette-Regular.ttf");
        headText.setTypeface(face);

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
