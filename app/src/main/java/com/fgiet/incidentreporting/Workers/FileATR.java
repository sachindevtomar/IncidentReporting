package com.fgiet.incidentreporting.Workers;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import com.fgiet.incidentreporting.Employees.EmpActivity;
import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.ZoomableImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.support.design.R.id.container;

/**
 * Created by This Pc on 3/27/2018.
 */

public class FileATR extends AppCompatActivity {


    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private static final int VOICE_SPEECH = 3;
    private Button btn_choose_image;
    int imageArray[] = new int[5];
    private Button send;
    private ProgressDialog progressDialog;
    private Uri selectedImage = null;
    private byte[] databaos1 = null, databaos2 = null, databaos3 = null, databaos4 = null, databaos5 = null;
    private ImageView mImageView1, mImageView2, mImageView3, mImageView4, mImageView5;
    private ImageButton close1, close2, close3, close4, close5;
    private FrameLayout frame1, frame2, frame3, frame4, frame5;
    int check = 0, count = 0, temp = 0, temp1 = 0, temp2 = 0;
    Bitmap bitmap = null, bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null, bitmap5 = null;
    int k = 0;
    String imageUrl[] = new String[5];
    String requestid;
    private Uri downloadUrl = null;
    private EditText desc;
    DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date date = new Date();
    Snackbar snackbar;
    View sbView,tempView;
    private FirebaseAuth  mAuth = FirebaseAuth.getInstance();


    DatabaseReference mDatabaseUser=FirebaseDatabase.getInstance().getReference("Users");

    private DatabaseReference mDatabase, newPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_atr);


        setTitle("Accident reporting");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ATR");

        imageArray[0] = imageArray[1] = imageArray[2] = imageArray[3] = imageArray[4] = 0;
        mImageView1 = (ImageView) findViewById(R.id.customView1);
        mImageView2 = (ImageView) findViewById(R.id.customView2);
        mImageView3 = (ImageView) findViewById(R.id.customView3);
        mImageView4 = (ImageView) findViewById(R.id.customView4);
        mImageView5 = (ImageView) findViewById(R.id.customView5);
        close1 = (ImageButton) findViewById(R.id.button1);
        close2 = (ImageButton) findViewById(R.id.button2);
        close3 = (ImageButton) findViewById(R.id.button3);
        close4 = (ImageButton) findViewById(R.id.button4);
        close5 = (ImageButton) findViewById(R.id.button5);
        frame1 = (FrameLayout) findViewById(R.id.frame1);
        frame2 = (FrameLayout) findViewById(R.id.frame2);
        frame3 = (FrameLayout) findViewById(R.id.frame3);
        frame4 = (FrameLayout) findViewById(R.id.frame4);
        frame5 = (FrameLayout) findViewById(R.id.frame5);
        btn_choose_image = (Button) findViewById(R.id.btn_choose_image);
