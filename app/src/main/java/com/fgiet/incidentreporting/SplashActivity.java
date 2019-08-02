package com.fgiet.incidentreporting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fgiet.incidentreporting.Users.SignUp;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class SplashActivity extends AppCompatActivity {

    int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_splash);
        Thread timer=new Thread(){
            public void run()
            {
                try{
                    if(isNetworkAvailable())
                    {
                        temp=1;
                        sleep(2000);
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this,NoInternetActivity.class));
                        finish();
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    if(temp==1) {
                        Intent open = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(open);
                    }
                }
            }
        };
        timer.start();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
