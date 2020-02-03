package com.example.taxidriver.webSocket;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.taxidriver.Activity.Login;
import com.example.taxidriver.Map.MainActivity;
import com.example.taxidriver.usersession.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;
import okio.ByteString;




@SuppressLint("Registered")
public class SocketService extends android.app.Service {

    OkHttpClient client;
    private WebSocket ws;
    UserSession userSession;
    Context context;
    Handler handler;
    Boolean socketconnection =false;
    String requestdata;
    String type= "blank";

    public SocketService(Context mainActivity) {
        this.context=mainActivity;
        userSession= new UserSession(context);
    }




    public int onStartCommand(final Intent intent, int flags, int startId ) {

//        Intent data =Intent.getIntent();
        if(intent.getExtras() != null ){
            String data = intent.getStringExtra("data");
            Log.e("ddf",data);

            try {
                JSONObject mainObject = new JSONObject(data);
            JSONObject clientdata = mainObject.getJSONObject("data");
             String pickuplat=clientdata.getString("pickuplat");
             String    picklong=clientdata.getString("pickuplong");
             String droplat=clientdata.getString("droplat");
             String   droplong=clientdata.getString("droplong");
             String  clientid=clientdata.getString("userId");
             requestdata= "{\"userID\" :\""+clientid+"\",\"driverID\":\""+userSession.getUserDetails().get("driverid")+"\",\"lat\":\""+userSession.getLatitute()+"\",\"long\":\""+userSession.getLogitute()+"\",\"type\":\"acceptReq\"}";
             type="requset";
            }
            catch (JSONException e)
            {
            e.printStackTrace();
         }

            }


        WebSocketListener webSocketListener=  new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                String JSON_STRING = "{\"userID\":\""+userSession.getUserDetails().get("driverid")+"\",\"userType\":\"Driver\",\"type\":\"initiate\"}";
                webSocket.send(JSON_STRING);
                socketconnection=true;
                userSession.setSocketConnection(true);


            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);

                JSONObject mainObject = null;
                try {
                    mainObject = new JSONObject(text);
                    String requesttype=mainObject.getString("type");
                    switch(requesttype){
                        //Case statements
                        case "vehicleRequest":

                            JSONObject clientdata = mainObject.getJSONObject("data");
                            String  clientid=clientdata.getString("userId");
                            final String JSON_STRING= "{\"userID\" :\""+clientid+"\",\"driverID\":\""+userSession.getUserDetails().get("driverid")+"\",\"lat\":\""+userSession.getLatitute()+"\",\"long\":\""+userSession.getLogitute()+"\",\"type\":\"acceptReq\"}";

                          Intent intent1 = new Intent(context , com.example.taxidriver.Activity.Request.class);
                          intent1.putExtra("data",JSON_STRING);
                          context.startActivity(intent1);
                            Animatoo.animateShrink(context);
//                         webSocket.send(JSON_STRING);
//                    sendRequest(text,webSocket);
                            break;
                        case "dhioj":
                            System.out.println("20");
                            break;
                        case "sdu": System.out.println("30");
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
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                socketconnection=false;
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                socketconnection=false;
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                socketconnection=false;
            }
        };


        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://173.212.226.143:8090/").build();
        ws = client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService();

        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Timer().scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        final String JSON_STRING= "{\"lat\":\""+userSession.getLatitute()+"\",\"long\":\""+userSession.getLogitute()+"\",\"area\":\""+userSession.getStreetname()+"\",\"type\":\"updateVehicleLocation\"}";

                        if(userSession.getSocketConnection()) {
                            if(socketconnection)
                          ws.send(JSON_STRING) ;
                        }
                    }
                }, 30, 5000);

            }
        });



        switch (type)
        {
            case "requset":
                ws.send(requestdata);
                break;

            default:
                break;
        }




        return START_NOT_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        Log.e("Hello","HELLO");

        return null;
    }
}

