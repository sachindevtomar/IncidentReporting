package com.fgiet.incidentreporting.Employees;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fgiet.incidentreporting.MapsActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.fgiet.incidentreporting.MainActivity;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.Users.Home;
import com.fgiet.incidentreporting.Users.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class EmpActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView name, empEmail;
    Button logOut, save,closeEditProfile;
    ImageView editPic,editDPBtn,edit,profilePicture;
    EditText empName, empPhone;
    private byte[] databaos = null;
    private AlertDialog dialog;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    Bitmap bitmap = null;
    private Uri downloadUrl = null,selectedImage = null;
    int check = 0;
    private ProgressDialog progressDialog;
    AlertDialog dialogInside;
    String namestr="",phonestr="";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(EmpActivity.this, MainActivity.class));

                }
            }
        };
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        progressDialog = new ProgressDialog(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        empEmail = (TextView) findViewById(R.id.emp_email);
        name = (TextView) findViewById(R.id.emp_name);
        logOut = (Button) findViewById(R.id.emp_logout);
        edit = (ImageView) findViewById(R.id.edit);
        profilePicture=(ImageView)findViewById(R.id.profile_picture);

        empEmail.setText(mAuth.getCurrentUser().getEmail());

        mDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("ProfilePicture").getValue().toString().equals("1")){
                    Picasso.with(EmpActivity.this).load(dataSnapshot.child("ProfilePictureUrl").getValue().toString()).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EmpActivity.this, R.style.AnimateDialog1);
                View mView = getLayoutInflater().inflate(R.layout.edit_profile_dialog, null);
                empName = (EditText)mView.findViewById(R.id.edit_name);
                empPhone=(EditText)mView.findViewById(R.id.edit_phone);
                save=(Button)mView.findViewById(R.id.save_profile);
                closeEditProfile=(Button)mView.findViewById(R.id.close_edit_profile);
                editDPBtn=(ImageView)mView.findViewById(R.id.edit_dp_btn);
                editPic=(ImageView)mView.findViewById(R.id.edit_profile_picture);

                editDPBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       dialogInside.show();

                    }
                });

                mDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("ProfilePicture").getValue().toString().equals("1")){
                            Picasso.with(EmpActivity.this).load(dataSnapshot.child("ProfilePictureUrl").getValue().toString()).into(editPic);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendData();
                    }
                });

                final String[] items = new String[]{getString(R.string.from_camera), getString(R.string.from_gallery)};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), android.R.layout.select_dialog_item, items);

                //For Dialog Appearance
                AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
                builder.setTitle(getString(R.string.select_image));
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_FROM_CAMERA);
                            dialog.cancel();
                        } else {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Complete Action Using"), PICK_FROM_FILE);

                        }
                    }
                });
                dialogInside = builder.create();

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        empName.setText(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Name").getValue().toString());
                        empPhone.setText(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Phone").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
                closeEditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });



        mDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EmpActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to Log Out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(new String[]{
                        Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS
                }, 10);
            }
        } else {

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PICK_FROM_CAMERA) {
            //get the camera image
            check = 1;
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageSet(baos);

        }
        else if(requestCode==PICK_FROM_FILE){
            //get the gallery image
            check = 2;
            if (resultCode == RESULT_OK) {

                selectedImage = data.getData();

                try {
                    bitmap = decodeBitmap(selectedImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    imageSet(baos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public void imageSet(ByteArrayOutputStream baos) {
            databaos = baos.toByteArray();
            editPic.setImageBitmap(bitmap);
    }

    public void sendData() {
        namestr = empName.getText().toString().trim();
        phonestr = empPhone.getText().toString().trim();
        if (phonestr.length() != 10 && phonestr.length() != 0) {
            Toast.makeText(EmpActivity.this,R.string.enter_valid_mobile_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(namestr)||TextUtils.isEmpty(phonestr)){
            Toast.makeText(EmpActivity.this, R.string.every_field_is_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if(databaos==null){
            Toast.makeText(EmpActivity.this,R.string.please_select_an_image, Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage(getString(R.string.uploading_image));
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        //Firebase storage folder where you want to put the images

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("ProfilePics").child("Pic_" + new Date().getTime());
        UploadTask uploadTask = null;
        uploadTask = imagesRef.putBytes(databaos);
        storeImage(bitmap);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(EmpActivity.this,R.string.uploading_failed +exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                dialog.dismiss();
                downloadUrl = taskSnapshot.getDownloadUrl();


                mDatabase.child(mAuth.getCurrentUser().getUid()).child("Name").setValue(namestr);
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("Phone").setValue(phonestr);
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("ProfilePicture").setValue("1");
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("ProfilePictureUrl").setValue(downloadUrl.toString());

                Intent intent=new Intent(EmpActivity.this,MainActivity.class);
                    //intent.putExtra("cro","2");
                Toast.makeText(EmpActivity.this, R.string.upload_done, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
                }

        });
}

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Jeevaashraya"
                + "/Images");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="image_"+timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    EmpTab1 tab1 = new EmpTab1();
                    return tab1;
                case 1:
                    EmpTab2 tab2 = new EmpTab2();
                    return tab2;
                case 2:
                    Post tab3 = new Post();
                    return tab3;
                case 3:
                    Extras tab4 = new Extras();
                    return tab4;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Requests";
                case 1:
                    return "Posts";
                case 2:
                    return "News Feed";
                case 3:
                    return "Extras";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}

