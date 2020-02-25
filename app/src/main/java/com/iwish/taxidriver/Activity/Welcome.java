package com.iwish.taxidriver.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.iwish.taxidriver.Map.MainActivity;
import com.iwish.taxidriver.R;
import com.iwish.taxidriver.usersession.UserSession;
import com.iwish.taxidriver.webSocket.BackgroundService;
import com.iwish.taxidriver.webSocket.SocketService;

public class Welcome extends AppCompatActivity {

    private static final int RC_OVERLAY = 0;
    private static int SPLASH_TIME_OUT = 3000;
    private static final int PERMISSION_REQUEST_GPS_CODE = 1234;
    //to get user session data
    private UserSession session;
    boolean isGPS;
    LocationManager locationManager;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
//        Location();


//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
//        {
//            setting();
//        }
        session =new UserSession(Welcome.this);
        if(!(session.getLocationStatus()))
        {
            if(!(session.getbookingstatus())){
                session.Booking("false");
                session.set_requestdata("null");
                session.setLocationStatus(false);
                session.setSocketConnection(false);
                session.set_current_duty_status(String.valueOf(false));
            }
            else
            {
                session.setSocketConnection(false);
                session.setsocketonoff("true");
                SocketService  socketService= new SocketService();
                socketService.setdata(Welcome.this);
                Intent intent= new Intent(Welcome.this,SocketService.class);
                socketService.onStartCommand(intent,1,1);
                session.set_current_duty_status("ture");
            }

        }
        else
        {
            session.setSocketConnection(false);
            session.setsocketonoff("true");
          SocketService  socketService= new SocketService();
            socketService.setdata(Welcome.this);
            Intent intent= new Intent(Welcome.this,SocketService.class);
            socketService.onStartCommand(intent,1,1);
            session.set_current_duty_status("ture");
        }

//        session.Booking("false");
//        session.set_requestdata("null");
//        session.setLocationStatus(false);
//        session.setSocketConnection(false);
//        session.set_current_duty_status(String.valueOf(false));


//        session.Booking("false");
//        session.set_requestdata("null");
////        session.checkLogin();
//        session.setLocationStatus(false);
//        session.setSocketConnection(false);
//        session.set_current_duty_status(String.valueOf(false));


//       Intent intent = new Intent(Welcome.this,Login.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        finish();

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

//    public void setting()
//    {
//        final Intent intent= new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getOpPackageName()));
//
//        try{
//            startActivityForResult(intent,RC_OVERLAY);
//        }catch (ActivityNotFoundException e){
//            Log.e("tag",e.getMessage());
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        switch(requestCode){
//
//            case RC_OVERLAY:
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                    final boolean overlayEnabled= Settings.canDrawOverlays(this);
//                }
//                break;
//
//        }
//    }



}
