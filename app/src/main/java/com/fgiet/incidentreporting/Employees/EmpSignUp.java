package com.fgiet.incidentreporting.Employees;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.fgiet.incidentreporting.MainActivity;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.Users.SignUp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class EmpSignUp extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String caseType="Select Case Type";
    private Button mRegister;
    private EditText email,name,pass,cnfpass,contact;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDataBase;
    private String worker;
    private static final Pattern VALID_EMAIL_ADDRESS =
           Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",Pattern.CASE_INSENSITIVE);

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_sign_up);

        spinner=(Spinner)findViewById(R.id.spinnerType);
        name=(EditText)findViewById(R.id.et_esignup_name);
        email=(EditText)findViewById(R.id.et_esignup_email);
        pass=(EditText)findViewById(R.id.et_esignup_password);
        mRegister=(Button)findViewById(R.id.bt_esignup_signup);
        mProgress=new ProgressDialog(this);
        cnfpass=(EditText)findViewById(R.id.et_esignup_password_confirm);
        contact=(EditText)findViewById(R.id.et_esignup_contact);
        mAuth=FirebaseAuth.getInstance();
        Crouton.showText(EmpSignUp.this,"Correct key",Style.CONFIRM);
        mDataBase= FirebaseDatabase.getInstance().getReference().child("Users");

        Intent intent=getIntent();
        if(intent!=null)
        {
            worker=intent.getStringExtra("worker");
        }

        adapter=ArrayAdapter.createFromResource(this,R.array.type_category,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        if(worker.equals("1")){
            spinner.setEnabled(false);
            spinner.setClickable(false);
            spinner.setVisibility(View.GONE);
            caseType="employee";
        }
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                caseType=adapterView.getItemAtPosition(position).toString();
                try {
                    if (caseType.equals("Select Case Type"))
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.rgb(255, 0, 0));
                    else
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.rgb(0, 105, 92));
                }
                catch(Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable())
                   startRegister();
                else {
                    Crouton.showText(EmpSignUp.this,getString(R.string.no_internet_connection),Style.ALERT);
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
        if(caseType.equals("Select Case Type"))
        {
            Crouton.showText(EmpSignUp.this, "Please select one of the case type", Style.ALERT);
            return;
        }
        if(TextUtils.isEmpty(namestr) && TextUtils.isEmpty(emailstr) && TextUtils.isEmpty(passstr)&& TextUtils.isEmpty(cnfpassstr) && TextUtils.isEmpty(contactstr))
        {

            Crouton.showText(EmpSignUp.this, getString(R.string.every_field_is_required), Style.ALERT);
            return;
        }

        if (!passstr.equals(cnfpassstr)) {
            pass.setText("");
            pass.requestFocus();
            cnfpass.setText("");

            Crouton.showText(EmpSignUp.this, getString(R.string.confirm_password_and_password_should_be_same), Style.ALERT);
            return;
        }


        if (contactstr.length() != 10) {


            Crouton.showText(EmpSignUp.this, getString(R.string.enter_valid_mobile_number), Style.ALERT);
            return;
        }

        if(validate(emailstr))
        {

        }
        else
        {

            Crouton.showText(EmpSignUp.this, getString(R.string.enter_valid_email_address), Style.ALERT);
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
                                current_user_db.child("RequestId").setValue("");
                                current_user_db.child("UserId").setValue(user_id);
                                current_user_db.child("Blocked").setValue("0");
                                current_user_db.child("ProfilePicture").setValue("0");
                                if(worker.equals("0")) {
                                    current_user_db.child("Employee").setValue("1");
                                    current_user_db.child("Engaged").setValue("");
                                    current_user_db.child("CaseType").setValue(caseType);
                                }
                                else {
                                    current_user_db.child("Employee").setValue("2");
                                    current_user_db.child("Engaged").setValue("0");
                                    current_user_db.child("CaseType").setValue("worker");
                                }
                                mProgress.dismiss();
                                Toast.makeText(EmpSignUp.this, R.string.successfully_registered, Toast.LENGTH_SHORT).show();
                                sendVerificationEmailId();
                                try {
                                    Intent intent = new Intent(EmpSignUp.this, MainActivity.class);
                                    //intent.putExtra("cro", "1");
                                    startActivity(intent);
                                    Thread.sleep(1000);
                                    mAuth.signOut();
                                }
                                catch (Exception e){}
                            }
                            else{
                                mProgress.dismiss();
                                Crouton.showText(EmpSignUp.this, task.getException().getMessage(), Style.ALERT);
                            }
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

    public void sendVerificationEmailId(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(EmpSignUp.this, R.string.verify_your_email_address, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
