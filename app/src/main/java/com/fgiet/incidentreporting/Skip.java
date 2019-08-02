package com.fgiet.incidentreporting;

import android.app.Application;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.Firebase;

/**
 * Created by 1401480 on 7/4/2017.
 */

public class Skip extends Application {

    Bitmap bitmap=null;
    private ImageView mImageView;
    private ProgressDialog progressDialog;
    private Button btn_choose_image,button;
    private static final int PICK_FROM_CAMERA=1;
    private static final int PICK_FROM_FILE=2;
    private Uri selectedImage=null;
    private byte[] databaos=null;
    int check=0;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
