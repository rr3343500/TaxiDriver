package com.example.taxidriver.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;

import com.example.taxidriver.R;
import com.example.taxidriver.usersession.UserSession;

public class Welcome extends AppCompatActivity {

    private static final int RC_OVERLAY = 0;
    private static int SPLASH_TIME_OUT = 3000;

    //to get user session data
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
        {
            setting();
        }

        session =new UserSession(Welcome.this);
        session.Booking("false");
        session.checkLogin();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(Welcome.this,Login.class));
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    public void setting()
    {
        final Intent intent= new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getOpPackageName()));

        try{
            startActivityForResult(intent,RC_OVERLAY);
        }catch (ActivityNotFoundException e){
            Log.e("tag",e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){

            case RC_OVERLAY:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    final boolean overlayEnabled= Settings.canDrawOverlays(this);
                }
                break;

        }
    }
}
