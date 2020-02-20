package com.example.taxidriver.Request;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.taxidriver.Map.MainActivity;
import com.example.taxidriver.R;
import com.example.taxidriver.config.Constants;
import com.example.taxidriver.connection.ConnectionServer;
import com.example.taxidriver.connection.JsonHelper;
import com.example.taxidriver.extended.ButtonFonts;
import com.example.taxidriver.usersession.UserSession;
import com.goodiebag.pinview.Pinview;
import com.kaopiz.kprogresshud.KProgressHUD;


public class RequestDialog   extends Dialog  implements android.view.View.OnClickListener {
    KProgressHUD kProgressHUD;
    Pinview otp;
    ButtonFonts varify;
    UserSession userSession;
    Context context;
    String  clientid,driverid;

    public RequestDialog(@NonNull Context context) {
        super(context);
        this.context= context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_otp);
        otp = findViewById(R.id.otp);
        varify= findViewById(R.id.varify);
        userSession= new UserSession(context);
//       clientid= userSession.getClientID();

        varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_otp(otp.getValue(),userSession.getUserDetails().get("driverid"),userSession.getClientID());


            }
        });

    }


    @Override
    public void onClick(View view) {

        check_otp(otp.getValue(),userSession.getUserDetails().get("driverid"),userSession.getClientID());

    }


    public void check_otp(final String otp ,String driverid, String client_id){
//        setProgressDialog("Varifying OTP ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.TRIPOTP);
        connectionServer.buildParameter("otp",otp);
        connectionServer.buildParameter("driver_id",driverid);
        connectionServer.buildParameter("client_id",client_id);

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
//                        remove_progress_Dialog();
                      userSession.setLocationStatus(true);
                      userSession.setotpconfirm(true);
                      Intent intent= new Intent(context,MainActivity.class);
                      context.startActivity(intent);
                        dismiss();
                        Toast.makeText(context, "ride is seted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
//                        remove_progress_Dialog();
                        Toast.makeText(context, "Incorrect otp", Toast.LENGTH_SHORT).show();
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
}
