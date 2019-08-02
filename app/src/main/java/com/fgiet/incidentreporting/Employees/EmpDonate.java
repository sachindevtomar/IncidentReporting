package com.fgiet.incidentreporting.Employees;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.fgiet.incidentreporting.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class EmpDonate extends Fragment {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String donateType="Select Category For Donation",downloadUrl;
    Button sendBtn;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    ImageButton imageBtn;
    EditText nameEt,priceEt,descEt;
    View sbView,tempView;
    Snackbar snackbar;
    Bitmap bitmap=null;
    byte[] databaos=null;
    Uri imageUri;
    private ProgressDialog progressDialog;
    DatabaseReference mDatabaseDonate= FirebaseDatabase.getInstance().getReference().child("Donate");
    DatabaseReference newDonate;

    public EmpDonate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView=inflater.inflate(R.layout.fragment_emp_donate, container, false);

        spinner=(Spinner)mView.findViewById(R.id.spinner);
        nameEt=(EditText)mView.findViewById(R.id.emp_donate_name);
        priceEt=(EditText)mView.findViewById(R.id.emp_donate_price);
        descEt=(EditText)mView.findViewById(R.id.emp_donate_desc);
        imageBtn=(ImageButton)mView.findViewById(R.id.emp_donate_img);
        sendBtn=(Button)mView.findViewById(R.id.emp_donate_send);
        progressDialog = new ProgressDialog(getActivity());

        adapter=ArrayAdapter.createFromResource(getActivity(),R.array.donate_category,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                donateType=adapterView.getItemAtPosition(position).toString();
               try {
                   if (donateType.equals("Select Category For Donation"))
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
        return mView;
    }

    @Override
    public void onViewCreated(View mView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mView, savedInstanceState);
        tempView=mView;
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkEmpty()){
                    sendData();
                }
                else{
                    snackbar= Snackbar.make(tempView, "Every Field is Required", Snackbar.LENGTH_LONG);
                    sbView=snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTypeface(null, Typeface.BOLD);
                    snackbar.show();
                }
            }
        });


        final String[] items = new String[]{"From Camera", "From Gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);

        //For Dialog Appearance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    public boolean checkEmpty(){
        if(nameEt.getText().toString().trim().equals("")|| descEt.getText().toString().trim().equals("")||
                priceEt.getText().toString().trim().equals("")||
                donateType.equals("Select Category For Donation")){
            return true;
        }
        else
            return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==PICK_FROM_CAMERA){
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                databaos=baos.toByteArray();
                imageBtn.setImageBitmap(bitmap);
                imageBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else if(requestCode==PICK_FROM_FILE){
                imageUri=data.getData();
               try {
                   bitmap = decodeBitmap(imageUri);
                   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                   databaos=baos.toByteArray();
                   imageBtn.setImageBitmap(bitmap);
                   imageBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
               }
               catch (Exception e){
                   Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        }
        else{
            Toast.makeText(getActivity(), "Error occured, try again", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

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
        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
    }


    public void sendData(){
        if(nameEt.getText().toString().trim().length()>20||descEt.getText().toString().trim().length()>20){
            return;
        }
        if(databaos==null){
            snackbar= Snackbar.make(tempView, "Please select image", Snackbar.LENGTH_LONG);
            sbView=snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
            tv.setTypeface(null, Typeface.BOLD);
            snackbar.show();
            return;
        }

        progressDialog.setMessage("Uploding Donation details...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        //Firebase storage folder where you want to put the images


        StorageReference storageDonation = FirebaseStorage.getInstance().getReference();

        StorageReference imagesRef = storageDonation.child("Donate").child("Donate_" + new Date().getTime());
        UploadTask uploadDonateTask = null;
        uploadDonateTask = imagesRef.putBytes(databaos);
        uploadDonateTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                snackbar= Snackbar.make(tempView, "Please select image", Snackbar.LENGTH_LONG);
                sbView=snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                tv.setTypeface(null, Typeface.BOLD);
                snackbar.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl=taskSnapshot.getDownloadUrl().toString();
                progressDialog.dismiss();
                newDonate=mDatabaseDonate.push();
                newDonate.child("name").setValue(nameEt.getText().toString().trim());
                newDonate.child("desc").setValue(descEt.getText().toString().trim());
                newDonate.child("price").setValue(priceEt.getText().toString().trim());
                newDonate.child("imageurl").setValue(downloadUrl);
                newDonate.child("type").setValue(donateType);
                databaos=null;
                nameEt.setText("");
                descEt.setText("");
                priceEt.setText("");
                imageBtn.setImageResource(R.drawable.ic_add_a_photo_white_24dp);
                snackbar= Snackbar.make(tempView, "Upload Done", Snackbar.LENGTH_LONG);
                sbView=snackbar.getView();
                sbView.setBackgroundColor(Color.GREEN);
                TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }
}
