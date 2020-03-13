package com.iwish.taxidriver.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.iwish.taxidriver.Map.MainActivity;
import com.iwish.taxidriver.R;
import com.iwish.taxidriver.config.Constants;
import com.iwish.taxidriver.connection.ConnectionServer;
import com.iwish.taxidriver.connection.JsonHelper;
import com.iwish.taxidriver.extended.ButtonFonts;
import com.iwish.taxidriver.extended.TexiFonts;
import com.iwish.taxidriver.usersession.UserSession;
import com.goodiebag.pinview.Pinview;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;

public class Login_check extends AppCompatActivity {
    TexiFonts mobile;
    Pinview otp;
    ButtonFonts varify;
    KProgressHUD kProgressHUD;
    TexiFonts resend;
    UserSession  userSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_check);

        kProgressHUD= new KProgressHUD(Login_check.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        mobile= findViewById(R.id.mobile1);
        otp= findViewById(R.id.otp);
        varify= findViewById(R.id.varify);
        resend= findViewById(R.id.resend);
        final Intent intent= getIntent();
        mobile.setText(intent.getStringExtra("mobile"));

        varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( validata_otp(otp.getValue())){
                    check_login(intent.getStringExtra("mobile"),otp.getValue());
                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request_otp(intent.getStringExtra("mobile"));
            }
        });

    }


    public void check_login(final String mobile, String otp){
        setProgressDialog("verificating...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.LOGIN);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("mobile", mobile);
        connectionServer.buildParameter("otp", otp);
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(),"data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);
                            UserSession userSession= new UserSession(Login_check.this);

                            userSession.createLoginSession(mobile,jsonHelper.GetResult("driver_id"),jsonHelper.GetResult("catagory_name"));
                        }

                        Intent intent = new Intent(Login_check.this, MainActivity.class);//Home
                        intent.putExtra("mobile",mobile);
                        remove_progress_Dialog();
                        startActivity(intent);
                        Animatoo.animateFade(Login_check.this);
                    } else {

                        remove_progress_Dialog();
                        Toast.makeText(Login_check.this, "Incorrect otp", Toast.LENGTH_SHORT).show();
                    }
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
                        Toast.makeText(Login_check.this,"OTP Send Successfully", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        remove_progress_Dialog();
                        Toast.makeText(Login_check.this,"Entered "+ jsonHelper.GetResult("data"), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Animatoo.animateShrink(Login_check.this); //fire the slide left animation
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

    public boolean validata_otp(String inemail){

        if(inemail.isEmpty()){
            Toast.makeText(this, "Please Enter OTP ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
