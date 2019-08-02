package com.fgiet.incidentreporting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by This Pc on 7/18/2017.
 */

public class ContactTab2 extends Fragment {

    private EditText subject;
    private EditText body;
    private Button send;
    private static final int VOICE_SPEECH = 3;
    private String emailstr="jeevaashraya@gmail.com";
    private DatabaseReference mDatabaseContact2=FirebaseDatabase.getInstance().getReference().child("JeevaashrayaDetails");
    View mView;

    public ContactTab2() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact_tab2, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subject=(EditText)mView.findViewById(R.id.enquiry_subject);
        body=(EditText)mView.findViewById(R.id.enquiry_body);
        send = (Button)mView.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        mDatabaseContact2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailstr=dataSnapshot.child("Email").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (body.getRight() - body.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something and for clearing text in the box say 'clear all'");

        try{
            startActivityForResult(i,VOICE_SPEECH);
        }
        catch(ActivityNotFoundException e){
            Toast.makeText(getActivity(), "Your device does not support this feature", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        //Getting content for email
        String subjectstr = subject.getText().toString().trim();
        String messagestr = body.getText().toString().trim();

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailstr});
        email.putExtra(Intent.EXTRA_SUBJECT, subjectstr);
        email.putExtra(Intent.EXTRA_TEXT, messagestr);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 3:
                    if(data!=null)
                    {
                        ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String tempString=result.get(0);
                        if(!tempString.equals("clear all"))
                            body.setText(body.getText()+" "+tempString);
                        else{
                            body.setText("");
                            Toast.makeText(getActivity(), "Cleared", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }
}
