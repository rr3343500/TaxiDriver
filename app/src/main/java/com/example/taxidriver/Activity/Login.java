package com.example.taxidriver.Activity;

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
import com.example.taxidriver.Map.MainActivity;
import com.example.taxidriver.R;
import com.example.taxidriver.config.Constants;
import com.example.taxidriver.connection.ConnectionServer;
import com.example.taxidriver.connection.JsonHelper;
import com.example.taxidriver.extended.ButtonFonts;
import com.example.taxidriver.extended.EdittextFont;
import com.example.taxidriver.webSocket.WebSocketConnection;
import com.example.taxidriver.webSocket.WebSocketManupulation;
import com.google.firebase.database.tubesock.WebSocket;
import com.kaopiz.kprogresshud.KProgressHUD;

import static com.example.taxidriver.Map.MainActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class Login extends AppCompatActivity {
    KProgressHUD kProgressHUD;
       EdittextFont mobile;
       ButtonFonts request;
       WebSocketConnection webSocketConnection;
    boolean isGPS= false;
    LocationManager locationManager;
    private static final int PERMISSION_REQUEST_GPS_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kProgressHUD= new KProgressHUD(Login.this);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
Location();
        checkLocationPermission();

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
//                buildGoogleApiClient();

            } else {
                //Request Location Permission
                Location();
//                latitude = mLastLocation.getLatitude();
//                longitude = mLastLocation.getLongitude();
            }
        }



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
                        Toast.makeText(Login.this,"your otp is :"+ jsonHelper.GetResult("data"), Toast.LENGTH_SHORT).show();
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


    public void Location()
    {
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);



        //get gps permission by user


            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
//                            ActivityCompat.requestPermissions(MapsActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    MY_PERMISSIONS_REQUEST_LOCATION);

                            showGPSSettingsAlert();
                        }
                    })
                    .create()
                    .show();

        }
        //get gps permission by user



    //method for enabling gps by user
    public void showGPSSettingsAlert() {
        isGPS = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.word_GPS);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(R.string.word_GPS_not_enabled);
        alertDialog.setPositiveButton(R.string.word_GPS_enable, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), PERMISSION_REQUEST_GPS_CODE);
            }
        });

        alertDialog.show();
    }
    //method for enabling gps by user

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
