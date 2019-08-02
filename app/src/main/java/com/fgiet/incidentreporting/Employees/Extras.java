package com.fgiet.incidentreporting.Employees;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fgiet.incidentreporting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Extras extends Fragment {
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("Request");
    DatabaseReference mDatabase1= FirebaseDatabase.getInstance().getReference().child("Users");
    int statusRed=0,statusYellow=0,statusGreen=0;
    TextView textRed,textYellow,textGreen;
    ImageButton redBtn,yellowBtn,greenBtn;
    ArrayAdapter<String> adapter;
    private FirebaseAuth mAuth;
    ListView myListView;
    ArrayList<String> listItems,requestID;
    DataSnapshot dataSnapshot1;
    String userCatagory="";
    public Extras() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_extras, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textRed=(TextView)view.findViewById(R.id.textRed);
        textYellow=(TextView)view.findViewById(R.id.textYellow);
        textGreen=(TextView)view.findViewById(R.id.textGreen);
        redBtn=(ImageButton)view.findViewById(R.id.redButton);
        yellowBtn=(ImageButton)view.findViewById(R.id.yellowButton);
        greenBtn=(ImageButton)view.findViewById(R.id.greenButton);

        myListView = (ListView)view.findViewById(R.id.myListView);

        listItems=new ArrayList<String>();
        requestID=new ArrayList<String>();

        mAuth=FirebaseAuth.getInstance();
        adapter=new ArrayAdapter<String>(getActivity(), R.layout.list_text, R.id.list_content,
                listItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#AB47BC"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#6A1B9A"));
                }
                return view;
            }
        };
        myListView.setAdapter(adapter);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot1=dataSnapshot;
                statusGreen=statusRed=statusYellow=0;
                mDatabase1.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userCatagory=dataSnapshot.child("CaseType").getValue().toString();
                        statusGreen=statusRed=statusYellow=0;
                        collectStatus((Map<String,Object>) dataSnapshot1.getValue());
                        textRed.setText(" "+statusRed);
                        textYellow.setText(" "+statusYellow);
                        textGreen.setText(" "+statusGreen);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            printRequestListStatus((Map<String,Object>) dataSnapshot.getValue(),0);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            }
        });
        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        printRequestListStatus((Map<String,Object>) dataSnapshot.getValue(),1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        printRequestListStatus((Map<String,Object>) dataSnapshot.getValue(),2);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, final View view, final int i, long i2) {

                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.modal_in);
                view.startAnimation(hyperspaceJumpAnimation);
                Intent intent=new Intent(getActivity(),RequestActivity.class);
                intent.putExtra("requestid",requestID.get(i));
                startActivity(intent);
            }
        });

    }


    private void collectStatus(Map<String,Object> requests) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : requests.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            if(singleUser.get("status").equals("0")&&singleUser.get("type").equals(userCatagory))
                statusRed++;
            else if(singleUser.get("status").equals("1")&&singleUser.get("type").equals(userCatagory))
                statusYellow++;
            else if(singleUser.get("status").equals("2")&&singleUser.get("type").equals(userCatagory))
                statusGreen++;
        }
    }

    private void printRequestListStatus(Map<String,Object> requests,int selected) {
        listItems.clear();
        requestID.clear();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : requests.entrySet()){
            //Get user map

            Map singleUser = (Map) entry.getValue();
            if(singleUser.get("status").toString().equals("0")&&selected==0&&singleUser.get("type").equals(userCatagory))
            {
                listItems.add(singleUser.get("serialno").toString()+":  "+singleUser.get("emailid").toString());
                requestID.add(singleUser.get("requestno").toString());
            }
            else if(singleUser.get("status").toString().equals("1")&&selected==1&&singleUser.get("type").equals(userCatagory))
            {
                listItems.add(singleUser.get("serialno").toString()+":  "+singleUser.get("emailid").toString());
                requestID.add(singleUser.get("requestno").toString());
            }
            else if(singleUser.get("status").toString().equals("2")&&selected==2&&singleUser.get("type").equals(userCatagory))
            {
                listItems.add(singleUser.get("serialno").toString()+":  "+singleUser.get("emailid").toString());
                requestID.add(singleUser.get("requestno").toString());
            }
        }
        adapter.notifyDataSetChanged();
    }

}
