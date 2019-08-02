package com.fgiet.incidentreporting.Users;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgiet.incidentreporting.R;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Admin on 8/24/2017.
 */

public class DonateViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    private TextView nametv,desctv,typetv,pricetv;
    private ImageView imagedonate;
    private Button buttondonate;
    public DonateViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        nametv=(TextView)mView.findViewById(R.id.tv_donate_name);
        desctv=(TextView)mView.findViewById(R.id.tv_donate_desc);
        typetv=(TextView)mView.findViewById(R.id.tv_donate_type);
        pricetv=(TextView)mView.findViewById(R.id.tv_donate_price);
        imagedonate=(ImageView)mView.findViewById(R.id.img_donate);
        buttondonate=(Button)mView.findViewById(R.id.btn_donate);

        buttondonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new SweetAlertDialog(mView.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Remember")
                        .setContentText("Don't Forget to mention 'Product Name' and 'Product Quantity' in the description box of PayTm")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                goDonate();
                            }
                        })
                        .show();
            }
        });
    }

    public void goDonate(){
        Intent launchIntent = mView.getContext().getPackageManager().getLaunchIntentForPackage("net.one97.paytm");
        String url="https://paytm.com";
        if (launchIntent != null) {
            mView.getContext().startActivity(launchIntent);//null pointer check in case package name was not found
        }
        else
        {
            launchIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            launchIntent.setPackage("com.android.chrome");
            try {
                mView.getContext().startActivity(launchIntent);
            } catch (ActivityNotFoundException ex) {

                Log.v("MainActivity", "NO Chrome APP INSTALLED");
                launchIntent.setPackage(null);
                mView.getContext().startActivity(launchIntent);
            }
        }
    }

    public void setDesc(String desc) {
       desctv.setText(desc);
    }

    public void setName(String name) {
       nametv.setText(name);
    }

    public void setType(String type) {
        typetv.setText(type);
    }

    public void setPrice(String price) {
        pricetv.setText("Price: "+price);
    }

    public  void setImage(String url){
        Picasso.with(mView.getContext()).load(url).into(imagedonate);
    }
}
