package com.fgiet.incidentreporting.Users;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.fgiet.incidentreporting.Employees.EmpSignUp;
import com.fgiet.incidentreporting.MainActivity;
import com.fgiet.incidentreporting.MapsActivity;
import com.fgiet.incidentreporting.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class SignUp extends AppCompatActivity {

    private Button mRegister,empRegister;
    private EditText email,name,pass,cnfpass,contact;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDataBase= FirebaseDatabase.getInstance().getReference().child("Users")
            , mDataBase1 =FirebaseDatabase.getInstance().getReference().child("Permission");
    private Button skip;

    private ArrayList<String> mUserName=new ArrayList<>();
    private DatabaseReference mDatabase;
    private String value[]=new String[200];
    private String flag[]=new String[200];
    private String impkey[]=new String[200];
    private String serialno[]=new String[200];
    private String serialkey=null;
    int temp=0,check=0;
    int i=0;
    private AlertDialog dialog;
    private static final Pattern VALID_EMAIL_ADDRESS =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",Pattern.CASE_INSENSITIVE);


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        name=(EditText)findViewById(R.id.etsignup_name);
        email=(EditText)findViewById(R.id.etsignup_email);
        pass=(EditText)findViewById(R.id.etsignup_password);
        mRegister=(Button)findViewById(R.id.btsignup);
        empRegister=(Button)findViewById(R.id.btsignup_employee);
        mProgress=new ProgressDialog(this);
        cnfpass=(EditText)findViewById(R.id.etsignup_password_confirm);
        contact=(EditText)findViewById(R.id.etsignup_contact);
        mAuth=FirebaseAuth.getInstance();

        skip=(Button)findViewById(R.id.btsignup_skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable())
                    startActivity(new Intent(SignUp.this,MapsActivity.class));
                else {
                    Crouton.showText(SignUp.this,getString(R.string.no_internet_connection),Style.ALERT);
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(isNetworkAvailable())
                    startRegister();
                else {
                    Crouton.showText(SignUp.this,getString(R.string.no_internet_connection),Style.ALERT);
                }
            }
        });

        empRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(isNetworkAvailable())
                    keyChecking();
                else {
                    Crouton.showText(SignUp.this,getString(R.string.no_internet_connection),Style.ALERT);
                }
            }
        });

    }


    private void startRegister() {

        final String namestr=name.getText().toString().trim();
        final String emailstr=email.getText().toString().trim();
        String passstr=pass.getText().toString().trim();
        String cnfpassstr=cnfpass.getText().toString().trim();
        final String contactstr=contact.getText().toString().trim();


        if(TextUtils.isEmpty(namestr) || TextUtils.isEmpty(emailstr) || TextUtils.isEmpty(passstr)|| TextUtils.isEmpty(cnfpassstr) || TextUtils.isEmpty(contactstr))
        {
            Crouton.showText(SignUp.this, getString(R.string.every_field_is_required), Style.ALERT);
            return;
        }

        if (!passstr.equals(cnfpassstr)) {
            pass.setText("");
            pass.requestFocus();
            cnfpass.setText("");

            Crouton.showText(SignUp.this, R.string.confirm_password_and_password_should_be_same, Style.ALERT);
            return;
        }


        if (contactstr.length() != 10) {


            Crouton.showText(SignUp.this, getString(R.string.enter_valid_mobile_number), Style.ALERT);
            return;
        }

        if(validate(emailstr))
        {

        }
        else
        {


            Crouton.showText(SignUp.this, getString(R.string.enter_valid_email_address), Style.ALERT);
            return;
        }




                        mProgress.setMessage(getString(R.string.signing_up));
                        mProgress.show();

                        mAuth.createUserWithEmailAndPassword(emailstr, passstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = mDataBase.child(user_id);
                                    current_user_db.child("Name").setValue(namestr);
                                    current_user_db.child("EmailId").setValue(emailstr);
                                    current_user_db.child("Phone").setValue(contactstr);
                                    current_user_db.child("Employee").setValue("0");
                                    current_user_db.child("RequestId").setValue("");
                                    current_user_db.child("Engaged").setValue("");
                                    current_user_db.child("UserId").setValue(user_id);
                                    current_user_db.child("Blocked").setValue("0");
                                    current_user_db.child("ProfilePicture").setValue("0");


                                    mProgress.dismiss();
                                    Toast.makeText(SignUp.this, R.string.successfully_registered, Toast.LENGTH_SHORT).show();
                                    sendVerificationEmailId();

                                    try {

                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                       // intent.putExtra("cro", "1");
                                        startActivity(intent);
                                        mAuth.signOut();
                                    }
                                    catch (Exception e){}

                                }
                                else{
                                    mProgress.dismiss();

                                    Crouton.showText(SignUp.this, task.getException().getMessage(), Style.ALERT);
                                }
                            }
                        });

    }

    public void sendVerificationEmailId(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this, R.string.verify_your_email_address, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void keyChecking(){

        final ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mUserName);

        mDataBase1.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                check=1;
                value[i] =dataSnapshot.child("ekey").getValue().toString();
                flag[i] =dataSnapshot.child("flag").getValue().toString();
                impkey[i]=dataSnapshot.child("impkey").getValue().toString();
                serialno[i] =dataSnapshot.child("serialno").getValue().toString();
                mUserName.add(value[i]);
                i++;
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                flag[temp] =dataSnapshot.child("flag").getValue().toString();
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


                AlertDialog.Builder mBuilder=new AlertDialog.Builder(SignUp.this);
                View mView=getLayoutInflater().inflate(R.layout.emp_dialog,null);
                final EditText mKey=(EditText) mView.findViewById(R.id.etkey);
                Button mOk=(Button) mView.findViewById(R.id.okbtn);
                mBuilder.setView(mView);
                dialog=mBuilder.create();
                dialog.show();

                mOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(check==1) {
                            serialkey = mKey.getText().toString();
                            if (serialkey.trim().length() > 0) {

                                for (int k = i - 1; k > -1; k--) {

                                    if (value[k].equals(serialkey)) {
                                        if (flag[k].equals("1")) {

                                            temp = k;
                                            mDataBase1.child("key" + serialno[k]).child("flag").setValue("0");
                                            dialog.dismiss();
                                            if(impkey[k].equals("1")) {
                                                Intent m = new Intent(SignUp.this, EmpSignUp.class);
                                                m.putExtra("worker", "0");
                                                startActivity(m);
                                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                            }
                                            else{
                                                Intent m = new Intent(SignUp.this, EmpSignUp.class);
                                                m.putExtra("worker", "1");
                                                startActivity(m);
                                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                            }
                                            break;
                                        }
                                        else {


                                            Crouton.showText(SignUp.this, getString(R.string.key_already_used), Style.ALERT);
                                            break;
                                        }
                                    }
                                    else {
                                        if(k==0)


                                            Crouton.showText(SignUp.this, getString(R.string.entered_key_is_wrong), Style.ALERT);
                                    }
                                }
                            }
                        }
                        else


                            Crouton.showText(SignUp.this, getString(R.string.wait_for_server_response), Style.ALERT);
                    }
                });
            }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean validate(String email1){
        Matcher matcher= VALID_EMAIL_ADDRESS.matcher(email1);
        return matcher.find();
    }


}

