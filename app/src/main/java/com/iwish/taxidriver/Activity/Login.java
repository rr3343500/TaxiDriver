package com.iwish.taxidriver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.iwish.taxidriver.R;
import com.iwish.taxidriver.config.Constants;
import com.iwish.taxidriver.connection.ConnectionServer;
import com.iwish.taxidriver.connection.JsonHelper;
import com.iwish.taxidriver.extended.ButtonFonts;
import com.iwish.taxidriver.extended.EdittextFont;
import com.iwish.taxidriver.usersession.UserSession;
import com.iwish.taxidriver.webSocket.WebSocketConnection;
import com.kaopiz.kprogresshud.KProgressHUD;

import static com.iwish.taxidriver.Map.MainActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class Login extends AppCompatActivity {
    KProgressHUD kProgressHUD;
       EdittextFont mobile;
       ButtonFonts request;
       UserSession userSession;
    boolean isGPS= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kProgressHUD= new KProgressHUD(Login.this);
        userSession =new UserSession(Login.this);
        userSession.checkLogin();
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        checkLocationPermission();




        request= findViewById(R.id.request);
        mobile=findViewById(R.id.mobile);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( validateemail(mobile.getText().toString())){
                    Request_otp(mobile.getText().toString());
                }

            }
        });

    }


    public void Request_otp(final String mobile){
        setProgressDialog("Send OTP ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.REQUEST_OTP);
        connectionServer.buildParameter("mobile",mobile);

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        remove_progress_Dialog();
//                        Toast.makeText(Login.this,"your otp is :"+ jsonHelper.GetResult("data"), Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(Login.this,Login_check.class);
                        intent.putExtra("mobile",mobile);
                        startActivity(intent);
                        Animatoo.animateShrink(Login.this);
                    }
                    else
                    {
                        remove_progress_Dialog();
                        Toast.makeText(Login.this,"Entered "+ jsonHelper.GetResult("data"), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

    }

    public void setProgressDialog(String msg) {
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(msg)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }

    public void remove_progress_Dialog() {
        kProgressHUD.dismiss();
    }

    public boolean validateemail(String inemail){

        if(inemail.isEmpty()){
            mobile.setError("mobile field is empty.");
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Animatoo.animateSlideLeft(Login.this); //fire the slide left animation
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to Request the permission.
                new AlertDialog.Builder(this).setTitle("Location Permission Needed").setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Login.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can Request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            }
        }
    }
}
