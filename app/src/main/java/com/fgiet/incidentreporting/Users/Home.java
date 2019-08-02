package com.fgiet.incidentreporting.Users;

        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.content.res.Resources;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.NavigationView;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.app.AppCompatDelegate;
        import android.support.v7.widget.Toolbar;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.fgiet.incidentreporting.DonateFragment;
        import com.fgiet.incidentreporting.HelpFragment;
        import com.fgiet.incidentreporting.MainActivity;
        import com.fgiet.incidentreporting.R;

        import java.util.Locale;

        import de.keyboardsurfer.android.widget.crouton.Crouton;
        import de.keyboardsurfer.android.widget.crouton.Style;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    TextView homeEmail,homeName;
    public static int locationEnabledIntend=0;
    Button btnLogOut;
    private GoogleApiClient mGoogleApiClient;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        homeEmail=(TextView)hView.findViewById(R.id.tv_home_email);
        homeName=(TextView)hView.findViewById(R.id.tv_home_name);
        btnLogOut=(Button)hView.findViewById(R.id.bt_nav_logout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override

                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

                        Crouton.showText(Home.this, "Check your network and try again", Style.ALERT);

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                new AlertDialog.Builder(Home.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to Log Out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

       if(mAuth.getCurrentUser()!=null) {
           homeEmail.setText(mAuth.getCurrentUser().getEmail());
           DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

           mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   homeName.setText(dataSnapshot.getValue(String.class));
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
       }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Home.this, MainActivity.class));
//                    finish();
                }
            }
        };
        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }



    public void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                //simulateLoading();
                fragment=new UserFragment();
                break;
            case R.id.nav_newsfeed:
                fragment=new Post();
                break;
            case R.id.nav_nearbyplaces:
                fragment=new NearbyPlaces();
                break;
//            case R.id.nav_about_us:
//                fragment=new AboutUsFragment();
////                Intent open=new Intent(Home.this,AboutUsActivity.class);
////                startActivity(open);
//                break;
//            case R.id.nav_contact_us:
//                fragment =new ContactFragment();
//                break;
            case R.id.nav_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.ebay.mobile&hl=en");
                startActivity(Intent.createChooser(intent,"Share"));
                break;
            case R.id.nav_help:
                fragment =new HelpFragment();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_home, fragment);
            ft.commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START, true);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void simulateLoading(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute(){
                progressDialog.setMessage("Loading...");
                progressDialog.show();

            }
            @Override
            protected  Void doInBackground(Void... params){
                try{
                    Thread.sleep(2000);
                }
                catch (Exception e){

                }
                return null;
            }
            @Override
            protected void onPostExecute(Void param){
                progressDialog.dismiss();
            }
        }.execute();
    }
}