//        send=(Button) mView.findViewById(R.id.button);
        FloatingActionButton send = (FloatingActionButton) findViewById(R.id.button);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("File ATR");
        }

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        tempView=View.inflate(this,R.layout.activity_file_atr,viewGroup);

        Intent intent = getIntent();
        requestid = intent.getStringExtra("requestid");

        desc = (EditText) findViewById(R.id.desc);

        //picture
        progressDialog = new ProgressDialog(this);

        frame1.setVisibility(View.GONE);
        frame2.setVisibility(View.GONE);
        frame3.setVisibility(View.GONE);
        frame4.setVisibility(View.GONE);
        frame5.setVisibility(View.GONE);

        desc.setScroller(new Scroller(this));
        desc.setMaxLines(10);
        desc.setVerticalScrollBarEnabled(true);
        desc.setMovementMethod(new ScrollingMovementMethod());

        final String[] items = new String[]{"From Camera", "From Gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);

        //For Dialog Appearance
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
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

        final AlertDialog dialog = builder.create();

        //To choose Image
        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
                if (count != 5) {
                    dialog.show();
                } else {

                    snackbar= Snackbar.make(tempView, "Select maximum 5 images", Snackbar.LENGTH_LONG);
                    sbView=snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
            }
        });

        //ImageView in Dialog
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap1);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap2);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap3);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap4);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();
                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        mImageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.overlay, null);
                final ZoomableImageView customView_overlay = (ZoomableImageView) mView.findViewById(R.id.customView_overlay);
                final ImageButton close_overlay = (ImageButton) mView.findViewById(R.id.close_overlay);
                customView_overlay.setImageBitmap(bitmap5);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        //Close Button in Overlay
        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[0] = 0;
                count = 0;
                frame1.setVisibility(View.GONE);
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[1] = 0;
                count = 0;
                frame2.setVisibility(View.GONE);
            }
        });

        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[2] = 0;
                count = 0;
                frame3.setVisibility(View.GONE);
            }
        });

        close4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[3] = 0;
                count = 0;
                frame4.setVisibility(View.GONE);
            }
        });

        close5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArray[4] = 0;
                count = 0;
                frame5.setVisibility(View.GONE);
            }
        });

        //Send Button for firebase
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable())
                    sendData();
                else {
                  //  Toast.makeText(this, "Your device does not support this feature", Toast.LENGTH_SHORT).show();
                }
            }
        });

        desc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                // TODO Auto-generated method stub
                if (v.getId() ==R.id.desc) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (desc.getRight() - desc.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        speechMethod();
                        return true;
                    }
                }
                return false;
            }
        });


    }

    //picture
    public void addImage() {
        int i = 0;
        for (; i < 5; i++) {
            if (imageArray[i] == 0) {
                temp = i + 1;
                break;
            }
        }
        if (i == 5)
            count = i;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            imageSet(baos, bitmap);

        } else if(requestCode==PICK_FROM_FILE){
            //get the gallery image
            check = 2;
            if (resultCode == RESULT_OK) {

                selectedImage = data.getData();

                try {
                    bitmap = decodeBitmap(selectedImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    imageSet(baos, bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            if(data!=null)
            {
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String tempString=result.get(0);
                if(!tempString.equals("clear all"))
                    desc.setText(desc.getText()+" "+tempString);
                else{
                    desc.setText("");
                    Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
            Toast.makeText(this, "Your device does not support this feature", Toast.LENGTH_SHORT).show();
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

    public void imageSet(ByteArrayOutputStream baos, Bitmap bitmapTemp) {
        if (temp == 1) {
            databaos1 = baos.toByteArray();
            bitmap1 = bitmapTemp;
            mImageView1.setImageBitmap(bitmap);
            imageArray[0] = 1;
            frame1.setVisibility(View.VISIBLE);
        } else if (temp == 2) {
            databaos2 = baos.toByteArray();
            bitmap2 = bitmapTemp;
            mImageView2.setImageBitmap(bitmap);
            imageArray[1] = 1;
            frame2.setVisibility(View.VISIBLE);
        } else if (temp == 3) {
            bitmap3 = bitmapTemp;
            databaos3 = baos.toByteArray();
            mImageView3.setImageBitmap(bitmap);
            imageArray[2] = 1;
            frame3.setVisibility(View.VISIBLE);
        } else if (temp == 4) {
            bitmap4 = bitmapTemp;
            databaos4 = baos.toByteArray();
            mImageView4.setImageBitmap(bitmap);
            imageArray[3] = 1;
            frame4.setVisibility(View.VISIBLE);
        } else {
            bitmap5 = bitmapTemp;
            databaos5 = baos.toByteArray();
            mImageView5.setImageBitmap(bitmap);
            imageArray[4] = 1;
            frame5.setVisibility(View.VISIBLE);
        }
    }


    public void sendData() {
        final String descstr = desc.getText().toString().trim();

        for (k = 0; k < 5 && imageArray[k] == 1; k++) {
            temp1++;
        }

        if (temp1 != 0) {

            progressDialog.setMessage("Uploding image...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            //Firebase storage folder where you want to put the images


            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            for (k = 0; k < 5 && imageArray[k] == 1; k++) { //name of the image file (add time to have different files to avoid rewrite on the same file)
                StorageReference imagesRef = storageRef.child("ATR").child("Camera_" + k + "_" + new Date().getTime());
//            //send this name to database
//            //upload image

                UploadTask uploadTask = null;
                if (k == 0) {
                    uploadTask = imagesRef.putBytes(databaos1);
                    storeImage(bitmap1, k);
                } else if (k == 1) {
                    uploadTask = imagesRef.putBytes(databaos2);
                    storeImage(bitmap2, k);
                } else if (k == 2) {
                    uploadTask = imagesRef.putBytes(databaos3);
                    storeImage(bitmap3, k);
                } else if (k == 3) {
                    uploadTask = imagesRef.putBytes(databaos4);
                    storeImage(bitmap4, k);
                } else {
                    uploadTask = imagesRef.putBytes(databaos5);
                    storeImage(bitmap5, k);
                }
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        snackbar= Snackbar.make( tempView,exception.getMessage(), Snackbar.LENGTH_LONG);
                        sbView=snackbar.getView();
                        sbView.setBackgroundColor(Color.RED);
                        snackbar.show();
                        temp1=0;
                        temp2=0;
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        downloadUrl = taskSnapshot.getDownloadUrl();
                        imageUrl[temp2] = downloadUrl.toString();
                        temp2++;
                        if (temp1 == temp2) {
                            progressDialog.dismiss();
                            newPost = mDatabase.child(requestid).push();
                            newPost.child("desc").setValue(descstr);
                            newPost.child("imagecount").setValue(temp1);
                            newPost.child("date").setValue(sdf.format(date).toString());
                            newPost.child("workerid").setValue(mAuth.getCurrentUser().getUid());
                            newPost.child("atrno").setValue(newPost.getKey());
                            newPost.child("requestid").setValue(requestid);
                            mDatabaseUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    newPost.child("workername").setValue(dataSnapshot.child("Name").getValue().toString());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            for (k = 0; k < temp2; k++) {
                                newPost.child("image_" + k).setValue(imageUrl[k]);
                            }
                            snackbar= Snackbar.make(tempView, "Upload Done", Snackbar.LENGTH_LONG);
                            startActivity(new Intent(FileATR.this,FileATR.class));
                            sbView=snackbar.getView();
                            sbView.setBackgroundColor(Color.GREEN);
                            snackbar.show();


                        }
                    }
                });
            }
        }
        else {
            snackbar= Snackbar.make(tempView, "Select atleast one image", Snackbar.LENGTH_LONG);
            sbView=snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

    private void storeImage(Bitmap image, int k) {
        File pictureFile = getOutputMediaFile(k);
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
    private File getOutputMediaFile(int k) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Jeevaashraya"
                + "/Post");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "image_" + k + "_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}