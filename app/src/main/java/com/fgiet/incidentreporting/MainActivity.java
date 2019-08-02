package com.fgiet.incidentreporting;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fgiet.incidentreporting.Employees.EmpActivity;
import com.fgiet.incidentreporting.Users.AboutUsActivity;
import com.fgiet.incidentreporting.Users.Home;
import com.fgiet.incidentreporting.Users.SignUp;
import com.fgiet.incidentreporting.Workers.CheckWorkerRequest;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MainActivity extends AppCompatActivity {


    private SignInButton mGoogleBtn;
    private static  final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private  static final String TAG ="MainActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button mSignUp,mLogin,skip;
    private ImageButton lang;
    private ProgressDialog mProgress,mProgress1;
    private EditText emailLogin,passLogin;
    private DatabaseReference mDatabase,mDatabase1;
    private String empUid=null,Employee;
    private ArrayList<String> mUserName=new ArrayList<>();
    private Button forgotButton,contactUs,aboutUs;
    int check=0;
    private AlertDialog dialog;
    private Locale myLocale;
    //private AdView mAdView;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final Pattern VALID_EMAIL_ADDRESS =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String flaglanguage="";
        prefs = getSharedPreferences("LastLogin", MODE_PRIVATE);
        if(prefs.getString("LastLanguage",null)!=null && prefs.getString("LastLanguage",null).equals("Hindi")) {
            setLocaleLanguage("hi");
            flaglanguage="Hindi";
        }
        else
        {
            setLocaleLanguage("en");
            flaglanguage="English";
        }
        setContentView(R.layout.activity_main);


//        MobileAds.initialize(this, "ca-app-pub-7833651479953301~6710517621");
//        //mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        forgotButton=(Button) findViewById(R.id.btforgot_password);
        emailLogin=(EditText)findViewById(R.id.etlogin_email);
        passLogin=(EditText)findViewById(R.id.etlogin_password);
        mLogin=(Button)findViewById(R.id.login_btn);
        contactUs=(Button)findViewById(R.id.tvsignup_contact_us);
        aboutUs=(Button)findViewById(R.id.tvsignup_about_us);
        mProgress=new ProgressDialog(this);
        mProgress1=new ProgressDialog(this);
        skip=(Button)findViewById(R.id.btskip);
        lang=(ImageButton)findViewById(R.id.language);

        if(flaglanguage.equals("Hindi"))
            lang.setImageResource(R.drawable.americaflag);
        else
            lang.setImageResource(R.drawable.indiaflag);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        editor = getSharedPreferences("LastLogin", MODE_PRIVATE).edit();
        String restoredText = prefs.getString("LastEmail",null);
        if (restoredText != null) {
            emailLogin.setText(restoredText);
        }

        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String restoredLanguage=prefs.getString("LastLanguage",null);
                if(restoredLanguage==null||!restoredLanguage.equals("Hindi"))
                {
                    setLocale("hi");
                    editor.putString("LastLanguage", "Hindi");
                    editor.apply();
                }
                else
                {
                    setLocale("en");
                    editor.putString("LastLanguage", "English");
                    editor.apply();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable())
                {
                    startActivity(new Intent(MainActivity.this,MapsActivity.class));
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
                else {
                    Crouton.showText(MainActivity.this,getString(R.string.no_internet_connection),Style.ALERT);
                }
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animatedStartActivity();
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            }
        });
        //ForgotBuuton
        forgotButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            forgotPassword();
            }
        });

        //Sign in button
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if(isNetworkAvailable())
                    checkLogin();
                else {
                    Crouton.showText(MainActivity.this,getString(R.string.no_internet_connection),Style.ALERT);
                }
            }
        });

        mAuthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if (firebaseAuth.getCurrentUser()!=null ) {
                    mProgress.setMessage("Loading");
                    try{
                        mProgress.show();
                    }
                    catch (Exception e){}
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified())
                    {
                        // user is verified, so you can finish this activity or send user to activity which you want.
                        empUid = (String) mAuth.getCurrentUser().getUid();
                        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(empUid).child("Employee");
//
                        mDatabase1.addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String value = dataSnapshot.getValue(String.class);
                                if (value.equals("0"))
                                    startActivity(new Intent(MainActivity.this, Home.class));
                                else if(value.equals("1"))
                                    startActivity(new Intent(MainActivity.this, EmpActivity.class));
                                else
                                    startActivity(new Intent(MainActivity.this,CheckWorkerRequest.class));
                                mProgress.dismiss();
                            }

                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        };

        mSignUp=(Button) findViewById(R.id.signup_btn);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        mGoogleBtn=(SignInButton)findViewById(R.id.btlogin_google);
        mGoogleBtn.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) mGoogleBtn.getChildAt(0);
        textView.setText(getString(R.string.login_with_google));
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override

                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

                        Crouton.showText(MainActivity.this, "Check your network and try again", Style.ALERT);

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mProgress.setMessage(getString(R.string.wait_for_some_time));
                mProgress.show();
                if(isNetworkAvailable())
                {
                    signIn();
                }
                else {
                    mProgress.dismiss();
                    Crouton.showText(MainActivity.this,getString(R.string.no_internet_connection),Style.ALERT);
                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                   && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {

            }
            else {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 10);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //map
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                return;
        }
    }

    //Sign In with Email and Password
    private void checkLogin() {

        String email=emailLogin.getText().toString().trim();
        String pass=passLogin.getText().toString().trim();
        check=1;
        if(TextUtils.isEmpty(email)){
            Crouton.showText(MainActivity.this,getString(R.string.enter_email_address),Style.ALERT);
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Crouton.showText(MainActivity.this,getString(R.string.enter_password),Style.ALERT);
            return;
        }
            mProgress.setMessage("Logging");
            mProgress.show();

            editor.putString("LastEmail", email);
            editor.apply();

            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mProgress.dismiss();
                        if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                        {
                             try{
                                 mAuth.signOut();
                                 Thread.sleep(1000);
                                 Crouton.showText(MainActivity.this,getString(R.string.email_is_not_verified),Style.ALERT);
                             }
                            catch (Exception e){}
                        }
                    } else {
                        mProgress.dismiss();
                        Crouton.showText(MainActivity.this, getString(R.string.error_in_login), Style.ALERT);
                    }
                }
            });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //Sign In with Google
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    //Google
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                 final GoogleSignInAccount account = result.getSignInAccount();
                mAuth.fetchProvidersForEmail(account.getEmail()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if(task.isSuccessful()) {
                            try{
                                if ((task.getResult().getProviders().get(0).toString()).equals("password")) {
                                    Toast.makeText(MainActivity.this, R.string.you_are_already_signed_up_with_this_email, Toast.LENGTH_SHORT).show();
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                    mProgress.dismiss();
                                } else {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    firebaseAuthWithGoogle(account);
                                }
                            }
                            catch (Exception e){
                                FirebaseUser user = mAuth.getCurrentUser();
                                firebaseAuthWithGoogle(account);
                            }
                        }
                        else{
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebaseAuthWithGoogle(account);
                        }
                    }
                });

            } else {
                mProgress.dismiss();
            }
        }
    }

    //Google
    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String name = account.getDisplayName();
                            String email = account.getEmail();
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference dbr=mDatabase.child(user_id);
                            dbr.child("Name").setValue(name);
                            dbr.child("EmailId").setValue(email);
                            dbr.child("Employee").setValue("0");
                            dbr.child("RequestId").setValue("");
                            dbr.child("Engaged").setValue("");
                            dbr.child("UserId").setValue(user_id);

                        } else {
                            // If sign in fails, display a message to the user.
                            mProgress.dismiss();
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



        public void forgotPassword(){
            AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this,R.style.AnimateDialog);
            View mView=getLayoutInflater().inflate(R.layout.forgot_dialog,null);
            final EditText forgotEmail=(EditText) mView.findViewById(R.id.et_forgot_emailid);
            Button mOk=(Button) mView.findViewById(R.id.bt_forgot);
            mBuilder.setView(mView);
            dialog=mBuilder.create();
            dialog.show();

            mOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!isNetworkAvailable())
                    {
                        dialog.dismiss();
                        Crouton.showText(MainActivity.this,getString(R.string.no_internet_connection),Style.ALERT);
                        return;
                    }

                    if(!validate(forgotEmail.getText().toString().trim())){
                        Crouton.showText(MainActivity.this,getString(R.string.enter_valid_email_address),Style.ALERT);
                        return;
                    }
                    if (TextUtils.isEmpty(forgotEmail.getText().toString().trim())) {
                        return;
                    }

                    mProgress.show();
                    mAuth.sendPasswordResetEmail(forgotEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Crouton.showText(MainActivity.this, getString(R.string.password_reset_link),Style.CONFIRM);

                                    } else {

                                        Crouton.showText(MainActivity.this, getString(R.string.sending_failed_check_your_connection),Style.ALERT);
                                    }

                                   mProgress.dismiss();
                                }
                            });
                }
            });
        }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a=new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.please_click_back_again, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public boolean validate(String email1){
        Matcher matcher= VALID_EMAIL_ADDRESS.matcher(email1);
        return matcher.find();
    }

    private void animatedStartActivity() {
        // we only animateOut this activity here.
        // The new activity will animateIn from its onResume() - be sure to
        // implement it.
        final Intent intent = new Intent(getApplicationContext(),
                ContactActivity.class);
        // disable default animation for new intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ActivitySwitcher.animationOut(findViewById(R.id.main_content),
                getWindowManager(),
                new ActivitySwitcher.AnimationFinishedListener() {
                    @Override
                    public void onAnimationFinished() {
                        startActivity(intent);
                    }
                });
    }
    @Override
    protected void onResume() {
        // animateIn this activity
        ActivitySwitcher.animationIn(findViewById(R.id.main_content),
                getWindowManager());
        super.onResume();
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

    public void setLocaleLanguage(String lang){
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
