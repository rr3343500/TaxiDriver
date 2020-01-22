package com.example.taxidriver.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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

public class Login extends AppCompatActivity {
    KProgressHUD kProgressHUD;
       EdittextFont mobile;
       ButtonFonts request;
       WebSocketConnection webSocketConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kProgressHUD= new KProgressHUD(Login.this);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();



//        webSocketConnection= new WebSocketConnection();
//        webSocketConnection.onCreate();
//        webSocketConnection.SendData();


//          webSocketManupulation= new WebSocketManupulation();




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
}
