package com.example.taxidriver.webSocket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.taxidriver.usersession.Globalcontext;
import com.example.taxidriver.usersession.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;




@SuppressLint("Registered")
public class SocketService extends android.app.Service {

    OkHttpClient client;
    public WebSocket ws;
    UserSession userSession;
    Context context;
    Handler handler;
    Boolean socketconnection =false;
    String requestdata;
    String type= "blank";
    WebSocketListener webSocketListener;
    public Globalcontext GlobalApplication;
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    public SocketService() {

    }




    public int onStartCommand(final Intent intent, int flags, int startId ) {

//        Intent data =Intent.getIntent();


//            try {
//                JSONObject mainObject = new JSONObject(data);
//            JSONObject clientdata = mainObject.getJSONObject("data");
//             String pickuplat=clientdata.getString("pickuplat");
//             String    picklong=clientdata.getString("pickuplong");
//             String droplat=clientdata.getString("droplat");
//             String   droplong=clientdata.getString("droplong");
//             String  clientid=clientdata.getString("userId");
//             requestdata= "{\"userID\" :\""+clientid+"\",\"driverID\":\""+userSession.getUserDetails().get("driverid")+"\",\"lat\":\""+userSession.getLatitute()+"\",\"long\":\""+userSession.getLogitute()+"\",\"type\":\"acceptReq\"}";
//             type="requset";
//            }
//            catch (JSONException e)
//            {
//            e.printStackTrace();
//         }



       webSocketListener = new WebSocketListener() {


           @Override
           public void onOpen(WebSocket webSocket, Response response) {
//               super.onOpen(webSocket, response);
               String JSON_STRING = "{\"userID\":\"" + userSession.getUserDetails().get("driverid") + "\",\"userType\":\"Driver\",\"type\":\"initiate\"}";
               webSocket.send(JSON_STRING);
               socketconnection = true;
               userSession.setSocketConnection(true);


           }

           @Override
           public void onMessage(WebSocket webSocket, String text) {
               super.onMessage(webSocket, text);
               Random rand = new Random();
               int n = rand.nextInt(10000);
//
//               final String JSON_STRING1 = "{\",\"driverID\":\"" + userSession.getUserDetails().get("driverid") + "\",\"lat\":\"" + userSession.getLatitute() + "\",\"long\":\"" + userSession.getLogitute() + "\",\"type\":\"acceptReq\"}";
//               Intent intent2 = new Intent(context, com.example.taxidriver.Activity.Request.class);
//               intent2.putExtra("data", JSON_STRING1);
//               context.startActivity(intent2);
//               Animatoo.animateShrink(context);

               JSONObject mainObject = null;
               try {
                   mainObject = new JSONObject(text);
                   String requesttype = mainObject.getString("type");
                   switch (requesttype) {
                       //Case statements
                       case "vehicleRequest":

                            if(!(userSession.getbookingstatus())) {
                                JSONObject clientdata = mainObject.getJSONObject("data");
                                String clientid = clientdata.getString("userId");
                                String piclat = clientdata.getString("pickuplat");
                                String piclong = clientdata.getString("pickuplong");
                                String droplat = clientdata.getString("droplat");
                                String droplong = clientdata.getString("droplong");

                                final String JSON_STRING = "{\"userID\" :\"" + clientid + "\",\"driverID\":\"" + userSession.getUserDetails().get("driverid") + "\",\"lat\":\"" + userSession.getLatitute() + "\",\"long\":\"" + userSession.getLogitute() + "\",\"type\":\"acceptReq\" ,\"trackid\":\""+clientid+n+"\"}";
//                           webSocket.send(JSON_STRING);
                                userSession.settrackid(String.valueOf(clientid+n));
                                Intent intent1 = new Intent(context, com.example.taxidriver.Activity.Request.class);
                                intent1.putExtra("data", JSON_STRING);
                                intent1.putExtra("clientid", clientid);
                                intent1.putExtra("dlat", droplat);
                                intent1.putExtra("dlong", droplong);
                                intent1.putExtra("plat", piclat);
                                intent1.putExtra("plong", piclong);
                                intent1.putExtra("book", "daily");
                                context.startActivity(intent1);
                                Animatoo.animateShrink(context);

                            }
//                    sendRequest(text,webSocket);
                           break;
                       case "Rental":
                           if(!(userSession.getbookingstatus())) {
                               JSONObject clientdata = mainObject.getJSONObject("data");
                               String clientid = clientdata.getString("userId");
                               String piclat = clientdata.getString("pickuplat");
                               String piclong = clientdata.getString("pickuplong");
                               String pickupCityName = clientdata.getString("PickupCityName");
                               String timeDuration = clientdata.getString("TimeDuration");


                               final String JSON_STRING = "{\"userID\" :\"" + clientid + "\",\"driverID\":\"" + userSession.getUserDetails().get("driverid") + "\",\"lat\":\"" + userSession.getLatitute() + "\",\"long\":\"" + userSession.getLogitute() + "\",\"type\":\"acceptReq\",\"trackid\":\""+clientid+n+"\"}";
                                userSession.settrackid(String.valueOf(clientid+n));
                               Intent intent1 = new Intent(context, com.example.taxidriver.Activity.Request.class);
                               intent1.putExtra("data", JSON_STRING);
                               intent1.putExtra("clientid", clientid);
                               intent1.putExtra("plat", piclat);
                               intent1.putExtra("plong", piclong);
                               intent1.putExtra("pickupcity", pickupCityName);
                               intent1.putExtra("timeduration", timeDuration);
                               intent1.putExtra("book", "rental");
                               context.startActivity(intent1);

                           }

                           break;
                       case "OutStation":
                           if(!(userSession.getbookingstatus())) {
                               JSONObject clientdata = mainObject.getJSONObject("data");
                               String clientid = clientdata.getString("userId");
                               String piclat = clientdata.getString("pickuplat");
                               String piclong = clientdata.getString("pickuplong");
                               String droplat = clientdata.getString("droplat");
                               String droplong = clientdata.getString("droplong");


                               final String JSON_STRING = "{\"userID\" :\"" + clientid + "\",\"driverID\":\"" + userSession.getUserDetails().get("driverid") + "\",\"lat\":\"" + userSession.getLatitute() + "\",\"long\":\"" + userSession.getLogitute() + "\",\"type\":\"acceptReq\",\"trackid\":\""+clientid+n+"\"}";
//                           webSocket.send(JSON_STRING);
                               userSession.settrackid(String.valueOf(clientid+n));
                               Intent intent1 = new Intent(context, com.example.taxidriver.Activity.Request.class);
                               intent1.putExtra("data", JSON_STRING);
                               intent1.putExtra("clientid", clientid);
                               intent1.putExtra("dlat", droplat);
                               intent1.putExtra("dlong", droplong);
                               intent1.putExtra("plat", piclat);
                               intent1.putExtra("plong", piclong);
                               intent1.putExtra("book", "OutStation");

                               context.startActivity(intent1);
                               Animatoo.animateShrink(context);
                           }
                           break;
                       //Default case statement
                       default:

                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }


           }

           @Override
           public void onMessage(WebSocket webSocket, ByteString bytes) {
//               super.onMessage(webSocket, bytes);
           }

           @Override
           public void onClosing(WebSocket webSocket, int code, String reason) {
               webSocket.close(NORMAL_CLOSURE_STATUS, null);
//               super.onClosing(webSocket, code, reason);
               userSession.setSocketConnection(false);
               socketconnection = false;
           }

           @Override
           public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//               super.onFailure(webSocket, t, response);
               webSocket.close(NORMAL_CLOSURE_STATUS, null);
               userSession.setSocketConnection(false);
               socketconnection = false;
               connect();
//               reconnect();

           }
       };

       connect();
       handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Timer().scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {





                        final String JSON_STRING = "{\"lat\":\"" + userSession.getLatitute() + "\",\"long\":\"" + userSession.getLogitute() + "\",\"area\":\"" + userSession.getStreetname() + "\",\"type\":\"updateVehicleLocation\"}";

                         if (socketconnection)
                                {
                                    ws.send(JSON_STRING);
                                    Log.e("fff",userSession.getRequestdata());
                                    if(!"null".equals(userSession.getRequestdata()))
                                    {
                                        ws.send(userSession.getRequestdata());
                                        userSession.set_requestdata("null");

                                    }

                                    if(!(userSession.getsocketonoff()))
                                    {
                                        ws.close(NORMAL_CLOSURE_STATUS, null);
                                    }
                                    if(userSession.getotpconfirm()){
                                        String otp = null;
                                        if("daily".equals(userSession.getBookingtype())){
                                             otp = "{\"driverID\":\"" +userSession.getUserDetails().get("driverid")+ "\",\"type\":\"otpconfirm\",\"clientID\":\""+userSession.getClientID()+"\"}";
                                        }
                                        else
                                        {
                                            if("rental".equals(userSession.getBookingtype()))
                                            {
                                                otp = "{\"driverID\":\"" +userSession.getUserDetails().get("driverid")+ "\",\"type\":\"otpconfirm\",\"clientID\":\""+userSession.getRentaldetail().get("rentalclientid")+"\"}";

                                            }
                                            else
                                            {
                                                otp = "{\"driverID\":\"" +userSession.getUserDetails().get("driverid")+ "\",\"type\":\"otpconfirm\",\"clientID\":\""+userSession.getClientID()+"\"}";

                                            }

                                        }
                                        ws.send(otp);
                                        userSession.setotpconfirm(false);
                                    }

                                    if(userSession.getendTripstatus()){
                                        ws.send(userSession.gettripdata());
                                        userSession.setendTripstatus(false);
                                    }

                                }


                        }

                }, 30, 5000);

            }
        });







        return START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        Log.e("Hello","HELLO");

        return null;
    }

    public  void setdata(Context context)
    {
        this.context=context;
        userSession= new UserSession(context);

    }



    public void connect()
    {


        userSession= new UserSession(context);
        if(!(userSession).getSocketConnection()) {
            client = new OkHttpClient();
            Request request = new Request.Builder().url("ws://173.212.226.143:8090/").build();
            ws = client.newWebSocket(request, webSocketListener);
            client.dispatcher().executorService();
        }
    }



    public void reconnect()
    {

        final Timer timer=new Timer();
               timer.scheduleAtFixedRate(new TimerTask() {

                   @Override
                    public void run() {
                       GlobalApplication= new Globalcontext();
                       final Context context = GlobalApplication.getAppContext();


//                        if(socketconnection){
//                            timer.cancel();
//                        }
//                        else {
//                            connect();
//                        }
                    }


                }, 0, 10000);

            }



}

