package com.iwish.taxidriver.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.iwish.taxidriver.Map.MainActivity;
import com.iwish.taxidriver.R;
import com.iwish.taxidriver.config.Constants;
import com.iwish.taxidriver.connection.ConnectionServer;
import com.iwish.taxidriver.connection.JsonHelper;
import com.iwish.taxidriver.extended.TexiFonts;
import com.iwish.taxidriver.usersession.UserSession;

import java.io.IOException;
import java.util.List;

public class Request extends AppCompatActivity {
    Button confirm;
    UserSession userSession;
    String piclat,piclong,droplat,droplong ,d;
    Geocoder geocoder;
    List<Address> address = null;
    TexiFonts add, bookingtype,time;
    LinearLayout duration;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        userSession= new UserSession(this);
        add= findViewById(R.id.address);
        bookingtype= findViewById(R.id.booktype);
        confirm = findViewById(R.id.request1);
        duration = findViewById(R.id.duration);
        time = findViewById(R.id.time);

       final Intent intent=getIntent();

       switch (intent.getStringExtra("book"))
       {
           case "daily":
               d = intent.getStringExtra("data");
//               confirm = findViewById(R.id.confirm);
               userSession.setClientid(intent.getStringExtra("clientid"));
               userSession.setpiclat(intent.getStringExtra("plat"));
               userSession.setpiclong(intent.getStringExtra("plong"));
               userSession.setdroplat(intent.getStringExtra("dlat"));
               userSession.setdroplong(intent.getStringExtra("dlong"));
               userSession.setBookingtype(intent.getStringExtra("book"));

               Geocoder geocoder = new Geocoder(this);
               try {
                   address = geocoder.getFromLocation(Double.parseDouble(intent.getStringExtra("plat")), Double.parseDouble(intent.getStringExtra("plong")), 1);
                   String clientaddress = address.get(0).getAddressLine(0);
                   userSession.setClientaddress(clientaddress);
                   add.setText(clientaddress);
                   bookingtype.setText(intent.getStringExtra("book"));
               } catch (IOException e) {
                   e.printStackTrace();
               }
               break;

           case "rental":
               d = intent.getStringExtra("data");

               userSession.setRentaldetail(intent.getStringExtra("clientid"),intent.getStringExtra("plat"),intent.getStringExtra("plong"),intent.getStringExtra("pickupcity"),intent.getStringExtra("timeduration"));
               userSession.setBookingtype(intent.getStringExtra("book"));
                geocoder = new Geocoder(this);
               try {
                   Log.e("dfd",intent.getStringExtra("plat")+""+intent.getStringExtra("plong"));
                   address = geocoder.getFromLocation(Double.parseDouble(intent.getStringExtra("plat")), Double.parseDouble(intent.getStringExtra("plong")), 1);
                  if(address.size()!=0)
                  {
                      String clientaddress = address.get(0).getAddressLine(0);
                      userSession.setClientaddress(clientaddress);
                      add.setText(clientaddress);
                      duration.setVisibility(View.VISIBLE);
                      time.setText(intent.getStringExtra("timeduration"));
                      bookingtype.setText(intent.getStringExtra("book"));
                  }

               } catch (IOException e) {
                   e.printStackTrace();
               }
               break;

           case "OutStation":
               d = intent.getStringExtra("data");
//               confirm = findViewById(R.id.confirm);
               userSession.setClientid(intent.getStringExtra("clientid"));
               userSession.setpiclat(intent.getStringExtra("plat"));
               userSession.setpiclong(intent.getStringExtra("plong"));
               userSession.setdroplat(intent.getStringExtra("dlat"));
               userSession.setdroplong(intent.getStringExtra("dlong"));
               userSession.setBookingtype(intent.getStringExtra("book"));

               geocoder = new Geocoder(this);
               try {
                   address = geocoder.getFromLocation(Double.parseDouble(intent.getStringExtra("plat")), Double.parseDouble(intent.getStringExtra("plong")), 1);
                   String clientaddress = address.get(0).getAddressLine(0);
                   userSession.setClientaddress(clientaddress);
                   add.setText(clientaddress);
                   bookingtype.setText(intent.getStringExtra("book"));
               } catch (IOException e) {
                   e.printStackTrace();
               }
           break;
       }




//       if(intent.getStringExtra("book")=="daily")
//       {
//
//       }



   confirm.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           if("daily".equals(userSession.getBookingtype()))
           {
               Savetomysql(userSession.getUserDetails().get("driverid"),intent.getStringExtra("clientid"),intent.getStringExtra("plat"),intent.getStringExtra("plong"),intent.getStringExtra("dlat"),intent.getStringExtra("dlong"));
           }
           else
           {
               Savetomysql(userSession.getUserDetails().get("driverid"),intent.getStringExtra("clientid"),intent.getStringExtra("plat"),intent.getStringExtra("plong"),"null","null");
           }
           userSession.set_requestdata(d);
//           Intent intent1= new Intent(Request.this,SocketService.class);
//           intent1.putExtra("data",d);
//           startService(intent1);
           userSession.Booking("true");
             onBackPressed();
       }
   });


    }


    public void onBackPressed() {
     Intent intent= new Intent(Request.this,MainActivity.class);
     startActivity(intent);
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
        Log.e("track",userSession.gettrackid());
        connectionServer.buildParameter("trackid",userSession.gettrackid());

        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
//                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(),"data");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            jsonHelper.setChildjsonObj(jsonArray, i);
//                            Log.e("trackid", jsonHelper.GetResult("trackid"));
//                            userSession.settrackid(jsonHelper.GetResult("trackid"));
//                        }

                    }
                }

            }
        });

    }





}
