package com.example.taxidriver.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.taxidriver.Distance.DistanceTimeCalculate;
import com.example.taxidriver.Map.MainActivity;
import com.example.taxidriver.R;
import com.example.taxidriver.config.Constants;
import com.example.taxidriver.connection.ConnectionServer;
import com.example.taxidriver.connection.JsonHelper;
import com.example.taxidriver.usersession.UserSession;
import com.example.taxidriver.webSocket.BackgroundService;
import com.example.taxidriver.webSocket.InternetConnectivity;
import com.example.taxidriver.webSocket.SocketService;
import com.example.taxidriver.webSocket.WebSocketManupulation;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class Request extends AppCompatActivity {
    Button confirm;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_dialog);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        intent=getIntent();
        final String d=intent.getStringExtra("data");
        confirm=findViewById(R.id.confirm);

//        Log.e("fghg",d);

   confirm.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           Intent intent1= new Intent(Request.this,SocketService.class);
           intent1.putExtra("data",d);
           startService(intent1);
       }
   });


//        String object= intent.getStringExtra("data");
//        Log.e("sdsdf",object);
//
//        try {
//
//            this.webSocket=c;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


//        try {
//            mainObject = new JSONObject(data);
//            JSONObject clientdata = mainObject.getJSONObject("data");
//             pickuplat=clientdata.getString("pickuplat");
//             picklong=clientdata.getString("pickuplong");
//             droplat=clientdata.getString("droplat");
//             droplong=clientdata.getString("droplong");
//             clientid=clientdata.getString("userId");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//                 distanceTimeCalculate= new DistanceTimeCalculate(pickuplat,picklong,droplat,droplong);







    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void Savetomysql(String driverid, String clientid, String pickuplat, String pickuplong, String droplat, String droplong){
//        setProgressDialog("Send OTP ...");
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.BOOKINGDATA);
        connectionServer.buildParameter("driver",driverid);
        connectionServer.buildParameter("client",clientid);
        connectionServer.buildParameter("pickuplat",pickuplat);
        connectionServer.buildParameter("pickuplong",pickuplong);
        connectionServer.buildParameter("droplat",droplat);
        connectionServer.buildParameter("droplong",droplong);

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
//                        remove_progress_Dialog();
                    }
                    else
                    {
//                        remove_progress_Dialog();

                    }
                }

            }
        });

    }

//    public  void setWebSocket(Class<?> webSocket)
//    {
//        this.webSocket=webSocket;
//    }



}
